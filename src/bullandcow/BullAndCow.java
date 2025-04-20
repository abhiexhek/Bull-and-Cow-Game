package bullandcow;

import SecretCodeStore.SecretCode;
import java.util.*;

public class BullAndCow {
    private String playerName1;
    private String playerName2;
    private int secretCodeLength;
    private final List<Character> secretCodeStore1 = new ArrayList<>();
    private final List<Character> secretCodeStore2 = new ArrayList<>();
    private List<Character> guessCodeStore = new ArrayList<>();

    public void setPlayers(Scanner scanner) {
        System.out.print("Enter Player 1 Name: ");
        playerName1 = scanner.nextLine();
        System.out.print("Enter Player 2 Name: ");
        playerName2 = scanner.nextLine();
    }

    public String getPlayerName1() {
        return playerName1;
    }

    public String getPlayerName2() {
        return playerName2;
    }

    public void setPlayerName1(String name) {
        this.playerName1 = name;
    }

    public void setPlayerName2(String name) {
        this.playerName2 = name;
    }

    public void setSecretCodeLength(Scanner scanner) {
        System.out.print("Enter length of the secret code: ");
        secretCodeLength = scanner.nextInt();
        scanner.nextLine(); // flush newline
    }

    public void setSecretCodeLength(int length) {
        this.secretCodeLength = length;
    }

    public void setSecretCodeStore2(List<Character> code) {
        secretCodeStore2.clear();
        secretCodeStore2.addAll(code);
    }

    public void configureSecretCode(Scanner scanner, SecretCode secretCode) {
        boolean anotherRound = true;
        boolean hasChar = false, hasNumber = false, hasSpecial = false;

        while (anotherRound) {
            System.out.println("\nChoose what to add:");
            System.out.println("1. Characters");
            System.out.println("2. Numbers");
            System.out.println("3. Special Characters");
            System.out.println("4. Done");

            System.out.print("Your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    if (!hasChar) {
                        System.out.print("Enter two characters between a - z: ");
                        String input = scanner.nextLine().toLowerCase();
                        String output = input.replaceAll("\\s+", "");
                        if (output.length() >= 2) {
                            secretCode.addCharRange(output.charAt(0), output.charAt(1));
                            hasChar = true;
                        }
                    } else {
                        System.out.println("Already entered! Please choose another option.");
                    }
                }
                case "2" -> {
                    if (!hasNumber) {
                        System.out.println("Enter number range (e.g., '1-9') or individual digits (e.g., '02468'): ");
                        String numberInput = scanner.nextLine().trim();

                        // Validate input contains at least one digit
                        if (!numberInput.matches(".*\\d.*")) {
                            System.out.println("Invalid input. Must contain at least one digit (0-9)");
                            continue;
                        }

                        secretCode.addNumberRangeFromString(numberInput);
                        hasNumber = true;
                    } else {
                        System.out.println("Already entered! Please choose another option.");
                    }
                }
                case "3" -> {
                    if (!hasSpecial) {
                        System.out.print("Enter special characters: ");
                        secretCode.addSpecialCharacters(scanner.nextLine());
                        hasSpecial = true;
                    } else {
                        System.out.println("Already entered! Please choose another option.");
                    }
                }
                case "4" -> anotherRound = false;
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }

    public void collectSecretCode(Scanner scanner, SecretCode secretCode, int playerNumber) {
        System.out.println("Enter the secret code using only allowed characters:");
        boolean valid = false;
        while (!valid) {
            valid = true;
            String input = scanner.nextLine();
            if (input.length() != secretCodeLength) {
                System.out.println("⚠️ Code must be " + secretCodeLength + " characters long.");
                valid = false;
                continue;
            }
            for (int i = 0; i < secretCodeLength; i++) {
                if (!secretCode.isValid(input.charAt(i))) {
                    valid = false;
                    System.out.println("❌ Invalid character found: " + input.charAt(i));
                    break;
                }
            }
            if (valid) {
                if (playerNumber == 1) {
                    secretCodeStore1.clear();
                    for (char c : input.toCharArray()) {
                        secretCodeStore1.add(c);
                    }
                } else {
                    secretCodeStore2.clear();
                    for (char c : input.toCharArray()) {
                        secretCodeStore2.add(c);
                    }
                }
            }
        }
    }

