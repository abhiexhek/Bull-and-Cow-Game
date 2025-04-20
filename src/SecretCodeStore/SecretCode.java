package SecretCodeStore;

import java.util.*;

public class SecretCode {
    private final Set<Character> allowedChars = new HashSet<>();
    private final Set<Character> allowedNumbers = new HashSet<>();
    private final Set<Character> allowedSpecialChars = new HashSet<>();

    public void addCharRange(char start, char end) throws IllegalArgumentException {
        // Validate inputs are letters
        if (!Character.isLetter(start) || !Character.isLetter(end)) {
            throw new IllegalArgumentException("Both characters must be letters (a-z or A-Z)");
        }

        // Convert to lowercase for consistency
        start = Character.toLowerCase(start);
        end = Character.toLowerCase(end);

        // Validate range order
        if (start > end) {
            throw new IllegalArgumentException("Start character must come before end character in alphabet");
        }

        // Add all characters in range (inclusive)
        for (char c = start; c <= end; c++) {
            allowedChars.add(c);
        }
    }

    // Updated to handle both range and individual characters
    public void addCharRangeFromInput(String input) {
        input = input.trim().toLowerCase();

        // Handle range format (e.g., "a-d")
        if (input.matches("[a-z]-[a-z]")) {
            String[] range = input.split("-");
            char start = range[0].charAt(0);
            char end = range[1].charAt(0);

            try {
                addCharRange(start, end);
                System.out.println("Added characters: " + start + " to " + end);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        // Handle individual characters (e.g., "abc")
        else if (input.matches("[a-z]+")) {
            for (char c : input.toCharArray()) {
                allowedChars.add(c);
            }
            System.out.println("Added characters: " + input);
        }
        else {
            System.out.println("Invalid format. Use either range (a-z) or characters (abc)");
        }
    }

    public void addNumberRangeFromString(String numberInput) {
        // Check if input is in range format (e.g., "1-9")
        if (numberInput.matches("\\d-\\d")) {
            String[] range = numberInput.split("-");
            char start = range[0].charAt(0);
            char end = range[1].charAt(0);

            // Validate range
            if (start >= '0' && end >= '0' && end <= '9' && start <= end) {
                for (char c = start; c <= end; c++) {
                    allowedNumbers.add(c);
                }
                return;
            }
        }

        // Fallback to original single-digit handling
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