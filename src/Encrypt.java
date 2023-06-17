import java.io.*;

public class Encrypt {
    private static final int[] VALID_KEYS = {3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25};

    public static void encryptFile(String filePath, String encryptedFilePath, int key) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter(encryptedFilePath))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                StringBuilder encryptedLine = new StringBuilder();
                line = line.replaceAll("ü", "ue").replaceAll("ß", "ss");
                for (int i = 0; i < line.length(); i++) {
                    char ch = line.charAt(i);
                    if (Character.isLetter(ch)) {
                        boolean isUpperCase = Character.isUpperCase(ch);
                        ch = Character.toLowerCase(ch);
                        char encryptedChar = (char) ((((ch - 'a') * key) % 26) + 'a');
                        if (isUpperCase) {
                            encryptedChar = Character.toUpperCase(encryptedChar);
                        }
                        encryptedLine.append(encryptedChar);
                    } else {
                        encryptedLine.append(ch);
                    }
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

    public static int getRandomKey() {
        int index = (int) (Math.random() * VALID_KEYS.length);
        return VALID_KEYS[index];
    }





}