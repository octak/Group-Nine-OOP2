package com.zephyr.filefixer.input.pdf.filestates;

import com.zephyr.filefixer.input.pdf.InputFile;
import com.zephyr.filefixer.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * An {@code InputFile} object is in the {@code DiscardedState} if the name of the student that it specifies is not present
 * in the CSV document.
 *
 * @see InputFile
 * @see InitialState
 * @see SetAsideState
 * @see PrimedState
 * @see DiscardedState
 */
public class DiscardedState implements InputFileState {

    /**
     * Moves the PDF referenced by the {@code InputFile} to the output directory (~/Desktop/filesToRename/manualReview)
     *
     * @param inputFile an {@code InputFile} object
     */
    @Override
    public void relocate(InputFile inputFile) {
        String outputPath = Utils.outputReviewPath() + Utils.separator + inputFile.getFilename();
        File outputFile = new File(outputPath);

        try {
            Files.move(inputFile.getFilePath(), outputFile.toPath(), REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error while moving file: " + inputFile.getFilename());
        }
    }
}


