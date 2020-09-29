package robin.sdk.ndk

import android.content.Context

class JniManager {
    init {
        System.loadLibrary("encrypt")
    }

    external fun getPublicKey(context : Context):String

    external fun getSecret(context : Context):String

    external fun getDesKey(context: Context):String
}