import java.text.MessageFormat;

public record Student(String identifier, String name, String id) {
    @Override
    public String toString() {
        return MessageFormat.format("[{0}, {1}, {2}]", identifier, name, id);
    }
}
