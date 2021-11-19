package com.zephyr.filefixer.exceptions;

/**
 * Signals that an input file does not exist.
 *
 * <p>Thrown when the program cannot identify the CSV file or the PDF
 * files that it requires.
 */
public class InputFileNotFoundException extends Exception {
    public InputFileNotFoundException(String message) {
        super(message);
    }
}
