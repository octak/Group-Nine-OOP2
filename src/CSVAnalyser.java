import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVAnalyser {
    private final File CSVFile;

    public CSVAnalyser(File CSVFile) {
        this.CSVFile = CSVFile;
    }

    public List<Student> getStudentInformation() throws IOException {
        Matcher matcher;
        List<Student> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(CSVFile.getPath()));
        Pattern pattern = Pattern.compile("^Participant (\\d+),([a-zA-Z-' ]+),(\\d+)(.+)");


        while (br.ready()) {
            matcher = pattern.matcher(br.readLine());
            if (matcher.matches())
                list.add(new Student(matcher.group(1), matcher.group(2), matcher.group(3)));
        }

        return list;
    }

}
