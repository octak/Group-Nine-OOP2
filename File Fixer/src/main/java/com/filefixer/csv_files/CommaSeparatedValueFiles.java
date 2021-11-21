package com.filefixer.csv_files;

import com.filefixer.student.Student;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Attempts to retrieve a CSV file to use as input. It scans a list of CSV files and determines if a
 * valid input file is found.
 *
 * <p>In the case where there is only one CSV file, it is assumed to be the input file. If the list
 * contains multiple CSV files then the method will consider the CSV file called "Students.csv" the
 * input file. In either case, once an input file is determined, its validity is checked. It returns
 * the valid input CSV file. Returns an empty {@code Optional<File>} if a valid CSV file is not
 * located.
 *
 * @param csvList a {@code List} of CSV files
 * @return a valid CSV input file or an empty {@code Optional<File>} if no valid files exist
 */
public class CommaSeparatedValueFiles {
  public static Optional<File> getCSVInput(List<File> csvList) {
    var stream = csvList.stream();

    if (csvList.size() == 1)
      return validate(csvList.get(0)) ? Optional.of(csvList.get(0)) : Optional.empty();
    else if (csvList.size() > 1 && stream.noneMatch(csv -> "Students.csv".equals(csv.getName())))
      return Optional.empty();

    File csv = stream.filter(user -> "Students.csv".equals(user.getName())).findAny().get();
    return validate(csv) ? Optional.of(csv) : Optional.empty();
  }

  /**
   * This method checks the validity of a CSV file.
   *
   * <p>A CSV input file is valid if it contains the headings "Identifier", * "Full name", "ID
   * number", "Email address", "Status", "Grade", * "Maximum Grade", "Grade can be changed", "Last
   * modified (grade)" and * "Feedback comments" and at least one entry.
   *
   * @param csvFile the CSV file to be checked
   * @return true if the CSV file is valid
   */
  public static boolean validate(File csvFile) {
    String correctHeadings =
        "Identifier,Full name,ID number,Email address,Status,Grade,Maximum Grade,Grade can be changed,Last modified (grade),Feedback comments";

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile.getPath()))) {
      return br.readLine().equals(correctHeadings) && br.ready();
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Accepts a CSV file as input and parses it to generate {@code Student} objects.
   *
   * @param csvFile the CSV file containing student entries
   * @return a {@code List} of {@code Student} objects
   */
  public static List<Student> getStudents(File csvFile) {
    Matcher matcher;
    List<Student> studentList = new ArrayList<>();
    Pattern pattern = Pattern.compile("^Participant (\\d+),([a-zA-Z-' ]+),(\\d+)(.+)");

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile.getPath()))) {
      while (br.ready()) {
        matcher = pattern.matcher(br.readLine());
        if (matcher.matches())
          studentList.add(new Student(matcher.group(1), matcher.group(2), matcher.group(3)));
      }
    } catch (IOException e) {
      return new ArrayList<>();
    }
    return studentList;
  }
}
