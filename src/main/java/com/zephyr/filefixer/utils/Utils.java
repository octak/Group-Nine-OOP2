package com.zephyr.filefixer.utils;

import com.zephyr.filefixer.input.csv.Student;
import com.zephyr.filefixer.input.pdf.FileForRename;

import java.util.HashMap;
import java.util.List;

/**
 * Utility functions
 */
public class Utils {
    // System-dependent separator. "/" for macOS and Linux, "\" for Windows.
    public static final String separator = System.getProperty("file.separator");

    public static String inputPath() {
        return System.getProperty("user.home") + separator + "Desktop" + separator + "filesToRename";
    }

    public static String outputRenamedPath() {
        return inputPath() + separator + "renamedFiles";
    }

    public static String outputReviewPath() {
        return inputPath() + separator + "manualReview";
    }

    /**
     * Static class containing methods to retrieve a student's identifier.
     */
    public static class IdentifierExtractor {
        private static HashMap<String, Student> hashMapId;
        private static HashMap<String, List<Student>> hashMapName;

        /**
         * Loads HashMaps into class.
         *
         * <p>hashMapId is used to attempt to retrieve the identifier if the attempt using hashMapName resulted in a naming
         * conflict.
         *
         * @param hashMapId   HashMap which maps a student ID to a {@code Student} object
         * @param hashMapName HashMap which maps a student's name to a list of {@code Student} objects
         */
        public static void setHashMaps(HashMap<String, Student> hashMapId, HashMap<String, List<Student>> hashMapName) {
            IdentifierExtractor.hashMapId = hashMapId;
            IdentifierExtractor.hashMapName = hashMapName;
        }

        /**
         * Attempts to use the HashMap of names from the CSV to retrieve a student's identifier.
         *
         * @param fileForRename data on a PDF file and the student that submitted it
         * @return student identifier or "DISCARDED" if the student's name does not exist in the CSV, "SET_ASIDE" if there
         * are naming conflicts.
         */
        public static String getIdentifier(FileForRename fileForRename) {
            String identifier = "DISCARDED";
            List<Student> studentList = hashMapName.get(fileForRename.studentName());

            if (studentList != null) {
                if (studentList.size() == 1) {
                    identifier = studentList.get(0).identifier();
                    removeStudent(studentList.get(0));
                } else {
                    identifier = getIdentifierFromId(fileForRename);
                }
            }

            return identifier;
        }

        /**
         * Attempts to use the HashMap of ISs from the CSV to retrieve a student's identifier.
         *
         * @param fileForRename data on a PDF file and the student that submitted it
         * @return student identifier or "DISCARDED" if the student's name does not exist in the CSV, "SET_ASIDE" if there
         * are unresolved naming conflicts.
         */
        private static String getIdentifierFromId(FileForRename fileForRename) {
            String identifier = "SET_ASIDE";

            for (String id : fileForRename.potentialIds()) {
                Student student = hashMapId.get(id);

                if (student != null) {
                    identifier = student.identifier();
                    removeStudent(student);
                }
            }
            return identifier;
        }

        /**
         * Removes a student from hashMapId and hashMapName
         *
         * @param student student to be removed
         */
        private static void removeStudent(Student student) {
            hashMapId.remove(student.id());
            List<Student> studentList = hashMapName.get(student.name());

            if (studentList.size() == 1) {
                hashMapName.remove(student.name());
            } else {
                studentList.removeIf(student1 -> student.id().equals(student1.id()));
            }
        }
    }
}
