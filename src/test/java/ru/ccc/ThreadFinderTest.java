package ru.ccc;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ThreadFinderTest {
    private ThreadFinder threadFinder;
    private InputStream inputStream;

    @Before
    public void before() {
        String stringForInputStream = "temp   dfasdf ahfwae h9ewhwf14=     `513=84h8u1u1ug gj  0\n" +
                "=3qti5ysj bx voAES {g' feuas58r\n" +
                " 6u  jk\n" +
                " 9t\n" +
                " eri0\n" +
                " temp\n" +
                " tmeptmeptmtepeptmepmettemp\n" +
                " temp.";
        inputStream = new ByteArrayInputStream(stringForInputStream.getBytes());
        threadFinder = new ThreadFinder(inputStream,"SOURCE",new String[]{"temp"},4);
    }

    @After
    public void after() {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        threadFinder = null;
    }

    /**
     * Проверка правильности поиска слова в исходных данных
     */
    @Test
    public void findCoincidencesTest() {
        Map<String, Long> result;
        try {
            result = threadFinder.findCoincidences();
        } catch (IOException e) {
            return;
        }
        Assert.assertTrue("Words is wrong", result.containsKey("temp"));
        Assert.assertSame("Result of search is wrong", result.get("temp") , 3L);
    }

    /**
     * проверка на исключение метода поиска слова в исходных данных
     * @throws IOException some errors
     */
    @Test(expected = IOException.class)
    public void findCoincidencesTestWithException() throws IOException {
        threadFinder = new ThreadFinder(new FileInputStream("thereisnofile"), "",null,0);
        threadFinder.findCoincidences();
    }
}
