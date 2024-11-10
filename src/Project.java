import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Project {

    public static void start_game () {
        // input принимать данные с клавиатуры и определять начать игру или выйти из игры
        boolean input = input_check();
        if (input) {
            System.out.println("Приготовтесь, происходит магия...");
            // guess_word
            make_game_loop();
        } else {
            System.out.println("Пока!");
        }
    }

    public static void show_rules_game() {
        System.out.println("Итак, ты в игре виселица");
        System.out.println("Познакомься с правилами ввода: ");
        System.out.println("1. Вводить нужно  только одну букву английского алфавита");
        System.out.println("2. Недопускается повторный ввод уже раннее вводимой буквы!!!");
        System.out.println("Напоминаю, тебе нужно отгадать слово которое загадал ИИ");
        System.out.println("У тебя есть только 4 попытки ошибиться в выборе буквы, которая содержится в слове");
        System.out.println("После каждого неудачного случаю воображаемому человечку будет дорисовваться виселица");
        System.out.println("Исхода 2а: либо 'висяк' либо победа ...ну что, погнали!!!");
    }

    public static String make_magic_word() {
        Random random = new Random();
        ArrayList<String> dictionaryOfRandomWords = new ArrayList<>();

        // Используем try-with-resources для безопасного закрытия Scanner
        try (Scanner sc = new Scanner(new File("Dictionary_of_random_word.txt"))) {
            while (sc.hasNextLine()) {
                String word = sc.nextLine();
                if (!word.trim().isEmpty()) { // Добавляем проверку на пустые строки
                    dictionaryOfRandomWords.add(word);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  // Обработка исключения, если файл не найден
            return null;  // Возвращаем null в случае ошибки
        }

        // Проверка, что список не пуст
        if (dictionaryOfRandomWords.isEmpty()) {
            return null;  // Если в файле нет слов, возвращаем null
        }

        int index = random.nextInt(dictionaryOfRandomWords.size());
        String magicWord = dictionaryOfRandomWords.get(index);
        return magicWord;
    }

    private static Scanner scanner = new Scanner(System.in);

    public static boolean input_check() {

        System.out.println("Добро пожаловать в игру виселица");
        System.out.println("Нажмите 1 если хотите начать игру и 2 если хотите выйти из игры");
        do {
            String input = scanner.next();
            if (input.equals("1")) {
                return true;
            } else if (input.equals("2")) {
                return false;
            } else {
                System.out.println("Вы ввели некорректно, пожалуйста нажмите 1 если хотите начать игру и 2 если хотите выйти из игры");
            }
        } while (true);
    }

    private static ArrayList <String> dictionary_of_entered_characters = new ArrayList<>();

    public static String letter_check() {
        System.out.println("Введите предположительную букву");
        String letter = scanner.next();
        if (letter.length()==1 && "qwertyuiopasdfghjklzxcvbnm".indexOf(letter.toLowerCase()) != -1 && dictionary_of_entered_characters.indexOf(letter.toLowerCase())==-1) {
//            System.out.println("Корректный ввод буквы");
            dictionary_of_entered_characters.add(letter.toLowerCase());
            return letter.toLowerCase();
        } else {
            do {
                System.out.println("Сожалею, вы нарушили одно из правил ввода");
                System.out.println("Напоминаю, необходимо ввести только одну букву английского алфавита, которая ещё не была использованна вами!!!");
                letter = scanner.next();
                if (letter.length()==1 && "qwertyuiopasdfghjklzxcvbnm".indexOf(letter.toLowerCase()) != -1) {
//                    System.out.println("Поздравляю, у вас получилось корректно ввести букву");
                    dictionary_of_entered_characters.add(letter.toLowerCase());
                    return letter.toLowerCase();
                }
            } while (true);
        }
    }

    public static String tablo_word(int a) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < a; i++) {
            str.append("*");
        }
        return str.toString();
    }

    public static void make_game_loop () {
        // принимает input
        // guess_a_word загадывает слово
        // сравнивает input c содержанием в слове и возвращает счётчик ошибок (count)
        // на основание count рисует виселицу
        show_rules_game();
        String magic_word = make_magic_word();
        System.out.println("Поздравляю, слово загадано");
        System.out.println("Оно состоит из " + magic_word.length() + " букв");
        String tablo_word = tablo_word(magic_word.length());
        System.out.println("Внимание на табло: " + tablo_word);
        int error_counter = 0;
        make_gallows(error_counter);
        LOOP_ERROR_COUNTER: while (true) {
            String Yuor_letter = letter_check();
            if (magic_word.indexOf(Yuor_letter.toLowerCase())==-1) {
                error_counter++;
                System.out.println("Сожалею, у вас не получилось отгадать букву");
                tablo_word = make_tablo_word(Yuor_letter, magic_word, tablo_word);// создать функцую которая будет раскрывать на табло отгаданные буквы
                System.out.println("Внимание на табло: " + tablo_word);
                make_gallows(error_counter);// создать функцию которая будет показывать состояние виселицы
                if (error_counter>5) {
                    System.out.println("Вы проиграли!!!");
                    break LOOP_ERROR_COUNTER;
                }
            } else {
                System.out.println("Поздравляю, вы отгадали букву " + Yuor_letter);
                tablo_word = make_tablo_word(Yuor_letter, magic_word, tablo_word);// создать функцую которая будет раскрывать на табло отгаданные буквы
                System.out.println("Внимание на табло: " + tablo_word);
                if (tablo_word.equals(magic_word)) {
                    System.out.println("Вы выйграли!!!");
                    break LOOP_ERROR_COUNTER;
                }
            }
        }
        start_game();
    }

    public static String make_tablo_word(String Yuor_letter, String magic_word, String tablo_word) {
        String[] mas_tablo = tablo_word.split("");
        String[] mas_magic_word = magic_word.split("");
        for (int i = 0; i < magic_word.length(); i++) {
            if (Yuor_letter.equals(mas_magic_word[i])) {
                mas_tablo[i]=Yuor_letter;
            }
        }
        return String.join("", mas_tablo);
    }

    public static void make_gallows (int a) {
        if (a==0) {
            System.out.println("  --------  ");
            System.out.println("  |      |  ");
            System.out.println("  |         ");
            System.out.println("  |         ");
            System.out.println("  |         ");
            System.out.println("__|__       ");
        }
        if (a==1) {
            System.out.println("  --------  ");
            System.out.println("  |      |  ");
            System.out.println("  |      0  ");
            System.out.println("  |         ");
            System.out.println("  |         ");
            System.out.println("__|__       ");
        }
        if (a==2) {
            System.out.println("  --------  ");
            System.out.println("  |      |  ");
            System.out.println("  |      0  ");
            System.out.println("  |      |  ");
            System.out.println("  |         ");
            System.out.println("__|__       ");
        }
        if (a==3) {
            System.out.println("  --------  ");
            System.out.println("  |      |  ");
            System.out.println("  |      0  ");
            System.out.println("  |      |  ");
            System.out.println("  |     /  ");
            System.out.println("__|__       ");
        }
        if (a==4) {
            System.out.println("  --------  ");
            System.out.println("  |      |  ");
            System.out.println("  |      0  ");
            System.out.println("  |      |  ");
            System.out.println("  |     / \\ ");
            System.out.println("__|__       ");
        }
        if (a==5) {
            System.out.println("  --------  ");
            System.out.println("  |      |  ");
            System.out.println("  |      0  ");
            System.out.println("  |     /|  ");
            System.out.println("  |     / \\ ");
            System.out.println("__|__       ");
        }
        if (a==6) {
            System.out.println("  --------  ");
            System.out.println("  |      |  ");
            System.out.println("  |      0  ");
            System.out.println("  |     /|\\ ");
            System.out.println("  |     / \\ ");
            System.out.println("__|__       ");
        }
    } // найти способ как нарисовать левую руку и левую ногу


    public static void main(String[] args) {
        start_game();
//        System.out.println(make_magic_word());
//        make_gallows(0);
//        System.out.println(make_tablo_word("a", "table", "*****"));

    }

}

