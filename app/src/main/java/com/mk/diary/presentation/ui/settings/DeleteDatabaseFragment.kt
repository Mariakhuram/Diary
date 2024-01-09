package com.mk.diary.presentation.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mk.diary.localization.SharedPref
import com.mk.diary.presentation.ui.tabs.BottomNavActivity
import com.mk.diary.presentation.viewmodels.noteview.NoteViewModel
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.appext.newScreen
import com.mk.diary.utils.appext.shortToast
import dagger.hilt.android.AndroidEntryPoint
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentDeleteDatabaseBinding

@AndroidEntryPoint
class DeleteDatabaseFragment : Fragment() {
    private val viewModel :NoteViewModel by viewModels()
    lateinit var binding: FragmentDeleteDatabaseBinding
    var deleteDataType:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentDeleteDatabaseBinding.inflate(inflater,container,false)
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        deleteDataType = "Local"
        deleteData()
        return binding.root
    }

    private fun deleteData() {
        binding.localRadioBtn.setOnClickListener {
            deleteDataType="Local"
            binding.localRadioBtn.setImageResource(R.drawable.radio_button_checked)
            binding.cloudRadioBtn.setImageResource(R.drawable.radio_button_unchecked)
        }
        binding.cloudRadioBtn.setOnClickListener {
            deleteDataType="Cloud"
            binding.cloudRadioBtn.setImageResource(R.drawable.radio_button_checked)
            binding.localRadioBtn.setImageResource(R.drawable.radio_button_unchecked)
        }
        binding.btndelete.setOnClickListener {
            if (deleteDataType==null){
                requireContext().shortToast("Please select any Type")
            }else{
                deleteDataType?.let { it1 -> deleteBottomSheet(it1) }
            }
        }
        if (deleteDataType=="Local"){
            binding.localRadioBtn.setImageResource(R.drawable.radio_button_checked)
            binding.cloudRadioBtn.setImageResource(R.drawable.radio_button_unchecked)
        }
    }
    private fun deleteBottomSheet(modelClass: String) {
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.delete_bottom_sheet, null)
        val cancelBtn = view.findViewById<TextView>(R.id.cancelBtn)
        val deleteBtn = view.findViewById<TextView>(R.id.deleteBtn)
        val imageRec = view.findViewById<ImageView>(R.id.mainImage)
        val mainDesc = view.findViewById<TextView>(R.id.mainDesc)
        val mainTitle = view.findViewById<TextView>(R.id.mainTitle)
        cancelBtn.setText(resources.getString(R.string.discard))
        mainTitle.setText(resources.getString(R.string.warning))
        mainDesc.setText(resources.getString(R.string.warningerror))
        imageRec.setImageResource(R.drawable.errorwarningicon)

        // Connect TabLayout and ViewPager2

        deleteBtn.setOnClickListener {
           if (modelClass=="Local"){
               try {
                   viewModel.deleteAllData()
                   requireContext().newScreen(BottomNavActivity::class.java)
               }catch (e:Exception){
                   requireContext().shortToast(e.message.toString())
               }
           }else{
               if (SharedPrefObj.getString(requireContext(), MyConstants.USER_AUTH_TOKEN)!=null){

               }else{
                   requireContext().shortToast("Please first make Authentication")
               }
           }
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


}