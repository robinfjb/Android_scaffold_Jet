package robin.scaffold.jet;

import android.app.Activity;
import android.app.Application;
import android.util.DisplayMetrics;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class Leetcode3 {
    @Test
    public void test() {
        String s1 = "abcaacbb";
        String s2 = "bbbbb";
        String s3 = "pwwkew";
        Assert.assertEquals(lengthOfLongestSubstring(s1), 3);
        Assert.assertEquals(lengthOfLongestSubstring(s2), 1);
        Assert.assertEquals(lengthOfLongestSubstring(s3), 3);
    }

    public int lengthOfLongestSubstring(String s) {
        if (s.length()==0) return 0;
        HashMap<Character, Integer> map = new HashMap<>();
        int max = 0;
        int left = 0;
        for(int i = 0; i < s.length(); i ++){
            if(map.containsKey(s.charAt(i))){
                left = Math.max(left,map.get(s.charAt(i)) + 1);
            }
            map.put(s.charAt(i),i);
            max = Math.max(max,i-left+1);
        }
        return max;
    }
}
