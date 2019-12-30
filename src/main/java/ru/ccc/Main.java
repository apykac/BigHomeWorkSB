package ru.ccc;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        new File("result.txt").delete();
        //String[] words = listToArray(fileToList("words.txt"));
        String[] words = {"starter", "ffdf", "wfrrf", "cdcd", "dc"};
        String[] sources = fileToList("c:\\temp4Java\\sources.txt").toArray(new String[0]);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
