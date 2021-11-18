import java.util.HashMap;
import java.util.List;

public class Utils {
    public static final String SEPARATOR = System.getProperty("file.separator");

    public static String inputPath() {
        return System.getProperty("user.home") + SEPARATOR + "Desktop" + SEPARATOR + "filesToRename";
    }

    public static String inputCSVPath() {
        return inputPath() + SEPARATOR + "input.csv";
    }

    public static String outputRenamedPath() {
        return inputPath() + SEPARATOR + "renamedFiles";
    }

    public static String outputReviewPath() {
        return inputPath() + SEPARATOR + "manualReview";
    }

    public static class IdentifierExtractor {
        private static HashMap<String, Student> hashMapId;
        private static HashMap<String, List<Student>> hashMapName;

        public static void setHashMaps(HashMap<String, Student> hashMapId, HashMap<String, List<Student>> hashMapName) {
            IdentifierExtractor.hashMapId = hashMapId;
            IdentifierExtractor.hashMapName = hashMapName;
        }

        public static String getIdentifier(FileForRename fileForRename) {
            String identifier = "DISCARDED";
            List<Student> studentList = hashMapName.get(fileForRename.studentName());

            if (studentList != null) {
                if (studentList.size() == 1) {
                    identifier = studentList.get(0).identifier();
                    removeStudent(studentList.get(0));
                } else {
                    identifier = getIdentifierFromId(fileForRename);
                }
            }

            return identifier;
        }

        private static String getIdentifierFromId(FileForRename fileForRename) {
            String identifier = "SET_ASIDE";

            for (String id : fileForRename.potentialIds()) {
                Student student = hashMapId.get(id);

                if (student != null) {
                    identifier = student.identifier();
                    removeStudent(student);
                }
            }
            return identifier;
        }

        private static void removeStudent(Student student) {
            hashMapId.remove(student.id());
            List<Student> studentList = hashMapName.get(student.name());

            if (studentList.size() == 1) {
                hashMapName.remove(student.name());
            } else {
                studentList.removeIf(student1 -> student.id().equals(student1.id()));
            }
        }
    }
}
