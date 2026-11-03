package com.VladBobin.mediachecklist;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateConverter {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @TypeConverter
    public static LocalDate toDate(String s) {
        if (s == null || s.isEmpty() || s.equals("---")) return null;
        return LocalDate.parse(s, FMT);
    }

    @TypeConverter
    public static String fromDate(LocalDate d) {
        return d == null ? null : d.format(FMT);
    }
}