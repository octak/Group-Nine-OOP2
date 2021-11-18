import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OldConvention implements ComponentExtractionStrategy {
    @Override
    public List<FileForRename> extractComponents(List<File> listOfFiles) {
        List<FileForRename> filesForRename = new ArrayList<>();

        for (File file : listOfFiles) {
            var components = file.getName().split("_");

            String studentName = components[1] + " " + components[2];
            String submissionName = rebuildSubmissionName(components);
            List<String> potentialIds = getPotentialIds(submissionName);

            filesForRename.add(new FileForRename(file, submissionName, studentName, potentialIds));
        }

        return filesForRename;
    }


    private String rebuildSubmissionName(String[] components) {
        StringBuilder sb = new StringBuilder();

        for (int i = 3; i < components.length - 2; i++)
            sb.append(components[i]).append("_");

        sb.setLength(sb.length() - 1);

        return sb.toString();
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
