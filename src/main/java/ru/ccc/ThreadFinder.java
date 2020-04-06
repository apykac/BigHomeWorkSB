package ru.ccc;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Класс нить для поиска слов в файле
 */
public class ThreadFinder implements Callable<Long> {
    final static Logger LOGGER = Logger.getLogger(ThreadFinder.class);

    private InputStream inputStream;
    /** количество симолов самого длинного слова */
    private long biggestLength;
    /** путь к файлу */
    private String source;
    /** спиок слов которых необходимо найти в файле */
    private String[] words;

    public ThreadFinder(InputStream inputStream, String source, String[] word, long biggestLength) {
        this.inputStream = inputStream;
        this.source = source;
        this.words = word;
        this.biggestLength = biggestLength;
    }

    /**
     * основной метод поиска слов в файле, использует StringBuilder созданным ввиде буфера размеров ~1мб, размер зависит
     * от самого длинного слова, после цикла поиска в буфере слов, буфер обнуляется и заполняется дальше пока не
     * закончится информация в файле
     */
    public Map<String, Long> findCoincidences() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "cp1251"))) {
            Map<String, Long> result = new HashMap<>();
            StringBuilder buffer = new StringBuilder(500_000);
            String s;
            boolean flag = false;
            while ((s = reader.readLine()) != null) {
                flag = false;
                buffer.append(s.toLowerCase()).append(' ');

                if ((buffer.length() < buffer.capacity() - 100_000) || (biggestLength > buffer.length())) continue;

                findAllCoincidencesInBuffer(buffer, result);
                flag = true;
                buffer.setLength(0);
            }
            if (!flag) findAllCoincidencesInBuffer(buffer, result);
            return result;
        }
    }

    /**
     * метод поиска всех слов в буфере, использует встроиный метод StringBuilder::indexOf для поиска вхождения слов
     *
     * @param bufferString буффер максимальным объемом ~1мб
     */
    private void findAllCoincidencesInBuffer(StringBuilder bufferString, Map<String, Long> result) {
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; bufferString.indexOf(words[i], j) != -1; j += words[i].length()) {
                if (isAWord(bufferString, j, i)) {
                    if (result.containsKey(words[i])) result.put(words[i], result.get(words[i]) + 1);
                    else                              result.put(words[i], 1L);
                }
            }
        }
    }

    /**
     * метод проверяющий вхождение наденое методом {@link StringBuilder#indexOf(String, int)} самостоятельное слово или
     * часть другого слова проверя какие символы стоят по краям найденного отрезка, если это символы пунктуации или
     * пробел, то слово найдено
     *
     * @param sb    буффер в котором ведеться поиск
     * @param begin индекс начала проверки слова
     * @param index индекс конкретного слова из массива слов, для поиска в файле, которое мы ищем в данный момент
     * @return нашли ли мы слово или нет
     */
    private boolean isAWord(StringBuilder sb, int begin, int index) {
        if (((begin - 1) >= 0) && !ConstantContainer.isCharPunc(sb.charAt(begin - 1))) return false;
        return ((begin + words[index].length()) >= sb.length()) || ConstantContainer.isCharPunc(sb.charAt(begin + words[index].length()));
    }

    /**
     * Метод интерфейся Callable для запуска нити
     */
    @Override
    public Long call() throws Exception {
        LOGGER.info("Start parsing by Thread: {" + Thread.currentThread().getName() + "} source: [" + source + "]");
        FileWriterForThread.writeResultToFile(source, findCoincidences());
        inputStream.close();
        LOGGER.info("Thread: {" + Thread.currentThread().getName() + "} finish parsing");
        return 0L;
    }
}
