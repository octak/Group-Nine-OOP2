package com.zephyr.filefixer.exceptions;

/**
 * Signals that a CSV input file is invalid.
 *
 * <p>Thrown when a CSV file does not contain the headings "Identifier",
 * "Full name", "ID number", "Email address", "Status", "Grade",
 * "Maximum Grade", "Grade can be changed", "Last modified (grade)" and
 * "Feedback comments" and at least one entry.
 */
public class InvalidCSVException extends Exception {
    public InvalidCSVException(String message) {
        super(message);
    }
}
