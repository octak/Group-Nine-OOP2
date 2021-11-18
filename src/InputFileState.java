import java.io.IOException;

public interface InputFileState {
    default void extractIdentifier(InputFile inputFile) {

    }

    default void relocate(InputFile inputFile) throws IOException {

    }


}
