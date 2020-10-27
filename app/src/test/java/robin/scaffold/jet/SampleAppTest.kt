package robin.scaffold.jet

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.junit.Test
import java.sql.DriverManager
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KProperty

class SampleAppTest {
    companion object {

    }
    data class Persion(
            val name:String,
            val age:String
    )

    var prop: String by Delegate()

    class Delegate {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
            return "$thisRef, thank you for delegating '${property.name}' to me!"
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            println("$value has been assigned to '${property.name}' in $thisRef.")
        }
    }

    @Test
    fun test() {
        println("start")
        sayHello{
            println("hello2")
        }
        println("end")
    }

    private inline fun sayHello(crossinline a: () -> Unit) {
        println("hello1")
        a()
    }

    open class Test2 {

    }

    class Test3 : Test2() {

    }

    @JvmOverloads fun f(a: String, b: Int = 0, c: String = "abc") {
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