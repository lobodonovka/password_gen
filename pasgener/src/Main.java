import java.security.SecureRandom;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

class PasswordGenerator {
    private int length;
    private String characterSet = "";

    private static final Map<String, String> LANGUAGE_MAP = new HashMap<>();
    static {
        LANGUAGE_MAP.put("en", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        LANGUAGE_MAP.put("ru", "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ");

    }

    public PasswordGenerator(int length) {
        this.length = length;
    }

    public void addCharacterSet(String set) {
        characterSet += set;
    }

    public String generatePassword() {
        if (characterSet.isEmpty()) {
            throw new IllegalStateException("Набор символов пуст. Добавьте языки, цифры или специальные символы.");
        }

        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            password.append(characterSet.charAt(random.nextInt(characterSet.length())));
        }

        return password.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите длину пароля: ");
        int length = scanner.nextInt();
        scanner.nextLine();

        PasswordGenerator generator = new PasswordGenerator(length);

        System.out.print("Введите коды языков через запятую (например, 'en,ru'): ");
        String[] languages = scanner.nextLine().split(",");
        for (String language : languages) {
            generator.addCharacterSet(LANGUAGE_MAP.getOrDefault(language.trim(), ""));
        }

        System.out.print("Использовать заглавные буквы? (y/n): ");
        boolean useUpperCase = scanner.nextLine().equalsIgnoreCase("y");
        if (useUpperCase) {

            for (String language : languages) {
                String langSet = LANGUAGE_MAP.getOrDefault(language.trim(), "");
                generator.addCharacterSet(langSet.toUpperCase());
            }
        }

        System.out.print("Хотите добавить специальные символы? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            generator.addCharacterSet("!@#$%^&*()_+");
        }

        System.out.print("Введите цифры, которые хотите использовать (например, '12345'): ");
        generator.addCharacterSet(scanner.nextLine());

        long startTime = System.nanoTime();

        try {
            String password = generator.generatePassword();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000;
            System.out.println("Сгенерированный пароль: " + password);
            System.out.println("Время генерации пароля: " + duration + " мс");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Нажмите Enter для выхода...");
        scanner.nextLine();
        scanner.close();
    }
}
