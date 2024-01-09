package com.mk.diary.presentation.ui.calendertabs

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.presentation.viewmodels.coordinator.CoordinatorViewModel
import com.mk.diary.presentation.viewmodels.noteview.NoteViewModel
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.appext.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentJulBinding

@AndroidEntryPoint
class JulFragment : Fragment() {
    private val allDates :ArrayList<String> by lazy { ArrayList() }
    private val allMonthYear :ArrayList<String> by lazy { ArrayList() }
    private val allYears :ArrayList<String> by lazy { ArrayList() }
    private val viewModel : NoteViewModel by viewModels()
    private val coordinatorViewModel: CoordinatorViewModel by activityViewModels()
    private var currentDateTextView: TextView? = null
    private var selectedDateTextView: TextView? = null
    lateinit var binding: FragmentJulBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentJulBinding.inflate(layoutInflater,container,false)
        getData()
        setInitialBackground()
        setClickListeners()
        return binding.root
    }

    private fun setEmojiBackground(dayNumber: Int, emojiDrawable: Int) {
        when (dayNumber) {
            1 -> binding.eOne.setBackgroundResource(emojiDrawable)
            2 -> binding.eTwo.setBackgroundResource(emojiDrawable)
            3 -> binding.eThree.setBackgroundResource(emojiDrawable)
            4 -> binding.eFour.setBackgroundResource(emojiDrawable)
            5 -> binding.e5.setBackgroundResource(emojiDrawable)
            6 -> binding.e6.setBackgroundResource(emojiDrawable)
            7 -> binding.e7.setBackgroundResource(emojiDrawable)
            8 -> binding.e8.setBackgroundResource(emojiDrawable)
            9 -> binding.e9.setBackgroundResource(emojiDrawable)
            10 -> binding.e10.setBackgroundResource(emojiDrawable)
            11 -> binding.e11.setBackgroundResource(emojiDrawable)
            12 -> binding.e12.setBackgroundResource(emojiDrawable)
            13 -> binding.e13.setBackgroundResource(emojiDrawable)
            14 -> binding.e14.setBackgroundResource(emojiDrawable)
            15 -> binding.e15.setBackgroundResource(emojiDrawable)
            16 -> binding.e16.setBackgroundResource(emojiDrawable)
            17 -> binding.e17.setBackgroundResource(emojiDrawable)
            18 -> binding.e18.setBackgroundResource(emojiDrawable)
            19 -> binding.e19.setBackgroundResource(emojiDrawable)
            20 -> binding.e20.setBackgroundResource(emojiDrawable)
            21 -> binding.e21.setBackgroundResource(emojiDrawable)
            22 -> binding.e22.setBackgroundResource(emojiDrawable)
            23 -> binding.e23.setBackgroundResource(emojiDrawable)
            24 -> binding.e24.setBackgroundResource(emojiDrawable)
            25 -> binding.e25.setBackgroundResource(emojiDrawable)
            26 -> binding.e26.setBackgroundResource(emojiDrawable)
            27 -> binding.e27.setBackgroundResource(emojiDrawable)
            28 -> binding.e28.setBackgroundResource(emojiDrawable)
            29 -> binding.e29.setBackgroundResource(emojiDrawable)
            30 -> binding.e30.setBackgroundResource(emojiDrawable)
            31 -> binding.e31.setBackgroundResource(emojiDrawable)
            else -> null
        }
    }

    private fun setEmoji(result: List<NoteViewModelClass>) {
        if (allDates.isNotEmpty()) {
            for (data in result) {
                if (data.monthString == "Jul" && data.year == "2024") {
                    data.date?.let { data.noteViewStatus?.let { it1 ->
                        setEmojiBackground(it.toInt(),
                            it1
                        )
                    } }
                }
            }
        }
    }

    private fun getData() {
        lifecycleScope.launch {
            viewModel.getAllData().observe(viewLifecycleOwner) { result ->
                if (result.isNotEmpty()) {
                    for (data in result) {
                        data.date?.let { allDates.add(it) }
                        data.monthString?.let { allMonthYear.add("${it} ${data.year}") }
                        data.year?.let { allYears.add(it) }
                    }
                    setEmoji(result)
                }
            }
        }
    }
    private fun setClickListeners() {
        for (day in 1..31) {
            getDayTextView(day)?.setOnClickListener {
                onDateSelected(getDayTextView(day))
            }
        }
    }
    private fun setInitialBackground() {
        try {
            val currentDate = SharedPrefObj.getString(requireContext(), MyConstants.CURRENT_DATE)
            val currentMonth = SharedPrefObj.getString(requireContext(), MyConstants.CURRENT_MONTH)
            if (currentDate!=null && currentMonth=="Jul") {
                currentDateTextView = getDayTextView(currentDate.toInt())
                currentDateTextView?.let { selectDate(it) }
            }
        }catch (e:Exception){
            requireContext().shortToast(e.message.toString())
        }
    }
    private fun onDateSelected(textView: TextView?) {
        textView?.let {
            if (it.text.toString().equals("1") || it.text.toString().equals("2") ||
                it.text.toString().equals("3") || it.text.toString().equals("4")
                || it.text.toString().equals("5")  || it.text.toString().equals("6")
                || it.text.toString().equals("7") || it.text.toString().equals("8")
                || it.text.toString().equals("9")){
                coordinatorViewModel.sendDateToViewModel("0${it.text.toString()}")
            }else{
                coordinatorViewModel.sendDateToViewModel(it.text.toString())
            }
            coordinatorViewModel.sendMonthToViewModel("Jul")
            coordinatorViewModel.sendYearToViewModel("2024")
            if (selectedDateTextView != null && selectedDateTextView != currentDateTextView) {
                resetSelectedDateBackground()
            }

            if (it != currentDateTextView) {
                selectedDateTextView = it
                selectDate(it)
            } else {
                currentDateTextView = null
            }
            SharedPrefObj.saveString(requireContext(), MyConstants.DATE, it.text.toString())
        }
    }
    private fun selectDate(textView: TextView) {
        textView.setBackgroundResource(R.drawable.background_colour)
        currentDateTextView?.setBackgroundResource(R.drawable.current_month_background)
        val theme=SharedPrefObj.getAppTheme(requireContext())
        val backgroundDrawable = currentDateTextView?.background as? LayerDrawable
        backgroundDrawable?.mutate()
// Get the specific drawable by index and apply the color filter
        val specificDrawable = backgroundDrawable?.getDrawable(1) as? Drawable
        specificDrawable?.mutate()?.setColorFilter(theme.color, PorterDuff.Mode.SRC_IN)
// Set the modified background back to the TextView
        currentDateTextView?.background = backgroundDrawable
        val unwrappedDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.background_colour)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, theme.color)
    }
    private fun resetSelectedDateBackground() {
        selectedDateTextView?.setBackground(null)
    }
    private fun getDayTextView(dayNumber: Int): TextView? {
        return when (dayNumber) {
            1 -> binding.dayOne
            2 -> binding.dayTwo
            3 -> binding.dayThree
            4 -> binding.dayFour
            5 -> binding.day5
            6 -> binding.day6
            7 -> binding.day7
            8 -> binding.day8
            9 -> binding.day9
            10 -> binding.day10
            11 -> binding.day11
            12 -> binding.day12
            13 -> binding.day13
            14 -> binding.day14
            15 -> binding.day15
            16 -> binding.day16
            17 -> binding.day17
            18 -> binding.day18
            19 -> binding.day19
            20 -> binding.day20
            21 -> binding.day21
            22 -> binding.day22
            23 -> binding.day23
            24 -> binding.day24
            25 -> binding.day25
            26 -> binding.day26
            27 -> binding.day27
            28 -> binding.day28
            29 -> binding.day29
            30 -> binding.day30
            31 -> binding.day31
            else -> null
        }
    }
}
