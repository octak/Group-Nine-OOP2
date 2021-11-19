package com.zephyr.filefixer.input.pdf.naming_conventions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Container class for a static method to determine the naming conventions of a {@code File} array of
 * the PDF files in the input directory.
 */
public class FilenameAnalyser {

    /**
     * Sorts PDF files based on their naming conventions.
     *
     * @param filesToRename an array of PDF files
     * @return a {@code NamingConventions} object
     * @see NamingConventions
     */
    public static NamingConventions analyse(File[] filesToRename) {
        List<File> newMyELearningConvention = new ArrayList<>();
        List<File> oldMyELearningConvention = new ArrayList<>();
        List<File> uploadConvention = new ArrayList<>();
        List<File> noConvention = new ArrayList<>();
        String filename;

        for (File file : filesToRename) {
            filename = file.getName();
            if (isNewMyELearningConvention(filename))
                newMyELearningConvention.add(file);
            else if (isOldMyELearningConvention(filename))
                oldMyELearningConvention.add(file);
            else if (isUploadConvention(filename))
                uploadConvention.add(file);
            else
                noConvention.add(file);
        }

        return new NamingConventions(newMyELearningConvention, oldMyELearningConvention, uploadConvention, noConvention);
    }

    /**
     * Determine if the PDF is named according to the new myeLearning convention.
     *
     * <p>A PDF is named according to this convention if it follows the naming scheme
     * Code1-Code2_FirstName_LastName_Code3_SubmissionName.pdf
     *
     * @param filename the name of the PDF file
     * @return true if the PDF follows the naming convention
     */
    private static boolean isNewMyELearningConvention(String filename) {
        Pattern pattern = Pattern.compile("^([0-9]+)-([0-9]+)_([a-zA-Z-'_]+)_([0-9]+)_(.+).pdf$");
        Matcher matcher = pattern.matcher(filename);
        return matcher.matches();
    }

    /**
     * Determine if the PDF is named according to the old myeLearning convention.
     *
     * <p>A PDF is named according to this convention if it follows the naming scheme
     * Code1-Code2_FirstName_LastName_SubmissionName_Code3_Code4.pdf
     *
     * @param filename the name of the PDF file
     * @return true if the PDF follows the naming convention
     */
    private static boolean isOldMyELearningConvention(String filename) {
        Pattern pattern = Pattern.compile("^([0-9]+)-([0-9]+)_(.+)_([0-9]+)_([0-9]+).pdf$");
        Matcher matcher = pattern.matcher(filename);
        return matcher.matches();
    }

    /**
     * Determine if the PDF is named according to the new myeLearning convention.
     *
     * <p>A PDF is named according to this convention if it follows the naming scheme
     * Name1 Name2_Identifier_assignsubmission_file_SubmissionName.pdf
     *
     * @param filename the name of the PDF file
     * @return true if the PDF follows the naming convention
     */
    private static boolean isUploadConvention(String filename) {
        Pattern pattern = Pattern.compile("^([a-zA-Z+\\s]+)_([0-9]+)_assignsubmission_file_(.+).pdf$");
        Matcher matcher = pattern.matcher(filename);
        return matcher.matches();
    }
}
