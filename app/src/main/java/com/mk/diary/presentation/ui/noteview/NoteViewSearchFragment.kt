package com.mk.diary.presentation.ui.noteview

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mk.diary.adapters.recyclerview.HomeFragmentNoteViewRecyclerAdapter
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.presentation.viewmodels.noteview.NoteViewModel
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.companion.Static
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentNoteViewSearchBinding

@AndroidEntryPoint
class NoteViewSearchFragment : Fragment() {
    val filteredItems = ArrayList<NoteViewModelClass>()
    private val viewModel : NoteViewModel by viewModels()
    private val modelList :ArrayList<NoteViewModelClass> by lazy { ArrayList() }
    private val mAdapter by lazy { HomeFragmentNoteViewRecyclerAdapter(modelList,) }
    lateinit var binding: FragmentNoteViewSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentNoteViewSearchBinding.inflate(layoutInflater,container,false)
        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        refreshLayout()
        getDataRecyclerView()
        searchRecyclerView()
        mAdapter.notifyDataSetChanged()
    }
    private fun refreshLayout() {
        binding.swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), android.R.color.holo_orange_dark),
            ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark),
            ContextCompat.getColor(requireContext(), R.color.pinkButtonColor),
            ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark)
        )
        binding.swipeRefreshLayout.setOnRefreshListener {
            modelList.clear()
            getDataRecyclerView()
            binding.swipeRefreshLayout.isRefreshing = false

        }
    }
    private fun getDataRecyclerView() {
        binding.homeRecyclerView.adapter = mAdapter
        lifecycleScope.launch {
            viewModel.getAllData().observe(viewLifecycleOwner) { result ->
                if (result.isEmpty()) {
                    binding.tvResultEmptyLayout.visibility = View.VISIBLE
                } else {
                    modelList.clear()
                    binding.tvResultEmptyLayout.visibility = View.GONE
                    modelList.addAll(result)
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
    }
    private fun searchRecyclerView(){
        mAdapter.recyclerClick(object : HomeFragmentNoteViewRecyclerAdapter.PassData{
            override fun clickFunction(modelClass: NoteViewModelClass,position:Int) {
                Static.positionDelete=position
                val bundle = Bundle()
                bundle.putSerializable(MyConstants.PASS_DATA, modelClass)
                findNavController().navigate(R.id.action_noteViewSearchFragment_to_noteViewDetailsFragment2, bundle)
            }
        })
        binding.searchViewEd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(text: Editable?) {
                for (item in modelList) {
                    if (item.mainTitle?.contains(text.toString(), ignoreCase = true) == true ||
                        item.description?.contains(text.toString(), ignoreCase = true) == true
                    ) {
                        filteredItems.clear()
                        filteredItems.add(item)
                    }
                }
                if (filteredItems.isEmpty()){
                    binding.tvResultEmptyLayout.visibility=View.VISIBLE
                    binding.homeRecyclerView.visibility=View.GONE
                }else{
                    binding.tvResultEmptyLayout.visibility=View.GONE
                    binding.homeRecyclerView.visibility=View.VISIBLE
                }
                mAdapter.filterList(filteredItems,text.toString())
                mAdapter.notifyDataSetChanged()
            }
        })
    }

}