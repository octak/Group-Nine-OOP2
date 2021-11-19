package com.zephyr.filefixer.input.pdf.filestates;

import com.zephyr.filefixer.input.pdf.InputFile;

/**
 * This interface represents the 4 states that an {@code InputFile} can be in.
 *
 * @see InputFile
 * @see InitialState
 * @see SetAsideState
 * @see PrimedState
 * @see DiscardedState
 */
public interface InputFileState {
    /**
     * This method accepts an {@code InputFile} object and sets the value of its identifier. The state
     * of the {@code InputFile} is changed based on the value of the determined identifier.
     *
     * @param inputFile an {@code InputFile} object
     * @see InputFile
     * @see InitialState
     * @see SetAsideState
     * @see PrimedState
     * @see DiscardedState
     */
    default void extractIdentifier(InputFile inputFile) {

    }

    /**
     * Relocates the PDF referenced by an {@code InputFile} object
     *
     * <p>If the {@code InputFile} is in a {@code PrimedState}, it will be copied to the specified output directory
     * on the user's machine. If the {@code InputFile} is in a {@code DiscardedState}, it will be moved to the output directory
     * on the user's machine.
     *
     * @param inputFile an {@code InputFile} object
     *                  *
     * @see InputFile
     * @see InitialState
     * @see SetAsideState
     * @see PrimedState
     * @see DiscardedState
     */
    default void relocate(InputFile inputFile) {

    }


}
