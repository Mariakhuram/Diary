package com.mk.diary.presentation.ui.noteview

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mk.diary.adapters.recyclerview.TagTitleRecyclerAdapter
import com.mk.diary.adapters.recyclerview.VoiceRecodingRecAdapter
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.domain.models.VoiceRecordingModelClass
import com.mk.diary.presentation.viewmodels.playing.VoicePlayingViewModel
import com.mk.diary.utils.fonts.FontTextSize
import com.mk.diary.utils.MyConstants
import dagger.hilt.android.AndroidEntryPoint
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentPreViewNoteBinding

@AndroidEntryPoint
class PreViewNoteFragment : FontTextSize() {
    private val listOfTextViews :ArrayList<TextView> by lazy { ArrayList() }
    private lateinit var allerTypeface: Typeface
    var checkPlayPause = false
    private val voicePlayViewModel :VoicePlayingViewModel by viewModels()
    private val voiceModelList :ArrayList<VoiceRecordingModelClass> by  lazy { ArrayList() }
    private val mVoiceRecAdapter by lazy { VoiceRecodingRecAdapter(voiceModelList) }
    private val listOfImages :ArrayList<String> by lazy { ArrayList() }
    private val listOfTags :ArrayList<String> by lazy { ArrayList() }
    private lateinit var mAdapter : TagTitleRecyclerAdapter
    lateinit var binding: FragmentPreViewNoteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreViewNoteBinding.inflate(layoutInflater, container, false)
        val receivedObject =arguments?.getSerializable(MyConstants.PASS_DATA) as NoteViewModelClass
        binding.backBtn.setOnClickListener {
            fragmentManager?.popBackStack()
        }
        for (index in receivedObject?.listOfImages?.indices!!) {
            val image = receivedObject.listOfImages[index]
            listOfImages.add(image)
        }
        if (receivedObject.tagTitle.size>0){
            receivedObject.tagTitle.remove("#")
            for (index in receivedObject?.tagTitle?.indices!!) {
                val image = receivedObject.tagTitle[index]
                listOfTags.add("#$image")
            }
        }
        voiceRecyclerView()
        setRecycler()
        backButton()
        setupPreview(receivedObject)
        return binding.root
    }
    private fun voiceRecyclerView() {
        binding.voiceRec.adapter=mVoiceRecAdapter
        mVoiceRecAdapter.recyclerClick(object:VoiceRecodingRecAdapter.PassData{
            override fun clickFunction(modelClass: VoiceRecordingModelClass, position: Int,imageView: ImageView) {
                if (!checkPlayPause){
                    modelClass.voicePath?.let {
                        voicePlayViewModel.playVoice(requireContext(),
                            it,imageView)
                    }
                    checkPlayPause = true
                    imageView.setImageResource(R.drawable.pause_icone)
                }else{
                    checkPlayPause = false
                    voicePlayViewModel.pauseVoice()
                    imageView.setImageResource(R.drawable.playvoiceicone)
                }
            }
            override fun deleteFunction(modelClass: VoiceRecordingModelClass) {

            }

        })
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
        mAdapter = TagTitleRecyclerAdapter(listOfTags)
        binding.recyclerTagLayout.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
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

            Glide.with(requireContext()).load(modelClass.backgroundTheme).into(binding.backGround)
        }
        if (modelClass.voice!=null) {
            voiceModelList.clear()
            for (m in modelClass.voice){
                voiceModelList.add(VoiceRecordingModelClass(m.voiceName,m.voicePath, m.duration))
                mVoiceRecAdapter.notifyDataSetChanged()
                binding.voiceLayout.visibility = View.VISIBLE
                binding.voiceLayout.visibility=View.VISIBLE
            }
        }
    }
    private fun setUpImagesAccordingtoSelection(){
        if (listOfImages.size==1){
            binding.imageViewCTwo.visibility=View.GONE
            binding.imageViewCThree.visibility=View.GONE
            binding.imageViewCFour.visibility=View.GONE
            binding.imageViewCOne.visibility=View.VISIBLE
            Glide.with(requireContext()).load(listOfImages[0]).into(binding.ImageViewOne)
        }else if (listOfImages.size==2){
            binding.imageViewCThree.visibility=View.GONE
            binding.imageViewCFour.visibility=View.GONE
            binding.imageViewCTwo.visibility=View.VISIBLE
            binding.imageViewCOne.visibility=View.VISIBLE
            Glide.with(requireContext()).load(listOfImages[0]).into(binding.ImageViewOne)
            Glide.with(requireContext()).load(listOfImages[1]).into(binding.ImageViewTwo)
        }else if (listOfImages.size==3){
            binding.imageViewCThree.visibility=View.VISIBLE
            binding.imageViewCFour.visibility=View.GONE
            binding.imageViewCTwo.visibility=View.VISIBLE
            binding.imageViewCOne.visibility=View.VISIBLE
            Glide.with(requireContext()).load(listOfImages[2]).into(binding.ImageViewThree)
            Glide.with(requireContext()).load(listOfImages[0]).into(binding.ImageViewOne)
            Glide.with(requireContext()).load(listOfImages[1]).into(binding.ImageViewTwo)
        }else{
            binding.imageViewCThree.visibility=View.VISIBLE
            binding.imageViewCFour.visibility=View.VISIBLE
            binding.imageViewCTwo.visibility=View.VISIBLE
            binding.imageViewCOne.visibility=View.VISIBLE
            Glide.with(requireContext()).load(listOfImages[2]).into(binding.ImageViewThree)
            Glide.with(requireContext()).load(listOfImages[0]).into(binding.ImageViewOne)
            Glide.with(requireContext()).load(listOfImages[1]).into(binding.ImageViewTwo)
            Glide.with(requireContext()).load(listOfImages[3]).into(binding.ImageViewFour)
        }
    }
    private fun setGravityOnView(gravity: Int) {
        binding.titleEd.gravity=gravity
        binding.descriptionEd.gravity=gravity
        binding.imageLayoutOne.gravity=gravity
        binding.imageLayoutTwo.gravity=gravity
        binding.hashTagLayout.gravity=gravity
    }
}