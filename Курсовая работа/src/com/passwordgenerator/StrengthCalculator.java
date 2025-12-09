package com.passwordgenerator;

import java.math.BigDecimal;
import java.math.BigInteger;

public class StrengthCalculator {
    private static final BigInteger ATTACKS_PER_SECOND = BigInteger.valueOf(1_000_000_000L); // 1 млрд

    private static final BigInteger SECONDS_PER_MINUTE = BigInteger.valueOf(60);
    private static final BigInteger SECONDS_PER_HOUR = BigInteger.valueOf(3600);
    private static final BigInteger SECONDS_PER_DAY = BigInteger.valueOf(86400);
    private static final BigInteger SECONDS_PER_YEAR = BigInteger.valueOf(31536000);

    public double calculateEntropy(PasswordConfig config) {
        PasswordGenerator generator = new PasswordGenerator(config);
        int alphabetSize = generator.getAlphabetSize();

        if (alphabetSize == 0) {
            return 0;
        }

        double bitsPerChar = Math.log(alphabetSize) / Math.log(2);
        return config.getLength() * bitsPerChar;
    }

    public BigInteger calculateCombinations(PasswordConfig config) {
        PasswordGenerator generator = new PasswordGenerator(config);
        int alphabetSize = generator.getAlphabetSize();

        if (alphabetSize == 0) {
            return BigInteger.ZERO;
        }

        BigInteger alphabet = BigInteger.valueOf(alphabetSize);
        return alphabet.pow(config.getLength());
    }

    public String estimateCrackTime(PasswordConfig config) {
        BigInteger combinations = calculateCombinations(config);

        if (combinations.equals(BigInteger.ZERO)) {
            return "Невозможно рассчитать (пустой алфавит)";
        }

        BigInteger seconds = combinations.divide(ATTACKS_PER_SECOND);

        if (seconds.compareTo(BigInteger.valueOf(60)) < 0) {
            return formatTime(seconds, "секунд", "секунды", "секунда");
        } else if (seconds.compareTo(SECONDS_PER_HOUR) < 0) {
            BigInteger minutes = seconds.divide(SECONDS_PER_MINUTE);
            return formatTime(minutes, "минут", "минуты", "минута");
        } else if (seconds.compareTo(SECONDS_PER_DAY) < 0) {
            BigInteger hours = seconds.divide(SECONDS_PER_HOUR);
            return formatTime(hours, "часов", "часа", "час");
        } else if (seconds.compareTo(SECONDS_PER_YEAR) < 0) {
            BigInteger days = seconds.divide(SECONDS_PER_DAY);
            return formatTime(days, "дней", "дня", "день");
        } else {
            BigInteger years = seconds.divide(SECONDS_PER_YEAR);
            return formatTime(years, "лет", "года", "год");
        }
    }

    private String formatTime(BigInteger time, String plural, String few, String single) {
        String timeStr = time.toString();
        int lastDigit = time.mod(BigInteger.TEN).intValue();
        int lastTwoDigits = time.mod(BigInteger.valueOf(100)).intValue();

        String word;
        if (lastTwoDigits >= 11 && lastTwoDigits <= 19) {
            word = plural;
        } else if (lastDigit == 1) {
            word = single;
        } else if (lastDigit >= 2 && lastDigit <= 4) {
            word = few;
        } else {
            word = plural;
        }

        return String.format("%,d %s", time, word);
    }

    public BigInteger calculateCrackTimeSeconds(PasswordConfig config) {
        BigInteger combinations = calculateCombinations(config);
        return combinations.divide(ATTACKS_PER_SECOND);
    }
}
