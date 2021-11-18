import java.io.File;
import java.text.MessageFormat;
import java.util.List;

public record NamingConventions(List<File> newMyELearningConvention,
                                List<File> oldMyELearningConvention,
                                List<File> uploadConvention,
                                List<File> noConvention) {
    @Override
    public String toString() {
        return MessageFormat.format("New Convention: {0}\nOld Convention: {1}\nUpload Convention: {2}\nNo Convention: {3}",
                newMyELearningConvention, oldMyELearningConvention, uploadConvention, noConvention);
    }
}
