package net.xipfs.tinynosql.test.io.sstable.filters;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import net.xipfs.tinynosql.core.sstable.MurMurHasher;
import net.xipfs.tinynosql.core.sstable.filters.BloomFilter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class BloomFilterTest {
    @Test
    public void isPresent() throws Exception {
        BloomFilter f = new BloomFilter(1000, new MurMurHasher());
        for (int i = 0; i < 1000; i++) {
            String s = RandomStringUtils.randomAlphanumeric(1000);
            assertFalse(f.isPresent(s));
        }
    }

    @Test
    public void add() throws Exception {
        BloomFilter f = new BloomFilter(1024, new MurMurHasher());
        int falsePresent = 0;
        int size = 1024 / 2;
        for (int i = 0; i < size; i++) {
            String s = RandomStringUtils.randomAlphanumeric(50);
            f.add(s);
            assertTrue(f.isPresent(s));
        }
        for (int i = 0; i < size; i++) {
            String s = RandomStringUtils.randomAlphanumeric(50);
            if (f.isPresent(s)) falsePresent++;
        }
        System.out.printf("false present: %.2f %%\n", falsePresent * 100. / size);
        for (long l : f.toLongs()) {
            System.out.println(l);
        }
    }

}