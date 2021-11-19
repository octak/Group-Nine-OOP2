package com.zephyr.filefixer.input.pdf.naming_conventions;

import java.io.File;
import java.util.List;

/**
 * A {@code NamingConventions} object contains lists of PDF files for each naming convention.
 *
 * @see FilenameAnalyser
 */
public record NamingConventions(
        List<File> newMyELearningConvention,
        List<File> oldMyELearningConvention,
        List<File> uploadConvention,
        List<File> noConvention
) {
}
