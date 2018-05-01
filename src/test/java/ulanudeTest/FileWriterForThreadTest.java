package ulanudeTest;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import ulanude.FileWriterForThread;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileWriterForThreadTest {
    private Logger LOGGER = Logger.getLogger(FileWriterForThreadTest.class);
    private String name = "temporary";

    @After
    public void after() {
        new File("temporary").delete();
    }

    @Test
    public void writeResultToFileTest() {
        Map<String, Long> map = new HashMap<>();
        map.put("WORD", 1L);
        FileWriterForThread.setFileName("temporary");
        FileWriterForThread.writeResultToFile("SOURCE", map);
        String result = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("temporary")))) {
            result = reader.readLine();
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        Assert.assertTrue("Result string is not correct", result.equals("[SOURCE] -> \"WORD\" @1"));
    }
}
