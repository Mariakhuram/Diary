package com.mk.diary.presentation.ui.noteview.tablayout

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mk.diary.R
import com.mk.diary.adapters.recyclerview.BackGroundThemeRecAdapter
import com.mk.diary.adapters.recyclerview.GalleryImagePickerRecAdapter
import com.mk.diary.databinding.FragmentAllBackroundThemeBinding
import com.mk.diary.databinding.FragmentCreateNoteBinding
import com.mk.diary.domain.models.ImageItem
import com.mk.mydiary.utils.companion.Static


class AllBackroundThemeFragment : Fragment() {
    private val modelList :ArrayList<ImageItem> by lazy { ArrayList() }
    private val mAdapter by lazy { BackGroundThemeRecAdapter(modelList) }
    lateinit var binding: FragmentAllBackroundThemeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAllBackroundThemeBinding.inflate(layoutInflater,container,false)
        recyclerView()
        return binding.root
    }

    private fun recyclerView() {
        val themeOne = R.drawable.themeone // Replace with your drawable resource ID
        val themeOneUri = Uri.parse("android.resource://${requireContext().packageName}/$themeOne")
        val themeTwo = R.drawable.themetwo // Replace with your drawable resource ID
        val themeTwoUri = Uri.parse("android.resource://${requireContext().packageName}/$themeTwo")
        val themeThree= R.drawable.themethree // Replace with your drawable resource ID
        val themeThreeUri = Uri.parse("android.resource://${requireContext().packageName}/$themeThree")
        modelList.add(ImageItem(themeOneUri))
        modelList.add(ImageItem(themeTwoUri))
        modelList.add(ImageItem(themeThreeUri))
        binding.recyclerView.adapter=mAdapter
        mAdapter.recyclerClick(object :BackGroundThemeRecAdapter.PassRateData{
            override fun clickFunction(modelClass: ImageItem, position: Int) {
                when(position){
                    0->{
                        Static.backGroundImage=themeOne
                    }
                    1->{
                        Static.backGroundImage=themeTwo
                    }
                    2->{
                        Static.backGroundImage=themeThree
                    }
                }
            }
        })

    }
}