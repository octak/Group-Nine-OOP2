package com.zephyr.filefixer.input.pdf.naming_conventions;

import com.zephyr.filefixer.input.pdf.FileForRename;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains methods which retrieve the student's name, possible IDs and the original filenames of a list of PDF files.
 */
public class NewConvention implements ComponentExtractionStrategy {

    /**
     * Uses regex to retrieve the student's name, possible IDs and the original filenames from an array of PDF files named
     * according to the new myeLearning convention and creates a list of {@code FileForRename} objects.
     *
     * @param listOfFiles an array of PDF files
     * @return a list of {@code FileForRename} objects
     */
    @Override
    public List<FileForRename> extractComponents(List<File> listOfFiles) {
        Matcher matcher;
        List<FileForRename> filesForRename = new ArrayList<>();
        Pattern pattern = Pattern.compile("^([0-9]+)-([0-9]+)_([a-zA-Z-'_]+)_([0-9]+)_(.+).pdf$");

        if (listOfFiles.isEmpty()) {
            return filesForRename;
        }

        for (File file : listOfFiles) {
            matcher = pattern.matcher(file.getName());
            matcher.matches();

            filesForRename.add(new FileForRename(file,
                    matcher.group(5),
                    matcher.group(3).replace("_", " "),
                    getPotentialIds(matcher.group(5))));
        }
        return filesForRename;
    }

//    /**
//     * Uses regex to retrieve all the number sequences in the filename of a student's PDF submission.
//     *
//     * @param   submissionName
//     *          the original name of the PDF file uploaded by the student
//     *
//     * @return  a list of numbers which could be the student's ID
//     */
//    private List<String> getPotentialIds(String submissionName) {
//        List<String> potentialIds = new ArrayList<>();
//
//        Pattern pattern = Pattern.compile("\\d+");
//        Matcher matcher = pattern.matcher(submissionName);
//
//        while (matcher.find()) {
//            potentialIds.add(matcher.group());
//        }
//        return potentialIds;
//    }
}
