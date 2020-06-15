package robin.scaffold.jet.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import robin.scaffold.jet.R

class NavTestActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        navController = findNavController(R.id.nav_fragment)
        navController.addOnDestinationChangedListener {
            controller, destination, arguments -> Log.i("addOnNavigatedListener", "label${destination.label}|id:${destination.id}")
        }
    }
}