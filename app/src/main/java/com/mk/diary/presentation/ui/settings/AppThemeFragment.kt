package com.mk.diary.presentation.ui.settings

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.mk.diary.adapters.slider.AppThemeImagesSliderAdapter
import com.mk.diary.helpers.AppThemeModelClass
import com.mk.diary.helpers.ResultCase
import com.mk.diary.presentation.ui.tabs.BottomNavActivity
import com.mk.diary.presentation.viewmodels.theme.BackgroundThemeViewModel
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.appext.newScreen
import com.mk.diary.utils.appext.shortToast
import dagger.hilt.android.AndroidEntryPoint
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentAppThemeBinding


@AndroidEntryPoint
class AppThemeFragment : Fragment() {
    lateinit var binding: FragmentAppThemeBinding
    lateinit var mAdapter :AppThemeImagesSliderAdapter
    private val viewModel :BackgroundThemeViewModel by viewModels()
    private val listOfThemes :ArrayList<String> by lazy { ArrayList() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAppThemeBinding.inflate(inflater,container,false)
        viewModel.getSlidingTheme()
        viewModel.getApplyTheme()
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        viewModel()
        return binding.root
    }
    private fun viewModel(){
        viewModel.sliderThemeLiveData.observe(viewLifecycleOwner){result->
            when(result){
                is ResultCase.Loading->{
                    hideProgressBar()
                }
                is ResultCase.Success->{
                    showProgressBar()
                    // Add each item individually to modelList
                    mAdapter= AppThemeImagesSliderAdapter(result.data)
                    Log.d("getSlidingTheme", "getSlidingTheme: "+result.data)
                    // Notify the adapter that the data has changed
                    mAdapter.notifyDataSetChanged()
                    binding.viewPager.adapter = mAdapter
                    binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                    // Optionally, you can add PageTransformer for animation effects
                    binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            checkPosition(position)
                        }
                    })
                    val data=arguments?.getInt(MyConstants.PASS_DATA)
                    if (data!=null){
                        binding.viewPager.setCurrentItem(data,false)
                        mAdapter.notifyDataSetChanged()
                    }
                }
                is ResultCase.Error->{
                    hideProgressBar()
                    requireContext().shortToast(result.message.toString())
                }
            }
        }
        viewModel.applyThemeLiveData.observe(viewLifecycleOwner){result->
            when(result){
                is ResultCase.Loading->{
                    showProgressBar()
                }
                is ResultCase.Success->{
                    hideProgressBar()
                    listOfThemes.addAll(result.data)
                }
                is ResultCase.Error->{
                    hideProgressBar()
                    requireContext().shortToast(result.message.toString())
                }
            }
        }


    }

    private fun checkPosition(position: Int) {
        when(position){
            0->{
                binding.applyThemeBtn.setCardBackgroundColor(resources.getColor(R.color.pinkButtonColor))
                binding.applyThemeBtn.setOnClickListener {
                    if (listOfThemes[0].isNotEmpty()){
                        val model=AppThemeModelClass(resources.getColor(R.color.pinkButtonColor),(listOfThemes[0]))
                        SharedPrefObj.saveAppTheme(requireContext(),model)
                        requireContext().newScreen(BottomNavActivity::class.java)
                    }else{
                        requireContext().shortToast(resources.getString(R.string.noThemeisfound))
                    }
                }
            }
            1->{
                binding.applyThemeBtn.setCardBackgroundColor(resources.getColor(R.color.appTheme1))
                binding.applyThemeBtn.setOnClickListener {
                    if (listOfThemes[1].isNotEmpty()){
                        val model=AppThemeModelClass(resources.getColor(R.color.appTheme1),(listOfThemes[1]))
                        SharedPrefObj.saveAppTheme(requireContext(),model)
                        requireContext().newScreen(BottomNavActivity::class.java)
                    }else{
                        requireContext().shortToast(resources.getString(R.string.noThemeisfound))
                    }
                }
            }
            2->{
                binding.applyThemeBtn.setCardBackgroundColor(resources.getColor(R.color.appTheme2))
                binding.applyThemeBtn.setOnClickListener {
                    if (listOfThemes[2].isNotEmpty()){
                        val model=AppThemeModelClass(resources.getColor(R.color.appTheme2),(listOfThemes[2]))
                        SharedPrefObj.saveAppTheme(requireContext(),model)
                        requireContext().newScreen(BottomNavActivity::class.java)
                    }else{
                        requireContext().shortToast(resources.getString(R.string.noThemeisfound))
                    }
                }

            }
            3->{
                binding.applyThemeBtn.setCardBackgroundColor(resources.getColor(R.color.appTheme3))
                binding.applyThemeBtn.setOnClickListener {
                    if (listOfThemes[3].isNotEmpty()){
                        val model=AppThemeModelClass(resources.getColor(R.color.appTheme3),(listOfThemes[3]))
                        SharedPrefObj.saveAppTheme(requireContext(),model)
                        requireContext().newScreen(BottomNavActivity::class.java)
                    }else{
                        requireContext().shortToast(resources.getString(R.string.noThemeisfound))
                    }
                }

            }
            4->{
                binding.applyThemeBtn.setCardBackgroundColor(resources.getColor(R.color.appTheme4))
                binding.applyThemeBtn.setOnClickListener {
                    if (listOfThemes[4].isNotEmpty()){
                        val model=AppThemeModelClass(resources.getColor(R.color.appTheme4),(listOfThemes[4]))
                        SharedPrefObj.saveAppTheme(requireContext(),model)
                        requireContext().newScreen(BottomNavActivity::class.java)
                    }else{
                        requireContext().shortToast(resources.getString(R.string.noThemeisfound))
                    }
                }
            }
            5->{
                binding.applyThemeBtn.setCardBackgroundColor(resources.getColor(R.color.appTheme5))
                binding.applyThemeBtn.setOnClickListener {
                    if (listOfThemes[5].isNotEmpty()){
                        val model=AppThemeModelClass(resources.getColor(R.color.appTheme5),(listOfThemes[5]))
                        SharedPrefObj.saveAppTheme(requireContext(),model)
                        requireContext().newScreen(BottomNavActivity::class.java)
                    }else{
                        requireContext().shortToast(resources.getString(R.string.noThemeisfound))
                    }
                }

            }
            6->{
                binding.applyThemeBtn.setCardBackgroundColor(resources.getColor(R.color.appTheme6))
                binding.applyThemeBtn.setOnClickListener {
                    if (listOfThemes[6].isNotEmpty()){
                        val model=AppThemeModelClass(resources.getColor(R.color.appTheme6),(listOfThemes[6]))
                        SharedPrefObj.saveAppTheme(requireContext(),model)
                        requireContext().newScreen(BottomNavActivity::class.java)
                    }else{
                        requireContext().shortToast(resources.getString(R.string.noThemeisfound))
                    }
                }

            }
            7->{
                binding.applyThemeBtn.setCardBackgroundColor(resources.getColor(R.color.appTheme7))
                binding.applyThemeBtn.setOnClickListener {
                    if (listOfThemes[7].isNotEmpty()){
                        val model=AppThemeModelClass(resources.getColor(R.color.appTheme7),(listOfThemes[7]))
                        SharedPrefObj.saveAppTheme(requireContext(),model)
                        requireContext().newScreen(BottomNavActivity::class.java)
                    }else{
                        requireContext().shortToast(resources.getString(R.string.noThemeisfound))
                    }
                }
            }
            8->{
                binding.applyThemeBtn.setCardBackgroundColor(resources.getColor(R.color.appTheme8))
                binding.applyThemeBtn.setOnClickListener {
                    if (listOfThemes[8].isNotEmpty()){
                        val model=AppThemeModelClass(resources.getColor(R.color.appTheme8),(listOfThemes[8]))
                        SharedPrefObj.saveAppTheme(requireContext(),model)
                        requireContext().newScreen(BottomNavActivity::class.java)
                    }else{
                        requireContext().shortToast(resources.getString(R.string.noThemeisfound))
                    }
                }
            }
            9->{
                binding.applyThemeBtn.setCardBackgroundColor(resources.getColor(R.color.appTheme9))
                binding.applyThemeBtn.setOnClickListener {
                    if (listOfThemes[9].isNotEmpty()){
                        val model=AppThemeModelClass(resources.getColor(R.color.appTheme9),(listOfThemes[9]))
                        SharedPrefObj.saveAppTheme(requireContext(),model)
                        requireContext().newScreen(BottomNavActivity::class.java)
                    }else{
                        requireContext().shortToast(resources.getString(R.string.noThemeisfound))
                    }
                }
            }
        }
    }
    private fun showProgressBar() {
        val theme=SharedPrefObj.getAppTheme(requireContext())
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