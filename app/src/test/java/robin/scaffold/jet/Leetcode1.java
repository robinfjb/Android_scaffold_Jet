package robin.scaffold.jet;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class Leetcode1 {
    @Test
    public void test() {
        int n = 4;
        int k = 2;
        List<List<Integer>> result = combine(n, k);
        result.toString();
    }

    List<List<Integer>> paths = new LinkedList<>();     // 总路径
    List<Integer> path = new LinkedList<>();            // 某条路径
    public List<List<Integer>> combine(int n, int k) {
        backtrack(1, n, k);
        return paths;
    }

    public void backtrack(int index, int n, int k) {
        int size = path.size();
        // 约束条件
        if (k - size > n - index + 1) {
            return;
        }
        // 结束条件
        if (size == k) {
            paths.add(new LinkedList(path));
            return;
        }

        // 选择列表
        for (int i = index; i <= n; i++) {
            path.add(i);                    // 选一个
            backtrack(i + 1, n, k);         // 进入下一层
            path.remove(path.size() - 1);   // 下一层返回，撤销选择
        }
    }
}
