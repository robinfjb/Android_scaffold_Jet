package robin.scaffold.jet

class StaticTest {
    companion object{

        const val field1="111"

        val field2="222"

        @JvmField val field3="333"

        fun callNonStatic(){

        }
        @JvmStatic
        fun callStatic(){

        }
    }
}