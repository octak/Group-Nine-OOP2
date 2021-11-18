import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class PrimedState implements InputFileState {
    @Override
    public void relocate(InputFile inputFile) throws IOException {
        String outputPath = Utils.outputRenamedPath() + Utils.SEPARATOR + inputFile.getNewFilename();
        File outputFile = new File(outputPath);

        Files.copy(inputFile.getFilePath(), outputFile.toPath(), REPLACE_EXISTING);
    }
}
