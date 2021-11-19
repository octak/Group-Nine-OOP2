package com.zephyr.filefixer.input.pdf.naming_conventions;

import com.zephyr.filefixer.input.pdf.FileForRename;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains methods which retrieve the student's name, possible IDs and the original filenames of a list of PDF files.
 */
public class OldConvention implements ComponentExtractionStrategy {

    /**
     * Retrieves the student's name, possible IDs and the original filenames from an array of PDF files named
     * according to the old myeLearning convention by splitting the filename around the underscores and creates
     * a list of {@code FileForRename} objects.
     *
     * @param listOfFiles an array of PDF files
     * @return a list of {@code FileForRename} objects
     */
    @Override
    public List<FileForRename> extractComponents(List<File> listOfFiles) {
        List<FileForRename> filesForRename = new ArrayList<>();

        for (File file : listOfFiles) {
            var components = file.getName().split("_");

            String studentName = components[1] + " " + components[2];
            String submissionName = rebuildSubmissionName(components);
            List<String> potentialIds = getPotentialIds(submissionName);

            filesForRename.add(new FileForRename(file, submissionName, studentName, potentialIds));
        }

        return filesForRename;
    }


    /**
     * Generates original name of the student's PDF submission.
     *
     * @param components an array of the parts of the PDF filename
     * @return a {@code String} of the name of the student's PDF submission
     */
    private String rebuildSubmissionName(String[] components) {
        StringBuilder sb = new StringBuilder();

        for (int i = 3; i < components.length - 2; i++)
            sb.append(components[i]).append("_");

        sb.setLength(sb.length() - 1);

        return sb.toString();
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
//
//        return potentialIds;
//    }
}
