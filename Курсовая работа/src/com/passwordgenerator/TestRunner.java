package com.passwordgenerator;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println(" ТЕСТОВЫЙ ПРОГОН ГЕНЕРАТОРА ПАРОЛЕЙ \n");

        System.out.println("Тест 1: Короткий пароль (10,000 символов)");
        PasswordConfig config1 = new PasswordConfig(10000, true, true, true, true, false, false);
        runTest(config1, "test1_password.txt");

        System.out.println("\nТест 2: Пароль средней длины (100,000 символов)");
        PasswordConfig config2 = new PasswordConfig(100000, true, true, true, true, true, true);
        runTest(config2, "test2_password.txt");

        System.out.println("\nТест 3: Длинный пароль (500,000 символов)");
        PasswordConfig config3 = new PasswordConfig(500000, true, true, true, true, true, true);
        runTest(config3, "test3_password.txt");

        System.out.println("\nТест 4: Очень длинный пароль (1,000,000 символов)");
        PasswordConfig config4 = new PasswordConfig(1000000, true, true, true, true, true, true);
        runPerformanceTest(config4);

        System.out.println("\n ВСЕ ТЕСТЫ ЗАВЕРШЕНЫ ");
    }

    private static void runTest(PasswordConfig config, String filename) {
        try {
            PasswordGenerator generator = new PasswordGenerator(config);
            StrengthCalculator calculator = new StrengthCalculator();

            long startTime = System.nanoTime();
            String password = generator.generatePassword();
            long endTime = System.nanoTime();

            System.out.println("   Длина: " + password.length() + " символов");
            System.out.println("   Время генерации: " + (endTime - startTime) + " нс");
            System.out.println("   Энтропия: " + calculator.calculateEntropy(config) + " бит");
            System.out.println("   Время взлома: " + calculator.estimateCrackTime(config));
            System.out.println("   Первые 50 символов: " +
                    (password.length() > 50 ? password.substring(0, 50) + "..." : password));

            FileExporter.exportPassword(password, config, filename);
            System.out.println("   Сохранено в: " + filename);

        } catch (Exception e) {
            System.err.println("   Ошибка: " + e.getMessage());
        }
    }

    private static void runPerformanceTest(PasswordConfig config) {
        PasswordGenerator generator = new PasswordGenerator(config);

        long startTime = System.nanoTime();
        generator.generatePassword();
        long endTime = System.nanoTime();

        double timeMs = (endTime - startTime) / 1_000_000.0;
        System.out.println("   Время генерации: " + timeMs + " мс");
        System.out.println("   Скорость: " +
                String.format("%,.2f", config.getLength() / timeMs) + " символов/мс");
    }
}
