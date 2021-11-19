package com.zephyr.filefixer.input.pdf;

import java.io.File;
import java.util.List;

/**
 * The {@code Student} record stores a PDF, the original name of the PDF, the name of the student
 * that submitted it, and all the potential IDs for that student.
 */
public record FileForRename(
        File file,
        String originalSubmissionName,
        String studentName,
        List<String> potentialIds
) {
}
