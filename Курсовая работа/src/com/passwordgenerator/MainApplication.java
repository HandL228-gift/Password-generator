package com.passwordgenerator;

import java.io.IOException;
import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("══════════════════════════════════════════════════════");
        System.out.println("       ГЕНЕРАТОР КРИПТОСТОЙКИХ ПАРОЛЕЙ НА JAVA");
        System.out.println("══════════════════════════════════════════════════════\n");

        try {
            PasswordConfig config = configurePassword(scanner);

            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║         ГЕНЕРАЦИЯ ПАРОЛЯ               ║");
            System.out.println("╚════════════════════════════════════════╝");

            PasswordGenerator generator = new PasswordGenerator(config);
            String password = generator.generatePassword();

            StrengthCalculator calculator = new StrengthCalculator();

            displayResults(password, config, calculator);

            if (askYesNoQuestion(scanner, "\nВыполнить анализ производительности для разных длин паролей?")) {
                PerformanceAnalyzer analyzer = new PerformanceAnalyzer(generator);
                analyzer.analyzePerformance();

                if (askYesNoQuestion(scanner, "Провести расширенный анализ с экспортом в CSV?")) {
                    analyzer.extendedAnalysis("performance_analysis.csv");
                }

                analyzer.compareCharacterSets();
            }

            if (askYesNoQuestion(scanner, "\nЭкспортировать результаты в файл?")) {
                exportResults(password, config, calculator, scanner);
            }

            System.out.println("\n══════════════════════════════════════════════════════");
            System.out.println("     ПРОГРАММА УСПЕШНО ЗАВЕРШИЛА РАБОТУ!");
            System.out.println("══════════════════════════════════════════════════════");

        } catch (Exception e) {
            System.err.println("\n ОШИБКА: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }


    private static PasswordConfig configurePassword(Scanner scanner) {
        PasswordConfig config = new PasswordConfig();

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║     НАСТРОЙКА ПАРАМЕТРОВ ПАРОЛЯ        ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        while (true) {
            System.out.print("Введите длину пароля (10,000-1,000,000): ");
            try {
                int length = Integer.parseInt(scanner.nextLine());
                if (length < 10000 || length > 1_000_000) {
                    System.out.println("Длина должна быть от 10,000 до 1,000,000 символов!");
                } else {
                    config.setLength(length);
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите целое число!");
            }
        }

        System.out.println("\nВыберите наборы символов:");
        config.setUseDigits(askYesNoQuestion(scanner, " Использовать цифры (0-9)?"));
        config.setUseLowercase(askYesNoQuestion(scanner, " Использовать латинские строчные буквы (a-z)?"));
        config.setUseUppercase(askYesNoQuestion(scanner, " Использовать латинские прописные буквы (A-Z)?"));
        config.setUseSpecialChars(askYesNoQuestion(scanner, " Использовать специальные символы (!@#$% и т.д.)?"));
        config.setUseRussianLowercase(askYesNoQuestion(scanner, " Использовать русские строчные буквы (а-я)?"));
        config.setUseRussianUppercase(askYesNoQuestion(scanner, " Использовать русские прописные буквы (А-Я)?"));

        if (!config.isUseDigits() && !config.isUseLowercase() && !config.isUseUppercase() &&
                !config.isUseSpecialChars() && !config.isUseRussianLowercase() && !config.isUseRussianUppercase()) {
            System.out.println("  Вы не выбрали ни одного набора символов! Будут использованы цифры и латинские буквы.");
            config.setUseDigits(true);
            config.setUseLowercase(true);
            config.setUseUppercase(true);
        }

        return config;
    }

    private static boolean askYesNoQuestion(Scanner scanner, String question) {
        while (true) {
            System.out.print(question + " (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("y") || answer.equals("да")) {
                return true;
            } else if (answer.equals("n") || answer.equals("нет")) {
                return false;
            } else {
                System.out.println("Пожалуйста, введите 'y' (да) или 'n' (нет)!");
            }
        }
    }

    private static void displayResults(String password, PasswordConfig config,
                                       StrengthCalculator calculator) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           РЕЗУЛЬТАТЫ                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.println(" ПАРАМЕТРЫ ГЕНЕРАЦИИ:");
        System.out.println("    Длина пароля: " + config.getLength() + " символов");
        System.out.println("    Используемые наборы: " +
                (config.isUseDigits() ? "цифры " : "") +
                (config.isUseLowercase() ? "латинские строчные " : "") +
                (config.isUseUppercase() ? "латинские прописные " : "") +
                (config.isUseSpecialChars() ? "спецсимволы " : "") +
                (config.isUseRussianLowercase() ? "русские строчные " : "") +
                (config.isUseRussianUppercase() ? "русские прописные " : ""));

        System.out.println("\n СГЕНЕРИРОВАННЫЙ ПАРОЛЬ:");
        System.out.println("    Полная длина: " + password.length() + " символов");

        if (password.length() <= 200) {
            System.out.println("    Пароль: " + password);
        } else {
            System.out.println("    Первые 100 символов: " + password.substring(0, 100) + "...");
            System.out.println("    Последние 100 символов: ..." +
                    password.substring(password.length() - 100));
        }

        System.out.println("\n  АНАЛИЗ СТОЙКОСТИ:");
        double entropy = calculator.calculateEntropy(config);
        System.out.println("    Энтропия: " + String.format("%,.2f", entropy) + " бит");

        if (entropy < 28) {
            System.out.println("       Слишком слабый пароль (менее 28 бит)");
        } else if (entropy < 60) {
            System.out.println("       Средняя стойкость (28-60 бит)");
        } else if (entropy < 128) {
            System.out.println("      Хорошая стойкость (60-128 бит)");
        } else {
            System.out.println("      Отличная стойкость (более 128 бит)");
        }

        System.out.println("    Примерное время взлома: " + calculator.estimateCrackTime(config));

        System.out.println("\n СТАТИСТИКА:");
        System.out.println("    Уникальных символов: " + countUniqueChars(password));
        System.out.println("    Дубликаты: " + (password.length() - countUniqueChars(password)));
    }

    private static void exportResults(String password, PasswordConfig config,
                                      StrengthCalculator calculator, Scanner scanner) {
        System.out.println("\nВыберите формат экспорта:");
        System.out.println("1. Только пароль (текстовый файл)");
        System.out.println("2. Полный отчет (текстовый файл)");
        System.out.print("Ваш выбор (1-2): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                FileExporter.exportPassword(password, config, "generated_password.txt");
                System.out.println(" Пароль сохранен в файл: generated_password.txt");
            } else if (choice == 2) {
                FileExporter.exportFullReport(password, config, calculator, "password_report.txt");
                System.out.println(" Полный отчет сохранен в файл: password_report.txt");
            } else {
                System.out.println("Неверный выбор. Экспорт отменен.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный ввод. Экспорт отменен.");
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла: " + e.getMessage());
        }
    }

    private static long countUniqueChars(String str) {
        return str.chars().distinct().count();
    }
}
