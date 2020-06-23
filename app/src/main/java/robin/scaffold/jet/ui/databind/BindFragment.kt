package robin.scaffold.jet.ui.databind

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import robin.scaffold.jet.R
import robin.scaffold.jet.databinding.FragmentDataBindingComponent
import robin.scaffold.jet.databinding.FragmentShareBinding
import robin.scaffold.jet.databinding.autoCleared

class BindFragment : Fragment(), TestAction{
    private lateinit var bindViewModel: BindViewModel
    private var binding by autoCleared<FragmentShareBinding>()
    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindViewModel = ViewModelProviders.of(this).get(BindViewModel::class.java)
        val dataBinding = DataBindingUtil.inflate<FragmentShareBinding>(
                inflater,
                R.layout.fragment_share,
                container,
                false,
                dataBindingComponent
        )
        bindViewModel.getText().observe(viewLifecycleOwner, Observer {
            binding.textShare.text = it
        })
        binding = dataBinding
        binding.lifecycleOwner = viewLifecycleOwner
        binding.presenter = this@BindFragment
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.user = User("robin", 777)
    }

    override fun onPageCLick() {
        binding.textShare.text = "页面被点击了${System.currentTimeMillis()}"
    }
}