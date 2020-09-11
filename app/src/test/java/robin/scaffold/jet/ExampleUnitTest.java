package robin.scaffold.jet;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    final char[] line1 = new char[]{'Q','W','E','R','T','Y','U','I','O','P'};
    final char[] line2 = new char[]{'A','S','D','F','G','H','J','K','L'};
    final char[] line3 = new char[]{'Z','X','C','V','B','N','M'};

    @Test
    public void test() {
        String[] ss = findWords(new String[]{"Hello", "Alaska", "Dad", "Peace"});
        ss.toString();
    }


    public String[] findWords(String[] words) {
        if(words == null) return null;
        List<String> results = new ArrayList<>();
        for(int i = 0; i < words.length; i++) {
            String word = words[i];
            if(isInsameLine(word)) {
                results.add(word);
            }
        }
        return results.toArray(new String[results.size()]);
    }

    private boolean isInsameLine(String word) {
        int lineIndexStart = -1;
        int lineIndexDetect = -1;
        char[] items = word.toCharArray();
        for(int i = 0; i < items.length; i++) {
            char item = Character.toUpperCase(items[i]);
            if(String.valueOf(line1).indexOf(item) > -1) {
                lineIndexDetect = 0;
            } else if(String.valueOf(line2).indexOf(item) > -1) {
                lineIndexDetect = 1;
            } else if(String.valueOf(line3).indexOf(item) > -1) {
                lineIndexDetect = 2;
            }
            if(i == 0) {
                lineIndexStart = lineIndexDetect;
            }
            if(lineIndexDetect != lineIndexStart) {
                return false;
            }
        }
        return lineIndexDetect > -1 && lineIndexStart > -1;
    }
}