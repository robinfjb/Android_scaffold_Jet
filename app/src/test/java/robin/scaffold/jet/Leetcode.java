package robin.scaffold.jet;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Leetcode {

    @Test
    public void test() {
        int[] big = new int[]{7,5,9,0,2,1,3,5,7,9,1,1,5,8,8,9,7};
        int[] small = new int[]{1,5,9};
        int[] result = shortestSeq(big, small);
        result.toString();
    }

    public int[] shortestSeq(int[] big, int[] small) {
        if(small.length>big.length)
            return new int[0];
        HashMap<Integer,Integer> map=new HashMap<>();
        int count=small.length;

        int[] ans= new int[]{0, big.length};

        for(int i:small)
            map.put(i,-1);

        for(int i=0;i<big.length;i++){
            if(map.containsKey(big[i])){
                if(map.get(big[i])==-1)
                    count--;
                map.put(big[i],i);
            }
            if(count<=0){
                Collection<Integer> c = map.values();
                List<Integer> list = new ArrayList<>();
                list.addAll(c);
                Collections.sort(list);
                int minNum=getMin(list);
                int maxNum = getMax(list);
                if(maxNum - minNum < ans[1]-ans[0]) {
                    ans[0]=minNum;
                    ans[1]=maxNum;
                }
            }
        }
        if(count>0)
            return new int[0];
        return ans;
    }

    int getMin(List<Integer> list){
        int minNum=Integer.MAX_VALUE;
        for(int i:list){
            minNum=Math.min(i,minNum);
        }
        return minNum;
    }

    int getMax(List<Integer> list){
        int maxNum=Integer.MIN_VALUE;
        for(int i:list){
            maxNum=Math.max(i,maxNum);
        }
        return maxNum;
    }
}