    public int startGuessing(Scanner scanner, SecretCode secretCode, String guesserName) {
        List<Character> secretCodeToGuess = guesserName.equals(playerName1) ? secretCodeStore2 : secretCodeStore1;
        System.out.println("\n" + guesserName + ", Your Turn!");

        boolean matched = false;
        int attempts = 0;

        while (!matched) {
            System.out.print("Enter your guess: ");
            String guess = scanner.nextLine();
            if (guess.length() != secretCodeLength) {
                System.out.println("⚠️ Guess must be " + secretCodeLength + " characters.");
                continue;
            }

            guessCodeStore.clear();
            for (char c : guess.toCharArray()) {
                guessCodeStore.add(c);
            }

            matched = checkBullsAndCows(secretCodeToGuess);
            attempts++;
        }

        System.out.println("🎉 " + guesserName + " cracked the code in " + attempts + " attempts!");
        return attempts;
    }

    public int computerGuessing(SecretCode secretCode, int codeLength) {
        List<Character> possibleChars = new ArrayList<>(secretCode.getAllAllowedCharacters());
        List<Character> currentGuess = new ArrayList<>();
        Random random = new Random();
        int attempts = 0;

        System.out.println("Computer is guessing...");
        // Generate initial random guess
        System.out.println("Computer is guessing");
        for (int i = 0; i < codeLength; i++) {
            currentGuess.add(possibleChars.get(random.nextInt(possibleChars.size())));
        }

        while (true) {
            attempts++;
            guessCodeStore = new ArrayList<>(currentGuess);

            System.out.print("Computer's guess: ");
            for (char c : currentGuess) {
                System.out.print(c);
            }
            System.out.println();

            boolean matched = checkBullsAndCows(secretCodeStore1);
            if (matched) {
                System.out.println("🎉 Computer cracked the code in " + attempts + " attempts!");
                return attempts;
            }

            // Simple AI: Generate a new random guess
            currentGuess.clear();
            for (int i = 0; i < codeLength; i++) {
                currentGuess.add(possibleChars.get(random.nextInt(possibleChars.size())));
            }
        }
    }

    public boolean checkBullsAndCows(List<Character> secretCode) {
        int bulls = 0, cows = 0;
        List<Character> unmatchedSecret = new ArrayList<>();
        List<Character> unmatchedGuess = new ArrayList<>();

        for (int i = 0; i < secretCodeLength; i++) {
            if (secretCode.get(i).equals(guessCodeStore.get(i))) {
                bulls++;
            } else {
                unmatchedSecret.add(secretCode.get(i));
                unmatchedGuess.add(guessCodeStore.get(i));
            }
        }

        for (Character ch : unmatchedGuess) {
            if (unmatchedSecret.contains(ch)) {
                cows++;
                unmatchedSecret.remove(ch);
            }
        }

        System.out.println("🐂 Bulls: " + bulls + ", 🐄 Cows: " + cows);
        return bulls == secretCodeLength;
    }

    public void determineWinner(int attempts1, int attempts2) {
        System.out.println("\n📊 Final Results:");
        System.out.println(playerName1 + " took " + attempts1 + " attempts.");
        System.out.println(playerName2 + " took " + attempts2 + " attempts.");

        if (attempts1 < attempts2) {
            System.out.println("🏆 " + playerName1 + " wins!");
        } else if (attempts2 < attempts1) {
            System.out.println("🏆 " + playerName2 + " wins!");
        } else {
            System.out.println("🤝 It's a draw!");
        }
    }

    public void gameMechanisms() {
        System.out.println("\n🐂🐄 Bulls and Cows Game Guide:");
        System.out.println(" - Each player sets a secret code.");
        System.out.println(" - The other player tries to guess it.");
        System.out.println(" - Bulls = correct char & position.");
        System.out.println(" - Cows = correct char, wrong position.");
        System.out.println(" - Player with fewer attempts wins.");
    }
}
