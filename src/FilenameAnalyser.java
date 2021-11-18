import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilenameAnalyser {
    public NamingConventions analyseFilenames(File[] filesToRename) {
        List<File> newMyELearningConvention = new ArrayList<>();
        List<File> oldMyELearningConvention = new ArrayList<>();
        List<File> uploadConvention = new ArrayList<>();
        List<File> noConvention = new ArrayList<>();
        String filename;

        for (File file : filesToRename) {
            filename = file.getName();
            if (isNewMyELearningConvention(filename))
                newMyELearningConvention.add(file);
            else if (isOldMyELearningConvention(filename))
                oldMyELearningConvention.add(file);
            else if (isUploadConvention(filename))
                uploadConvention.add(file);
            else
                noConvention.add(file);
        }

        return new NamingConventions(newMyELearningConvention, oldMyELearningConvention, uploadConvention, noConvention);
    }

    private boolean isNewMyELearningConvention(String filename) {
        Pattern pattern = Pattern.compile("^([0-9]+)-([0-9]+)_([a-zA-Z-'_]+)_([0-9]+)_(.+).pdf$");
        Matcher matcher = pattern.matcher(filename);
        return matcher.matches();
    }

    private boolean isOldMyELearningConvention(String filename) {
        Pattern pattern = Pattern.compile("^([0-9]+)-([0-9]+)_(.+)_([0-9]+)_([0-9]+).pdf$");
        Matcher matcher = pattern.matcher(filename);
        return matcher.matches();
    }

    private boolean isUploadConvention(String filename) {
        Pattern pattern = Pattern.compile("^([a-zA-Z+\\s]+)_([0-9]+)_assignsubmission_file_(.+).pdf$");
        Matcher matcher = pattern.matcher(filename);
        return matcher.matches();
    }
}
