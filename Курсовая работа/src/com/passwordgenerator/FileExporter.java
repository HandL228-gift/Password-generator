package com.passwordgenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileExporter {

    public static void exportPassword(String password, PasswordConfig config,
                                      String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(" СГЕНЕРИРОВАННЫЙ ПАРОЛЬ \n");
            writer.write("Дата генерации: " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + "\n");
            writer.write("Длина: " + password.length() + " символов\n\n");

            writer.write("Параметры генерации:\n");
            writer.write("  - Цифры (0-9): " + (config.isUseDigits() ? "ДА" : "НЕТ") + "\n");
            writer.write("  - Латинские строчные: " + (config.isUseLowercase() ? "ДА" : "НЕТ") + "\n");
            writer.write("  - Латинские прописные: " + (config.isUseUppercase() ? "ДА" : "НЕТ") + "\n");
            writer.write("  - Специальные символы: " + (config.isUseSpecialChars() ? "ДА" : "НЕТ") + "\n");
            writer.write("  - Русские строчные: " + (config.isUseRussianLowercase() ? "ДА" : "НЕТ") + "\n");
            writer.write("  - Русские прописные: " + (config.isUseRussianUppercase() ? "ДА" : "НЕТ") + "\n\n");

            writer.write("Пароль:\n");

            if (password.length() > 100) {
                for (int i = 0; i < password.length(); i += 100) {
                    int end = Math.min(i + 100, password.length());
                    writer.write(password.substring(i, end) + "\n");
                }
            } else {
                writer.write(password + "\n");
            }

            writer.write("\n КОНЕЦ ДОКУМЕНТА \n");
        }
    }

    public static void exportFullReport(String password, PasswordConfig config,
                                        StrengthCalculator calculator,
                                        String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(" ПОЛНЫЙ ОТЧЕТ О ГЕНЕРАЦИИ ПАРОЛЯ \n");
            writer.write("Сгенерировано: " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + "\n\n");

            writer.write("1. КОНФИГУРАЦИЯ ГЕНЕРАЦИИ\n");
            writer.write("   Длина пароля: " + config.getLength() + " символов\n");
            writer.write("   Используемые наборы символов:\n");
            if (config.isUseDigits()) writer.write("      Цифры: " + config.getRequiredDigits() + "\n");
            if (config.isUseLowercase()) writer.write("      Латинские строчные буквы\n");
            if (config.isUseUppercase()) writer.write("      Латинские прописные буквы\n");
            if (config.isUseSpecialChars()) writer.write("      Специальные символы: " + config.getRequiredSpecialChars() + "\n");
            if (config.isUseRussianLowercase()) writer.write("      Русские строчные буквы\n");
            if (config.isUseRussianUppercase()) writer.write("      Русские прописные буквы\n");

            writer.write("\n2. СГЕНЕРИРОВАННЫЙ ПАРОЛЬ\n");
            writer.write("   Длина: " + password.length() + " символов\n");
            writer.write("   Первые 100 символов: " +
                    (password.length() > 100 ? password.substring(0, 100) + "..." : password) + "\n");

            writer.write("\n3. АНАЛИЗ СТОЙКОСТИ\n");
            writer.write("   Энтропия: " +
                    new DecimalFormat("#,##0.00").format(calculator.calculateEntropy(config)) + " бит\n");
            writer.write("   Количество возможных комбинаций: " +
                    String.format("%,d", calculator.calculateCombinations(config)) + "\n");
            writer.write("   Примерное время взлома (при 1 млрд попыток/сек): " +
                    calculator.estimateCrackTime(config) + "\n");

            writer.write("\n4. СТАТИСТИКА\n");
            writer.write("   Всего символов: " + password.length() + "\n");
            writer.write("   Уникальных символов: " + countUniqueChars(password) + "\n");

            writer.write("\n=== ОТЧЕТ СФОРМИРОВАН ===\n");
        }
    }

    private static int countUniqueChars(String str) {
        return (int) str.chars().distinct().count();
    }
}