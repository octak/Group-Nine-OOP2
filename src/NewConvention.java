import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewConvention implements ComponentExtractionStrategy {

    @Override
    public List<FileForRename> extractComponents(List<File> listOfFiles) {
        Matcher matcher;
        List<FileForRename> filesForRename = new ArrayList<>();
        Pattern pattern = Pattern.compile("^([0-9]+)-([0-9]+)_([a-zA-Z-'_]+)_([0-9]+)_(.+).pdf$");

        if (listOfFiles.isEmpty()) {
            return filesForRename;
        }

        for (File file : listOfFiles) {
            matcher = pattern.matcher(file.getName());
            matcher.matches();

            filesForRename.add(
                    new FileForRename(
                            file,
                            matcher.group(5),
                            matcher.group(3).replace("_", " "),
                            getPotentialIds(matcher.group(5)
                            )
                    )
            );
        }
        return filesForRename;
    }

    private List<String> getPotentialIds(String submissionName) {
        List<String> potentialIds = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(submissionName);

        while (matcher.find()) {
            potentialIds.add(matcher.group());
        }
        return potentialIds;
    }
}
