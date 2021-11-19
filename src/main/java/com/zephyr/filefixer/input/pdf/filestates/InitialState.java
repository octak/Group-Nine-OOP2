package com.zephyr.filefixer.input.pdf.filestates;

import com.zephyr.filefixer.input.pdf.InputFile;
import com.zephyr.filefixer.utils.Utils;

/**
 * An {@code InputFile} object is in the {@code InitialState} when it is instantiated and before any attempt is made to
 * retrieve its identifier.
 *
 * @see InputFile
 * @see InitialState
 * @see SetAsideState
 * @see PrimedState
 * @see DiscardedState
 */
public class InitialState implements InputFileState {

    /**
     * Attempts to retrieve an {@code InputFile} object's identifier.
     *
     * <p>If the student referenced by the {@code InputFile} has a name which is unique in the CSV, then the identifier
     * of the {@code InputFile} will be set to the student's identifier and the state of the {@code InputFile} is changed
     * to the {@code PrimedState}. If the student's name is not unique in the CSV, that is, one or more of the students
     * share the same name, the state is set to the {@code SetAsideState}. If the name does not exist in the CSV, the state
     * is changed to the {@code DiscardedState}.
     *
     * @param inputFile an {@code InputFile} object
     */
    @Override
    public void extractIdentifier(InputFile inputFile) {
        String identifier = Utils.IdentifierExtractor.getIdentifier(inputFile.getSubmission());

        switch (identifier) {
            case "SET_ASIDE" -> inputFile.setState(new SetAsideState());
            case "DISCARDED" -> inputFile.setState(new DiscardedState());
            default -> {
                inputFile.setState(new PrimedState());
                inputFile.setIdentifier(identifier);
            }
        }
    }
}