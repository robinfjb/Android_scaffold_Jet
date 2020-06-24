package robin.scaffold.jet.ui.paging

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_second_home.*
import robin.scaffold.jet.R
import robin.scaffold.jet.databinding.FragmentDataBindingComponent
import robin.scaffold.jet.databinding.FragmentSecondHomeBinding
import robin.scaffold.jet.databinding.autoCleared
import robin.scaffold.jet.net.Status

class PagingFragment : Fragment() {
    private lateinit var pagingViewModel: PagingViewModel
    private lateinit var mAdapter: PagingDataAdpter
    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent()
    private var binding by autoCleared<FragmentSecondHomeBinding>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        pagingViewModel = ViewModelProviders.of(this)[PagingViewModel::class.java]
        val dataBinding = DataBindingUtil.inflate<FragmentSecondHomeBinding>(
                inflater,
                R.layout.fragment_second_home,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding
        return dataBinding.root
        return inflater.inflate(R.layout.fragment_second_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        go_to_home.setOnClickListener {
            Navigation.findNavController(it).navigate(
                    PagingFragmentDirections.actionHomeSecondFragmentToHomeFragment("这是传过来的参数")
            )
        }
        refresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW)
        refresh.setOnRefreshListener {
            pagingViewModel.refresh()
        }
        pagingViewModel.refreshState.observe(viewLifecycleOwner, Observer{
            if (it==null){
                return@Observer
            }
            when(it.status){
                Status.LOADING->{
                    refresh.isRefreshing=true
                }
                Status.SUCCESS->{
                    refresh.isRefreshing=false
                }
                Status.ERROR->{
                    refresh.isRefreshing=false
                }
            }
        })
        rv.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))//添加分割线

        mAdapter= PagingDataAdpter(dataBindingComponent) {book, retry ->
            if(retry) {
                pagingViewModel.retry()
            } else {
                Log.d("PagingFragment", book?.name ?: "unknow data")
            }
        }
        rv.adapter = mAdapter

        pagingViewModel.networkState.observe(viewLifecycleOwner, Observer{
            mAdapter.setNetworkState(it)
        })
        pagingViewModel.posts.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
        pagingViewModel.invoke()
    }
}