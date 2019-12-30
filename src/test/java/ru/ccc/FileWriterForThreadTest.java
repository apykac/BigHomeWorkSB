package ru.ccc;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileWriterForThreadTest {
    private Logger LOGGER = Logger.getLogger(FileWriterForThreadTest.class);
    private String name = "temporary";

    /**
     * проверка корректности записи в файл результата поиска
     */
    @Test
    public void writeResultToFileTest() {
        Map<String, Long> map = new HashMap<>();
        map.put("WORD", 1L);
        FileWriterForThread.setFileName("temporary");
        FileWriterForThread.writeResultToFile("SOURCE", map);
        String result = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("temporary")))) {
            result = reader.readLine();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        Assert.assertEquals("Result string is not correct", result, "[SOURCE] -> \"WORD\" @1");
    }
}
