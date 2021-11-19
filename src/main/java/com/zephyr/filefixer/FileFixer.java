package com.zephyr.filefixer;

import com.zephyr.filefixer.exceptions.InputDirectoryNotFoundException;
import com.zephyr.filefixer.exceptions.InputFileNotFoundException;
import com.zephyr.filefixer.exceptions.InvalidCSVException;
import com.zephyr.filefixer.input.csv.CSV;
import com.zephyr.filefixer.input.csv.Student;
import com.zephyr.filefixer.input.pdf.FileForRename;
import com.zephyr.filefixer.input.pdf.InputFile;
import com.zephyr.filefixer.input.pdf.naming_conventions.*;
import com.zephyr.filefixer.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Entry point to application.
 */
public class FileFixer {
    public static final File inputDirectory = new File(Utils.inputPath());
    public static File inputCSV;
    public static File[] filesToRename;


    /**
     * Loads the CSV file and the PDF files.<br>
     * Creates the HashMaps.<br>
     * Performs rename/relocation operations on the PDF files.<br>
     * Logs missing submissions and PDFs with invalid names.<br>
     */
    public static void main(String[] args) {
        try {
            setupEnvironment();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("\nExiting program...");
            return;
        }

        var directories = createDirectories(
                "manualReview",
                "renamedFiles",
                "correctlyRenamed",
                "invalidFileNames"
        );

        NamingConventions namingConventions = FilenameAnalyser.analyse(filesToRename);

        List<Student> listOfStudents = CSV.analyse(inputCSV);

        List<FileForRename> filesForRename = new ArrayList<>();
        filesForRename.addAll(extractComponents(namingConventions.newMyELearningConvention(), new NewConvention()));
        filesForRename.addAll(extractComponents(namingConventions.oldMyELearningConvention(), new OldConvention()));

        HashMap<String, Student> hashMapId = makeHashMapId(listOfStudents);
        HashMap<String, List<Student>> hashMapName = makeHashMapName(listOfStudents);

        Utils.IdentifierExtractor.setHashMaps(hashMapId, hashMapName);

        List<InputFile> inputFiles = InputFile.toInputFiles(filesForRename);
        renameInputFiles(inputFiles);

        if (!hashMapId.isEmpty())
            logMissingSubmissions(hashMapId);

        if (!namingConventions.noConvention().isEmpty())
            logInvalidFiles(namingConventions.noConvention());


        relocateFiles(namingConventions.noConvention(), directories.get("invalidFileNames"));
        relocateFiles(namingConventions.uploadConvention(), directories.get("correctlyRenamed"));

        for (var directory : directories.values()) {
            directory.delete();
        }
    }

    /**
     * Converts an array of PDF files to a list of {@code FileForRename} objects.
     *
     * @param listOfFiles list of PDF files to convert
     * @param strategy {@code ComponentExtractionStrategy} to be used when extracting the components
     * @return a list of {@code FileForRename} objects
     */
    private static List<FileForRename> extractComponents(List<File> listOfFiles, ComponentExtractionStrategy strategy) {
        return strategy.extractComponents(listOfFiles);
    }


    /**
     * Attempts to locate the input files required for the program to run. Throws exceptions if they are not found.
     *
     * @throws InputDirectoryNotFoundException if the input directory is not found (~/Desktop/filesToRename/renamedFiles)
     * @throws InputFileNotFoundException      if a valid CSV file is not found
     * @throws InvalidCSVException             if no PDF files are found
     */
    private static void setupEnvironment() throws InputDirectoryNotFoundException, InputFileNotFoundException, InvalidCSVException {
        if (!inputDirectory.exists()) {
            throw new InputDirectoryNotFoundException("Input folder not found. (" + inputDirectory + ")");
        }

        setupCSVInput();

        filesToRename = inputDirectory.listFiles(f -> !f.isDirectory() && f.getName().endsWith(".pdf"));
        if (Objects.requireNonNull(filesToRename).length == 0) {
            throw new InputFileNotFoundException("Input PDF files not found.");
        }
    }

