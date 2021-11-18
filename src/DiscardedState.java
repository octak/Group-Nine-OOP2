import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class DiscardedState implements InputFileState {
    @Override
    public void relocate(InputFile inputFile) throws IOException {
        String outputPath = Utils.outputReviewPath() + Utils.SEPARATOR + inputFile.getFilename();
        File outputFile = new File(outputPath);

        Files.move(inputFile.getFilePath(), outputFile.toPath(), REPLACE_EXISTING);
    }
}
