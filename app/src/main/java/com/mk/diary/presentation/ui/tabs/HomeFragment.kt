package com.mk.diary.presentation.ui.tabs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mk.diary.R
import com.mk.diary.adapters.recyclerview.HomeFragmentNoteViewRecyclerAdapter
import com.mk.diary.databinding.FragmentHomeBinding
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.interfaces.ItemClick
import com.mk.diary.presentation.viewmodels.noteview.NoteViewModel
import com.mk.mydiary.utils.MyConstants
import com.mk.mydiary.utils.SharedPrefObj
import com.mk.mydiary.utils.appext.shortToast
import com.mk.mydiary.utils.companion.Static
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel : NoteViewModel by viewModels()
    private val modelList :ArrayList<NoteViewModelClass> by lazy { ArrayList() }
    private val mAdapter by lazy { HomeFragmentNoteViewRecyclerAdapter(modelList,) }
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        Static.checkBBB=false

        binding.searchViewBtn.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_noteViewSearchFragment) }
        recyclerView()
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        if (Static.positionDelete!=null){
            modelList.removeAt(Static.positionDelete!!)
            Static.positionDelete?.let { mAdapter.notifyItemRemoved(it) }
            Static.positionDelete=null
        }
        mAdapter.notifyDataSetChanged()
    }
    private fun recyclerView() {
        binding.homeRecyclerView.adapter=mAdapter
        lifecycleScope.launch {
            viewModel.getAllData().observe(viewLifecycleOwner){result->
                if (result.isEmpty()){
                    binding.tvResultEmptyLayout.visibility=View.VISIBLE
                }else{
                    modelList.clear()
                    binding.tvResultEmptyLayout.visibility=View.GONE
                    modelList.addAll(result)
                }
            }
        }
        mAdapter.recyclerClick(object : HomeFragmentNoteViewRecyclerAdapter.PassData{
            override fun clickFunction(modelClass: NoteViewModelClass,position:Int) {
                Static.positionDelete=position
                val bundle = Bundle()
                bundle.putSerializable(MyConstants.PASS_DATA, modelClass)
                findNavController().navigate(R.id.action_homeFragment_to_noteViewDetailsFragment2, bundle)
            }
        })
    }
}