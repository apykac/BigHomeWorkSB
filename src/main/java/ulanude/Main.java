package ulanude;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        new File("result.txt").delete();
        //String[] words = listToArray(fileToList("words.txt"));
        String[] words = {"starter", "ffdf", "wfrrf", "cdcd", "dc"};
        String[] sources = listToArray(fileToList("c:\\temp4Java\\sources.txt"));
        Occurencies oh = new OccurenciesHandler();
        oh.getOccurencies(sources, words, "c:\\temp4Java\\result.txt");
    }

    /**
     * перевод данных из файла в список
     *
     * @param fileName путь к файлу
     * @return возвращает список путей к файлам
     */
    private static List<String> fileToList(String fileName) {
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "CP1251"))) {
            String s;
            while ((s = reader.readLine()) != null)
                    list.add("c:\\temp4Java\\testSet\\medium\\" + s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * перевод из списка путей к файлам в массив путей к файлам
     *
     * @param list список пуйте к файлам
     * @return возвращается массив
     */
    private static String[] listToArray(List<String> list) {
        String[] array = new String[list.size()];
        int i = 0;
        for (String s : list) {
            array[i] = s;
            i++;
        }
        return array;
    }
}
