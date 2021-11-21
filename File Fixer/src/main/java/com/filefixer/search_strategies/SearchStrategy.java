package com.filefixer.search_strategies;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/** Interface implemented by {@code SearchStrategy} instances. */
public interface SearchStrategy {
  String getOriginalFilename(File pdf);

  /**
   * Determines if a given submission name contains a given ID.
   *
   * @param filename the name of a student's submission
   * @param id a student's ID
   * @return true if the ID is found in the submission name
   */
  default boolean containsID(String filename, String id) {
    Pattern pattern = Pattern.compile(MessageFormat.format(".*({0}).*", id));
    return pattern.matcher(filename).matches();
  }

  /**
   * @param pdfList a {@code List} of PDF files
   * @param id a student's ID
   * @return the original name under which a student submitted PDF
   */
  default Optional<String> getSubmissionName(List<File> pdfList, String id) {
    String originalFilename;

    if (pdfList.size() == 1) {
      originalFilename = getOriginalFilename(pdfList.get(0));
      return Optional.of(originalFilename);
    } else {
      for (File pdf : pdfList) {
        originalFilename = getOriginalFilename(pdf);
        if (containsID(originalFilename, id)) {
          return Optional.of(originalFilename);
        }
      }
      return Optional.empty();
    }
  }
}
