package com.filefixer.search_strategies;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewConventionStrategy implements SearchStrategy {
  /**
   * Generates original name of the student's PDF submission.
   *
   * @param pdf PDF file
   * @return the name of the student's PDF submission
   */
  @Override
  public String getOriginalFilename(File pdf) {
    Matcher matcher;
    Pattern pattern = Pattern.compile("^([0-9]+)-([0-9]+)_([a-zA-Z-'_]+)_([0-9]+)_(.+).pdf$");

    matcher = pattern.matcher(pdf.getName());
    matcher.matches();

    return matcher.group(5);
  }
}
