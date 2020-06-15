package robin.scaffold.jet.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_second_home.*
import robin.scaffold.jet.R

class HomeSecondFragment : Fragment() {
    private lateinit var homeViewModel: HomeSecondViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProviders.of(this)[HomeSecondViewModel::class.java]
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            text_home2.text = "${text_home2.text}\n$it"
        })
        return inflater.inflate(R.layout.fragment_second_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        go_to_home.setOnClickListener {
            Navigation.findNavController(it).navigate(
                    HomeSecondFragmentDirections.actionHomeSecondFragmentToHomeFragment("这是传过来的参数")
            )
        }
    }
}