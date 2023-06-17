import java.io.*;

public class MultiplicativeCipher {
    private static final int[] VALID_KEYS = {3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25};
    private static final int ALPHABET_SIZE = 26;

    public static void encryptFile(String filePath, String encryptedFilePath, int key) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter(encryptedFilePath))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                line = preprocessText(line);
                StringBuilder encryptedLine = new StringBuilder();
                for (char ch : line.toCharArray()) {
                    if (Character.isLetter(ch)) {
                        int charValue = ch - getBaseValue(ch);
                        charValue = (charValue * key) % ALPHABET_SIZE;
                        ch = (char) (charValue + getBaseValue(ch));
                    }
                    encryptedLine.append(ch);
                }
                fileWriter.write(encryptedLine.toString());
                fileWriter.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while encrypting the file.");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void decryptFile(String filePath, String decryptedFilePath, int key) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter(decryptedFilePath))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                StringBuilder decryptedLine = new StringBuilder();
                for (char ch : line.toCharArray()) {
                    if (Character.isLetter(ch)) {
                        int charValue = ch - getBaseValue(ch);
                        charValue = (charValue * modInverse(key, ALPHABET_SIZE)) % ALPHABET_SIZE;
                        ch = (char) (charValue + getBaseValue(ch));
                    }
                    decryptedLine.append(ch);
                }
                fileWriter.write(decryptedLine.toString());
                fileWriter.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while decrypting the file.");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static boolean isValidKey(int key) {
        for (int validKey : VALID_KEYS) {
            if (validKey == key) {
                return true;
            }
        }
        return false;
    }

    public static int getRandomKey() {
        int index = (int) (Math.random() * VALID_KEYS.length);
        return VALID_KEYS[index];
    }

    private static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return 1;
    }

    private static String preprocessText(String text) {
        text = text.toLowerCase();
        text = text.replaceAll("ü", "ue").replaceAll("ß", "ss");
        return text;
    }

    private static int getBaseValue(char ch) {
        if (Character.isLowerCase(ch)) {
            return 'a';
        } else {
            return 'A';
        }
    }
}