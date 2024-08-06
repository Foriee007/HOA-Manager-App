package org.cenchev.hoamanagerapp.services.impl;

public class FormatTextCapitalWords {
    private FormatTextCapitalWords() {

    }

    public static String formatText(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        String[] words = text.trim().split("\\s+");
        StringBuilder capitalizedText = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                capitalizedText.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    capitalizedText.append(word.substring(1).toLowerCase());
                }
                capitalizedText.append(" ");
            }
        }
        return capitalizedText.toString().trim();
    }

    public static String capitalizeAllLetters(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        return text.toUpperCase();
    }
}
