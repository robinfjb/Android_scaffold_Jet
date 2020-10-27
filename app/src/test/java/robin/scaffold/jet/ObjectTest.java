package robin.scaffold.jet;

import org.junit.Test;

import java.util.Objects;
import java.util.Random;

public class ObjectTest {
    @Test
    public void test() {
        Cat cat1 = new Cat(100,100);
        Cat cat2 = new Cat(100,100);
        System.out.println(cat1.equals(cat2));
        System.out.println(cat1.hashCode());
        System.out.println(cat1.hashCode());
    }

    private class Cat {
        private int height;
        private int weight;
        public Cat( int h, int w)
        {
            this.height = h;
            this.weight = w;
        }


        @Override
        public int hashCode() {
            return Objects.hash(height, weight) +  new Random().nextInt();
        }

        @Override
        public boolean equals (Object o){
        if (!(o instanceof Cat))
            return false;
        Cat c = (Cat) o;
        return c.height == height && c.weight == weight;
        }
    }
}
