package com.zephyr.filefixer.input.pdf;

import com.zephyr.filefixer.input.pdf.filestates.InitialState;
import com.zephyr.filefixer.input.pdf.filestates.InputFileState;

import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code InputFile} class contains methods for the relocation of a PDF file and performs the relocation.
 */
public class InputFile {
    private final FileForRename submission;
    private String identifier = null;
    private InputFileState state;

    /**
     * {@code InputFile} constructor. Sets the state of the object to the {@code InitialState}.
     *
     * @param submission a {@code FileForRename} object representing a PDF and information about the student
     */
    public InputFile(FileForRename submission) {
        this.submission = submission;
        this.state = new InitialState();
    }

    /**
     * Static function for converting a list of {@code FileForRename} into a list of {@code InputFile}.
     *
     * @param filesForRename a list of {@code FileForRename} objects representing a PDF and information about the student
     * @return a list of a {@code InputFile} objects
     */
    public static List<InputFile> toInputFiles(List<FileForRename> filesForRename) {
        List<InputFile> inputFiles = new ArrayList<>();
        for (var file : filesForRename) {
            inputFiles.add(new InputFile(file));
        }
        return inputFiles;
    }

    /**
     * Attempts to retrieve the student's identifier.
     *
     * <p>Does nothing in the {@code PrimedState} and {@code DiscardedState}.
     */
    public void extractIdentifier() {
        state.extractIdentifier(this);
    }

    /**
     * Attempts to retrieve the student's identifier.
     *
     * <p>Does nothing in the {@code InitialState} and {@code SetAsideState}.
     *
     * @throws IOException if an I/O error occurs
     */
    public void relocate() {
        state.relocate(this);
    }

    /**
     * Changes the state of the {@code InputFile}.
     *
     * @param state the new state of the {@code InputFile}
     */
    public void setState(InputFileState state) {
        this.state = state;
    }

    /**
     * Sets the identifier of the {@code InputFile} object.
     *
     * @param identifier the student's identifier
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Gets the name of the PDF.
     *
     * @return the name of the PDF
     */
    public String getFilename() {
        return submission.file().getName();
    }

    /**
     * Gets the filepath of the PDF.
     *
     * @return the file path of the PDF
     */
    public Path getFilePath() {
        return submission.file().toPath();
    }

    /**
     * Returns the {@code FileForRename} object of the {@code InputFile}.
     *
     * @return the {@code FileForRename} object of the {@code InputFile}
     */
    public FileForRename getSubmission() {
        return submission;
    }

    /**
     * Generates the new name of th PDF conforming to the myeLearning upload convention.
     *
     * @return the name of the PDF conforming to the myeLearning upload convention
     */
    public String getNewFilename() {
        return MessageFormat.format(
                "{0}_{1}_assignsubmission_file_{2}.pdf",
                submission.studentName(),
                identifier,
                submission.originalSubmissionName()
        );
    }
}