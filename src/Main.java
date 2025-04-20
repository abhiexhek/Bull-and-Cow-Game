import bullandcow.BullAndCow;
import SecretCodeStore.SecretCode;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            try {
                System.out.println("\n🎮 Welcome to Bulls and Cows Game!");
                System.out.println("1. Play Game");
                System.out.println("2. Game Mechanisms");
                System.out.println("3. Quit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> playGameMenu();
                    case 2 -> showGameMechanisms();
                    case 3 -> {
                        System.out.println("Thanks for playing! Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please enter 1-3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void playGameMenu() {
        System.out.println("\nChoose game mode:");
        System.out.println("1. Single Player (vs Computer)");
        System.out.println("2. Multiplayer");
        System.out.println("3. Back to main menu");
        System.out.print("Enter your choice: ");

        try {
            int modeChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (modeChoice) {
                case 1 -> startSinglePlayerGame();
                case 2 -> startMultiplayerGame();
                case 3 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice. Please enter 1-3.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // Clear the invalid input
        }
    }

    private static void startSinglePlayerGame() {
        BullAndCow game = new BullAndCow();
        SecretCode playerSecretCode = new SecretCode();

        System.out.print("\nEnter your name: ");
        String playerName = scanner.nextLine();
        game.setPlayerName1(playerName);
        game.setPlayerName2("Computer");

        System.out.print("Enter length of the secret code: ");
        int codeLength = scanner.nextInt();
        scanner.nextLine(); // flush newline
        game.setSecretCodeLength(codeLength);

        System.out.println("\n🔐 " + playerName + ", configure your secret code options:");
        game.configureSecretCode(scanner, playerSecretCode);

        System.out.println("\n🔐 " + playerName + ", now enter your secret code:");
        game.collectSecretCode(scanner, playerSecretCode, 1);

        // Computer generates its code
        SecretCode computerSecretCode = new SecretCode();
        computerSecretCode.copyAllowedCharacters(playerSecretCode);
        List<Character> computerCode = computerSecretCode.generateRandomCode(codeLength);
        game.setSecretCodeStore2(computerCode);

        System.out.println("\n🔍 Now it's time to guess!");

        // Player guesses computer's code
        int playerAttempts = game.startGuessing(scanner, computerSecretCode, playerName);

        // Computer guesses player's code
        System.out.println("\n⏳ Computer's Turn!");
        int computerAttempts = game.computerGuessing(playerSecretCode, codeLength);

        game.determineWinner(playerAttempts, computerAttempts);
    }

    private static void startMultiplayerGame() {
        BullAndCow game = new BullAndCow();
        SecretCode secretCode1 = new SecretCode();
        SecretCode secretCode2 = new SecretCode();

        System.out.println("\n🎮 Multiplayer Mode");
        game.gameMechanisms();
        game.setPlayers(scanner);

        game.setSecretCodeLength(scanner);

        System.out.println("\n🔐 " + game.getPlayerName1() + ", it's your turn to set the secret code.");
        game.configureSecretCode(scanner, secretCode1);
        game.collectSecretCode(scanner, secretCode1, 1);

        System.out.println("\n🔐 " + game.getPlayerName2() + ", now it's your turn to set the secret code.");
        game.configureSecretCode(scanner, secretCode2);
        game.collectSecretCode(scanner, secretCode2, 2);

        System.out.println("\n🔍 Now it's time to guess!");

        int attemptsPlayer1 = game.startGuessing(scanner, secretCode2, game.getPlayerName1());
        System.out.println("\n⏳ " + game.getPlayerName2() + ", Your Turn!");
        int attemptsPlayer2 = game.startGuessing(scanner, secretCode1, game.getPlayerName2());

        game.determineWinner(attemptsPlayer1, attemptsPlayer2);
    }

    private static void showGameMechanisms() {
        BullAndCow game = new BullAndCow();
        game.gameMechanisms();
    }
}