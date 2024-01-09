package com.mk.diary.presentation.ui.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mk.diary.adapters.recyclerview.HomeFragmentNoteViewRecyclerAdapter
import com.mk.diary.adapters.tabslayout.CalenderTabLayoutAdapter
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.presentation.viewmodels.coordinator.CoordinatorViewModel
import com.mk.diary.presentation.viewmodels.noteview.NoteViewModel
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.companion.CurrentTime
import com.mk.diary.utils.companion.Static
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentCalenderBinding
import java.text.DateFormatSymbols
import java.util.Calendar

@AndroidEntryPoint
class CalenderFragment : Fragment() {
    lateinit var binding: FragmentCalenderBinding
    var selectedMonth: String? =null
    var selectedYear: String? = null
    var selectedDate: String? = null
    private val viewModel : NoteViewModel by viewModels()
    private val modelList :ArrayList<NoteViewModelClass> by lazy { ArrayList() }
    private val mAdapter by lazy { HomeFragmentNoteViewRecyclerAdapter(modelList,) }
    private val coordinatorViewModel: CoordinatorViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalenderBinding.inflate(layoutInflater, container, false)
        tabLayout()
        defaultDateMonth()
        binding.calenderCurrentDateViewBtn.setOnClickListener {
            defaultDateMonth()
        }
        setDataCoordinator()
        recyclerView()
        binding.bottomSheetDatePickerBtn.setOnClickListener { datePickerBottomSheet() }
        return binding.root
    }

    private fun setDataCoordinator() {
        coordinatorViewModel.dateLiveData.observe(viewLifecycleOwner){
            selectedDate=it
            recyclerView()
            mAdapter.notifyDataSetChanged()
        }
        coordinatorViewModel.monthLiveData.observe(viewLifecycleOwner){
            selectedMonth=it
            mAdapter.notifyDataSetChanged()
        }
        coordinatorViewModel.yearLiveData.observe(viewLifecycleOwner){
            selectedYear=it
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun defaultDateMonth() {
        selectedDate= CurrentTime.currentDate
        selectedMonth= CurrentTime.currentMonth
        selectedYear= CurrentTime.currentYear
        binding.monthDateTv.setText(selectedMonth)
        SharedPrefObj.saveString(requireContext(), MyConstants.CURRENT_MONTH, CurrentTime.currentMonth)
        SharedPrefObj.saveString(requireContext(), MyConstants.CURRENT_DATE, CurrentTime.currentDate)
        SharedPrefObj.saveString(requireContext(), MyConstants.CHECK_YEAR, CurrentTime.currentYear)
        checkdata()
        recyclerView()
        mAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        mAdapter.notifyDataSetChanged()
//        if (Static.deleteBoolean){
//            if (Static.positionDelete!=null){
//                modelList.removeAt(Static.positionDelete!!)
//                Static.positionDelete?.let { mAdapter.notifyItemRemoved(it) }
//                Static.positionDelete = null
//                Static.deleteBoolean = false
//                mAdapter.notifyDataSetChanged()
//            }
//        }
    }
    private fun recyclerView() {
        binding.homeRecyclerView.adapter=mAdapter
        lifecycleScope.launch {
            selectedDate?.let {
                selectedMonth?.let { it1 ->
                    selectedYear?.let { it2 ->
                        viewModel.getDataForDateAndYear(it,
                            it1, it2
                        ).observe(viewLifecycleOwner){ result->
                            if (result.isEmpty()){

                            }else{
                                modelList.clear()
                                modelList.addAll(result)
                                mAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }
        mAdapter.recyclerClick(object : HomeFragmentNoteViewRecyclerAdapter.PassData{
            override fun clickFunction(modelClass: NoteViewModelClass,position:Int) {
                Static.positionDelete=position
                val bundle = Bundle()
                bundle.putSerializable(MyConstants.PASS_DATA, modelClass)
                findNavController().navigate(R.id.action_calenderFragment2_to_noteViewDetailsFragment2, bundle)
            }
        })
    }
    private fun datePickerBottomSheet() {
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.date_picker_bottom_sheet, null)
        val cancelBtn = view.findViewById<CardView>(R.id.cancelBtn)
        val saveBtn = view.findViewById<TextView>(R.id.saveBtn)
        val monthPicker = view.findViewById<NumberPicker>(R.id.monthPicker)
        val yearPicker = view.findViewById<NumberPicker>(R.id.yearPicker)
        setupNumberPickers(monthPicker, yearPicker)

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        saveBtn.setOnClickListener {

            selectedYear?.let { it1 ->
                SharedPrefObj.saveString(requireActivity(), MyConstants.YEAR,
                    it1
                )
            }
            selectedMonth?.let { it1 ->
                SharedPrefObj.saveString(requireActivity(), MyConstants.MONTH,
                    it1
                )
            }
            //
            binding.monthDateTv.text = "$selectedMonth $selectedYear"
            dialog.dismiss()
        }

        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.setOnCancelListener {
            // Handle the cancel listener if needed
        }
        dialog.show()
    }

    private fun setupNumberPickers(monthPicker: NumberPicker, yearPicker: NumberPicker) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val monthNames = DateFormatSymbols().months

        monthPicker.minValue = 0
        monthPicker.maxValue = monthNames.size - 1
        monthPicker.displayedValues = monthNames.sliceArray(0 until monthNames.size)
        monthPicker.value = currentMonth

        yearPicker.minValue = currentYear
        yearPicker.maxValue = currentYear + 1
        yearPicker.value = currentYear

        yearPicker.setOnValueChangedListener { _, _, newVal ->
            selectedYear = newVal.toString()
            checkdata()
        }

        monthPicker.setOnValueChangedListener { _, _, _ ->
            selectedMonth = monthNames[monthPicker.value].substring(0, 3)
            checkdata()
        }

        val initialMonth = monthNames[currentMonth].substring(0, 3)
        SharedPrefObj.saveString(requireActivity(), MyConstants.YEAR, currentYear.toString())
        SharedPrefObj.saveString(requireActivity(), MyConstants.MONTH, initialMonth)
    }
    fun checkdata() {
        if (selectedMonth=="Dec" && selectedYear=="2023"){
            binding.viewPager.setCurrentItem(0,false)
            SharedPrefObj.saveInt(requireContext(), MyConstants.CURRENT_CALENDER_TABS_POSITION,0)
            binding.s.setText("T")
            binding.m.setText("W")
            binding.t.setText("T")
            binding.w.setText("F")
            binding.th.setText("S")
            binding.f.setText("S")
            binding.sa.setText("M")
        }
        if (selectedMonth=="Jan" && selectedYear=="2024"){
            binding.viewPager.setCurrentItem(1,false)
            SharedPrefObj.saveInt(requireContext(), MyConstants.CURRENT_CALENDER_TABS_POSITION,1)
            binding.s.setText("F")
            binding.m.setText("S")
            binding.t.setText("S")
            binding.w.setText("M")
            binding.th.setText("T")
            binding.f.setText("W")
            binding.sa.setText("T")
        }
        if (selectedMonth=="Feb" && selectedYear=="2024"){
            binding.viewPager.setCurrentItem(2,false)
            SharedPrefObj.saveInt(requireContext(), MyConstants.CURRENT_CALENDER_TABS_POSITION,2)
            binding.s.setText("T")
            binding.m.setText("W")
            binding.t.setText("T")
            binding.w.setText("F")
            binding.th.setText("S")
            binding.f.setText("S")
            binding.sa.setText("M")
        }
        if (selectedMonth=="Mar" && selectedYear=="2024"){
            binding.viewPager.setCurrentItem(3,false)
            SharedPrefObj.saveInt(requireContext(), MyConstants.CURRENT_CALENDER_TABS_POSITION,3)
            binding.s.setText("T")
            binding.m.setText("W")
            binding.t.setText("T")
            binding.w.setText("F")
            binding.th.setText("S")
            binding.f.setText("S")
            binding.sa.setText("M")
        }
        if (selectedMonth=="Apr" && selectedYear=="2024"){
            binding.viewPager.setCurrentItem(4,false)
            SharedPrefObj.saveInt(requireContext(), MyConstants.CURRENT_CALENDER_TABS_POSITION,4)
            binding.s.setText("F")
            binding.m.setText("S")
            binding.t.setText("S")
            binding.w.setText("M")
            binding.th.setText("T")
            binding.f.setText("W")
            binding.sa.setText("T")
        }
        if (selectedMonth=="May" && selectedYear=="2024"){
            binding.viewPager.setCurrentItem(5,false)
            SharedPrefObj.saveInt(requireContext(), MyConstants.CURRENT_CALENDER_TABS_POSITION,5)
            binding.s.setText("S")
            binding.m.setText("M")
            binding.t.setText("T")
            binding.w.setText("W")
            binding.th.setText("T")
            binding.f.setText("F")
            binding.sa.setText("S")
        }
        if (selectedMonth=="Jun" && selectedYear=="2024"){
            binding.viewPager.setCurrentItem(6,false)
            SharedPrefObj.saveInt(requireContext(), MyConstants.CURRENT_CALENDER_TABS_POSITION,6)
            binding.s.setText("W")
            binding.m.setText("T")
            binding.t.setText("F")
            binding.w.setText("S")
            binding.th.setText("S")
            binding.f.setText("M")
            binding.sa.setText("T")
        }
        if (selectedMonth=="Jul" && selectedYear=="2024"){
            binding.viewPager.setCurrentItem(7,false)
            SharedPrefObj.saveInt(requireContext(), MyConstants.CURRENT_CALENDER_TABS_POSITION,7)
            binding.s.setText("F")
            binding.m.setText("S")
            binding.t.setText("S")
            binding.w.setText("M")
            binding.th.setText("T")
            binding.f.setText("W")
            binding.sa.setText("T")
        }
        if (selectedMonth=="Aug" && selectedYear=="2024"){
            binding.viewPager.setCurrentItem(8,false)
            SharedPrefObj.saveInt(requireContext(), MyConstants.CURRENT_CALENDER_TABS_POSITION,8)
            binding.s.setText("M")
            binding.m.setText("T")
            binding.t.setText("W")
            binding.w.setText("T")
            binding.th.setText("F")
            binding.f.setText("S")
            binding.sa.setText("S")
        }
        if (selectedMonth == "Sep" && selectedYear == "2024") {
            binding.viewPager.setCurrentItem(9,false)
            SharedPrefObj.saveInt(requireContext(), MyConstants.CURRENT_CALENDER_TABS_POSITION,9)
            binding.s.setText("T")
            binding.m.setText("F")
            binding.t.setText("S")
            binding.w.setText("S")
            binding.th.setText("M")
            binding.f.setText("T")
            binding.sa.setText("W")
        }
        if (selectedMonth == "Oct" && selectedYear == "2024") {
            binding.viewPager.setCurrentItem(10,false)
            SharedPrefObj.saveInt(requireContext(), MyConstants.CURRENT_CALENDER_TABS_POSITION,10)
            binding.s.setText("S")
            binding.m.setText("S")
            binding.t.setText("M")
            binding.w.setText("T")
            binding.th.setText("W")
            binding.f.setText("T")
            binding.sa.setText("F")
        }
        if (selectedMonth == "Nov" && selectedYear == "2024") {
            binding.viewPager.setCurrentItem(11,false)
            SharedPrefObj.saveInt(requireContext(), MyConstants.CURRENT_CALENDER_TABS_POSITION,11)
            binding.s.setText("T")
            binding.m.setText("W")
            binding.t.setText("T")
            binding.w.setText("F")
            binding.th.setText("S")
            binding.f.setText("S")
            binding.sa.setText("M")
        }
    }
    private fun tabLayout() {
        val adapter = CalenderTabLayoutAdapter(requireActivity())
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = ""
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                checkMonthWithPosition(tab.position)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }


    private fun setDaysOfWeek(selectedMonth: String?, selectedYear: String?) {
        if (selectedMonth == "Dec" && selectedYear == "2023") {
            binding.s.setText("T")
            binding.m.setText("W")
            binding.t.setText("T")
            binding.w.setText("F")
            binding.th.setText("S")
            binding.f.setText("S")
            binding.sa.setText("M")
        } else if (selectedMonth == "Jan" && selectedYear == "2024") {
            binding.s.setText("F")
            binding.m.setText("S")
            binding.t.setText("S")
            binding.w.setText("M")
            binding.th.setText("T")
            binding.f.setText("W")
            binding.sa.setText("T")
        } else if (selectedMonth == "Feb" && selectedYear == "2024") {
            binding.s.setText("T")
            binding.m.setText("W")
            binding.t.setText("T")
            binding.w.setText("F")
            binding.th.setText("S")
            binding.f.setText("S")
            binding.sa.setText("M")
        } else if (selectedMonth == "Mar" && selectedYear == "2024") {
            binding.s.setText("T")
            binding.m.setText("W")
            binding.t.setText("T")
            binding.w.setText("F")
            binding.th.setText("S")
            binding.f.setText("S")
            binding.sa.setText("M")
        } else if (selectedMonth == "Apr" && selectedYear == "2024") {
            binding.s.setText("F")
            binding.m.setText("S")
            binding.t.setText("S")
            binding.w.setText("M")
            binding.th.setText("T")
            binding.f.setText("W")
            binding.sa.setText("T")
        } else if (selectedMonth == "May" && selectedYear == "2024") {
            binding.s.setText("S")
            binding.m.setText("M")
            binding.t.setText("T")
            binding.w.setText("W")
            binding.th.setText("T")
            binding.f.setText("F")
            binding.sa.setText("S")
        } else if (selectedMonth == "Jun" && selectedYear == "2024") {
            binding.s.setText("W")
            binding.m.setText("T")
            binding.t.setText("F")
            binding.w.setText("S")
            binding.th.setText("S")
            binding.f.setText("M")
            binding.sa.setText("T")
        } else if (selectedMonth == "Jul" && selectedYear == "2024") {
            binding.s.setText("F")
            binding.m.setText("S")
            binding.t.setText("S")
            binding.w.setText("M")
            binding.th.setText("T")
            binding.f.setText("W")
            binding.sa.setText("T")
        } else if (selectedMonth == "Aug" && selectedYear == "2024") {
            binding.s.setText("M")
            binding.m.setText("T")
            binding.t.setText("W")
            binding.w.setText("T")
            binding.th.setText("F")
            binding.f.setText("S")
            binding.sa.setText("S")
        } else if (selectedMonth == "Sep" && selectedYear == "2024") {
            binding.s.setText("T")
            binding.m.setText("F")
            binding.t.setText("S")
            binding.w.setText("S")
            binding.th.setText("M")
            binding.f.setText("T")
            binding.sa.setText("W")
        } else if (selectedMonth == "Oct" && selectedYear == "2024") {
            binding.s.setText("S")
            binding.m.setText("S")
            binding.t.setText("M")
            binding.w.setText("T")
            binding.th.setText("W")
            binding.f.setText("T")
            binding.sa.setText("F")
        } else if (selectedMonth == "Nov" && selectedYear == "2024") {
            binding.s.setText("T")
            binding.m.setText("W")
            binding.t.setText("T")
            binding.w.setText("F")
            binding.th.setText("S")
            binding.f.setText("S")
            binding.sa.setText("M")
        }
    }
    private fun checkMonthWithPosition(position: Int) {
        when(position){
            0 ->{
                binding.monthDateTv.setText("Dec")
                selectedMonth = "Dec"
                selectedYear = "2023"
                setDaysOfWeek("Dec","2023")

            }
            1->{
                binding.monthDateTv.setText("Jan  2024")
                selectedMonth = "Jan"
                selectedYear = "2024"
                setDaysOfWeek("Jan","2024")
            }  2 ->{
            binding.monthDateTv.setText("Feb 2024")
            selectedMonth = "Feb"
            selectedYear = "2024"
            setDaysOfWeek("Feb","2024")
            SharedPrefObj.saveString(
                requireContext(),
                MyConstants.CHECK_FEB_YEAR,
                if (selectedYear == "2024") "29" else "28"
            )
        }3->{
            binding.monthDateTv.setText("Mar  2024")
            selectedMonth = "Mar"
            selectedYear = "2024"
            setDaysOfWeek("Mar","2024")
        } 4->{
            binding.monthDateTv.setText("Apr  2024")
            selectedMonth = "Apr"
            selectedYear = "2024"
            setDaysOfWeek("Apr","2024")
        }
            5->{
                binding.monthDateTv.setText("May  2024")
                selectedMonth = "May"
                selectedYear = "2024"
                setDaysOfWeek("May","2024")
            }
            6->{
                binding.monthDateTv.setText("Jun  2024")
                selectedMonth = "Jun"
                selectedYear = "2024"
                setDaysOfWeek("Jun","2024")
            }
            7 ->{
            binding.monthDateTv.setText("Jul  2024")
            selectedMonth = "Jul"
            selectedYear = "2024"
            setDaysOfWeek("Jul","2024")
        } 8 ->{
            binding.monthDateTv.setText("Aug  2024")
            selectedMonth = "Aug"
            selectedYear = "2024"
            setDaysOfWeek("Aug","2024")
        }9-> {
            binding.monthDateTv.setText("Sep  2024")
            selectedMonth = "Sep"
            selectedYear = "2024"
            setDaysOfWeek("Sep","2024")
        } 10 ->{
            binding.monthDateTv.setText("Oct  2024")
            selectedMonth = "Oct"
            selectedYear = "2024"
            setDaysOfWeek("Oct","2024")
        }
        11 ->{
            binding.monthDateTv.setText("Nov  2024")
            selectedMonth = "Nov"
            selectedYear = "2024"
            setDaysOfWeek("Nov","2024")
        }
        }

    }
}
