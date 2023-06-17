import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Decrypt class provides methods for file decryption and key finding.
 */
public class Decrypt {


    /**
     * Decrypts a file using the specified key and saves the decrypted content to a new file.
     *
     * @param encryptedFilePath  the path of the encrypted file
     * @param decryptedFilePath  the path to save the decrypted file
     * @param key                the decryption key to use
     */
    public static void decryptFile(String encryptedFilePath, String decryptedFilePath, int key) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(encryptedFilePath));
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter(decryptedFilePath))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                StringBuilder decryptedText = new StringBuilder();
                for (char ch : line.toCharArray()) {
                    if (Character.isLetter(ch)) {
                        boolean isUpperCase = Character.isUpperCase(ch);
                        ch = Character.toLowerCase(ch);
                        int decryptedIndex = ((ch - 'a') * getMultiplicativeInverse(key) % 26);
                        if (decryptedIndex < 0)
                            decryptedIndex += 26;
                        char decryptedChar = (char) (decryptedIndex + 'a');
                        if (isUpperCase) {
                            decryptedChar = Character.toUpperCase(decryptedChar);
                        }
                        decryptedText.append(decryptedChar);
                    } else {
                        decryptedText.append(ch);
                    }
                }
                fileWriter.write(decryptedText.toString());
                fileWriter.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while decrypting the file.");
            e.printStackTrace();
        }
    }

    /**
     * Replaces German special characters in the decrypted file.
     *
     * @param decryptedFilePath the path of the decrypted file
     */
    public static void replaceGermanSpecialCharacters(String decryptedFilePath) {
        try {
            File file = new File(decryptedFilePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace("ue", "ü").replace("ss", "ß");
                content.append(line).append(System.lineSeparator());
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content.toString());
            writer.close();

            System.out.println("German special characters replaced in the decrypted file.");
        } catch (IOException e) {
            System.out.println("An error occurred while replacing German special characters in the decrypted file.");
            e.printStackTrace();
        }
    }

    /**
     * Finds the decryption key used in the encrypted file.
     *
     * @param encryptedFilePath the path of the encrypted file
     * @return the decryption key if found, or -1 if the key was not found
     */
    public static int findKey(String encryptedFilePath) {
        int[] letterFrequencies = countOccurrences(encryptedFilePath.toLowerCase());

        int maxFrequency = 0;
        char mostFrequentCharacter = 'a';

        for (char c = 'a'; c <= 'z'; c++) {
            int frequency = letterFrequencies[c - 'a'];
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
                mostFrequentCharacter = c;
            }
        }

        System.out.println("Most frequent character: " + mostFrequentCharacter);
        int commonCharacterIndex = mostFrequentCharacter - 'a';

        List<Character> germanCommonCharacters = new ArrayList<>(Arrays.asList('e', 'n', 'i', 's', 'r', 'a', 't',
                'd', 'h', 'u', 'l', 'c', 'g', 'm', 'o', 'b', 'w', 'f', 'k', 'z', 'p', 'v', 'j', 'y',
                'x', 'q'));

        for (char c : germanCommonCharacters) {
            int germanCommonCharacterIndex = c - 'a';
            for (int key : new int[]{3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25}) {
                int decryptedCharacterIndex = (germanCommonCharacterIndex * key) % 26;
                if (decryptedCharacterIndex == commonCharacterIndex) {
                    System.out.println("Key found: " + key);
                    return key;
                }
            }
        }

        System.out.println("Key not found");
        return -1; // Key not found
    }

    private static int[] countOccurrences(String filePath) {
        int[] letterFrequencies = new int[26];
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                for (char c : line.toLowerCase().toCharArray()) {
                    if (Character.isLetter(c)) {
                        letterFrequencies[c - 'a']++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while counting letter occurrences.");
            e.printStackTrace();
        }

        System.out.println("Letter frequencies: " + Arrays.toString(letterFrequencies));
        return letterFrequencies;
    }

    private static int getMultiplicativeInverse(int a) {
        a = a % 26;
        for (int x = 1; x < 26; x++) {
            if ((a * x) % 26 == 1) {
                return x;
            }
        }
        return 1;
    }
}
