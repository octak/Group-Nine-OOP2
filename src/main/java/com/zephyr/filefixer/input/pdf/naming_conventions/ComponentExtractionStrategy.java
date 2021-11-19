package com.zephyr.filefixer.input.pdf.naming_conventions;

import com.zephyr.filefixer.input.pdf.FileForRename;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interface for methods which parse the name of a PDF and generate {@code FileForRename} objects
 *
 * @see NewConvention
 * @see OldConvention
 * @see FileForRename
 */

public interface ComponentExtractionStrategy {

    /**
     * Generates a list of {@code FileForRename} objects from an array of PDF files
     *
     * @param listOfFiles an array of PDF files
     * @return a list of {@code FileForRename} objects
     */
    List<FileForRename> extractComponents(List<File> listOfFiles);

    /**
     * Uses regex to retrieve all the number sequences in the filename of a student's PDF submission.
     *
     * @param submissionName the original name of the PDF file uploaded by the student
     * @return a list of numbers which could be the student's ID
     */
    default List<String> getPotentialIds(String submissionName) {
        List<String> potentialIds = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(submissionName);

        while (matcher.find()) {
            potentialIds.add(matcher.group());
        }

        return potentialIds;
    }
}
