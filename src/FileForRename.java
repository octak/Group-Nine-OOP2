import java.io.File;
import java.text.MessageFormat;
import java.util.List;

public record FileForRename(File file,
                            String originalSubmissionName,
                            String studentName,
                            List<String> potentialIds) {

    @Override
    public String toString() {
        return MessageFormat.format(
                "[{0}, {1}, {2}, {3}]",
                file.getName(),
                studentName,
                originalSubmissionName,
                potentialIds);
    }
}
