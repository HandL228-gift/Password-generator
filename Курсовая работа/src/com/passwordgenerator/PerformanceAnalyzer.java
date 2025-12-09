package com.passwordgenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class PerformanceAnalyzer {
    private PasswordGenerator generator;
    private StrengthCalculator calculator;
    private NumberFormat numberFormat;

    public PerformanceAnalyzer(PasswordGenerator generator) {
        this.generator = generator;
        this.calculator = new StrengthCalculator();
        this.numberFormat = NumberFormat.getNumberInstance(Locale.US);
    }

    public void analyzePerformance() {
        int[] lengths = {10_000, 50_000, 100_000, 500_000, 1_000_000};

        System.out.println("\n=== АНАЛИЗ ПРОИЗВОДИТЕЛЬНОСТИ ===");
        System.out.println("Длина пароля | Время генерации (нс) | Время генерации (мс) | Энтропия (бит)");
        System.out.println("-------------|----------------------|----------------------|----------------");

        for (int length : lengths) {
            long timeNs = generator.measureGenerationTime(length);
            double timeMs = timeNs / 1_000_000.0;
            double entropy = calculator.calculateEntropy(getConfigForLength(length));

            System.out.printf("%12d | %,19d | %,18.2f | %,12.2f%n",
                    length, timeNs, timeMs, entropy);
        }
    }

    public void extendedAnalysis(String filename) throws IOException {
        int[] lengths = {100, 1000, 10000, 50000, 100000, 500000, 1000000};

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Длина;Время генерации (нс);Время генерации (мс);Энтропия (бит);Комбинации;Время взлома\n");

            for (int length : lengths) {
                PasswordConfig config = getConfigForLength(length);
                long timeNs = generator.measureGenerationTime(length);
                double timeMs = timeNs / 1_000_000.0;
                double entropy = calculator.calculateEntropy(config);
                BigInteger combinations = calculator.calculateCombinations(config);
                String crackTime = calculator.estimateCrackTime(config);

                writer.write(String.format("%d;%,d;%.2f;%.2f;%s;%s%n",
                        length, timeNs, timeMs, entropy,
                        numberFormat.format(combinations), crackTime));

                System.out.printf("Проанализировано: %d символов%n", length);
            }

            System.out.println("\nРезультаты сохранены в файл: " + filename);
        }
    }

    private PasswordConfig getConfigForLength(int length) {
        PasswordConfig config = new PasswordConfig();
        config.setLength(length);
        config.setUseDigits(true);
        config.setUseLowercase(true);
        config.setUseUppercase(true);
        config.setUseSpecialChars(true);
        config.setUseRussianLowercase(true);
        config.setUseRussianUppercase(true);
        return config;
    }

    public void compareCharacterSets() {
        System.out.println("\n=== СРАВНЕНИЕ НАБОРОВ СИМВОЛОВ ===");
        System.out.println("Набор символов | Размер алфавита | Энтропия (при 12 символах)");
        System.out.println("---------------|-----------------|---------------------------");

        PasswordConfig[] configs = {
                createConfig(true, false, false, false, false, false),  // Только цифры
                createConfig(true, true, false, false, false, false),   // Цифры + lowercase
                createConfig(true, true, true, false, false, false),    // Цифры + lowercase + uppercase
                createConfig(true, true, true, true, false, false),     // Все латинские + спецсимволы
                createConfig(true, true, true, true, true, true)        // Все возможные
        };

        String[] names = {
                "Только цифры",
                "Цифры + латинские строчные",
                "Цифры + латинские все",
                "Латинские все + спецсимволы",
                "Все наборы символов"
        };

        for (int i = 0; i < configs.length; i++) {
            PasswordGenerator gen = new PasswordGenerator(configs[i]);
            StrengthCalculator calc = new StrengthCalculator();

            int alphabetSize = gen.getAlphabetSize();
            double entropy = calc.calculateEntropy(configs[i]);

            System.out.printf("%-15s | %15d | %25.2f%n",
                    names[i], alphabetSize, entropy);
        }
    }

    private PasswordConfig createConfig(boolean digits, boolean lower,
                                        boolean upper, boolean special,
                                        boolean ruLower, boolean ruUpper) {
        PasswordConfig config = new PasswordConfig();
        config.setLength(12);
        config.setUseDigits(digits);
        config.setUseLowercase(lower);
        config.setUseUppercase(upper);
        config.setUseSpecialChars(special);
        config.setUseRussianLowercase(ruLower);
        config.setUseRussianUppercase(ruUpper);
        return config;
    }
}

