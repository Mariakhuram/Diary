package com.mk.diary.AdsImplimentation

import android.app.Dialog

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentLoadingAdsBinding


class LoadingAdsFragment : DialogFragment() {
    lateinit var binding: FragmentLoadingAdsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val main = LinearLayout(activity)
        main.orientation = LinearLayout.VERTICAL
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoadingAdsBinding.inflate(layoutInflater)
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            dialog?.dismiss()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
