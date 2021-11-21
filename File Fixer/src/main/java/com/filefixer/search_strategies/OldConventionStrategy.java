package com.filefixer.search_strategies;

import java.io.File;

public class OldConventionStrategy implements SearchStrategy {
  /**
   * Generates original name of the student's PDF submission.
   *
   * @param pdf PDF file
   * @return the name of the student's PDF submission
   */
  @Override
  public String getOriginalFilename(File pdf) {
    StringBuilder sb = new StringBuilder();
    var components = pdf.getName().split("_");

    for (int i = 3; i < components.length - 2; i++) sb.append(components[i]).append("_");

    sb.setLength(sb.length() - 1);

    return sb.toString();
  }
}
