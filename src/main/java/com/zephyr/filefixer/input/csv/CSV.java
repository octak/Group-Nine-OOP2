package com.zephyr.filefixer.input.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class consists exclusively of static methods that operate on CSV files.
 */
public class CSV {
    /**
     * Accepts a CSV file as input and parses it to retrieve the name, identifier
     * and ID number of every student contained in it.
     *
     * @param csvFile the CSV file containing the student information
     * @return a list of {@code Student} objects
     */
    public static List<Student> analyse(File csvFile) {
        Matcher matcher;
        List<Student> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("^Participant (\\d+),([a-zA-Z-' ]+),(\\d+)(.+)");

        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile.getPath()));
            while (br.ready()) {
                matcher = pattern.matcher(br.readLine());
                if (matcher.matches())
                    list.add(new Student(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
            br.close();
        } catch (IOException ignored) {

        }

        return list;
    }

    /**
     * This method checks the validity of a CSV file.
     *
     * <p>A CSV input file is valid if it contains the headings "Identifier",
     * * "Full name", "ID number", "Email address", "Status", "Grade",
     * * "Maximum Grade", "Grade can be changed", "Last modified (grade)" and
     * * "Feedback comments" and at least one entry.
     *
     * @param csvFile the CSV file to be checked
     * @return true if the CSV file is valid
     */
    public static boolean validate(File csvFile) {
        String headings;
        String correctHeadings = "Identifier," +
                "Full name," +
                "ID number," +
                "Email address," +
                "Status," +
                "Grade," +
                "Maximum Grade," +
                "Grade can be changed," +
                "Last modified (grade)," +
                "Feedback comments";
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile.getPath()));
            headings = br.readLine();

            if (!br.ready())
                return false;

            br.close();
        } catch (IOException e) {
            return false;
        }

        return headings.equals(correctHeadings);
    }
}
