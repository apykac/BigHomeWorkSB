package ulanudeTest;

import org.junit.*;
import ulanude.ThreadFinder;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ThreadFinderTest {
    ThreadFinder threadFinder;
    InputStream inputStream;

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

    @Test
    public void findCoincidencesTest() {
        Map<String, Long> result;
        try {
            result = threadFinder.findCoincidences();
        } catch (IOException e) {
            return;
        }
        Assert.assertTrue("Words is wrong", result.containsKey("temp"));
        Assert.assertTrue("Result of search is wrong", result.get("temp") == 3);
    }

    @Test(expected = IOException.class)
    public void findCoincidencesTestWithException() throws IOException {
        threadFinder = new ThreadFinder(new FileInputStream("thereisnofile"), "",null,0);
        threadFinder.findCoincidences();
    }
}