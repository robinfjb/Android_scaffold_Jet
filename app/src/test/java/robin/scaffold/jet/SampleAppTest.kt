package robin.scaffold.jet

import org.junit.Test
import java.util.*
import kotlin.collections.HashMap

class SampleAppTest {



    @Test
    fun test() {
        val s = "ADOBECODEBANC"
        val t = "ABC"
    }

    fun topKFrequent(nums: IntArray, k: Int): IntArray? {
        val hashMap = HashMap<Int, Int>()
        nums.forEach {
            var value = hashMap.get(it)
            if(value == null) {
                value = 1
            } else {
                value ++
            }
            hashMap.put(it, value)
        }
        val pairList = hashMap.toList()
        Collections.sort(pairList, kotlin.Comparator { o1, o2 ->
            return@Comparator o2.second.compareTo(o1.second)
        })
        val result = IntArray(k)
        var i = 0
        pairList.iterator().forEach {
            if(i < k) {
                result[i] = it.first
            }
            i++
        }
        return result
    }
}