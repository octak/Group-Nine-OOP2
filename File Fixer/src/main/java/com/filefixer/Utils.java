package com.filefixer;

import java.io.File;
import java.util.regex.Pattern;

/** Class containing only static utility functions. */
public class Utils {
  public static final String SEPARATOR = System.getProperty("file.separator");
  public static final String FILES_TO_RENAME = "filesToRename";

  /**
   * Uses regex to determine naming convention of a PDF file.
   *
   * @param pdf a PDF file
   * @return an integer value representing the file's naming convention (0 = new, 1 = old, 2 =
   *     upload, 3 = random)
   */
  public static int naming_convention(File pdf) {
    String old_convention = "^([0-9]+)-([0-9]+)_(.+)_([0-9]+)_([0-9]+).pdf$";
    String new_convention = "^([0-9]+)-([0-9]+)_([a-zA-Z-'_]+)_([0-9]+)_(.+).pdf$";
    String upload_convention = "^([a-zA-Z+\\s]+)_([0-9]+)_assignsubmission_file_(.+).pdf$";

    String[] naming_conventions = {old_convention, new_convention, upload_convention};

    for (int index = 0; index < 3; index++) {
      Pattern pattern = Pattern.compile(naming_conventions[index]);
      if (pattern.matcher(pdf.getName()).matches()) return index;
    }

    return 3;
  }
}
