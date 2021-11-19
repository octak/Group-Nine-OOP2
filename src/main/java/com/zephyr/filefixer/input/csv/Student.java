package com.zephyr.filefixer.input.csv;

import java.text.MessageFormat;

/**
 * The {@code Student} record stores a student's identifier, name and ID.
 */
public record Student(String identifier, String name, String id) {
    @Override
    public String toString() {
        return MessageFormat.format("[{0}, {1}, {2}]", identifier, name, id);
    }
}
