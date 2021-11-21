package com.filefixer;

import com.filefixer.csv_files.CommaSeparatedValueFiles;
import com.filefixer.student.Student;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/** Entry point to the application. */
public class FileFixer {

  public static String CURRENT_DIRECTORY;

  public static void main(String[] args) {
    File filesToRename = new File(Utils.FILES_TO_RENAME);
    filesToRename.mkdir();
    List<File> directoryQueue = new LinkedList<>();
    directoryQueue.add(filesToRename);

    LinkedList<File> zipFiles =
        new LinkedList<>(
            Arrays.asList(
                filesToRename.listFiles(f -> !f.isDirectory() && f.getName().endsWith(".zip"))));

    for (File zipFile : zipFiles) {
      try {
        String zipFilePath = zipFile.getPath();
        File destDir =
            new File(filesToRename + Utils.SEPARATOR + zipFile.getName().replace(".zip", ""));
        destDir.mkdir();
        directoryQueue.add(destDir);

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry zipEntry = zis.getNextEntry();

        while (zipEntry != null) {
          String zipEntryName = zipEntry.getName();

          if (!zipEntryName.contains("__MACOSX")) {
            String filepath = destDir.getPath() + Utils.SEPARATOR + zipEntryName;
            if (!zipEntry.isDirectory()) {
              FileOutputStream fos = new FileOutputStream(filepath);
              int len;
              while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
              }
              fos.close();
            } else {
              File dir = new File(filepath);
              dir.mkdirs();
            }
          }

          zis.closeEntry();
          zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    for (File dir : directoryQueue) {
      performFileFixing(dir);
    }
  }

  /**
   * This func
   *
   * @param directory the directory in which the input files (CSV, PDFs) are located.
   */
  private static void performFileFixing(File directory) {
    CURRENT_DIRECTORY = directory.getPath();
    List<File> pdfFiles =
        new LinkedList<>(
            Arrays.asList(
                directory.listFiles(f -> !f.isDirectory() && f.getName().endsWith(".pdf"))));
    List<File> csvFiles =
        new LinkedList<>(
            Arrays.asList(
                directory.listFiles(f -> !f.isDirectory() && f.getName().endsWith(".csv"))));
    File csvInput;

    if (csvFiles.isEmpty()) return;

    var csvInputOptional = CommaSeparatedValueFiles.getCSVInput(csvFiles);

    if (csvInputOptional.isPresent()) csvInput = csvInputOptional.get();
    else return;

    var directories =
        createDirectories("manualReview", "renamedFiles", "correctlyRenamed", "invalidFileNames");

    List<Student> studentList = CommaSeparatedValueFiles.getStudents(csvInput);

    removeCorrectlyNamedFiles(pdfFiles);

    for (Student student : studentList) {
      student.performRename(pdfFiles);
      student.performRename(pdfFiles);
    }

    for (var dir : directories.values()) {
      dir.delete();
    }

    writeLog(generateInvalidNameLog(pdfFiles), "log_invalid_names.txt");
    writeLog(generateMissingSubmissionLog(studentList), "log_missing_submissions.txt");
  }

  /**
   * Creates directories.
   *
   * @param names names of the directories to be created
   * @return a HashMap of the created directories
   */
  public static HashMap<String, File> createDirectories(String... names) {
    HashMap<String, File> directories = new HashMap<>();

    for (String name : names) {
      File directory = new File(CURRENT_DIRECTORY + Utils.SEPARATOR + name);
      directory.mkdir();
      directories.put(name, directory);
    }

    return directories;
  }

  /**
   * Prepares a list of the names of students that didn't submit.
   *
   * @param studentList a {@code List} of students
   * @return a {@code String} containing the invalid names
   */
  private static String generateMissingSubmissionLog(List<Student> studentList) {
    StringBuilder sb = new StringBuilder();

    for (Student student : studentList) {
      if (!student.hasSubmitted) sb.append(student.name).append("\n");
    }

    return sb.toString();
  }

  /**
   * Prepares a list of PDFs with invalid names.
   *
   * @param pdfList a {@code List} of PDF files
   * @return a {@code String} containing the invalid names
   */
  private static String generateInvalidNameLog(List<File> pdfList) {
    StringBuilder sb = new StringBuilder();

    for (File pdf : pdfList) sb.append(pdf.getName()).append("\n");

    return sb.toString();
  }

  /**
   * Creates a text file.
   *
   * @param logText text to be written to the text file
   * @param logName name of the text file
   */
  private static void writeLog(String logText, String logName) {
    if (logText.isEmpty()) return;

    try (FileWriter fileWriter = new FileWriter(CURRENT_DIRECTORY + Utils.SEPARATOR + logName)) {
      fileWriter.write(logText);
    } catch (IOException e) {
      System.out.println("Error writing file: " + logName);
    }
  }

  /**
   * Moves files already following naming convention 2 to a subdirectory called
   * alreadyUploadConvention.
   *
   * @param pdfList a {@code List} of PDF files
   */
  private static void removeCorrectlyNamedFiles(List<File> pdfList) {
    for (File pdf : pdfList) {
      if (Utils.naming_convention(pdf) == 2) {
        try {
          File outputFile =
              new File(
                  CURRENT_DIRECTORY + Utils.SEPARATOR + "alreadyUploadConvention/" + pdf.getName());
          Files.move(pdf.toPath(), outputFile.toPath(), REPLACE_EXISTING);
          pdfList.remove(pdf);
        } catch (IOException ignored) {
          System.out.println("Error moving file: " + pdf.getName());
        }
      }
    }
  }
}
