package com.mk.diary.presentation.ui.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mk.diary.adapters.recyclerview.GalleryMainRecyclerAdapter
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.presentation.viewmodels.noteview.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentGalleryBinding


@AndroidEntryPoint
class GalleryFragment : Fragment() {
    private val viewModel :NoteViewModel by viewModels()
    lateinit var binding: FragmentGalleryBinding
    private val uniqueDatesSet = HashSet<String>()

    private val modelList :ArrayList<NoteViewModelClass> by lazy { ArrayList() }
    private val mAdapter by lazy {
        GalleryMainRecyclerAdapter(modelList,findNavController()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentGalleryBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        modelList.clear()
        mAdapter.resetAdapter()
        lifecycleScope.launch {
            viewModel.getAllData().observe(viewLifecycleOwner) { result ->
                if (result.isNotEmpty()) {
                    modelList.addAll(result)
                    var hasImages = false
                    for (m in result) {
                        if (!m.listOfImages.isEmpty()) {
                            hasImages = true
                            break
                        }
                    }
                    if (!hasImages) {
                        binding.tvResultEmptyLayout.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    } else {
                        binding.tvResultEmptyLayout.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                    mAdapter.notifyDataSetChanged()
                }else{
                    binding.tvResultEmptyLayout.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
            }
        }

        binding.recyclerView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
    }
}