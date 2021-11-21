package com.filefixer.student;

import com.filefixer.FileFixer;
import com.filefixer.Utils;
import com.filefixer.search_strategies.SearchStrategy;
import com.filefixer.search_strategies.SearchStrategyFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/** The {@code Student} class contains the information needed to rename a file. */
public class Student {
  public final String name;
  private final String identifier;
  private final String id;

  private final List<String> regexes = new ArrayList<>();
  private final SearchStrategyFactory strategyFactory = new SearchStrategyFactory();

  public boolean hasSubmitted = false;

  public Student(String identifier, String name, String id) {
    this.identifier = identifier;
    this.name = name;
    this.id = id;

    var names = name.split(" ");

    String idRegex = MessageFormat.format(".*({0}).*", id);
    String fnameLnameRegex =
        MessageFormat.format("(?i).*({0}{1}).*", names[0], names[names.length - 1]);
    String fnameLnameSeparatedRegex =
        MessageFormat.format("(?i).*({0}.{1}).*", names[0], names[names.length - 1]);
    String fullNameRegex = MessageFormat.format("(?i).*({0}).*", name);
    String fullNameSeparatedRegex = MessageFormat.format("(?i).*({0}).*", name.replace(" ", ""));

    regexes.add(idRegex);
    regexes.add(fnameLnameRegex);
    regexes.add(fnameLnameSeparatedRegex);
    regexes.add(fullNameRegex);
    regexes.add(fullNameSeparatedRegex);
  }

  /**
   * Recieves a {@code List} of PDF files. Uses regex to gather the PDF files that may belong to a
   * particular student.
   *
   * @param pdfList a {@code List} of PDF files
   */
  public void performRename(List<File> pdfList) {
    List<File> attributableDocuments = returnAttributableDocuments(pdfList);
    copyNewFile(attributableDocuments)
        .ifPresent(
            doc -> {
              pdfList.remove(doc);
              hasSubmitted = true;
            });
  }

  /**
   * Returns a {@code List} of PDF files matching the regexes based on the student's name and ID.
   *
   * @param pdfList a {@code List} of PDF files
   * @return a list of PDF files that may belong to the student
   */
  public List<File> returnAttributableDocuments(List<File> pdfList) {
    HashSet<File> documents = new HashSet<>();
    Matcher matcher;
    File document;

    for (var regex : regexes) {
      Pattern pattern = Pattern.compile(regex);
      documents.addAll(
          pdfList.stream()
              .filter(pdf -> pattern.matcher(pdf.getName()).matches())
              .collect(Collectors.toList()));
    }

    return new ArrayList<>(documents);
  }

  /**
   * Renames a file according to the myeLearning upload convention.
   *
   * @param pdfFiles a {@code List} of PDF files that may belong to a student
   * @return the PDF file belonging to the student, if it exists, an {@code Optional.empty()}
   *     otherwise.
   */
  public Optional<File> copyNewFile(List<File> pdfFiles) {
    LinkedList<SearchStrategy> strategies = new LinkedList<>();

    Optional<String> submissionName;

    for (var pdf : pdfFiles) {
      submissionName = strategyFactory.getSearchStrategy(pdf).getSubmissionName(List.of(pdf), id);

      if (submissionName.isPresent()) {
        String newFileName =
            MessageFormat.format(
                "{0}_{1}_assignsubmission_file_{2}", name, identifier, submissionName.get());
        File outputFile =
            new File(
                FileFixer.CURRENT_DIRECTORY
                    + Utils.SEPARATOR
                    + "renamedFiles/"
                    + (newFileName.endsWith(".pdf") ? newFileName : newFileName + ".pdf"));

        try {
          Files.copy(pdf.toPath(), outputFile.toPath(), REPLACE_EXISTING);
        } catch (IOException ignored) {
          System.out.println("Error writing file: " + newFileName);
        }
        return Optional.of(pdf);
      }
    }
    return Optional.empty();
  }

  @Override
  public String toString() {
    return MessageFormat.format("[{0}, {1}, {2}]", identifier, name, id);
  }
}
