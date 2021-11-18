import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileFixer {



    public static void main(String[] args) throws IOException {
//        System.out.println(Utils.inputDirectoryPath());
//        System.out.println(Utils.outputDirectoryRenamedPath());
//        System.out.println(Utils.outputDirectoryReviewPath());
//        System.out.println(Utils.inputCSVPath());
//        System.out.println("\n\n");

        File inputDirectory = new File(Utils.inputPath());
        File inputCSV = new File(Utils.inputCSVPath());
        File[] filesToRename = getInputFiles(inputDirectory, inputCSV);
        if (filesToRename == null)
            return;

        File outputDirectoryReview = new File(Utils.outputReviewPath());
        File outputDirectoryRenamed = new File(Utils.outputRenamedPath());
        outputDirectoryReview.mkdir();
        outputDirectoryRenamed.mkdir();

        FilenameAnalyser filenameAnalyser = new FilenameAnalyser();
        NamingConventions namingConventions = filenameAnalyser.analyseFilenames(filesToRename);

        CSVAnalyser csvAnalyser = new CSVAnalyser(inputCSV);

        List<Student> listOfStudents = csvAnalyser.getStudentInformation();

//        for(Student s : listOfStudents)
//            System.out.println(s);

        List<FileForRename> filesForRename = new ArrayList<>();
        filesForRename.addAll(extractComponents(namingConventions.newMyELearningConvention(), new NewConvention()));
        filesForRename.addAll(extractComponents(namingConventions.oldMyELearningConvention(), new OldConvention()));

        HashMap<String, Student> hashMapId = makeHashMapId(listOfStudents);
        HashMap<String, List<Student>> hashMapName = makeHashMapName(listOfStudents);

        Utils.IdentifierExtractor.setHashMaps(hashMapId, hashMapName);

//        for(var entry : hashMapName.entrySet()){
//            System.out.println(entry.getKey() + " --> " + entry.getValue());
//        }
//
//        for(var entry : hashMapId.entrySet()){
//            System.out.println(entry.getKey() + " --> " + entry.getValue());
//        }

        List<InputFile> inputFiles = getInputFiles(filesForRename);
        renameInputFiles(inputFiles);

//        for (var entry : hashMapId.entrySet())
//            System.out.println(entry);
//
//        System.out.println("\nStudents with missing files:");
//        int x = 0;
//        for (var entry : hashMapName.entrySet()) {
//            System.out.println(entry.getKey());
//            x++;
//        }
//        System.out.println("\n" + x + " missing files.");

        if(!hashMapId.isEmpty())
            logMissingSubmissions(hashMapId);

        if(!namingConventions.noConvention().isEmpty())
            logInvalidFiles(namingConventions.noConvention());
    }

    public static List<FileForRename> extractComponents(List<File> listOfFiles, ComponentExtractionStrategy strategy) {
        return strategy.extractComponents(listOfFiles);
    }

    public static File[] getInputFiles(File inputDirectory, File inputCSV) {
        if (inputDirectory.exists()) {
            if (inputCSV.exists()) {
                File[] filesToRename = inputDirectory.listFiles(file -> !file.isDirectory() && file.getName().endsWith(".pdf"));
                if (filesToRename.length > 0)
                    return filesToRename;
                else
                    System.out.println("Input PDFs not found. Place the files in the input folder.");
            } else
                System.out.println("Input CSV not found. Place the file (\"input.csv\") in the input folder.");
        } else
            System.out.println("Input folder not found. The folder should be at " + Utils.inputPath() + Utils.SEPARATOR);
        return null;
    }

    private static HashMap<String, Student> makeHashMapId(List<Student> listOfStudents) {
        HashMap<String, Student> hashMapId = new HashMap<>();
        for (Student student : listOfStudents) {
            hashMapId.put(student.id(), student);
        }
        return hashMapId;
    }

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

    private static List<InputFile> getInputFiles(List<FileForRename> filesForRename) {
        List<InputFile> inputFiles = new ArrayList<>();
        for (var file : filesForRename) {
            inputFiles.add(new InputFile(file));
        }
        return inputFiles;
    }

    private static void renameInputFiles(List<InputFile> inputFiles) throws IOException {
        for (int i = 0; i < 2; i++) {
            for (var inputFile : inputFiles)
                inputFile.extractIdentifier();
        }
        for (var inputFile : inputFiles)
            inputFile.relocate();
    }

    private static void logMissingSubmissions(HashMap<String, Student> missingSubmissions) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (var entry : missingSubmissions.values()) {
            sb.append(entry.name()).append("\n");
        }

        FileWriter fileWriter = new FileWriter(Utils.inputPath() + Utils.SEPARATOR + "error_log_missing_submissions.txt");
        fileWriter.write(sb.toString());
        fileWriter.close();
    }

    private static void logInvalidFiles(List<File> invalidFiles) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (var file : invalidFiles) {
            sb.append(file.getName());
        }

        FileWriter fileWriter = new FileWriter(Utils.inputPath() + Utils.SEPARATOR + "error_log_invalid_names.txt");
        fileWriter.write(sb.toString());
        fileWriter.close();
    }
}
