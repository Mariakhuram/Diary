package com.mk.diary.presentation.ui.noteview.tablayout

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mk.diary.adapters.recyclerview.BackGroundThemeRecAdapter
import com.mk.diary.domain.models.BackgroundThemeModelClass
import com.mk.diary.helpers.ResultCase
import com.mk.diary.presentation.viewmodels.coordinator.CoordinatorViewModel
import com.mk.diary.presentation.viewmodels.theme.BackgroundThemeViewModel
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.appext.shortToast
import com.mk.diary.utils.companion.Static
import dagger.hilt.android.AndroidEntryPoint
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentHolidayThemeBinding

@AndroidEntryPoint
class HolidayThemeFragment : Fragment() {
    private val modelList :ArrayList<BackgroundThemeModelClass> by lazy { ArrayList() }
    private val mAdapter by lazy { BackGroundThemeRecAdapter(modelList) }
    lateinit var binding: FragmentHolidayThemeBinding
    private val viewModel : BackgroundThemeViewModel by viewModels()
    private val coordinatorViewModel : CoordinatorViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHolidayThemeBinding.inflate(layoutInflater,container,false)
        viewModel.getHoliday()
        recyclerView()
        viewModel()
        return binding.root
    }
    private fun recyclerView() {
        binding.recyclerView.adapter=mAdapter
        mAdapter.recyclerClick(object :BackGroundThemeRecAdapter.PassRateData{
            override fun clickFunction(modelClass: BackgroundThemeModelClass, position: Int) {
                Static.backGroundImage=modelClass.uri
                coordinatorViewModel.sendDataToViewModel(modelClass.uri)
            }
        })
    }
    private fun viewModel(){
        viewModel.holidayLiveData.observe(viewLifecycleOwner){result->
            when(result){
                is ResultCase.Loading->{
                    showProgressBar()
                }
                is ResultCase.Success->{
                    hideProgressBar()
                    modelList.clear()
                    // Add each item individually to modelList
                    for (data in result.data) {
                        modelList.add(BackgroundThemeModelClass(data))
                    }
                    // Notify the adapter that the data has changed
                    mAdapter.notifyDataSetChanged()
                }
                is ResultCase.Error->{
                    hideProgressBar()
                    requireContext().shortToast(result.message.toString())
                }
            }

        }
    }
    private fun showProgressBar() {
        val theme= SharedPrefObj.getAppTheme(requireContext())
        theme.color?.let { color ->
            // Use the color value to set the progress bar tint
            binding.progressBar.progressTintList = ColorStateList.valueOf(color)
        }
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}