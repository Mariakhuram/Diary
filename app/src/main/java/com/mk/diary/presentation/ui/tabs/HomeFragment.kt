package com.mk.diary.presentation.ui.tabs

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mk.diary.adapters.recyclerview.HomeFragmentNoteViewRecyclerAdapter
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.firebase.FirebaseKey
import com.mk.diary.firebase.RealtimeFirebaseInstance
import com.mk.diary.presentation.viewmodels.noteview.NoteViewModel
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.companion.Static
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: NoteViewModel by viewModels()
    private val modelList: ArrayList<NoteViewModelClass> by lazy { ArrayList() }
    private val mAdapter by lazy { HomeFragmentNoteViewRecyclerAdapter(modelList) }
    lateinit var binding: FragmentHomeBinding
    private var checkItemCLick = true
    private var bounceHandler = Handler(Looper.getMainLooper())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)


        // Inflate the layout for this fragment
        Static.checkBBB = false
        setRecyclerListType()
        binding.searchViewBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_noteViewSearchFragment)
        }
        recyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bounceAnimator = ObjectAnimator.ofFloat(
            binding.bouncingEffectContainer,
            "translationY",
            -50f,
            20f,
            -50f,
            20f,
            -50f
        )
        bounceAnimator.duration = 3000L // 3 seconds for the bouncing animation (2 bounces)

        val delayBetweenBounces = 500L // 0.5 seconds delay between bounces

        val overallDuration = 3500L // 3.5 seconds (3 seconds for bouncing + 0.5 second delay)

        val delayAnimator = ValueAnimator.ofFloat(0f, 1f)
        delayAnimator.duration = delayBetweenBounces
        delayAnimator.addUpdateListener { valueAnimator ->
            if (valueAnimator.animatedValue as Float == 0f) {
                bounceAnimator.start()
            }
        }

        val repeatBounceRunnable = object : Runnable {
            override fun run() {
                delayAnimator.start()
                bounceHandler.postDelayed(this, overallDuration)
            }
        }
        bounceHandler.postDelayed(repeatBounceRunnable, overallDuration)
    }
    private fun setRecyclerListType() {
        binding.showListTypeBtn.setOnClickListener {
            if (checkItemCLick) {
                binding.setShowListItemsTypeCard.visibility = View.VISIBLE
                checkItemCLick = false
            } else {
                binding.setShowListItemsTypeCard.visibility = View.GONE
                checkItemCLick = true
            }
        }
        binding.listGroupBtn.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.oldestFirstBtn -> {
                    SharedPrefObj.saveBoolean(requireContext(), MyConstants.ITEM_VIEW_LIST_TYPE, true)
                    binding.setShowListItemsTypeCard.visibility = View.GONE
                    recyclerView()
                    mAdapter.notifyDataSetChanged()
                }
                R.id.newesttFirstBtn -> {
                    SharedPrefObj.saveBoolean(requireContext(), MyConstants.ITEM_VIEW_LIST_TYPE, false)
                    binding.setShowListItemsTypeCard.visibility = View.GONE
                    recyclerView()
                    mAdapter.notifyDataSetChanged()
                    // Handle the click for newestFirstBtn
                }
                // Add more cases if you have additional RadioButtons
            }
        }
        if (SharedPrefObj.getBoolean(requireContext(), MyConstants.ITEM_VIEW_LIST_TYPE, false)) {
            binding.oldestFirstBtn.isChecked = true
        } else {
            binding.newesttFirstBtn.isChecked = true
        }
    }

    override fun onResume() {
        super.onResume()

        mAdapter.notifyDataSetChanged()
    }
    private fun recyclerView() {
        binding.homeRecyclerView.adapter = mAdapter
        lifecycleScope.launch {
            viewModel.getAllData().observe(viewLifecycleOwner) { result ->
                if (result.isEmpty()) {
                    binding.tvResultEmptyLayout.visibility = View.VISIBLE
                } else {
                    modelList.clear()
                    binding.tvResultEmptyLayout.visibility = View.GONE
                    if (SharedPrefObj.getBoolean(requireContext(), MyConstants.ITEM_VIEW_LIST_TYPE, false)) {
                        modelList.addAll(result)
                    } else {
                        modelList.addAll(result)
                        modelList.reverse()
                    }
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
        mAdapter.recyclerClick(object : HomeFragmentNoteViewRecyclerAdapter.PassData {
            override fun clickFunction(modelClass: NoteViewModelClass, position: Int) {
                Static.positionDelete = position
                val bundle = Bundle()
                bundle.putSerializable(MyConstants.PASS_DATA, modelClass)
                findNavController().navigate(R.id.action_homeFragment_to_noteViewDetailsFragment2, bundle)
            }
        })
    }
}
