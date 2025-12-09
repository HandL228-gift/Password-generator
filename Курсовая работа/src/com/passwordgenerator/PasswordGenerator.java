package com.passwordgenerator;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class PasswordGenerator {
    private static final SecureRandom random = new SecureRandom();
    private PasswordConfig config;
    private StringBuilder characterPool;
    private int alphabetSize;

    public PasswordGenerator(PasswordConfig config) {
        this.config = config;
        buildCharacterPool();
        calculateAlphabetSize();
    }


    private void buildCharacterPool() {
        characterPool = new StringBuilder();

        if (config.isUseDigits()) {
            characterPool.append(config.getRequiredDigits());
        }
        if (config.isUseLowercase()) {
            characterPool.append("abcdefghijklmnopqrstuvwxyz");
        }
        if (config.isUseUppercase()) {
            characterPool.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        if (config.isUseSpecialChars()) {
            characterPool.append(config.getRequiredSpecialChars());
        }
        if (config.isUseRussianLowercase()) {
            characterPool.append("абвгдеёжзийклмнопрстуфхцчшщъыьэюя");
        }
        if (config.isUseRussianUppercase()) {
            characterPool.append("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ");
        }
    }


    private void calculateAlphabetSize() {
        alphabetSize = 0;
        if (config.isUseDigits()) alphabetSize += config.getRequiredDigits().length();
        if (config.isUseLowercase()) alphabetSize += 26;
        if (config.isUseUppercase()) alphabetSize += 26;
        if (config.isUseSpecialChars()) alphabetSize += config.getRequiredSpecialChars().length();
        if (config.isUseRussianLowercase()) alphabetSize += 33;
        if (config.isUseRussianUppercase()) alphabetSize += 33;
    }


    public String generatePassword() {
        if (characterPool.length() == 0) {
            throw new IllegalArgumentException("Не выбран ни один набор символов!");
        }

        StringBuilder password = new StringBuilder(config.getLength());

        List<Character> guaranteedChars = getGuaranteedCharacters();
        for (Character c : guaranteedChars) {
            password.append(c);
        }

        for (int i = guaranteedChars.size(); i < config.getLength(); i++) {
            int index = random.nextInt(characterPool.length());
            password.append(characterPool.charAt(index));
        }

        return shuffleString(password.toString());
    }

    private List<Character> getGuaranteedCharacters() {
        List<Character> guaranteed = new ArrayList<>();

        if (config.isUseDigits()) {
            String digits = config.getRequiredDigits();
            if (!digits.isEmpty()) {
                guaranteed.add(digits.charAt(random.nextInt(digits.length())));
            }
        }

        if (config.isUseLowercase()) {
            guaranteed.add((char) ('a' + random.nextInt(26)));
        }

        if (config.isUseUppercase()) {
            guaranteed.add((char) ('A' + random.nextInt(26)));
        }

        if (config.isUseSpecialChars()) {
            String special = config.getRequiredSpecialChars();
            if (!special.isEmpty()) {
                guaranteed.add(special.charAt(random.nextInt(special.length())));
            }
        }

        if (config.isUseRussianLowercase()) {
            String russianLower = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
            guaranteed.add(russianLower.charAt(random.nextInt(russianLower.length())));
        }

        if (config.isUseRussianUppercase()) {
            String russianUpper = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
            guaranteed.add(russianUpper.charAt(random.nextInt(russianUpper.length())));
        }

        return guaranteed;
    }


    private String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }


    public long measureGenerationTime(int length) {
        config.setLength(length);
        buildCharacterPool();

        long startTime = System.nanoTime();
        generatePassword();
        long endTime = System.nanoTime();

        return endTime - startTime;
    }


    public int getAlphabetSize() {
        return alphabetSize;
    }


    public String getCharacterPool() {
        return characterPool.toString();
    }
}