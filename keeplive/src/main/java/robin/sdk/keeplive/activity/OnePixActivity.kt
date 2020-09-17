package robin.sdk.keeplive.activity

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.view.Gravity

class OnePixActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设定一像素的activity
        val window = window
        window.setGravity(Gravity.LEFT or Gravity.TOP)
        val params = window.attributes
        params.x = 0
        params.y = 0
        params.height = 1
        params.width = 1
        window.attributes = params
        checkScreenOn()
    }

    override fun onResume() {
        super.onResume()
        checkScreenOn()
    }


    /**
     * 检查屏幕是否点亮
     */
    private fun checkScreenOn() {
        val pm = this@OnePixActivity.getSystemService(Context.POWER_SERVICE) as PowerManager
        val isScreenOn = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            pm.isInteractive
        } else {
            pm.isScreenOn
        }
        if (isScreenOn) {
            finish()
        }
    }
}