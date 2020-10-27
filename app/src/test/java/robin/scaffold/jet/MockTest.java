package robin.scaffold.jet;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockTest {
    @Test
    public void test() {
        List<String> mockedList = new ArrayList();
        //mock creation List
        mockedList = mock(List.class);
//using mock object - it does not throw any "unexpected interaction" exception
        mockedList.add("one");
// selective, explicit, highly readable verification
        verify(mockedList).add("one");
    }
}
