package com.zephyr.filefixer.input.pdf.filestates;

import com.zephyr.filefixer.input.pdf.InputFile;
import com.zephyr.filefixer.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * An {@code InputFile} object is in the {@code PrimedState} if the name of the student that it specifies is retrieved
 * from the CSV document.
 *
 * @see InputFile
 * @see InitialState
 * @see SetAsideState
 * @see PrimedState
 * @see DiscardedState
 */
public class PrimedState implements InputFileState {

    /**
     * Copies the PDF referenced by the {@code InputFile} to the output directory (~/Desktop/filesToRename/renamedFiles)
     *
     * @param inputFile an {@code InputFile} object
     */
    @Override
    public void relocate(InputFile inputFile) {
        String outputPath = Utils.outputRenamedPath() + Utils.separator + inputFile.getNewFilename();
        File outputFile = new File(outputPath);

        try {
            Files.copy(inputFile.getFilePath(), outputFile.toPath(), REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error while copying file: " + inputFile.getFilename());
        }

    }
}
