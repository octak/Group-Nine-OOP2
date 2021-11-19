package com.zephyr.filefixer.input.pdf.filestates;

import com.zephyr.filefixer.input.pdf.InputFile;
import com.zephyr.filefixer.utils.Utils;

/**
 * An {@code InputFile} object is in the {@code SetAsideState} if multiple students in the CSV file have the same name as
 * the one referenced by the {@code InputFile}.
 *
 * @see InputFile
 * @see InitialState
 * @see SetAsideState
 * @see PrimedState
 * @see DiscardedState
 */
public class SetAsideState implements InputFileState {

    /**
     * Attempts to retrieve an {@code InputFile} object's identifier for the second time.
     *
     * <p>If the student referenced by the {@code InputFile} has a name which is unique in the CSV, then the identifier
     * of the {@code InputFile} will be set to the student's identifier and the state of the {@code InputFile} is changed
     * to the {@code PrimedState}. If the student's name is not unique in the CSV, that is, one or more of the students
     * share the same name, the state is set to the {@code DiscardedState}.
     *
     * @param inputFile an {@code InputFile} object
     */
    @Override
    public void extractIdentifier(InputFile inputFile) {
        String identifier = Utils.IdentifierExtractor.getIdentifier(inputFile.getSubmission());
        inputFile.setIdentifier(identifier);

        if (identifier.equals("SET_ASIDE")) {
            inputFile.setState(new DiscardedState());
        } else {
            inputFile.setState(new PrimedState());
        }
    }
}