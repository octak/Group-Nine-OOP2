package com.zephyr.filefixer.exceptions;

/**
 * Signals that the input directory cannot be located.
 *
 * <p>Thrown when the program cannot identify the input directory (~/Desktop/filesToRename/).
 */
public class InputDirectoryNotFoundException extends Exception {
    public InputDirectoryNotFoundException(String message) {
        super(message);
    }
}
