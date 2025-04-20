package SecretCodeStore;

import java.util.*;

public class SecretCode {
    private final Set<Character> allowedChars = new HashSet<>();
    private final Set<Character> allowedNumbers = new HashSet<>();
    private final Set<Character> allowedSpecialChars = new HashSet<>();

    public void addCharRange(char start, char end) {
        for (char c = start; c <= end; c++) {
            allowedChars.add(c);
        }
    }

    public void addNumberRangeFromString(String numberInput) {
        for (char c : numberInput.toCharArray()) {
            if (Character.isDigit(c)) {
                allowedNumbers.add(c);
            }
        }
    }

    public void addSpecialCharacters(String specialChars) {
        for (char c : specialChars.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                allowedSpecialChars.add(c);
            }
        }
    }

    public boolean isValid(char c) {
        return allowedChars.contains(c) || allowedNumbers.contains(c) || allowedSpecialChars.contains(c);
    }

    public List<Character> getAllAllowedCharacters() {
        List<Character> allChars = new ArrayList<>();
        allChars.addAll(allowedChars);
        allChars.addAll(allowedNumbers);
        allChars.addAll(allowedSpecialChars);
        return allChars;
    }

    public void copyAllowedCharacters(SecretCode other) {
        this.allowedChars.addAll(other.allowedChars);
        this.allowedNumbers.addAll(other.allowedNumbers);
        this.allowedSpecialChars.addAll(other.allowedSpecialChars);
    }

    public List<Character> generateRandomCode(int length) {
        List<Character> allAllowed = getAllAllowedCharacters();
        if (allAllowed.isEmpty()) {
            throw new IllegalStateException("No allowed characters defined");
        }

        List<Character> code = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            code.add(allAllowed.get(random.nextInt(allAllowed.size())));
        }

        return code;
    }
}