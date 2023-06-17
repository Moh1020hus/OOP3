import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\ckdkcdkccv\\Downloads\\Gedicht.txt";
        String encryptedFilePath = "C:\\Users\\ckdkcdkccv\\Downloads\\Encrypted.txt";
        String decryptedFilePath = "C:\\Users\\ckdkcdkccv\\Downloads\\Decrypted.txt";
        int key = 0; // Default key value

        // Read user input for encryption/decryption and key choice
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter 'E' for encryption or 'D' for decryption:");
            String mode = reader.readLine();

            if (mode.equalsIgnoreCase("E")) {
                System.out.println("Enter 'S' for key = 3 or 'R' for a random key:");
                String keyChoice = reader.readLine();

                if (keyChoice.equalsIgnoreCase("S")) {
                    key = 3;
                } else if (keyChoice.equalsIgnoreCase("R")) {
                    key = Encrypt.getRandomKey();
                    System.out.println("Random key used for encryption: " + key);
                } else {
                    System.out.println("Invalid key choice. Exiting the program.");
                    System.exit(0);
                }

                Encrypt.encryptFile(filePath, encryptedFilePath, key);
                System.out.println("Encryption complete. Encrypted file saved at: " + encryptedFilePath);
            } else if (mode.equalsIgnoreCase("D")) {
                System.out.println("Enter 'S' to decrypt using key = 3 or 'F' to find the used key:");
                String keyChoice = reader.readLine();

                if (keyChoice.equalsIgnoreCase("S")) {
                    key = 3;
                    Decrypt.decryptFile(encryptedFilePath, decryptedFilePath, key);
                    System.out.println("Decryption complete. Decrypted file saved at: " + decryptedFilePath);
                    Decrypt.replaceGermanSpecialCharacters(decryptedFilePath);
                } else if (keyChoice.equalsIgnoreCase("F")) {
                    key = Decrypt.findKey(encryptedFilePath);
                    if (key != -1) {
                        Decrypt.decryptFile(encryptedFilePath, decryptedFilePath, key);
                        System.out.println("Decryption complete using key: " + key);
                        System.out.println("Decrypted file saved at: " + decryptedFilePath);
                        Decrypt.replaceGermanSpecialCharacters(decryptedFilePath);
                    } else {
                        System.out.println("Unable to find the key. Decryption failed.");
                    }
                } else {
                    System.out.println("Invalid key choice. Exiting the program.");
                    System.exit(0);
                }
            } else {
                System.out.println("Invalid mode choice. Exiting the program.");
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading user input. Exiting the program.");
            System.exit(0);
        }
    }
}
