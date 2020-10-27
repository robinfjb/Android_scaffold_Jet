package robin.scaffold.jet;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 例如，当 S = "abc" 时，考虑字母 "a", "b", "c"，初始令 ans = [""]，依次更新 ans = ["a", "A"]， ans = ["ab", "Ab", "aB", "AB"]， ans = ["abc", "Abc", "aBc", "ABc", "abC", "AbC", "aBC", "ABC"]。
 *
 * 作者：LeetCode
 * 链接：https://leetcode-cn.com/problems/letter-case-permutation/solution/zi-mu-da-xiao-xie-quan-pai-lie-by-leetcode/
 * 来源：力扣（LeetCode）
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class Leetcode4 {
    @Test
    public void test() {
        List<String> result = letterCasePermutation("a1b2");
        result.toString();

        List<String> result2 = backtrack(new ArrayList(), "a1b2", "", 0);
        result2.toString();


        Utils.one();
        Utils.two();
    }

    public List<String> letterCasePermutation(String S) {
        List<StringBuilder> ans = new ArrayList();
        ans.add(new StringBuilder());

        for (char c: S.toCharArray()) {
            int n = ans.size();
            if (Character.isLetter(c)) {
                for (int i = 0; i < n; ++i) {
                    ans.add(new StringBuilder(ans.get(i)));
                    ans.get(i).append(Character.toLowerCase(c));
                    ans.get(n+i).append(Character.toUpperCase(c));
                }
            } else {
                for (int i = 0; i < n; ++i)
                    ans.get(i).append(c);
            }
        }

        List<String> finalans = new ArrayList();
        for (StringBuilder sb: ans)
            finalans.add(sb.toString());
        return finalans;
    }

    private List<String> backtrack(List<String> ans, String S, String str, int index) {
        if (index ==  S.length()) {
            ans.add(str);
            return ans;
        }
        char c = S.charAt(index);
        if (Character.isLetter(c)) {
            String low = str + Character.toLowerCase(c);
            String high = str + Character.toUpperCase(c);
            backtrack(ans, S, low, index + 1);
            backtrack(ans, S, high, index + 1);
        } else {
            backtrack(ans, S, str + c, index + 1);
        }
        return ans;
    }
}
