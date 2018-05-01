package ulanude;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Класс нить для поиска слов в файле
 */
public class ThreadFinder implements Callable {
    final static Logger LOGGER = Logger.getLogger(ThreadFinder.class);
    /**
     * @param result содержит работу нити ввиде ввиде пары (СЛОВО) -> (КОЛИЧЕСТВО_ВХОЖДЕНИЙ_В_ФАЙЛЕ)
     * @param biggestLength количество симолов самого длинного слова
     * @param source путь к файлу
     * @param words спиок слов которых необходимо найти в файле
     */
    private InputStream inputStream;
    private long biggestLength = 0;
    private String source;
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "cp1251"));
        Map<String, Long> result = new HashMap<>();
        StringBuilder buffer = new StringBuilder(500_000);
        String s;
        boolean flag = false;
        while ((s = reader.readLine()) != null) {
            flag = false;
            buffer.append(s.toLowerCase());
            buffer.append(' ');
            if ((buffer.length() < buffer.capacity() - 100_000) || (biggestLength > buffer.length())) continue;
            findAllCoincidencesInBuffer(buffer, result);
            flag = true;
            buffer.setLength(0);
        }
        if (!flag) findAllCoincidencesInBuffer(buffer, result);
        return result;
    }

    /**
     * метод поиска всех слов в буфере, использует встроиный метод StringBuilder::indexOf для поиска вхождения слов
     *
     * @param bufferString буффер максимальным объемом ~1мб
     */
    private void findAllCoincidencesInBuffer(StringBuilder bufferString, Map<String, Long> result) {
        for (int i = 0; i < words.length; i++) {
            int j = 0;
            while ((j = bufferString.indexOf(words[i], j)) != -1) {
                if (isAWord(bufferString, j, i))
                    if (result.containsKey(words[i])) result.put(words[i], result.get(words[i]) + 1);
                    else result.put(words[i], 1L);
                j+=words[i].length();
            }
        }
    }

    /**
     * метод проверяющий вхождение наденое методом StringBuilder::indexOf самостоятельное слово или часть другого слова
     * проверя какие символы стоят по краям найденного отрезка, если это символы пунктуации или пробел, то слово найдено
     *
     * @param sb    буффер в котором ведеться поиск
     * @param begin индекс начала проверки слова
     * @param index индекс конкретного слова из массива слов, для поиска в файле, которое мы ищем в данный момент
     * @return нашли ли мы слово или нет
     */
    private boolean isAWord(StringBuilder sb, int begin, int index) {
        if (((begin - 1) >= 0) && !isCharPunc(sb.charAt(begin - 1))) return false;
        if (((begin + words[index].length()) < sb.length()) && !isCharPunc(sb.charAt(begin + words[index].length())))
            return false;
        return true;
    }

    /**
     * метод проверки символа на то что он являет символом пунктуации или пробелом
     *
     * @param c символ для сравнения
     * @return символ пунктуации или пробел на переданный на вход символ
     */
    private boolean isCharPunc(char c) {
        for (int i = 0; i < ConstantContainer.punctuation.length(); i++)
            if (c == ConstantContainer.punctuation.charAt(i)) return true;
        return false;
    }

    /**
     * Метод интерфейся Callable для запуска нити
     *
     * @return
     * @throws Exception
     */
    @Override
    public Object call() throws Exception {
        LOGGER.info("Start parsing by Thread: {" + Thread.currentThread().getName() + "} source: [" + source + "]");
        FileWriterForThread.writeResultToFile(source, findCoincidences());
        inputStream.close();
        LOGGER.info("Thread: {" + Thread.currentThread().getName() + "} finish parsing");
        return 0;
    }
}
