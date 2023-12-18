package com.mk.diary.presentation.ui.noteview

import android.graphics.Typeface
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.BulletSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mk.diary.R
import com.mk.diary.adapters.recyclerview.CreateNoteImageRecyclerAdapter
import com.mk.diary.adapters.recyclerview.NoteViewDetailsImageViewRecyclerAdapter
import com.mk.diary.adapters.recyclerview.TagTitleRecyclerAdapter
import com.mk.diary.databinding.FragmentNoteViewDetailsBinding
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.helpers.ResultCase
import com.mk.diary.interfaces.ItemClick
import com.mk.diary.presentation.viewmodels.noteview.NoteViewModel
import com.mk.diary.presentation.viewmodels.playing.VoicePlayingViewModel
import com.mk.diary.utils.fonts.FontTextSize
import com.mk.mydiary.utils.MyConstants
import com.mk.mydiary.utils.appext.shortToast
import com.mk.mydiary.utils.companion.Static
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException

@AndroidEntryPoint
class NoteViewDetailsFragment : FontTextSize() {
    var checkPlayPause = false
    private val voicePlayer : VoicePlayingViewModel by viewModels()
    private val viewModel :NoteViewModel by viewModels()
    private val listOfTextViews :ArrayList<TextView> by lazy { ArrayList() }
    private lateinit var allerTypeface: Typeface
    var imageUri:String?=null
    var position:Int?=null
    var deleteImageViewItem :String?=null
    private val createNoteModelList :ArrayList<String> by lazy { ArrayList()}
    private val mCreateNoteImagesAdapter by lazy { NoteViewDetailsImageViewRecyclerAdapter(createNoteModelList) }
    private val listOfImages :ArrayList<String> by lazy { ArrayList() }
    private val listOfTags :ArrayList<String> by lazy { ArrayList() }
    private lateinit var mAdapter : TagTitleRecyclerAdapter
    lateinit var binding: FragmentNoteViewDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentNoteViewDetailsBinding.inflate(inflater,container,false)
        val receivedObject = arguments?.getSerializable(MyConstants.PASS_DATA) as? NoteViewModelClass
        for (m in receivedObject!!.listOfImages){
            createNoteModelList.add(m)
            mCreateNoteImagesAdapter.notifyDataSetChanged()
        }
        mCreateNoteImagesAdapter.notifyDataSetChanged()
        binding.createNoteRec.adapter=mCreateNoteImagesAdapter
        mCreateNoteImagesAdapter.recyclerClick(object :NoteViewDetailsImageViewRecyclerAdapter.PassRateData{
            override fun clickFunction(modelClass: String) {
                val bundle = Bundle()
                bundle.putString(MyConstants.PASS_DATA,modelClass)
                findNavController().navigate(R.id.action_noteViewDetailsFragment2_to_noteViewDetailsImageViewFragment,bundle)
            }

        })
        binding.editBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(MyConstants.PASS_DATA, receivedObject)
            findNavController().navigate(R.id.action_noteViewDetailsFragment2_to_editNoteFragment, bundle)
        }
        for (index in receivedObject?.listOfImages?.indices!!) {
            val image = receivedObject.listOfImages[index]
            listOfImages.add(image)
        }
        for (index in receivedObject?.tagTitle?.indices!!) {
            val tag = receivedObject.tagTitle[index]
            val tagWithHash = "#$tag"
            listOfTags.add(tagWithHash)
        }
        selectedImageViewClicks()
        setRecycler()
        backButton()
        binding.deleteBtn.setOnClickListener {
            deleteBottomSheet(receivedObject)
        }
        setupPreview(receivedObject)
        return binding.root
    }
    private fun selectedImageViewClicks() {
        val bundle = Bundle()
    }

    override fun onResume() {
        super.onResume()
        if (Static.deleteBoolean && deleteImageViewItem!=null){
            createNoteModelList.remove(deleteImageViewItem)
            MyConstants.listOfImageCounting.remove(Uri.parse(deleteImageViewItem))
            MyConstants.list.remove(deleteImageViewItem)
            mCreateNoteImagesAdapter.notifyDataSetChanged()
        }
    }
    private fun backButton() {
        binding.backBtn.setOnClickListener {
            MyConstants.list.clear()
            MyConstants.listOfImageCounting.clear()
            MyConstants.tagList.clear()
            findNavController().popBackStack()
        }
    }

    private fun setRecycler() {
        mAdapter= TagTitleRecyclerAdapter(listOfTags)
        binding.tagRecycler.adapter=mAdapter
        mAdapter.notifyDataSetChanged()
    }

    private fun deleteBottomSheet(modelClass: NoteViewModelClass) {
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.delete_bottom_sheet, null)
        val cancelBtn = view.findViewById<ConstraintLayout>(R.id.cancelBtn)
        val deleteBtn = view.findViewById<ConstraintLayout>(R.id.deleteBtn)
        // Connect TabLayout and ViewPager2
        cancelBtn.setOnClickListener {
            // Handle the cancel button click
            dialog.dismiss() // Dismiss the dialog when the cancel button is clicked
        }
        deleteBtn.setOnClickListener {
            lifecycleScope.launch {
                try {
                    when(val result = viewModel.delete(modelClass)){
                        is ResultCase.Success->{
                            requireContext().shortToast(resources.getString(R.string.deletedsuccessfuly))
                            setupPreview(modelClass)
                            findNavController().popBackStack()
                        }
                        is ResultCase.Error->{
                            requireContext().shortToast(result.message)
                        }
                        is ResultCase.Loading->{

                        }
                    }

                } catch (e: Exception) {
                    // Handle any exceptions that might occur during the delete operation
                    dialog.dismiss()
                } finally {
                    dialog.dismiss()
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

    private fun setupPreview(modelClass: NoteViewModelClass) {
        listOfTextViews.add(binding.dateTv)
        listOfTextViews.add(binding.monthYearTv)
        listOfTextViews.add(binding.dayTv)
        listOfTextViews.add(binding.titleEd)
        listOfTextViews.add(binding.descriptionEd)
        if (modelClass.textSize!=null){
            setupTextSizeOnForPreview(modelClass.textSize!!,listOfTextViews)
        }
        if (modelClass.fontFamily!=null){
            allerTypeface = Typeface.createFromAsset(requireActivity().assets, modelClass.fontFamily)
            setupCustomFonts(allerTypeface,listOfTextViews,null)
        }
        if (modelClass.date!=null && modelClass.dayOfWeek!=null && modelClass.monthString!=null && modelClass.year !=null){
            binding.dateTv.text= modelClass.date
            binding.dayTv.text= modelClass.dayOfWeek
            binding.monthYearTv.text="${modelClass.monthString} ${modelClass.year}"
        }
        if (modelClass.mainTitle!=null){
            binding.titleEd.text= modelClass.mainTitle
        }
        if(modelClass.description!=null && modelClass.descriptionHighLight!=null){
            binding.descriptionEd.visibility=View.VISIBLE
            val copiedText = modelClass.descriptionHighLight
            val editTextText = modelClass.description
            if (copiedText?.let { editTextText?.contains(it) } == true) {
                // Get the start and end indices of the copied text
                val startIndex = editTextText?.indexOf(copiedText)
                val endIndex = startIndex?.plus(copiedText.length)
                // Create a SpannableString object containing the entire text
                val spannableString = SpannableString(editTextText)
                // Apply color to the copied text
                if (startIndex != null) {
                    if (endIndex != null) {
                        spannableString.setSpan(
                            BackgroundColorSpan(resources.getColor(R.color.colorSix_Yellow)),
                            startIndex,
                            endIndex,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
                binding.descriptionEd.visibility=View.VISIBLE
                // Set the EditText's text to the SpannableString
                binding.descriptionEd.text = spannableString
            }
        }else if (modelClass.description!=null && modelClass.descriptionUnderline!=null) {
            // Show the EditText
            binding.descriptionEd.visibility = View.VISIBLE
            val copiedText = modelClass.descriptionUnderline
            val editTextText = modelClass.description
            val spannable =SpannableString(editTextText)
            if (copiedText?.let { editTextText.contains(it) } == true) {
                val startIndex = copiedText?.let { editTextText.indexOf(it) }
                val endIndex = startIndex?.plus(copiedText.length)
                if (startIndex != null) {
                    if (endIndex != null) {
                        spannable.setSpan(
                            UnderlineSpan(),
                            startIndex,
                            endIndex,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    binding.descriptionEd.text=spannable
                }
            }
            Log.d("Span",  "Copied ${copiedText}   Edited $editTextText  Span ${spannable}")
        }else if (modelClass.description!=null){
            binding.descriptionEd.visibility=View.VISIBLE
            binding.descriptionEd.text = modelClass.description
        }
        if (!modelClass.tagTitle.equals("#") && listOfTags.isNotEmpty()){
            binding.hashTagLayout.visibility=View.VISIBLE
        }
        if (modelClass.gravityStyle!=null){
            setGravityOnView(modelClass.gravityStyle!!)
        }
        if (listOfImages.isNotEmpty()){
            setUpImagesAccordingtoSelection()
        }
        if (modelClass.noteViewStatus!=null){
            binding.noteViewStatusCard.visibility=View.VISIBLE
            binding.noteViewStatusImage.setImageResource(modelClass.noteViewStatus!!)
        }
        if (modelClass.textColor!=null){
            setColorOnView(
                modelClass.textColor!!,listOfTextViews)
        }
        if (modelClass.backgroundTheme!=null){
            binding.backGround.setBackgroundResource(modelClass.backgroundTheme!!)
        }
        if (modelClass.voice!=null) {
            binding.voiceLayout.visibility=View.VISIBLE
            binding.voiceDuration.text=modelClass.voiceDuration
            binding.playPauseBtn.setOnClickListener {
                    if (!checkPlayPause) {
                        modelClass.voice?.let { it1 ->voicePlayer.playVoice(requireContext(),modelClass.voice!!,binding.playPauseBtn)}
                        binding.playPauseBtn.setImageResource(R.drawable.pause_icone)
                        checkPlayPause = true
                    } else {
                        voicePlayer.pauseVoice()
                        binding.playPauseBtn.setImageResource(R.drawable.next_arrow)
                        checkPlayPause = false
                    }
            }
        }
    }

    private fun setUpImagesAccordingtoSelection(){
    }
    private fun setGravityOnView(gravity: Int) {
        binding.titleEd.gravity=gravity
        binding.descriptionEd.gravity=gravity
        binding.ImageViewLayout.gravity=gravity
        binding.hashTagLayout.gravity=gravity
    }
}