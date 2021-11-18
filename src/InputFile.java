import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;

public class InputFile {
    private final FileForRename submission;
    private String identifier = null;
    private InputFileState state;

    public InputFile(FileForRename submission) {
        this.submission = submission;
        this.state = new InitialState();
    }

    public void extractIdentifier() {
        state.extractIdentifier(this);
    }

    public void relocate() throws IOException {
        state.relocate(this);
    }

    public void setState(InputFileState state) {
        this.state = state;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFilename() {
        return submission.file().getName();
    }

    public Path getFilePath() {
        return submission.file().toPath();
    }

    public FileForRename getSubmission() {
        return submission;
    }

    public String getNewFilename() {
        return MessageFormat.format(
                "{0}_{1}_assignsubmission_file_{2}.pdf",
                submission.studentName(),
                identifier,
                submission.originalSubmissionName()
        );
    }
}