package robin.scaffold.jet.ui.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import robin.scaffold.jet.R
import robin.scaffold.jet.databinding.FragmentDataBindingComponent
import robin.scaffold.jet.databinding.FragmentShareBinding
import robin.scaffold.jet.databinding.autoCleared

class ShareFragment : Fragment(), ShareAction{
    private lateinit var shareViewModel: ShareViewModel
    private var binding by autoCleared<FragmentShareBinding>()
    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shareViewModel = ViewModelProviders.of(this).get(ShareViewModel::class.java)
        val dataBinding = DataBindingUtil.inflate<FragmentShareBinding>(
                inflater,
                R.layout.fragment_share,
                container,
                false,
                dataBindingComponent
        )
        shareViewModel.getText().observe(viewLifecycleOwner, Observer {
            binding.textShare.text = it
        })
        binding = dataBinding
        binding.lifecycleOwner = viewLifecycleOwner
        binding.presenter = this@ShareFragment
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