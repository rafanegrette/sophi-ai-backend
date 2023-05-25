package com.rafanegrette.books.string.formats;

public class StringFormatFunctions {

    public static String formatPages(String text) {
        return text.toString().replace("\t\t\t\t\t\t", "\n\n")
        .replace("\t", " ")
        .replace("  ", " ")
        .replace("\n \n","\n\n\n");
    }
    
    public static String formatTitles(String text) {
        return text.toString().replace("\t\t\t\t\t\t", "\n\n")
        .replace("\t", " ")
        .replace("  ", " ");
    }
}
