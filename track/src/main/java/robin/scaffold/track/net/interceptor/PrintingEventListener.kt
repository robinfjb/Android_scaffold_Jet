package robin.scaffold.track.net.interceptor

import android.util.Log
import okhttp3.Call
import okhttp3.EventListener
import robin.scaffold.track.Constants.TAG
import java.net.InetAddress

class PrintingEventListener(private val callId:String, private val callStartNanos:Long) : EventListener() {
    companion object {
        val FACTORY = object : Factory {
            override fun create( call:Call):EventListener {
                val callId = call.request().url.toString()
                return PrintingEventListener(callId, System.nanoTime());
            }
        }
    }

    private fun printEvent(name: String) {
        val elapsedNanos = System.nanoTime() - callStartNanos
        Log.d(TAG, String.format("%s %.3f %s%n", callId, (elapsedNanos / 1000000000.toDouble()).toFloat(), name))
    }

    override fun callStart(call: Call) {
        printEvent("callStart");
    }

    override fun callEnd(call: Call) {
        printEvent("callEnd");
    }

    override fun dnsStart(call: Call, domainName: String) {
        printEvent("dnsStart");
    }

    override fun dnsEnd(call: Call, domainName: String, inetAddressList: List<InetAddress>) {
        printEvent("dnsEnd");
    }
}