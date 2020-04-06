package ru.ccc;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
        FileWriterForThread.setFileName(name);
        FileWriterForThread.writeResultToFile("SOURCE", map);
        String result = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(name)))) {
            result = reader.readLine();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        Assert.assertEquals("Result string is not correct", result, "[SOURCE] -> \"WORD\" @1");
    }
}