    /**
     * Attempts to load an input CSV file. Throws exceptions if one cannot be found.
     *
     * @throws InputFileNotFoundException if multiple CSV files are present and no "input.csv" is found
     * @throws InvalidCSVException        if the CSV file has no entries or if the CSV file has invalid headers
     * @see CSV
     */
    private static void setupCSVInput() throws InputFileNotFoundException, InvalidCSVException {
        var CSVs = inputDirectory.listFiles(f -> !f.isDirectory() && f.getName().endsWith(".csv"));

        if (Objects.requireNonNull(CSVs).length == 0) {
            throw new InputFileNotFoundException("CSV file not found.");
        } else if (CSVs.length == 1) {
            inputCSV = CSVs[0];
        } else {
            boolean found = false;
            for (File file : CSVs) {
                if (file.getName().equals("input.csv")) {
                    inputCSV = file;
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new InputFileNotFoundException("Multiple CSV files present in folder. Cannot determine input file.");
            }
        }

        if (!CSV.validate(inputCSV)) {
            throw new InvalidCSVException("Input CSV file has invalid format.");
        }
    }


    /**
     * Creates the subfolders in filesToRename.
     *
     * @param names names of the folders to be created
     * @return a HashMap of the created folders
     */
    public static HashMap<String, File> createDirectories(String... names) {
        HashMap<String, File> directories = new HashMap<>();

        for (String name : names) {
            File directory = new File(Utils.inputPath() + Utils.separator + name);
            directory.mkdir();
            directories.put(name, directory);
        }

        return directories;
    }

    /**
     * Generates a HashMap binding a {@code Student} object to a student's ID
     *
     * @param listOfStudents student information from the CSV file
     * @return HashMap binding a {@code Student} object to a student's ID
     */
    private static HashMap<String, Student> makeHashMapId(List<Student> listOfStudents) {
        HashMap<String, Student> hashMapId = new HashMap<>();
        for (Student student : listOfStudents) {
            hashMapId.put(student.id(), student);
        }
        return hashMapId;
    }

    /**
     * Generates a HashMap binding a list of {@code Student} object to a student's name
     *
     * @param listOfStudents student information from the CSV file
     * @return HashMap binding a list of {@code Student} objects to a student's name
     */
    private static HashMap<String, List<Student>> makeHashMapName(List<Student> listOfStudents) {
        HashMap<String, List<Student>> hashMapName = new HashMap<>();
        for (Student student : listOfStudents) {
            if (hashMapName.containsKey(student.name())) {
                hashMapName.get(student.name()).add(student);
            } else {
                hashMapName.put(student.name(), new ArrayList<>(List.of(student)));
            }
        }
        return hashMapName;
    }

    /**
     * Attempts to rename a list of PDF files and place them in their correct output locations.
     *
     * @param inputFiles list of PDFs to rename
     */
    private static void renameInputFiles(List<InputFile> inputFiles) {
        for (int i = 0; i < 2; i++) {
            for (var inputFile : inputFiles)
                inputFile.extractIdentifier();
        }
        for (var inputFile : inputFiles)
            inputFile.relocate();
    }

    /**
     * Generates a text file in the input folder containing the names of the students that did not submit an assignment.
     *
     * @param missingSubmissions HashMap containing the students that did not submit an assignment
     */
    private static void logMissingSubmissions(HashMap<String, Student> missingSubmissions) {
        StringBuilder sb = new StringBuilder();

        for (var entry : missingSubmissions.values()) {
            sb.append(entry.name()).append("\n");
        }

        try {
            FileWriter fileWriter = new FileWriter(Utils.inputPath() + Utils.separator + "error_log_missing_submissions.txt");
            fileWriter.write(sb.toString());
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing file: error_log_missing_submissions.txt");
        }
    }

    /**
     * Generates a text file in the input folder containing the names of invalid PDF files.
     *
     * @param invalidFiles list of invalid PDF files.
     */
    private static void logInvalidFiles(List<File> invalidFiles) {
        StringBuilder sb = new StringBuilder();

        for (var file : invalidFiles) {
            sb.append(file.getName());
        }

        try {
            FileWriter fileWriter = new FileWriter(Utils.inputPath() + Utils.separator + "error_log_invalid_names.txt");
            fileWriter.write(sb.toString());
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing file: error_log_invalid_names.txt");
        }
    }

    /**
     * Moves the PDF files which were already renamed to the myeLearning convention and those with invalid names.
     *
     * @param files       list of PDF files to move
     * @param destination folder to which the files will be moved
     */
    public static void relocateFiles(List<File> files, File destination) {
        try {
            for (var file : files) {
                Files.move(file.toPath(), destination.toPath(), REPLACE_EXISTING);
            }
        } catch (IOException e) {
            System.out.println("Error moving files to " + destination.getPath());
        }
    }
}