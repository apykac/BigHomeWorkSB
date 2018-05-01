package ulanude;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Класс имплементирующий интерфейс посика
 */
public class OccurenciesHandler implements Occurencies {
    final static Logger LOGGER = Logger.getLogger(OccurenciesHandler.class);

    /**
     * Метод проиводит поиск слов в файла, одному потоку выдается один ресурс и весь массив слов, если же файл большого
     * размера то массив слов делиться на под массивы котрые отдаются отдельным потокам
     *
     * @param sources массив ресурсов
     * @param words   массив слов для поиска
     * @param res     файл в который будет писаться результат
     */
    @Override
    public void getOccurencies(String[] sources, String[] words, String res) {
        LOGGER.info("Begin of programm.");
        File file;
        int cores = Runtime.getRuntime().availableProcessors();
        long maxLength = biggestWord(words);
        FileWriterForThread.setFileName(res);
        ExecutorService executor = Executors.newFixedThreadPool(cores);
        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 0; i < sources.length; i++) {
            file = new File(sources[i]);

            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(sources[i]);
            } catch (FileNotFoundException e) {
                LOGGER.error(e.getMessage());
                continue;
            }
            // если файл слишклм большой, то он разделяется между потоками
            if (file.length() > 10_000_000) {
                int howMuchMore = (int) (file.length() % 10_000_000);
                int splitIndex = howMuchMore > cores ? cores : howMuchMore;
                boolean isLastPeace = false;
                for (int j = 0; j < splitIndex; j++) {
                    if (j + 1 == splitIndex) isLastPeace = true;
                    futures.add(executor.submit(new ThreadFinder(
                            inputStream,
                            sources[i],
                            clipArray(words, words.length / splitIndex, words.length / splitIndex * j, isLastPeace),
                            maxLength)));
                }
                continue;
            }
            //если файл малого размера, то он отдается одному потоку
            futures.add(executor.submit(new ThreadFinder(inputStream, sources[i], words, maxLength)));
        }

        for (Future<Long> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            } catch (ExecutionException e) {
                LOGGER.error(e.getMessage());
            }
        }

        executor.shutdown();
        LOGGER.info("End of programm.");
    }

    /**
     * возвращает чачть исходного массива, создан для разделения исходного массива слов на равномерные отрезки для
     * асинхронного посика слов в файлах большого размера
     *
     * @param arr         исходный массив слов
     * @param peace       размер куска на которые делится исходный массив
     * @param begin       начало куска
     * @param isLastPeace последний ли кусок в массиве (может быть большего размера чем обычный кусок)
     * @return возвращает кусок массива
     */
    private String[] clipArray(String[] arr, int peace, int begin, boolean isLastPeace) {
        int end = isLastPeace ? arr.length : begin + peace;
        String[] result = new String[end - begin];
        for (int i = begin, j = 0; i < end; i++, j++)
            result[j] = arr[i];
        return result;
    }

    /**
     * нахождение количество символов максимально длинного слова
     *
     * @param words массив слов
     * @return возвращает количество символов
     */
    private long biggestWord(String[] words) {
        long max = 0;
        for (int i = 0; i < words.length; i++)
            if (max < words[i].length())
                max = words[i].length();
        return max;
    }
}
