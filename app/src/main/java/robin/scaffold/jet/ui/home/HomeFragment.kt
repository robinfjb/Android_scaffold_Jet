package robin.scaffold.jet.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_home.*
import robin.scaffold.jet.R
import robin.scaffold.jet.ui.NavTestActivity
import robin.scaffold.jet.utils.startActivity

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.getText().observe(viewLifecycleOwner, Observer {
            text_home.text = "${text_home.text}\n$it"
        })
        homeViewModel.displayArgu(arguments)
        return root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        display_button.setOnClickListener {
            homeViewModel.display()
        }
        go_to_nav.setOnClickListener {
            requireContext().startActivity<NavTestActivity>()
        }
    }
}