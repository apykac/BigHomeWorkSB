package ulanude;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Класс для Записи результата работы потока
 */

public class FileWriterForThread {
    final static Logger LOGGER = Logger.getLogger(FileWriterForThread.class);
    /**
     * @param fileName путь к файлу в котором будет вестись запись результата
     */
    private static String fileName;

    public static boolean setFileName(String fileName) {
        FileWriterForThread.fileName = fileName;
        LOGGER.info("Set a file to record the results: " + fileName);
        return true;
    }

    /**
     * Метод записывает данные работы потока
     *
     * @param source путь к файлу в котором велся поиск
     * @param map    данные работы потока, ввиде пары (СЛОВО) -> (КОЛИЧЕСТВО_ВХОЖДЕНИЙ_В_ФАЙЛЕ)
     */
    synchronized public static void writeResultToFile(String source, Map<String, Long> map) {
        LOGGER.info("Writing result of parsing of source: [" + source + "] by Thread {" + Thread.currentThread().getName() + "}");
        for (Map.Entry<String, Long> pair : map.entrySet()) {
            tryToWrite(source, pair.getKey(), pair.getValue());
        }
    }

    /**
     * Метод прямой записи в конец файла
     *
     * @param source пусть к файлу в котором велся поиск
     * @param word   слово которое искалось в файле
     * @param value  количество вхождений слова в файле
     *               результат записывается ввиде:
     *               [source] -> "word" @value
     */
    private static boolean tryToWrite(String source, String word, long value) {
        try (FileOutputStream writer = new FileOutputStream(fileName, true)) {
            writer.write(ConstantContainer.s_1);
            writer.write(source.getBytes());
            writer.write(ConstantContainer.s_2);
            writer.write(word.getBytes());
            writer.write(ConstantContainer.s_3);
            writer.write(("" + value).getBytes());
            writer.write(ConstantContainer.s_4);
        } catch (FileNotFoundException e1) {
            LOGGER.error(e1.getMessage());
            return false;
        } catch (IOException e1) {
            LOGGER.error(e1.getMessage());
            return false;
        }
        return true;
    }
}
