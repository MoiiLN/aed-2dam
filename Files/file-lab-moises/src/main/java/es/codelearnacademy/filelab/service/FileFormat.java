package es.codelearnacademy.filelab.service;

import java.util.Locale;

public enum FileFormat {
    CSV,
    JSON,
    XML;

    public static FileFormat from(String value) {
        return FileFormat.valueOf(value.toUpperCase(Locale.ROOT));
    }
}
