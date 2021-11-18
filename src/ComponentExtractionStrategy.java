import java.io.File;
import java.util.List;

public interface ComponentExtractionStrategy {
    List<FileForRename> extractComponents(List<File> listOfFiles);
}
