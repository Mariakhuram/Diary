package com.mk.diary.presentation.ui.tabs

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mk.diary.R
import com.mk.diary.adapters.tabslayout.CalenderTabLayoutAdapter
import com.mk.diary.databinding.FragmentCalenderBinding
import com.mk.mydiary.utils.SharedPrefObj
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.util.Calendar


class CalenderFragment : Fragment() {
    private var job: Job? = null
    lateinit var binding: FragmentCalenderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentCalenderBinding.inflate(layoutInflater,container,false)
        tabLayout()
        task()
        binding.imageView.setOnClickListener {
        }
        binding.bottomSheetDatePickerBtn.setOnClickListener { datePickerBottomSheet() }
        return binding.root
    }
    private fun task() {
        job = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                // Your task to be executed every one second
                val d=SharedPrefObj.getString(requireContext(),"k")
                if (d.equals(SharedPrefObj.getString(requireContext(),"k"))){
                    updateUi()
                }
                // Delay for one second
                delay(100)
            }
        }
    }

    private fun updateUi(){
        val d=SharedPrefObj.getString(requireContext(),"k")
        if (d.isNullOrBlank()){
        }else{
            binding.monthDateTv.setText(d.toString())
        }
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
            // Handle the cancel button click
            dialog.dismiss() // Dismiss the dialog when the cancel button is clicked
        }
        saveBtn.setOnClickListener {
            // Handle the save button click
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.setOnCancelListener {
            // Handle the cancel listener if needed
        }
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
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
        yearPicker.minValue = currentYear - 10
        yearPicker.maxValue = currentYear + 10
        yearPicker.value = currentYear
//        yearPicker.setOnValueChangedListener { _, _, newVal ->
//            onDateSelectedListener?.onDateSelected(
//                monthNames[monthPicker.value],
//                newVal.toString()
//            )
//        }
    }
    private fun tabLayout() {
        val adapter = CalenderTabLayoutAdapter(requireActivity())
        binding.viewPager.adapter = adapter
        // Connect TabLayout and ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = ""
                1 -> tab.text = ""
                2 -> tab.text = ""
                3 -> tab.text = ""
                4 -> tab.text = ""
                5 -> tab.text = ""
                6 -> tab.text = ""
                7 -> tab.text = ""
                8 -> tab.text = ""
                9 -> tab.text = ""
                10 -> tab.text = ""
                11-> tab.text = ""
                // Add more tabs as needed
            }
        }.attach()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val selectedPosition = tab.position
                checkPosition(selectedPosition)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Do something when a tab is unselected
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Do something when a tab is reselected
            }
        })
    }

    private fun checkPosition(selectedPosition: Int) {
        when(selectedPosition){
            0->binding.monthDateTv.setText("Jan 2023")
            1->binding.monthDateTv.setText("Feb 2023")
            2->binding.monthDateTv.setText("Mar 2023")
            3->binding.monthDateTv.setText("Apr 2023")
            4->binding.monthDateTv.setText("May 2023")
            5->binding.monthDateTv.setText("Jun 2023")
            6->binding.monthDateTv.setText("Jul 2023")
            7->binding.monthDateTv.setText("Aug 2023")
            8->binding.monthDateTv.setText("Sep 2023")
            9->binding.monthDateTv.setText("Oct 2023")
            10->binding.monthDateTv.setText("Nov 2023")
            11->binding.monthDateTv.setText("Dec 2023")
        }
    }
}