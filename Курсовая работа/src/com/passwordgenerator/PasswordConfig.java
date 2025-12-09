package com.passwordgenerator;
public class PasswordConfig {
    private int length;
    private boolean useDigits;
    private boolean useLowercase;
    private boolean useUppercase;
    private boolean useSpecialChars;
    private boolean useRussianLowercase;
    private boolean useRussianUppercase;
    private String requiredDigits;
    private String requiredSpecialChars;

     PasswordConfig() {
        this.length = 10000;
        this.useDigits = true;
        this.useLowercase = true;
        this.useUppercase = true;
        this.useSpecialChars = false;
        this.useRussianLowercase = false;
        this.useRussianUppercase = false;
        this.requiredDigits = "0123456789";
        this.requiredSpecialChars = "!@#$%^&*()_+-=[]{}|;:'\",.<>/?";
    }

    public PasswordConfig(int length, boolean useDigits, boolean useLowercase,
                          boolean useUppercase, boolean useSpecialChars,
                          boolean useRussianLowercase, boolean useRussianUppercase) {
        this.length = length;
        this.useDigits = useDigits;
        this.useLowercase = useLowercase;
        this.useUppercase = useUppercase;
        this.useSpecialChars = useSpecialChars;
        this.useRussianLowercase = useRussianLowercase;
        this.useRussianUppercase = useRussianUppercase;
        this.requiredDigits = "0123456789";
        this.requiredSpecialChars = "!@#$%^&*()_+-=[]{}|;:'\",.<>/?";
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isUseDigits() {
        return useDigits;
    }

    public void setUseDigits(boolean useDigits) {
        this.useDigits = useDigits;
    }

    public boolean isUseLowercase() {
        return useLowercase;
    }

    public void setUseLowercase(boolean useLowercase) {
        this.useLowercase = useLowercase;
    }

    public boolean isUseUppercase() {
        return useUppercase;
    }

    public void setUseUppercase(boolean useUppercase) {
        this.useUppercase = useUppercase;
    }

    public boolean isUseSpecialChars() {
        return useSpecialChars;
    }

    public void setUseSpecialChars(boolean useSpecialChars) {
        this.useSpecialChars = useSpecialChars;
    }

    public boolean isUseRussianLowercase() {
        return useRussianLowercase;
    }

    public void setUseRussianLowercase(boolean useRussianLowercase) {
        this.useRussianLowercase = useRussianLowercase;
    }

    public boolean isUseRussianUppercase() {
        return useRussianUppercase;
    }

    public void setUseRussianUppercase(boolean useRussianUppercase) {
        this.useRussianUppercase = useRussianUppercase;
    }

    public String getRequiredDigits() {
        return requiredDigits;
    }

    public void setRequiredDigits(String requiredDigits) {
        this.requiredDigits = requiredDigits;
    }

    public String getRequiredSpecialChars() {
        return requiredSpecialChars;
    }

    public void setRequiredSpecialChars(String requiredSpecialChars) {
        this.requiredSpecialChars = requiredSpecialChars;
    }

    @Override
    public String toString() {
        return String.format(
                "PasswordConfig[length=%d, digits=%b, lowercase=%b, uppercase=%b, " +
                        "special=%b, russianLower=%b, russianUpper=%b]",
                length, useDigits, useLowercase, useUppercase,
                useSpecialChars, useRussianLowercase, useRussianUppercase
        );
    }
}