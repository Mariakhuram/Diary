package com.mk.diary.presentation.ui.noteview

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.text.style.BulletSpan
import android.text.style.UnderlineSpan
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mk.diary.R
import com.mk.diary.adapters.recyclerview.CreateNoteImageRecyclerAdapter
import com.mk.diary.adapters.recyclerview.GalleryImagePickerRecAdapter
import com.mk.diary.adapters.recyclerview.HashTagRecyclerAdapter
import com.mk.diary.adapters.tabslayout.BackgroundThemeTabLayoutAdapter
import com.mk.diary.databinding.FragmentCreateNoteBinding
import com.mk.diary.domain.models.ImageItem
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.helpers.ResultCase
import com.mk.diary.helpers.VoiceRecorderHelper
import com.mk.diary.presentation.viewmodels.noteview.NoteViewModel
import com.mk.diary.presentation.viewmodels.playing.VoicePlayingViewModel
import com.mk.diary.presentation.viewmodels.recoding.VoiceRecordingViewModel
import com.mk.diary.utils.fonts.FontTextSize
import com.mk.mydiary.utils.MyConstants
import com.mk.mydiary.utils.SharedPrefObj
import com.mk.mydiary.utils.appext.longToast
import com.mk.mydiary.utils.appext.shortToast
import com.mk.mydiary.utils.companion.CurrentTime
import com.mk.mydiary.utils.companion.Static
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.math.log

@AndroidEntryPoint
class CreateNoteFragment : FontTextSize(),DatePickerDialog.OnDateSetListener {
    val viewModel: NoteViewModel by viewModels()
    var checkClick = false
    val voicePlayViewModel: VoicePlayingViewModel by viewModels()
    val voiceRecordingViewModel: VoiceRecordingViewModel by viewModels()
    val scope = MainScope()
    private var seconds = 0
    private val createNoteModelList :ArrayList<String> by lazy { ArrayList()}
    private val mCreateNoteImagesAdapter by lazy { CreateNoteImageRecyclerAdapter(createNoteModelList) }
    lateinit var durationTextView: TextView
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val linesList :ArrayList<String> by lazy { ArrayList() }
    private val listOfSelectedImages :ArrayList<String> by lazy { ArrayList() }
    private val hashTagList: ArrayList<String> by lazy { ArrayList() }
    private val hashTagRecyclerAdapter by lazy { HashTagRecyclerAdapter(hashTagList) }
    private val listOfTextView: ArrayList<TextView> by lazy { ArrayList() }
    private val listOfEditTexts: ArrayList<EditText> by lazy { ArrayList() }
    private val galleryImagesList: ArrayList<ImageItem> by lazy { ArrayList() }
    private val mGalleryImagesPickerAdapter by lazy { GalleryImagePickerRecAdapter(galleryImagesList) }
    private lateinit var binding: FragmentCreateNoteBinding
    private lateinit var allerTypeface: Typeface
    private val PICK_AUDIO_REQUEST_CODE = 123 // Use your own request code
    private val CAMERA_REQUEST_CODE = 125
    private var selectedTextForUnderLine: String? = null
    private var selectedTextForHighLighting: String? = null
    private val calendar by lazy { Calendar.getInstance() }
    private var REQUEST_CODE_PERMISSIONS_ANDROID_13 = 3243
    private var REQUEST_CODE_PERMISSIONS_ANDROID_11 = 32423
    private var REQUEST_CODE_PERMISSIONS_ANDROID_6 = 323421
    private var formattedDuration: String? = null
    private var key: String? = null
    private var audio_File: File? = null
    private var audioFilePath: String? = null
    private var updated: String? = null
    private var checkPlayPause = false
    private var checkSaveBtn = false
    private var checkRecording = false
    private lateinit var clipboardManager: ClipboardManager
    var checkBulletsStyleClick = false
    var counter = 0
    var gravity:Int?=null
    var dayOfWeek:String?=null
    var date:String?=null
    var month:String?=null
    var year:String?=null
    var voice:String?=null
    var imageUri:String?=null
    var position:Int?=null
    var backGroundImage:Int?=null
    var status:Int?=null
    var textSize:Float?=null
    var textColor:Int?=null
    var emoji:String?=null
    var fontFamily:String?=null
    var capitalization:String?=null
    var bulletStyle:String?=null
    var title:String?=null
    var deleteImageViewItem :String?=null
    var description:String?=null
    var durationOfVoice:String?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreateNoteBinding.inflate(layoutInflater, container, false)
        Static.checkBolean = false
        dateView()
        setUpImagesAccordingtoSelection()
        askPermissionsForAll()
        setupAudio()
        hashTagRecycler()
        setupListsForTextSize()
        selectedImageViewClicks()
        binding.createNoteRec.adapter=mCreateNoteImagesAdapter
        mCreateNoteImagesAdapter.recyclerClick(object :CreateNoteImageRecyclerAdapter.PassRateData{
            override fun clickFunction(modelClass: String) {
                deleteImageViewItem=modelClass
                val bundle = Bundle()
                bundle.putString(MyConstants.PASS_DATA,modelClass)
                findNavController().navigate(R.id.action_createNoteFragment_to_imageViewDetailsFragment,bundle)
            }
            override fun delete(modelClass: String) {
                createNoteModelList.remove(modelClass)
                MyConstants.list.remove(modelClass)
                MyConstants.listOfImageCounting.remove(Uri.parse(modelClass))
                mCreateNoteImagesAdapter.notifyDataSetChanged()
            }
        })
        removeImagesAccordingToPositions()
        playPauseAudio()
        setupPreView()
        observeRecordingState()
        saveNoteViewDataSetup()
        clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        customBottomBarClickEvents()
        checkUserStatus()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (backGroundImage!=null){
            binding.backGround.setBackgroundResource(backGroundImage!!)
        }
        if (voice!=null){
            binding.voiceLayout.visibility=View.VISIBLE
            binding.voiceDuration.text=formattedDuration
        }
        if (hashTagList.isNotEmpty()){
            binding.hashTagLayout.visibility=View.VISIBLE
        }
        if(status!=null){
            binding.noteViewStatusImage.setImageResource(status!!)
        }
        if (Static.deleteBoolean && deleteImageViewItem!=null){
            createNoteModelList.remove(deleteImageViewItem)
            MyConstants.listOfImageCounting.remove(Uri.parse(deleteImageViewItem))
            MyConstants.list.remove(deleteImageViewItem)
            mCreateNoteImagesAdapter.notifyDataSetChanged()
        }
        gravity?.let { setGravityOnView(it) }
    }
    private fun hashTagRecycler() {
        hashTagList.add("#")
        binding.recyclerTagLayout.adapter = hashTagRecyclerAdapter
        hashTagRecyclerAdapter.hashRecClick(object :HashTagRecyclerAdapter.HashTagItemClick{
            override fun hashTagClick(data: String) {
                hashTagList.add(data)
                hashTagRecyclerAdapter.notifyDataSetChanged()
            }
            override fun hashTagDelete(data: String) {
                hashTagList.remove(data)
            }
        })
    }
    private fun setupPreView() {
        binding.preViewBtn.setOnClickListener {
            val mainTitle = binding.titleEd.text.toString()
            val mainDescription = binding.descriptionEd.text.toString()
            val model = NoteViewModelClass(
                0,
                CurrentTime.currentTime,
                dayOfWeek,
                date,
                year,
                month,
                listOfSelectedImages,
                voice,
                formattedDuration,
                mainTitle,
                mainDescription,
                selectedTextForUnderLine,
                selectedTextForHighLighting,
                hashTagList,
                status,
                backGroundImage,
                fontFamily,
                capitalization,
                textSize,
                textColor,
                gravity,
                linesList
            )
            val bundle = Bundle()
            bundle.putSerializable(MyConstants.PASS_DATA,model)
            findNavController().navigate(R.id.action_createNoteFragment_to_preViewNoteFragment,bundle)
        }
    }

    private fun saveDataBottomSheet() {
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.dontsave_noteview_bottom_sheet, null)
        val cancelBtn = view.findViewById<ConstraintLayout>(R.id.cancelBtn)
        val saveBtn = view.findViewById<ConstraintLayout>(R.id.deleteBtn)
        // Connect TabLayout and ViewPager2
        cancelBtn.setOnClickListener {
            // Handle the cancel button click
            dialog.dismiss() // Dismiss the dialog when the cancel button is clicked
        }
        saveBtn.setOnClickListener {
            val mainTitle = binding.titleEd.text.toString()
            val mainDescription = binding.descriptionEd.text.toString()
            val model = NoteViewModelClass(
                0,
                CurrentTime.currentTime,
                dayOfWeek,
                date,
                year,
                month,
                listOfSelectedImages,
                voice,
                formattedDuration,
                mainTitle,
                mainDescription,
                selectedTextForUnderLine,
                selectedTextForHighLighting,
                hashTagList,
                status,
                backGroundImage,
                fontFamily,
                capitalization,
                textSize,
                textColor,
                gravity,
                linesList
            )
            lifecycleScope.launch {
                when (val result = viewModel.saveData(model)) {
                    is ResultCase.Success -> {
                        // Handle success, if needed
                        if (checkClick) {
                            requireContext().shortToast("Saved")
                            title = null
                            description = null
                            textSize = null
                            textSize = null
                            emoji = null
                            status = null
                            bulletStyle = null
                            capitalization = null
                            fontFamily = null
                            backGroundImage=null
                            listOfSelectedImages.clear()
                            binding.descriptionEd.setText("")
                            binding.titleEd.setText("")
                            dialog.dismiss()
                            findNavController().popBackStack()
                        } else {
                            requireContext().shortToast("Saved")
                            title = null
                            description = null
                            textSize = null
                            textSize = null
                            emoji = null
                            status = null
                            bulletStyle = null
                            capitalization = null
                            fontFamily = null
                            backGroundImage
                            MyConstants.list
                            MyConstants.listOfImageCounting
                            SharedPrefObj.removeInt(requireContext(), MyConstants.FONTS_KEY)
                            binding.descriptionEd.setText("")
                            binding.titleEd.setText("")
                            dialog.dismiss()
                        }
                    }

                    is ResultCase.Error -> {
                        // Handle error
                        requireContext().shortToast(result.message.toString())
                    }

                    else -> {
                    }
                }
            }
        }
        cancelBtn.setOnClickListener {
            if (checkClick) {
                dialog.dismiss()
                findNavController().popBackStack()
            } else {
                dialog.dismiss()
            }
        }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.setOnCancelListener {
            // Handle the cancel listener if needed
        }
        dialog.show()
    }
    private fun saveNoteViewDataSetup() {
        binding.saveNoteViewBtn.setOnClickListener {

            checkSaveBtn = true
            saveDataBottomSheet()
        }
    }
    private fun setupListsForTextSize() {
        listOfEditTexts.add(binding.titleEd)
        listOfEditTexts.add(binding.descriptionEd)
        listOfTextView.add(binding.dateTv)
        listOfTextView.add(binding.dayTv)
        listOfTextView.add(binding.monthYearTv)
    }
    private fun askPermissionsForAll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            askPermissionsForAndroid13AndAbove()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            askPermissionsForAndroid11AndAbove()
        } else {
            askPermissionsForAndroid6AndAbove()
        }
    }
    private fun askPermissionsForAndroid13AndAbove() {
        val mediaAudioAccess = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_MEDIA_AUDIO
        )
        val mediaImageAccess = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_MEDIA_IMAGES
        )
        val recordAudio =
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
        if (mediaAudioAccess == PackageManager.PERMISSION_DENIED || mediaImageAccess == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.RECORD_AUDIO
                ),
                REQUEST_CODE_PERMISSIONS_ANDROID_13
            )
        }
    }
    private fun askPermissionsForAndroid11AndAbove() {
        val readExternalStorageAccess = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val pickAudioAccess =
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
        val pickImageAccess =
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        if (
            readExternalStorageAccess == PackageManager.PERMISSION_DENIED ||
            pickAudioAccess == PackageManager.PERMISSION_DENIED ||
            pickImageAccess == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA
                ),
                REQUEST_CODE_PERMISSIONS_ANDROID_11
            )
        }
    }
    private fun askPermissionsForAndroid6AndAbove() {
        val readExternalStorageAccess = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val pickAudioAccess =
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
        val pickImageAccess = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (readExternalStorageAccess == PackageManager.PERMISSION_DENIED || pickAudioAccess == PackageManager.PERMISSION_DENIED || pickImageAccess == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSIONS_ANDROID_6
            )
        }
    }
    private fun showDatePicker() {
        // Set minimum and maximum dates
        val minCalendar = Calendar.getInstance()
        minCalendar.set(2023, Calendar.JANUARY, 1)
        val maxCalendar = Calendar.getInstance()
        maxCalendar.set(2024, Calendar.DECEMBER, 31)

        // Set initial values for the date picker
        val initialYear = minCalendar.get(Calendar.YEAR)
        val initialMonth = minCalendar.get(Calendar.MONTH)
        val initialDay = minCalendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
            // Handle the selected date
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day)

            // Check if the selected date is within the allowed range
            if (selectedDate.timeInMillis in minCalendar.timeInMillis..maxCalendar.timeInMillis) {
                // Format the date
                val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                // Get the day of the week
                val dayOfWeek =
                    SimpleDateFormat("EEEE", Locale.getDefault()).format(selectedDate.time)
                val monthFormat = SimpleDateFormat("MMM", Locale.getDefault()).format(selectedDate.time)

                // Update TextViews with formatted date and day of the week
                binding.dateTv.text = formattedDate
                binding.dayTv.text = dayOfWeek
                binding.monthYearTv.text = "${monthFormat} $year"

                // Store the selected date components
                this.dayOfWeek = dayOfWeek
                this.date = formattedDate
                this.month = monthFormat
                this.year = year.toString()
            } else {
                // The selected date is outside the allowed range, show an error message or take appropriate action
                Toast.makeText(
                    requireContext(),
                    "Selected date is outside the allowed range",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, initialYear, initialMonth, initialDay)

        // Set the min and max dates for the date picker
        datePickerDialog.datePicker.minDate = minCalendar.timeInMillis
        datePickerDialog.datePicker.maxDate = maxCalendar.timeInMillis

        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        // Check if the selected date is within the allowed range

    }



    private fun playPauseAudio() {
        binding.playPauseBtn.setOnClickListener {
            if (voice?.isNotEmpty() == true) {
                if (!checkPlayPause) {
                    requireContext().shortToast("Play")
                    voice?.let { it1 -> voicePlayViewModel.playVoice(requireContext(), it1,binding.playPauseBtn) }
                    binding.playPauseBtn.setImageResource(R.drawable.pause_icone)
                    checkPlayPause = true
                } else {
                    requireContext().shortToast("Pause")
                    voicePlayViewModel.pauseVoice()
                    binding.playPauseBtn.setImageResource(R.drawable.next_arrow)
                    checkPlayPause = false
                }
            }
        }
        binding.voiceDeleteBtn.setOnClickListener {
            binding.voiceLayout.visibility = View.GONE
            voice = null
            voicePlayViewModel.releaseVoice()
        }
    }

    private fun setupAudio() {
        val job = scope.launch {
            while (true) {
                key = generateRandomString()
                delay(1000)
            }
        }
        job.start()
        val directory = File(requireContext().filesDir, "audioFiles")
        if (!directory.exists()) {
            directory.mkdir()
        }
        val fileName = "${UUID.randomUUID()}.3gp"
        audioFilePath = File(directory, fileName).absolutePath

    }

    private fun generateRandomString(): String {
        val length = 10 // or any desired length
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomString = (1..length)
            .map { charset.random() }
            .joinToString("")
        return randomString
    }

    private fun dateView() {
        binding.dateTv.text = CurrentTime.currentDate
        binding.monthYearTv.text = "${CurrentTime.currentMonth} ${CurrentTime.currentYear}"
        binding.dayTv.text = CurrentTime.currentDayOfWeek
        dayOfWeek = CurrentTime.currentDayOfWeek
        date = CurrentTime.currentDate
        month = CurrentTime.currentMonth
        year = CurrentTime.currentYear
    }
    private fun customBottomBarClickEvents() {
        binding.hashTagBtn.setOnClickListener {
            binding.hashTagLayout.visibility = View.VISIBLE
        }
        binding.calenderDailogueBtn.setOnClickListener {
            showDatePicker()
        }
        binding.noteViewStatusImage.setOnClickListener { bottomSheetNoteViewStatus() }
        binding.emojiBtn.setOnClickListener { emojiBottomSheet() }
        binding.imageBtn.setOnClickListener { galleryImagePickerBottomSheet() }
        binding.voiceBtn.setOnClickListener { voiceRecordingBottomSheet() }
        binding.textBtn.setOnClickListener { textFontBottomSheet() }
        binding.backgroundSelectionBtn.setOnClickListener { backGroundThemeBottomSheet() }
        binding.backBtn.setOnClickListener {
            if (checkSaveBtn){
                findNavController().popBackStack()
            }else{
                checkClick = true
                val mainTitle = binding.titleEd.text.toString()
                val mainDescription = binding.descriptionEd.text.toString()
                if (status != null || emoji != null || title != null
                    || description != null
                    || MyConstants.listOfImageCounting != null || MyConstants.list != null
                    || imageUri != null || textColor != null || textSize != null
                    || voice != null || durationOfVoice != null || mainDescription != null
                    || mainTitle != null
                ) {
                    saveDataBottomSheet()
                } else {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun backGroundThemeBottomSheet() {
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.background_theme_bottom_sheet, null)
        val cancelBtn = view.findViewById<CardView>(R.id.cancelBtn)
        val saveBtn = view.findViewById<CardView>(R.id.saveBtn)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        val adapter = BackgroundThemeTabLayoutAdapter(requireActivity())
        viewPager.adapter = adapter
        // Connect TabLayout and ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "All"
                1 -> tab.text = "Classic"
                2 -> tab.text = "Simple"
                3 -> tab.text = "Cute"
                4 -> tab.text = "Holiday"
                5 -> tab.text = "Colors"
                // Add more tabs as needed
            }
        }.attach()
        cancelBtn.setOnClickListener {
            // Handle the cancel button click
            dialog.dismiss() // Dismiss the dialog when the cancel button is clicked
        }
        saveBtn.setOnClickListener {
            // Handle the save button click
            if (Static.backGroundImage != null) {
                backGroundImage=Static.backGroundImage
                binding.backGround.setBackgroundResource(Static.backGroundImage!!)
            }
            dialog.dismiss()
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

    private fun textFontBottomSheet() {
        var check = false
        var checkU = false
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.font_text_bottom_sheet, null)
        val cancelBtn = view.findViewById<CardView>(R.id.cancelBtn)
        val saveBtn = view.findViewById<CardView>(R.id.saveBtn)
        //fontUnderline
        val underLineBtn = view.findViewById<CardView>(R.id.underLineBtn)
        val underLineImg = view.findViewById<ImageView>(R.id.underLineImg)
        underLineBtn.setOnClickListener {
            if (!checkU) {
                underLineSelection(true, underLineBtn, underLineImg)
                checkU = true
            } else {
                underLineSelection(false, underLineBtn, underLineImg)
                checkU = false
            }
        }
        val backgroundBtn = view.findViewById<CardView>(R.id.highLighterBtn)
        val backgroundImage = view.findViewById<ImageView>(R.id.highLighterImg)
        backgroundBtn.setOnClickListener {
            if (!check) {
                highLightSelection(true, backgroundBtn, backgroundImage)
                check = true
            } else {
                highLightSelection(false, backgroundBtn, backgroundImage)
                check = false
            }
        }
        //Gravity
        val gravityCenterBtn = view.findViewById<LinearLayout>(R.id.centerGravityTextViewBtn)
        val gravityStartBtn = view.findViewById<LinearLayout>(R.id.startGravityTextVieBtn)
        val gravityEndBtn = view.findViewById<LinearLayout>(R.id.endGravityTextViewBtn)
        val gravityCenterImg = view.findViewById<ImageView>(R.id.centerGravityImage)
        val gravityStartImg = view.findViewById<ImageView>(R.id.startGravityImage)
        val gravityEndImg = view.findViewById<ImageView>(R.id.endGravityImage)
        gravityCenterBtn.setOnClickListener {
            gravity=Gravity.CENTER
            gravitySetupCenter(
                gravityCenterBtn, gravityStartBtn, gravityEndBtn,
                gravityCenterImg, gravityStartImg, gravityEndImg
            )
        }
        gravityStartBtn.setOnClickListener {
            gravitySetupStart(
                gravityCenterBtn, gravityStartBtn, gravityEndBtn,
                gravityCenterImg, gravityStartImg, gravityEndImg
            )
        }
        gravityEndBtn.setOnClickListener {
            gravitySetupEnd(
                gravityCenterBtn, gravityStartBtn, gravityEndBtn,
                gravityCenterImg, gravityStartImg, gravityEndImg
            )
        }
        val selectedGravity = gravity
        when (selectedGravity) {
            Gravity.CENTER -> gravitySetupCenter(
                gravityCenterBtn, gravityStartBtn, gravityEndBtn,
                gravityCenterImg, gravityStartImg, gravityEndImg
            )

            Gravity.START -> gravitySetupStart(
                gravityCenterBtn, gravityStartBtn, gravityEndBtn,
                gravityCenterImg, gravityStartImg, gravityEndImg
            )

            Gravity.END -> gravitySetupEnd(
                gravityCenterBtn, gravityStartBtn, gravityEndBtn,
                gravityCenterImg, gravityStartImg, gravityEndImg
            )
        }
        //Counting Style
        val digitsBtn = view.findViewById<CardView>(R.id.digitsStylesCardBtn)
        val dotsBtn = view.findViewById<CardView>(R.id.dotsStylesCardBtn)
        val dotsImage = view.findViewById<ImageView>(R.id.dotsImageView)
        val digitsImage = view.findViewById<ImageView>(R.id.digitsImageView)
        dotsBtn.setOnClickListener {
            binding.descriptionEd.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val string: String = text.toString()
                    string.length > 1 && string[string.length - 2] == '\n'
                }
                override fun onTextChanged(txt: CharSequence, start: Int, before: Int, count: Int) {
                    val string: String = txt.toString()
                    if (string.isNotEmpty() && string[string.length - 1] == '\n') {
                        val previousLineText = getPreviousLineText(txt)
                        // Add the previous line text to the list
                        if (previousLineText.isNotEmpty()) {
                            linesList.add(previousLineText)
                            // You can do something with the list here
                            // For example, print the list:
                        }
                        requireContext().shortToast(string)
                        if ( binding.descriptionEd.selectionStart == txt.length ||  binding.descriptionEd.selectionEnd == txt.length) {
                            // Add bullet point and spaces for indentation
                            binding.descriptionEd.text.append("\u2022    ")


                        }
                    }
                }
                override fun afterTextChanged(s: Editable?) {

                }
            })
            val draw = dotsImage.drawable
            draw.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.white),
                PorterDuff.Mode.SRC_IN
            )
            dotsImage.setImageDrawable(draw)
            val d = digitsImage.drawable
            d.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.black),
                PorterDuff.Mode.SRC_IN
            )
            digitsImage.setImageDrawable(d)
            dotsBtn.setCardBackgroundColor(resources.getColor(R.color.pinkButtonColor))
            digitsBtn.setCardBackgroundColor(resources.getColor(R.color.white))
            bulletStyle = MyConstants.BULLET_STYLE_DOTS
        }
        digitsBtn.setOnClickListener {
            binding.descriptionEd.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val string: String = text.toString()
                    string.length > 1 && string[string.length - 2] == '\n'
                }
                override fun onTextChanged(txt: CharSequence, start: Int, before: Int, count: Int) {
                    val string: String = txt.toString()
                    if (string.isNotEmpty() && string[string.length - 1] == '\n') {
                        counter ++
                        if ( binding.descriptionEd.selectionStart == txt.length ||  binding.descriptionEd.selectionEnd == txt.length) {
                            // Add bullet point and spaces for indentation
                            binding.descriptionEd.text.append("$counter    ")
                        }
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                    // You can perform additional actions after the text changes if needed
                }
            })
            val draw = dotsImage.drawable
            draw.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.black),
                PorterDuff.Mode.SRC_IN
            )
            dotsImage.setImageDrawable(draw)
            val drawabl = digitsImage.drawable
            drawabl.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.white),
                PorterDuff.Mode.SRC_IN
            )
            digitsImage.setImageDrawable(drawabl)
            dotsBtn.setCardBackgroundColor(resources.getColor(R.color.white))
            digitsBtn.setCardBackgroundColor(resources.getColor(R.color.pinkButtonColor))
            bulletStyle = MyConstants.BULLET_STYLE_DIGITS
        }
        val string=bulletStyle
        when(string){
            MyConstants.BULLET_STYLE_DOTS->{
                val draw = dotsImage.drawable
                draw.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.white),
                    PorterDuff.Mode.SRC_IN
                )
                dotsImage.setImageDrawable(draw)
                val d = digitsImage.drawable
                d.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.black),
                    PorterDuff.Mode.SRC_IN
                )
                digitsImage.setImageDrawable(d)
                dotsBtn.setCardBackgroundColor(resources.getColor(R.color.pinkButtonColor))
                digitsBtn.setCardBackgroundColor(resources.getColor(R.color.white))
            }
            MyConstants.BULLET_STYLE_DIGITS->{
                val draw = dotsImage.drawable
                draw.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.black),
                    PorterDuff.Mode.SRC_IN
                )
                dotsImage.setImageDrawable(draw)
                val drawabl = digitsImage.drawable
                drawabl.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.white),
                    PorterDuff.Mode.SRC_IN
                )
                digitsImage.setImageDrawable(drawabl)
                dotsBtn.setCardBackgroundColor(resources.getColor(R.color.white))
                digitsBtn.setCardBackgroundColor(resources.getColor(R.color.pinkButtonColor))
            }
        }
        //Text Size
        val normalTextSizeBtn = view.findViewById<LinearLayout>(R.id.normalSizeTextBtn)
        val mediumTextSizeBtn = view.findViewById<LinearLayout>(R.id.mediumTextSizeBtn)
        val largeTextSizeBtn = view.findViewById<LinearLayout>(R.id.largeSizeTextBtn)
        val normalTextSizeImg = view.findViewById<ImageView>(R.id.normalSizeTextImage)
        val mediumTextSizeImg = view.findViewById<ImageView>(R.id.mediumTextSizeImage)
        val largeTextSizeImg = view.findViewById<ImageView>(R.id.largeSizeTextImage)
        largeTextSizeBtn.setOnClickListener {
            textSizeLargeSetup(
                normalTextSizeBtn, mediumTextSizeBtn, largeTextSizeBtn,
                normalTextSizeImg, mediumTextSizeImg, largeTextSizeImg
            )
            setupTextSizeOnViews(20f, listOfTextView, listOfEditTexts)
            textSize = 20f
        }
        mediumTextSizeBtn.setOnClickListener {
            textSizeMediumSetup(
                normalTextSizeBtn, mediumTextSizeBtn, largeTextSizeBtn,
                normalTextSizeImg, mediumTextSizeImg, largeTextSizeImg
            )
            setupTextSizeOnViews(16f, listOfTextView, listOfEditTexts)
            textSize = 16f
        }
        normalTextSizeBtn.setOnClickListener {
            textSizeNormalSetup(
                normalTextSizeBtn, mediumTextSizeBtn, largeTextSizeBtn,
                normalTextSizeImg, mediumTextSizeImg, largeTextSizeImg
            )
            setupTextSizeOnViews(12f, listOfTextView, listOfEditTexts)
            textSize = 12f
        }
        val selectedTextSize =textSize
        when (selectedTextSize) {
            16f -> textSizeMediumSetup(
                normalTextSizeBtn, mediumTextSizeBtn, largeTextSizeBtn,
                normalTextSizeImg, mediumTextSizeImg, largeTextSizeImg
            )

            12f -> textSizeNormalSetup(
                normalTextSizeBtn, mediumTextSizeBtn, largeTextSizeBtn,
                normalTextSizeImg, mediumTextSizeImg, largeTextSizeImg
            )

            20f -> textSizeLargeSetup(
                normalTextSizeBtn, mediumTextSizeBtn, largeTextSizeBtn,
                normalTextSizeImg, mediumTextSizeImg, largeTextSizeImg
            )
        }
        //Letters Style
        val allNormalBtn = view.findViewById<LinearLayout>(R.id.normalLettersBtn)
        val allCapitalBtn = view.findViewById<LinearLayout>(R.id.allCapitalLettersBtn)
        val allSmallBtn = view.findViewById<LinearLayout>(R.id.allSmallLettersBtn)
        val allCapImg = view.findViewById<ImageView>(R.id.allCapitalLettersImage)
        val allSamImg = view.findViewById<ImageView>(R.id.allSmallLettersImage)
        val normalCapImg = view.findViewById<ImageView>(R.id.normalLettersImage)
        allCapitalBtn.setOnClickListener {
            setupTextLettersCustomizationOnViews(true, listOfTextView, listOfEditTexts)
            allCapitalLettersSetup(
                allNormalBtn,
                allCapitalBtn,
                allSmallBtn,
                allSamImg,
                normalCapImg,
                allCapImg
            )
            capitalization = MyConstants.ALL_CAPITAL
        }
        allSmallBtn.setOnClickListener {
            setupTextLettersCustomizationOnViews(false, listOfTextView, listOfEditTexts)
            allSmallLettersSetup(
                allNormalBtn,
                allCapitalBtn,
                allSmallBtn,
                allSamImg,
                normalCapImg,
                allCapImg
            )
            capitalization = MyConstants.ALL_SMALL
        }
        allNormalBtn.setOnClickListener {
            allNormalLettersSetup(
                allNormalBtn,
                allCapitalBtn,
                allSmallBtn,
                allSamImg,
                normalCapImg,
                allCapImg
            )
            capitalization = MyConstants.ALL_NORMAL
        }
        val letterCapitalization =capitalization
        when (letterCapitalization) {
            MyConstants.ALL_CAPITAL -> {
                allCapitalLettersSetup(
                    allNormalBtn,
                    allCapitalBtn,
                    allSmallBtn,
                    allSamImg,
                    normalCapImg,
                    allCapImg
                )
            }

            MyConstants.ALL_NORMAL -> {
                allNormalLettersSetup(
                    allNormalBtn,
                    allCapitalBtn,
                    allSmallBtn,
                    allSamImg,
                    normalCapImg,
                    allCapImg
                )
            }

            MyConstants.ALL_SMALL -> {
                allSmallLettersSetup(
                    allNormalBtn,
                    allCapitalBtn,
                    allSmallBtn,
                    allSamImg,
                    normalCapImg,
                    allCapImg
                )
            }
        }
        //Colors
        val colorOneBtn = view.findViewById<ConstraintLayout>(R.id.colorOne_Purple)
        val colorTwoBtn = view.findViewById<ConstraintLayout>(R.id.colorTwo_Red)
        val colorThreeBtn = view.findViewById<ConstraintLayout>(R.id.colorThree_Blue)
        val colorFourBtn = view.findViewById<ConstraintLayout>(R.id.colorFour_Pink)
        val colorFiveBtn = view.findViewById<ConstraintLayout>(R.id.colorFive_Green)
        val colorSixBtn = view.findViewById<ConstraintLayout>(R.id.colorSix_Yellow)
        colorOneBtn.setOnClickListener {
            binding.titleEd.setHintTextColor(resources.getColor(R.color.colorOne_Purple))
            binding.descriptionEd.setHintTextColor(resources.getColor(R.color.colorOne_Purple))
            binding.dateTv.setTextColor(resources.getColor(R.color.colorOne_Purple))
            binding.monthYearTv.setTextColor(resources.getColor(R.color.colorOne_Purple))
            binding.dayTv.setTextColor(resources.getColor(R.color.colorOne_Purple))
            binding.titleEd.setTextColor(resources.getColor(R.color.colorOne_Purple))
            binding.descriptionEd.setTextColor(resources.getColor(R.color.colorOne_Purple))
            colorOneBtn.setBackgroundResource(R.drawable.circle_shape_pink)
            colorTwoBtn.setBackgroundResource(android.R.color.transparent)
            colorThreeBtn.setBackgroundResource(android.R.color.transparent)
            colorFourBtn.setBackgroundResource(android.R.color.transparent)
            colorFiveBtn.setBackgroundResource(android.R.color.transparent)
            colorSixBtn.setBackgroundResource(android.R.color.transparent)
            textColor = resources.getColor(R.color.colorOne_Purple)
        }
        colorTwoBtn.setOnClickListener {
            binding.titleEd.setHintTextColor(resources.getColor(R.color.colorTwo_Red))
            binding.descriptionEd.setHintTextColor(resources.getColor(R.color.colorTwo_Red))
            binding.dateTv.setTextColor(resources.getColor(R.color.colorTwo_Red))
            binding.monthYearTv.setTextColor(resources.getColor(R.color.colorTwo_Red))
            binding.dayTv.setTextColor(resources.getColor(R.color.colorTwo_Red))
            binding.titleEd.setTextColor(resources.getColor(R.color.colorTwo_Red))
            binding.descriptionEd.setTextColor(resources.getColor(R.color.colorTwo_Red))
            colorOneBtn.setBackgroundResource(android.R.color.transparent)
            colorTwoBtn.setBackgroundResource(R.drawable.circle_shape_pink)
            colorThreeBtn.setBackgroundResource(android.R.color.transparent)
            colorFourBtn.setBackgroundResource(android.R.color.transparent)
            colorFiveBtn.setBackgroundResource(android.R.color.transparent)
            colorSixBtn.setBackgroundResource(android.R.color.transparent)
            textColor = resources.getColor(R.color.colorTwo_Red)
        }
        colorThreeBtn.setOnClickListener {
            binding.titleEd.setHintTextColor(resources.getColor(R.color.colorThree_Blue))
            binding.descriptionEd.setHintTextColor(resources.getColor(R.color.colorThree_Blue))
            binding.dateTv.setTextColor(resources.getColor(R.color.colorThree_Blue))
            binding.monthYearTv.setTextColor(resources.getColor(R.color.colorThree_Blue))
            binding.dayTv.setTextColor(resources.getColor(R.color.colorThree_Blue))
            binding.titleEd.setTextColor(resources.getColor(R.color.colorThree_Blue))
            binding.descriptionEd.setTextColor(resources.getColor(R.color.colorThree_Blue))
            colorOneBtn.setBackgroundResource(android.R.color.transparent)
            colorTwoBtn.setBackgroundResource(android.R.color.transparent)
            colorThreeBtn.setBackgroundResource(R.drawable.circle_shape_pink)
            colorFourBtn.setBackgroundResource(android.R.color.transparent)
            colorFiveBtn.setBackgroundResource(android.R.color.transparent)
            colorSixBtn.setBackgroundResource(android.R.color.transparent)
            textColor = resources.getColor(R.color.colorThree_Blue)
        }
        colorFourBtn.setOnClickListener {
            binding.titleEd.setHintTextColor(resources.getColor(R.color.colorFour_Pink))
            binding.descriptionEd.setHintTextColor(resources.getColor(R.color.colorFour_Pink))
            binding.dateTv.setTextColor(resources.getColor(R.color.colorFour_Pink))
            binding.monthYearTv.setTextColor(resources.getColor(R.color.colorFour_Pink))
            binding.dayTv.setTextColor(resources.getColor(R.color.colorFour_Pink))
            binding.titleEd.setTextColor(resources.getColor(R.color.colorFour_Pink))
            binding.descriptionEd.setTextColor(resources.getColor(R.color.colorFour_Pink))
            colorOneBtn.setBackgroundResource(android.R.color.transparent)
            colorTwoBtn.setBackgroundResource(android.R.color.transparent)
            colorThreeBtn.setBackgroundResource(android.R.color.transparent)
            colorFourBtn.setBackgroundResource(R.drawable.circle_shape_pink)
            colorFiveBtn.setBackgroundResource(android.R.color.transparent)
            colorSixBtn.setBackgroundResource(android.R.color.transparent)
            textColor = resources.getColor(R.color.colorFour_Pink)
        }
        colorFiveBtn.setOnClickListener {
            binding.titleEd.setHintTextColor(resources.getColor(R.color.colorFive_Green))
            binding.descriptionEd.setHintTextColor(resources.getColor(R.color.colorFive_Green))
            binding.dateTv.setTextColor(resources.getColor(R.color.colorFive_Green))
            binding.monthYearTv.setTextColor(resources.getColor(R.color.colorFive_Green))
            binding.dayTv.setTextColor(resources.getColor(R.color.colorFive_Green))
            binding.titleEd.setTextColor(resources.getColor(R.color.colorFive_Green))
            binding.descriptionEd.setTextColor(resources.getColor(R.color.colorFive_Green))
            colorOneBtn.setBackgroundResource(android.R.color.transparent)
            colorTwoBtn.setBackgroundResource(android.R.color.transparent)
            colorThreeBtn.setBackgroundResource(android.R.color.transparent)
            colorFourBtn.setBackgroundResource(android.R.color.transparent)
            colorFiveBtn.setBackgroundResource(R.drawable.circle_shape_pink)
            colorSixBtn.setBackgroundResource(android.R.color.transparent)
            textColor = resources.getColor(R.color.colorFive_Green)
        }
        colorSixBtn.setOnClickListener {
            binding.titleEd.setHintTextColor(resources.getColor(R.color.colorSix_Yellow))
            binding.descriptionEd.setHintTextColor(resources.getColor(R.color.colorSix_Yellow))
            binding.dateTv.setTextColor(resources.getColor(R.color.colorSix_Yellow))
            binding.monthYearTv.setTextColor(resources.getColor(R.color.colorSix_Yellow))
            binding.dayTv.setTextColor(resources.getColor(R.color.colorSix_Yellow))
            binding.titleEd.setTextColor(resources.getColor(R.color.colorSix_Yellow))
            binding.descriptionEd.setTextColor(resources.getColor(R.color.colorSix_Yellow))
            colorOneBtn.setBackgroundResource(android.R.color.transparent)
            colorTwoBtn.setBackgroundResource(android.R.color.transparent)
            colorThreeBtn.setBackgroundResource(android.R.color.transparent)
            colorFourBtn.setBackgroundResource(android.R.color.transparent)
            colorFiveBtn.setBackgroundResource(android.R.color.transparent)
            colorSixBtn.setBackgroundResource(R.drawable.circle_shape_pink)
            textColor = resources.getColor(R.color.colorSix_Yellow)
        }
        // Fonts Family
        val fontOneBtn = view.findViewById<CardView>(R.id.fontFamilyOneBtn)
        val fontTwoBtn = view.findViewById<CardView>(R.id.fontFamilyTwoBtn)
        val fontThreeBtn = view.findViewById<CardView>(R.id.fontFamilyThreeBtn)
        val fontFourBtn = view.findViewById<CardView>(R.id.fontFamilyFourBtn)
        val fontFiveBtn = view.findViewById<CardView>(R.id.fontFamilyFiveBtn)
        val fontSixBtn = view.findViewById<CardView>(R.id.fontFamilySixBtn)
        val fontSevenBtn = view.findViewById<CardView>(R.id.fontFamilySevenBtn)
        val fontEightBtn = view.findViewById<CardView>(R.id.fontFamilyEightBtn)
        val fontFamilyImgOne = view.findViewById<ImageView>(R.id.fontFamilyImgOne)
        val fontFamilyImgTwo = view.findViewById<ImageView>(R.id.fontFamilyImgTwo)
        val fontFamilyImgThree = view.findViewById<ImageView>(R.id.fontFamilyImgThree)
        val fontFamilyImgFour = view.findViewById<ImageView>(R.id.fontFamilyImgFour)
        val fontFamilyImgFive = view.findViewById<ImageView>(R.id.fontFamilyImgFive)
        val fontFamilyImgSix = view.findViewById<ImageView>(R.id.fontFamilyImgSix)
        val fontFamilyImgSeven = view.findViewById<ImageView>(R.id.fontFamilyImgSeven)
        val fontFamilyImgEight = view.findViewById<ImageView>(R.id.fontFamilyImgEight)
        fontOneBtn.setOnClickListener {
            setupFontOne(
                fontOneBtn,
                fontTwoBtn,
                fontThreeBtn,
                fontFourBtn,
                fontFiveBtn,
                fontSixBtn,
                fontSevenBtn,
                fontEightBtn,
                fontFamilyImgOne,
                fontFamilyImgTwo,
                fontFamilyImgThree,
                fontFamilyImgFour,
                fontFamilyImgFive,
                fontFamilyImgSix,
                fontFamilyImgSeven,
                fontFamilyImgEight
            )
        }
        fontTwoBtn.setOnClickListener {
            setupFontTwo(
                fontOneBtn,
                fontTwoBtn,
                fontThreeBtn,
                fontFourBtn,
                fontFiveBtn,
                fontSixBtn,
                fontSevenBtn,
                fontEightBtn,
                fontFamilyImgOne,
                fontFamilyImgTwo,
                fontFamilyImgThree,
                fontFamilyImgFour,
                fontFamilyImgFive,
                fontFamilyImgSix,
                fontFamilyImgSeven,
                fontFamilyImgEight
            )
        }
        fontThreeBtn.setOnClickListener {
            setupFontThree(
                fontOneBtn,
                fontTwoBtn,
                fontThreeBtn,
                fontFourBtn,
                fontFiveBtn,
                fontSixBtn,
                fontSevenBtn,
                fontEightBtn,
                fontFamilyImgOne,
                fontFamilyImgTwo,
                fontFamilyImgThree,
                fontFamilyImgFour,
                fontFamilyImgFive,
                fontFamilyImgSix,
                fontFamilyImgSeven,
                fontFamilyImgEight
            )
        }
        fontFourBtn.setOnClickListener {
            setupFontFour(
                fontOneBtn,
                fontTwoBtn,
                fontThreeBtn,
                fontFourBtn,
                fontFiveBtn,
                fontSixBtn,
                fontSevenBtn,
                fontEightBtn,
                fontFamilyImgOne,
                fontFamilyImgTwo,
                fontFamilyImgThree,
                fontFamilyImgFour,
                fontFamilyImgFive,
                fontFamilyImgSix,
                fontFamilyImgSeven,
                fontFamilyImgEight
            )
        }
        fontFiveBtn.setOnClickListener {
            setupFontFive(
                fontOneBtn,
                fontTwoBtn,
                fontThreeBtn,
                fontFourBtn,
                fontFiveBtn,
                fontSixBtn,
                fontSevenBtn,
                fontEightBtn,
                fontFamilyImgOne,
                fontFamilyImgTwo,
                fontFamilyImgThree,
                fontFamilyImgFour,
                fontFamilyImgFive,
                fontFamilyImgSix,
                fontFamilyImgSeven,
                fontFamilyImgEight
            )
        }
        fontSixBtn.setOnClickListener {
            setupFontSix(
                fontOneBtn,
                fontTwoBtn,
                fontThreeBtn,
                fontFourBtn,
                fontFiveBtn,
                fontSixBtn,
                fontSevenBtn,
                fontEightBtn,
                fontFamilyImgOne,
                fontFamilyImgTwo,
                fontFamilyImgThree,
                fontFamilyImgFour,
                fontFamilyImgFive,
                fontFamilyImgSix,
                fontFamilyImgSeven,
                fontFamilyImgEight
            )
        }
        fontSevenBtn.setOnClickListener {
            setupFontSeven(
                fontOneBtn,
                fontTwoBtn,
                fontThreeBtn,
                fontFourBtn,
                fontFiveBtn,
                fontSixBtn,
                fontSevenBtn,
                fontEightBtn,
                fontFamilyImgOne,
                fontFamilyImgTwo,
                fontFamilyImgThree,
                fontFamilyImgFour,
                fontFamilyImgFive,
                fontFamilyImgSix,
                fontFamilyImgSeven,
                fontFamilyImgEight
            )
        }
        fontEightBtn.setOnClickListener {
            setupFontEight(
                fontOneBtn,
                fontTwoBtn,
                fontThreeBtn,
                fontFourBtn,
                fontFiveBtn,
                fontSixBtn,
                fontSevenBtn,
                fontEightBtn,
                fontFamilyImgOne,
                fontFamilyImgTwo,
                fontFamilyImgThree,
                fontFamilyImgFour,
                fontFamilyImgFive,
                fontFamilyImgSix,
                fontFamilyImgSeven,
                fontFamilyImgEight
            )
        }

        cancelBtn.setOnClickListener {
            gravity = null
            textSize = null
            dialog.dismiss() // Dismiss the dialog when the cancel button is clicked
        }
        saveBtn.setOnClickListener {
            dialog.dismiss()
            // Handle the save button click
        }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.setOnCancelListener {
            // Handle the cancel listener if needed
        }
        dialog.show()
    }

    private fun checkBulletStyle(b:Boolean) {
        when(b){
            true->{
                requireContext().shortToast(checkBulletsStyleClick.toString())
            }
            false->{
                requireContext().shortToast(checkBulletsStyleClick.toString())
                binding.descriptionEd.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        val string: String = text.toString()
                        string.length > 1 && string[string.length - 2] == '\n'
                    }
                    override fun onTextChanged(txt: CharSequence, start: Int, before: Int, count: Int) {
                        val previousLineText = getPreviousLineText(txt)

                            val string: String = txt.toString()
                            if (string.isNotEmpty() && string[string.length - 1] == '\n') {
                                requireContext().shortToast(string)
                                if ( binding.descriptionEd.selectionStart == txt.length ||  binding.descriptionEd.selectionEnd == txt.length) {
                                    // Add bullet point and spaces for indentation
                                    binding.descriptionEd.text.append("\u2022    ")

                                }
                            }
                    }
                    override fun afterTextChanged(s: Editable?) {
                    }
                })
            }
        }
    }

    private fun underLineSelection(b: Boolean, underLineBtn: CardView, underLineImg: ImageView) {
        val spannable = SpannableStringBuilder(binding.descriptionEd.text)
        val startSelection = binding.descriptionEd.selectionStart
        val endSelection = binding.descriptionEd.selectionEnd
        // Remove existing spans
        val existingUnderlineSpans =
            spannable.getSpans(0, spannable.length, UnderlineSpan::class.java)
        for (span in existingUnderlineSpans) {
            spannable.removeSpan(span)
        }
        // Remove existing background color spans
        val existingBackgroundColorSpans =
            spannable.getSpans(0, spannable.length, BackgroundColorSpan::class.java)
        for (span in existingBackgroundColorSpans) {
            spannable.removeSpan(span)
        }
        if (b) {
            // Apply underline to selected text
            if (startSelection != endSelection) {
                    // Get the selected text
                selectedTextForUnderLine = spannable.subSequence(startSelection, endSelection).toString()
                    // Apply underline to the selected text in the SpannableString
                    spannable.setSpan(
                        UnderlineSpan(),
                        startSelection,
                        endSelection,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

            } else {
                // Check for copied text and apply underline
                val clipboardManager =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = clipboardManager.primaryClip
                if (clipData != null && clipData.itemCount > 0) {
                    val copiedText = clipData.getItemAt(0).text.toString()
                    selectedTextForUnderLine = copiedText
                    val editTextText = binding.descriptionEd.text.toString()
                    if (editTextText.contains(copiedText)) {
                        val startIndex = editTextText.indexOf(copiedText)
                        val endIndex = startIndex + copiedText.length
                        spannable.setSpan(
                            UnderlineSpan(),
                            startIndex,
                            endIndex,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
        } else {
            // Set background color for selected text
            if (startSelection != endSelection) {
                spannable.setSpan(
                    BackgroundColorSpan(Color.TRANSPARENT),
                    startSelection,
                    endSelection,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else {
                // Check for copied text and set background color
                val clipboardManager =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = clipboardManager.primaryClip
                if (clipData != null && clipData.itemCount > 0) {
                    val copiedText = clipData.getItemAt(0).text.toString()
                    val editTextText = binding.descriptionEd.text.toString()
                    if (editTextText.contains(copiedText)) {
                        val startIndex = editTextText.indexOf(copiedText)
                        val endIndex = startIndex + copiedText.length
                        spannable.setSpan(
                            BackgroundColorSpan(Color.TRANSPARENT),
                            startIndex,
                            endIndex,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
        }
        // Set other properties based on state
        val drawable = underLineImg.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                if (b) R.color.white else R.color.black
            ), PorterDuff.Mode.SRC_IN
        )
        underLineImg.setImageDrawable(drawable)
        underLineBtn.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                if (b) R.color.pinkButtonColor else R.color.white
            )
        )

        // Apply the modified text
        binding.descriptionEd.text = spannable
    }

    private fun highLightSelection(b: Boolean, btn: CardView, img: ImageView) {
        when (b) {
            true -> {
                val spannable = binding.descriptionEd.text as Spannable
                val drawable = img.drawable
                drawable.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.white),
                    PorterDuff.Mode.SRC_IN
                )
                img.setImageDrawable(drawable)
                btn.setCardBackgroundColor(resources.getColor(R.color.pinkButtonColor))
                val startSelection = binding.descriptionEd.selectionStart
                val endSelection = binding.descriptionEd.selectionEnd
                if (startSelection != endSelection) {
                    selectedTextForHighLighting =
                        spannable.subSequence(startSelection, endSelection).toString()
                    spannable.setSpan(
                        android.text.style.BackgroundColorSpan(Color.YELLOW),
                        startSelection,
                        endSelection,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else {
                    val clipboardManager =
                        requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    // Get the text from the clipboard
                    val clipData = clipboardManager.primaryClip
                    if (clipData != null && clipData.itemCount > 0) {
                        val copiedText = clipData.getItemAt(0).text.toString()
                        selectedTextForHighLighting = copiedText
                        // Check if the copied text is present in the EditText
                        val editTextText = binding.descriptionEd.text.toString()
                        if (editTextText.contains(copiedText)) {
                            // Apply color to the copied line
                            val startIndex = editTextText.indexOf(copiedText)
                            val endIndex = startIndex + copiedText.length

                            val spannable = binding.descriptionEd.text as Spannable
                            spannable.setSpan(
                                android.text.style.BackgroundColorSpan(Color.YELLOW),
                                startIndex,
                                endIndex,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    }
                }
            }

            false -> {
                img.setImageResource(R.drawable.highlighte_iconer)
                btn.setCardBackgroundColor(resources.getColor(R.color.white))
                val startSelection = binding.descriptionEd.selectionStart
                val endSelection = binding.descriptionEd.selectionEnd
                if (startSelection != endSelection) {
                    val spannable = binding.descriptionEd.text as Spannable
                    spannable.setSpan(
                        android.text.style.BackgroundColorSpan(Color.TRANSPARENT),
                        startSelection,
                        endSelection,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else {
                    val clipboardManager =
                        requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    // Get the text from the clipboard
                    val clipData = clipboardManager.primaryClip
                    if (clipData != null && clipData.itemCount > 0) {
                        val copiedText = clipData.getItemAt(0).text.toString()
                        // Check if the copied text is present in the EditText
                        val editTextText = binding.descriptionEd.text.toString()
                        if (editTextText.contains(copiedText)) {
                            // Apply color to the copied line
                            val startIndex = editTextText.indexOf(copiedText)
                            val endIndex = startIndex + copiedText.length
                            val spannable = binding.descriptionEd.text as Spannable
                            spannable.setSpan(
                                android.text.style.BackgroundColorSpan(Color.TRANSPARENT),
                                startIndex,
                                endIndex,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    }
                }
            }
        }
    }
    private fun getPreviousLineText(text: CharSequence): String {
        val cursorPosition = binding.descriptionEd.selectionStart
        val lineStart = text.lastIndexOf('\n', cursorPosition - 2) + 1
        val lineEnd = cursorPosition - 1

        return if (lineStart in 0 until lineEnd) {
            text.subSequence(lineStart, lineEnd).toString()
        } else {
            ""
        }
    }
    private fun setupFontEight(
        fontOneBtn: CardView,
        fontTwoBtn: CardView,
        fontThreeBtn: CardView,
        fontFourBtn: CardView,
        fontFiveBtn: CardView,
        fontSixBtn: CardView,
        fontSevenBtn: CardView,
        fontEightBtn: CardView,
        fontFamilyImgOne: ImageView,
        fontFamilyImgTwo: ImageView,
        fontFamilyImgThree: ImageView,
        fontFamilyImgFour: ImageView,
        fontFamilyImgFive: ImageView,
        fontFamilyImgSix: ImageView,
        fontFamilyImgSeven: ImageView,
        fontFamilyImgEight: ImageView
    ) {
        //one
        val drawable = fontFamilyImgOne.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgOne.setImageDrawable(drawable)
        //two
        val drawa = fontFamilyImgTwo.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgTwo.setImageDrawable(drawa)
        //three
        val drawble = fontFamilyImgThree.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgThree.setImageDrawable(drawble)
        //four
        val drawbl = fontFamilyImgFour.drawable
        drawbl.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFour.setImageDrawable(drawbl)
        //five
        val drawb = fontFamilyImgFive.drawable
        drawb.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFive.setImageDrawable(drawb)
        //six
        val rawble = fontFamilyImgSix.drawable
        rawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSix.setImageDrawable(rawble)
        //seven
        val drawle = fontFamilyImgSeven.drawable
        drawle.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSeven.setImageDrawable(drawle)
        //eight
        val drae = fontFamilyImgEight.drawable
        drae.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgEight.setImageDrawable(drae)
        fontOneBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontTwoBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontThreeBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFourBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFiveBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontSixBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontSevenBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontEightBtn.setCardBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        fontFamily = "baloo_bhai_2_bold.ttf"
        allerTypeface = Typeface.createFromAsset(requireActivity().assets, "baloo_bhai_2_bold.ttf")
        setupCustomFonts(allerTypeface, listOfTextView, listOfEditTexts)
    }

    private fun setupFontSeven(
        fontOneBtn: CardView,
        fontTwoBtn: CardView,
        fontThreeBtn: CardView,
        fontFourBtn: CardView,
        fontFiveBtn: CardView,
        fontSixBtn: CardView,
        fontSevenBtn: CardView,
        fontEightBtn: CardView,
        fontFamilyImgOne: ImageView,
        fontFamilyImgTwo: ImageView,
        fontFamilyImgThree: ImageView,
        fontFamilyImgFour: ImageView,
        fontFamilyImgFive: ImageView,
        fontFamilyImgSix: ImageView,
        fontFamilyImgSeven: ImageView,
        fontFamilyImgEight: ImageView
    ) {
        //one
        val drawable = fontFamilyImgOne.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgOne.setImageDrawable(drawable)
        //two
        val drawa = fontFamilyImgTwo.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgTwo.setImageDrawable(drawa)
        //three
        val drawble = fontFamilyImgThree.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgThree.setImageDrawable(drawble)
        //four
        val drawbl = fontFamilyImgFour.drawable
        drawbl.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFour.setImageDrawable(drawbl)
        //five
        val drawb = fontFamilyImgFive.drawable
        drawb.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFive.setImageDrawable(drawb)
        //six
        val rawble = fontFamilyImgSix.drawable
        rawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSix.setImageDrawable(rawble)
        //seven
        val drawle = fontFamilyImgSeven.drawable
        drawle.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSeven.setImageDrawable(drawle)
        //eight
        val drae = fontFamilyImgEight.drawable
        drae.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgEight.setImageDrawable(drae)
        fontOneBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontTwoBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontThreeBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFourBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFiveBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontSixBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontSevenBtn.setCardBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        fontEightBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFamily = "trade_winds.ttf"
        allerTypeface = Typeface.createFromAsset(requireActivity().assets, "trade_winds.ttf")
        setupCustomFonts(allerTypeface, listOfTextView, listOfEditTexts)
    }

    private fun setupFontSix(
        fontOneBtn: CardView,
        fontTwoBtn: CardView,
        fontThreeBtn: CardView,
        fontFourBtn: CardView,
        fontFiveBtn: CardView,
        fontSixBtn: CardView,
        fontSevenBtn: CardView,
        fontEightBtn: CardView,
        fontFamilyImgOne: ImageView,
        fontFamilyImgTwo: ImageView,
        fontFamilyImgThree: ImageView,
        fontFamilyImgFour: ImageView,
        fontFamilyImgFive: ImageView,
        fontFamilyImgSix: ImageView,
        fontFamilyImgSeven: ImageView,
        fontFamilyImgEight: ImageView
    ) {
        //one
        val drawable = fontFamilyImgOne.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgOne.setImageDrawable(drawable)
        //two
        val drawa = fontFamilyImgTwo.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgTwo.setImageDrawable(drawa)
        //three
        val drawble = fontFamilyImgThree.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgThree.setImageDrawable(drawble)
        //four
        val drawbl = fontFamilyImgFour.drawable
        drawbl.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFour.setImageDrawable(drawbl)
        //five
        val drawb = fontFamilyImgFive.drawable
        drawb.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFive.setImageDrawable(drawb)
        //six
        val rawble = fontFamilyImgSix.drawable
        rawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSix.setImageDrawable(rawble)
        //seven
        val drawle = fontFamilyImgSeven.drawable
        drawle.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSeven.setImageDrawable(drawle)
        //eight
        val drae = fontFamilyImgEight.drawable
        drae.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgEight.setImageDrawable(drae)
        fontOneBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontTwoBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontThreeBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFourBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFiveBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontSixBtn.setCardBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        fontSevenBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontEightBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        Static.fontFamily = "zeyada.ttf"
        allerTypeface = Typeface.createFromAsset(requireActivity().assets, "zeyada.ttf")
        setupCustomFonts(allerTypeface, listOfTextView, listOfEditTexts)
    }

    private fun setupFontFive(
        fontOneBtn: CardView,
        fontTwoBtn: CardView,
        fontThreeBtn: CardView,
        fontFourBtn: CardView,
        fontFiveBtn: CardView,
        fontSixBtn: CardView,
        fontSevenBtn: CardView,
        fontEightBtn: CardView,
        fontFamilyImgOne: ImageView,
        fontFamilyImgTwo: ImageView,
        fontFamilyImgThree: ImageView,
        fontFamilyImgFour: ImageView,
        fontFamilyImgFive: ImageView,
        fontFamilyImgSix: ImageView,
        fontFamilyImgSeven: ImageView,
        fontFamilyImgEight: ImageView
    ) {
        //one
        val drawable = fontFamilyImgOne.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgOne.setImageDrawable(drawable)
        //two
        val drawa = fontFamilyImgTwo.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgTwo.setImageDrawable(drawa)
        //three
        val drawble = fontFamilyImgThree.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgThree.setImageDrawable(drawble)
        //four
        val drawbl = fontFamilyImgFour.drawable
        drawbl.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFour.setImageDrawable(drawbl)
        //five
        val drawb = fontFamilyImgFive.drawable
        drawb.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFive.setImageDrawable(drawb)
        //six
        val rawble = fontFamilyImgSix.drawable
        rawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSix.setImageDrawable(rawble)
        //seven
        val drawle = fontFamilyImgSeven.drawable
        drawle.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSeven.setImageDrawable(drawle)
        //eight
        val drae = fontFamilyImgEight.drawable
        drae.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgEight.setImageDrawable(drae)
        fontOneBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontTwoBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontThreeBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFourBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFiveBtn.setCardBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        fontSixBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontSevenBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontEightBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFamily = "playfair_display_sc.ttf"
        allerTypeface =
            Typeface.createFromAsset(requireActivity().assets, "playfair_display_sc.ttf")
        setupCustomFonts(allerTypeface, listOfTextView, listOfEditTexts)
    }

    private fun setupFontFour(
        fontOneBtn: CardView,
        fontTwoBtn: CardView,
        fontThreeBtn: CardView,
        fontFourBtn: CardView,
        fontFiveBtn: CardView,
        fontSixBtn: CardView,
        fontSevenBtn: CardView,
        fontEightBtn: CardView,
        fontFamilyImgOne: ImageView,
        fontFamilyImgTwo: ImageView,
        fontFamilyImgThree: ImageView,
        fontFamilyImgFour: ImageView,
        fontFamilyImgFive: ImageView,
        fontFamilyImgSix: ImageView,
        fontFamilyImgSeven: ImageView,
        fontFamilyImgEight: ImageView
    ) {
        //one
        val drawable = fontFamilyImgOne.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgOne.setImageDrawable(drawable)
        //two
        val drawa = fontFamilyImgTwo.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgTwo.setImageDrawable(drawa)
        //three
        val drawble = fontFamilyImgThree.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgThree.setImageDrawable(drawble)
        //four
        val drawbl = fontFamilyImgFour.drawable
        drawbl.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFour.setImageDrawable(drawbl)
        //five
        val drawb = fontFamilyImgFive.drawable
        drawb.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFive.setImageDrawable(drawb)
        //six
        val rawble = fontFamilyImgSix.drawable
        rawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSix.setImageDrawable(rawble)
        //seven
        val drawle = fontFamilyImgSeven.drawable
        drawle.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSeven.setImageDrawable(drawle)
        //eight
        val drae = fontFamilyImgEight.drawable
        drae.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgEight.setImageDrawable(drae)
        fontOneBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontTwoBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontThreeBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFourBtn.setCardBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        fontFiveBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontSixBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontSevenBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontEightBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFamily = "timmana.ttf"
        allerTypeface = Typeface.createFromAsset(requireActivity().assets, "timmana.ttf")
        setupCustomFonts(allerTypeface, listOfTextView, listOfEditTexts)
    }

    private fun setupFontThree(
        fontOneBtn: CardView,
        fontTwoBtn: CardView,
        fontThreeBtn: CardView,
        fontFourBtn: CardView,
        fontFiveBtn: CardView,
        fontSixBtn: CardView,
        fontSevenBtn: CardView,
        fontEightBtn: CardView,
        fontFamilyImgOne: ImageView,
        fontFamilyImgTwo: ImageView,
        fontFamilyImgThree: ImageView,
        fontFamilyImgFour: ImageView,
        fontFamilyImgFive: ImageView,
        fontFamilyImgSix: ImageView,
        fontFamilyImgSeven: ImageView,
        fontFamilyImgEight: ImageView
    ) {
        //one
        val drawable = fontFamilyImgOne.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgOne.setImageDrawable(drawable)
        //two
        val drawa = fontFamilyImgTwo.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgTwo.setImageDrawable(drawa)
        //three
        val drawble = fontFamilyImgThree.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgThree.setImageDrawable(drawble)
        //four
        val drawbl = fontFamilyImgFour.drawable
        drawbl.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFour.setImageDrawable(drawbl)
        //five
        val drawb = fontFamilyImgFive.drawable
        drawb.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFive.setImageDrawable(drawb)
        //six
        val rawble = fontFamilyImgSix.drawable
        rawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSix.setImageDrawable(rawble)
        //seven
        val drawle = fontFamilyImgSeven.drawable
        drawle.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSeven.setImageDrawable(drawle)
        //eight
        val drae = fontFamilyImgEight.drawable
        drae.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgEight.setImageDrawable(drae)
        fontOneBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontTwoBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontThreeBtn.setCardBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        fontFourBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFiveBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontSixBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontSevenBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontEightBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFamily = "shrikhand.ttf"
        allerTypeface = Typeface.createFromAsset(requireActivity().assets, "shrikhand.ttf")
        setupCustomFonts(allerTypeface, listOfTextView, listOfEditTexts)
    }

    private fun setupFontTwo(
        fontOneBtn: CardView,
        fontTwoBtn: CardView,
        fontThreeBtn: CardView,
        fontFourBtn: CardView,
        fontFiveBtn: CardView,
        fontSixBtn: CardView,
        fontSevenBtn: CardView,
        fontEightBtn: CardView,
        fontFamilyImgOne: ImageView,
        fontFamilyImgTwo: ImageView,
        fontFamilyImgThree: ImageView,
        fontFamilyImgFour: ImageView,
        fontFamilyImgFive: ImageView,
        fontFamilyImgSix: ImageView,
        fontFamilyImgSeven: ImageView,
        fontFamilyImgEight: ImageView
    ) {
        //one
        val drawable = fontFamilyImgOne.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgOne.setImageDrawable(drawable)
        //two
        val drawa = fontFamilyImgTwo.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgTwo.setImageDrawable(drawa)
        //three
        val drawble = fontFamilyImgThree.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgThree.setImageDrawable(drawble)
        //four
        val drawbl = fontFamilyImgFour.drawable
        drawbl.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFour.setImageDrawable(drawbl)
        //five
        val drawb = fontFamilyImgFive.drawable
        drawb.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFive.setImageDrawable(drawb)
        //six
        val rawble = fontFamilyImgSix.drawable
        rawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSix.setImageDrawable(rawble)
        //seven
        val drawle = fontFamilyImgSeven.drawable
        drawle.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSeven.setImageDrawable(drawle)
        //eight
        val drae = fontFamilyImgEight.drawable
        drae.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgEight.setImageDrawable(drae)
        fontOneBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontTwoBtn.setCardBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        fontThreeBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFourBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFiveBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontSixBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontSevenBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontEightBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFamily = "satisfy.ttf"
        allerTypeface = Typeface.createFromAsset(requireActivity().assets, "satisfy.ttf")
        setupCustomFonts(allerTypeface, listOfTextView, listOfEditTexts)
    }

    private fun setupFontOne(
        fontOneBtn: CardView,
        fontTwoBtn: CardView,
        fontThreeBtn: CardView,
        fontFourBtn: CardView,
        fontFiveBtn: CardView,
        fontSixBtn: CardView,
        fontSevenBtn: CardView,
        fontEightBtn: CardView,
        fontFamilyImgOne: ImageView,
        fontFamilyImgTwo: ImageView,
        fontFamilyImgThree: ImageView,
        fontFamilyImgFour: ImageView,
        fontFamilyImgFive: ImageView,
        fontFamilyImgSix: ImageView,
        fontFamilyImgSeven: ImageView,
        fontFamilyImgEight: ImageView
    ) {
        //one
        val drawable = fontFamilyImgOne.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgOne.setImageDrawable(drawable)
        //two
        val drawa = fontFamilyImgTwo.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgTwo.setImageDrawable(drawa)
        //three
        val drawble = fontFamilyImgThree.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgThree.setImageDrawable(drawble)
        //four
        val drawbl = fontFamilyImgFour.drawable
        drawbl.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFour.setImageDrawable(drawbl)
        //five
        val drawb = fontFamilyImgFive.drawable
        drawb.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgFive.setImageDrawable(drawb)
        //six
        val rawble = fontFamilyImgSix.drawable
        rawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSix.setImageDrawable(rawble)
        //seven
        val drawle = fontFamilyImgSeven.drawable
        drawle.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgSeven.setImageDrawable(drawle)
        //eight
        val drae = fontFamilyImgEight.drawable
        drae.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        fontFamilyImgEight.setImageDrawable(drae)
        fontOneBtn.setCardBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        fontTwoBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontThreeBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFourBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFiveBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontSixBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontSevenBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontEightBtn.setCardBackgroundColor(resources.getColor(R.color.white))
        fontFamily = "roboto.ttf"
        allerTypeface = Typeface.createFromAsset(requireActivity().assets, "roboto.ttf")
        setupCustomFonts(allerTypeface, listOfTextView, listOfEditTexts)
    }

    private fun allCapitalLettersSetup(
        allNormalBtn: LinearLayout, allCapitalBtn: LinearLayout,
        allSmallBtn: LinearLayout, allSamImg: ImageView, normalCapImg: ImageView,
        allCapImg: ImageView
    ) {
        val drawable = normalCapImg.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        normalCapImg.setImageDrawable(drawable)
        val drawa = allCapImg.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        allCapImg.setImageDrawable(drawa)
        val drawble = allSamImg.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        allSamImg.setImageDrawable(drawble)
        allCapitalBtn.setBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        allSmallBtn.setBackgroundColor(resources.getColor(R.color.white))
        allNormalBtn.setBackgroundColor(resources.getColor(R.color.white))
    }

    private fun allSmallLettersSetup(
        allNormalBtn: LinearLayout, allCapitalBtn: LinearLayout,
        allSmallBtn: LinearLayout, allSamImg: ImageView, normalCapImg: ImageView,
        allCapImg: ImageView
    ) {
        val drawable = normalCapImg.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        normalCapImg.setImageDrawable(drawable)
        val drawa = allCapImg.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        allCapImg.setImageDrawable(drawa)
        val drawble = allSamImg.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        allSamImg.setImageDrawable(drawble)
        allCapitalBtn.setBackgroundColor(resources.getColor(R.color.white))
        allSmallBtn.setBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        allNormalBtn.setBackgroundColor(resources.getColor(R.color.white))
    }

    private fun allNormalLettersSetup(
        allNormalBtn: LinearLayout, allCapitalBtn: LinearLayout,
        allSmallBtn: LinearLayout, allSamImg: ImageView, normalCapImg: ImageView,
        allCapImg: ImageView
    ) {
        val drawable = normalCapImg.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        normalCapImg.setImageDrawable(drawable)
        val drawa = allCapImg.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        allCapImg.setImageDrawable(drawa)
        val drawble = allSamImg.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        allSamImg.setImageDrawable(drawble)
        allCapitalBtn.setBackgroundColor(resources.getColor(R.color.white))
        allSmallBtn.setBackgroundColor(resources.getColor(R.color.white))
        allNormalBtn.setBackgroundColor(resources.getColor(R.color.pinkButtonColor))
    }

    private fun textSizeNormalSetup(
        normalTextSizeBtn: LinearLayout, mediumTextSizeBtn: LinearLayout,
        largeTextSizeBtn: LinearLayout, normalTextSizeImg: ImageView, mediumTextSizeImg: ImageView,
        largeTextSizeImg: ImageView
    ) {
        val drawable = normalTextSizeImg.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        normalTextSizeImg.setImageDrawable(drawable)
        val drawa = mediumTextSizeImg.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        mediumTextSizeImg.setImageDrawable(drawa)
        val drawble = largeTextSizeImg.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        largeTextSizeImg.setImageDrawable(drawble)
        mediumTextSizeBtn.setBackgroundColor(resources.getColor(R.color.white))
        largeTextSizeBtn.setBackgroundColor(resources.getColor(R.color.white))
        normalTextSizeBtn.setBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        textSize = 12f
    }

    private fun textSizeMediumSetup(
        normalTextSizeBtn: LinearLayout, mediumTextSizeBtn: LinearLayout,
        largeTextSizeBtn: LinearLayout, normalTextSizeImg: ImageView, mediumTextSizeImg: ImageView,
        largeTextSizeImg: ImageView
    ) {
        val drawable = normalTextSizeImg.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        normalTextSizeImg.setImageDrawable(drawable)
        val drawa = mediumTextSizeImg.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        mediumTextSizeImg.setImageDrawable(drawa)
        val drawble = largeTextSizeImg.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        largeTextSizeImg.setImageDrawable(drawble)
        mediumTextSizeBtn.setBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        largeTextSizeBtn.setBackgroundColor(resources.getColor(R.color.white))
        normalTextSizeBtn.setBackgroundColor(resources.getColor(R.color.white))
        textSize = 16f
    }

    private fun textSizeLargeSetup(
        normalTextSizeBtn: LinearLayout, mediumTextSizeBtn: LinearLayout,
        largeTextSizeBtn: LinearLayout, normalTextSizeImg: ImageView, mediumTextSizeImg: ImageView,
        largeTextSizeImg: ImageView
    ) {
        val drawable = normalTextSizeImg.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        normalTextSizeImg.setImageDrawable(drawable)
        val drawa = mediumTextSizeImg.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN)
        mediumTextSizeImg.setImageDrawable(drawa)
        val drawble = largeTextSizeImg.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN)
        largeTextSizeImg.setImageDrawable(drawble)
        mediumTextSizeBtn.setBackgroundColor(resources.getColor(R.color.white))
        largeTextSizeBtn.setBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        normalTextSizeBtn.setBackgroundColor(resources.getColor(R.color.white))
        textSize = 20f
    }
    private fun gravitySetupCenter(
        gravityCenterBtn: LinearLayout, gravityStartBtn: LinearLayout,
        gravityEndBtn: LinearLayout, gravityCenterImg: ImageView, gravityStartImg: ImageView,
        gravityEndImg: ImageView
    ) {
        val drawable = gravityCenterImg.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        gravityCenterImg.setImageDrawable(drawable)
        val drawa = gravityStartImg.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        gravityStartImg.setImageDrawable(drawa)
        val drawble = gravityEndImg.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN)
        gravityEndImg.setImageDrawable(drawble)
        gravityCenterBtn.setBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        gravityEndBtn.setBackgroundColor(resources.getColor(R.color.white))
        gravityStartBtn.setBackgroundColor(resources.getColor(R.color.white))
        setGravityOnView(Gravity.CENTER)
    }
    private fun gravitySetupStart(
        gravityCenterBtn: LinearLayout, gravityStartBtn: LinearLayout,
        gravityEndBtn: LinearLayout, gravityCenterImg: ImageView, gravityStartImg: ImageView,
        gravityEndImg: ImageView
    ) {
        val drawable = gravityCenterImg.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        gravityCenterImg.setImageDrawable(drawable)
        val drawa = gravityStartImg.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        gravityStartImg.setImageDrawable(drawa)
        val drawble = gravityEndImg.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN)
        gravityEndImg.setImageDrawable(drawble)
        gravityCenterBtn.setBackgroundColor(resources.getColor(R.color.white))
        gravityEndBtn.setBackgroundColor(resources.getColor(R.color.white))
        gravityStartBtn.setBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        gravity = Gravity.START
        setGravityOnView(Gravity.START)
    }
    private fun gravitySetupEnd(
        gravityCenterBtn: LinearLayout, gravityStartBtn: LinearLayout,
        gravityEndBtn: LinearLayout, gravityCenterImg: ImageView, gravityStartImg: ImageView,
        gravityEndImg: ImageView
    ) {
        val drawable = gravityCenterImg.drawable
        drawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        gravityCenterImg.setImageDrawable(drawable)
        val drawa = gravityStartImg.drawable
        drawa.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.black),
            PorterDuff.Mode.SRC_IN
        )
        gravityStartImg.setImageDrawable(drawa)
        val drawble = gravityEndImg.drawable
        drawble.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        gravityEndImg.setImageDrawable(drawble)
        gravityCenterBtn.setBackgroundColor(resources.getColor(R.color.white))
        gravityEndBtn.setBackgroundColor(resources.getColor(R.color.pinkButtonColor))
        gravityStartBtn.setBackgroundColor(resources.getColor(R.color.white))
        gravity = Gravity.END
        setGravityOnView(Gravity.END)
    }

    private fun setGravityOnView(gravity: Int) {
        binding.titleEd.gravity = gravity
        binding.descriptionEd.gravity = gravity
        binding.ImageViewLayout.gravity=gravity
        binding.hashTagLayout.gravity = gravity
    }

    private fun voiceRecordingBottomSheet() {
        // Permissions are granted, proceed with opening the bottom sheet
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.voice_recording_picking_bottom_sheet, null)
        val cancelBtn = view.findViewById<CardView>(R.id.cancelBtn)
        val saveBtn = view.findViewById<CardView>(R.id.saveBtn)
        val voicePickBtn = view.findViewById<ImageView>(R.id.pickAudioBtn)
        val recordingBtn = view.findViewById<ImageView>(R.id.recordVoiceBtn)
        val recordingLayout = view.findViewById<ConstraintLayout>(R.id.recordingLayout)
        val stopRecordingBtn = view.findViewById<CardView>(R.id.recordingStopBtn)
        val stopRecordingImg = view.findViewById<ImageView>(R.id.recordingStopImg)
        durationTextView = view.findViewById<TextView>(R.id.countingVoiceDurationTv)
        val recordingTextView = view.findViewById<TextView>(R.id.recordingText)
        var title=view.findViewById<TextView>(R.id.textView)
        val pickFileTextView = view.findViewById<TextView>(R.id.pickAudioText)
        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                updateDuration(durationTextView)
                handler.postDelayed(this, 1000) // Update every 1 second
            }
        }
        voicePickBtn.setOnClickListener {
            openAudioPicker()
        }
        cancelBtn.setOnClickListener {
            // Handle the cancel button click
            dialog.dismiss() // Dismiss the dialog when the cancel button is clicked
        }
        recordingBtn.setOnClickListener {
            if (audioFilePath != null) {
                // Start recording
                startRecordingViewModel(audioFilePath!!)
                saveBtn.visibility=View.GONE
                cancelBtn.visibility=View.GONE
                voicePickBtn.visibility=View.GONE
                recordingBtn.visibility=View.GONE
                title.visibility=View.GONE
                recordingTextView.visibility=View.GONE
                pickFileTextView.visibility=View.GONE
                recordingLayout.visibility=View.VISIBLE

            }
        }
        stopRecordingBtn.setOnClickListener {
            stopRecordingViewModel()
            saveBtn.visibility=View.VISIBLE
            cancelBtn.visibility=View.VISIBLE
            voicePickBtn.visibility=View.VISIBLE
            recordingBtn.visibility=View.VISIBLE
            title.visibility=View.VISIBLE
            recordingTextView.visibility=View.VISIBLE
            pickFileTextView.visibility=View.VISIBLE
            recordingLayout.visibility=View.GONE
        }

        saveBtn.setOnClickListener {
            if (formattedDuration?.isNotEmpty() == true) {
                binding.voiceDuration.text = formattedDuration
                durationOfVoice = formattedDuration
            }
            binding.playPauseBtn.setImageResource(R.drawable.next_arrow)
            binding.voiceLayout.visibility = View.VISIBLE
            // Handle the save button click
            dialog.dismiss()
        }


        cancelBtn.setOnClickListener {
            checkRecording = false
            voice = null
            dialog.dismiss()
        }

        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.setOnCancelListener {
            // Handle the cancel listener if needed
        }
        dialog.show()


    }

    private fun observeRecordingState() {
        voiceRecordingViewModel.recordingState.observe(viewLifecycleOwner, { state ->
            when (state) {
                is VoiceRecorderHelper.Recording -> {
                    // Handle recording started
                    requireContext().shortToast("Recording started")
                }

                is VoiceRecorderHelper.Done -> {
                    val recordingModel = state.data
                    formattedDuration = recordingModel.formattedDuration
                    voice = recordingModel.filePath
                    // Handle recording completed
                    requireContext().shortToast("Recording completed: $recordingModel")

                    // Update UI or perform any other actions as needed
                    binding.voiceDuration.text = formattedDuration
                }

                is VoiceRecorderHelper.Error -> {
                    // Handle recording error
                    requireContext().shortToast("Error: ${state.message}")
                }

                is VoiceRecorderHelper.Loading -> {
                    // Handle loading state if needed
                }

                else -> {

                }
            }
        })
    }
    private fun updateDuration(durationTextView:TextView) {
        seconds++
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60

        val durationString = String.format("%02d:%02d", minutes, remainingSeconds)
        durationTextView.text = durationString
    }
    private fun startRecordingViewModel(filePath: String) {
        voiceRecordingViewModel.startRecording(filePath)
        seconds = 0
        handler.postDelayed(runnable, 1000)
    }
    private fun stopRecordingViewModel() {
        handler.removeCallbacks(runnable)
        voiceRecordingViewModel.stopRecording()
    }

    private fun galleryImagePickerBottomSheet() {
        // Check for permissions before opening the bottom sheet
        // Permissions are granted, proceed with opening the bottom sheet
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.images_bottom_sheet, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.galleryImagesRecyclerView)
        val cancelBtn = view.findViewById<CardView>(R.id.cancelBtn)
        val saveBtn = view.findViewById<CardView>(R.id.saveBtn)
        val imagesList = getAllImages()
        galleryImagesList.addAll(imagesList)
        mGalleryImagesPickerAdapter.notifyDataSetChanged()
        mGalleryImagesPickerAdapter.recyclerClick(object :
            GalleryImagePickerRecAdapter.PassRateData {
            override fun clickFunction(modelClass: ImageItem, position: Int) {
                // Handle item click
                if (position == 0) {
                    captureImage()
                } else {
                    if (listOfSelectedImages.contains(modelClass.uri.toString())){
                        listOfSelectedImages.remove(modelClass.uri.toString())
                    }else{
                        listOfSelectedImages.add(modelClass.uri.toString())
                    }
                    val selectedItems = mGalleryImagesPickerAdapter.getSelectedItems()
                    val isSelected = modelClass.isSelected
                    if (selectedItems.size < 5) {
                        // If less than 5 items are selected, proceed


                    } else {
                        // If 5 items are already selected, show a toast
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.maxImageSize),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        })
        recyclerView.adapter = mGalleryImagesPickerAdapter
        cancelBtn.setOnClickListener {
            // Handle the cancel button click
            dialog.dismiss() // Dismiss the dialog when the cancel button is clicked
        }
        saveBtn.setOnClickListener {
            if (listOfSelectedImages.size>4){
                requireContext().shortToast("Max Four Images Allowed")
            }else{
                if (listOfSelectedImages.isNotEmpty()) {
                    setUpImagesAccordingtoSelection()
                }
            }
            // Handle the save button click
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.setOnCancelListener {
            // Handle the cancel listener if needed
        }
        dialog.show()

    }

    private fun selectedImageViewClicks() {
        val bundle = Bundle()

    }

    private fun removeImagesAccordingToPositions() {

    }

    private fun setUpImagesAccordingtoSelection() {
        if (listOfSelectedImages.isNotEmpty()) {
            createNoteModelList.addAll(listOfSelectedImages)
            mCreateNoteImagesAdapter.notifyDataSetChanged()
        }
    }

    private fun removeImageAtIndex(index: Int) {
        if (MyConstants.listOfImageCounting.size > index) {
            listOfSelectedImages.removeAt(index)
//            MyConstants.listOfImageCounting.removeAt(index)
            updateImageViewsVisibility()
        }

    }

    private fun updateImageViewsVisibility() {
        // Update the visibility of ImageViews based on the size of selectedImageUris
        val visibilityArray = arrayOf(
            View.GONE,
            View.GONE,
            View.GONE,
            View.GONE
        )

        for (i in 0 until listOfSelectedImages.size) {
            visibilityArray[i] = View.VISIBLE
        }

    }

    private fun captureImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val context = requireContext()
            val contentResolver = context.contentResolver
            val hasCameraPermission =
                context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

            if (hasCameraPermission) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            }
        } else {
            if (checkCameraPermission()) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            }
        }

    }

    private fun checkCameraPermission(): Boolean {
        val context = requireContext()
        val hasCameraPermission =
            context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Check for both camera and microphone audio permissions on Android 13 and above
            val hasAudioPermission =
                context.checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
            hasCameraPermission && hasAudioPermission
        } else {
            // Check for camera permission on older Android versions
            hasCameraPermission
        }
    }

    // Define a constant for the permission request code
    private val PERMISSION_REQUEST_CODE = 123

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS_ANDROID_6
        ) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All permissions granted, proceed with the operation
                requireContext().shortToast("6 to 10 version")
                galleryImagePickerBottomSheet()
            } else {
                // Permissions not granted, handle accordingly
                Toast.makeText(context, "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == REQUEST_CODE_PERMISSIONS_ANDROID_11) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All permissions granted, proceed with the operation
                galleryImagePickerBottomSheet()
            } else {
                // Permissions not granted, handle accordingly
                Toast.makeText(context, "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == REQUEST_CODE_PERMISSIONS_ANDROID_13) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All permissions granted, proceed with the operation
                galleryImagePickerBottomSheet()
            } else {
                // Permissions not granted, handle accordingly
                Toast.makeText(context, "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }else if (requestCode==CAMERA_REQUEST_CODE){
            captureImage()
        }else if (requestCode==PICK_AUDIO_REQUEST_CODE){
            playPauseAudio()
        }
    }
    private fun openAudioPicker() {
        try {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*"
            startActivityForResult(intent,PICK_AUDIO_REQUEST_CODE)
        }catch (e:Exception){
            Log.d("Crash", "PICK_AUDIO_REQUEST_CODE: "+e.message.toString())
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if (requestCode == PICK_AUDIO_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
                val audioUri: Uri? = data.data
                if (audioUri != null) {
                    // Access the selected audio file using the URI
                    val audioContentResolver = context?.contentResolver
                    val audioInputStream = audioContentResolver?.openInputStream(audioUri)
                    if (audioInputStream != null) {
                        val audilFile = key?.let { createTempFile(audioInputStream, it) }
                        // Use MediaPlayer to get the audio duration
                        val mediaPlayer = MediaPlayer()
                        mediaPlayer.setDataSource(
                            audioContentResolver.openFileDescriptor(
                                audioUri,
                                "r"
                            )?.fileDescriptor
                        )
                        mediaPlayer.prepare()
                        val duration = mediaPlayer.duration
                        mediaPlayer.release()

                        // Convert duration to a human-readable format (hh:mm:ss)
                        formattedDuration = String.format(
                            "%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
                            TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) % TimeUnit.MINUTES.toSeconds(
                                1
                            )
                        )
                        if (audilFile != null) {
                            voice = audilFile.absolutePath
                            audio_File = audilFile
                        }
                        // Display the duration in a TextView
                    }
                }
            } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
                // Handle captured image from the camera
                val imageBitmap = data.extras?.get("data") as Bitmap?
                imageBitmap?.let { bitmapToString(it) }?.let { MyConstants.list.add(it) }
                if (imageBitmap != null) {
                    bitmapToUri(imageBitmap)?.let { MyConstants.listOfImageCounting.add(it) }
                }

                // Do something with the captured image (e.g., display it in an ImageView)
            }
        }catch (e:Exception){

        }

//            } else if (requestCode == REQUEST_CODE_PERMISSIONS_ANDROID_11 && resultCode == RESULT_OK) {
//                if (requestCode == PICK_AUDIO_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
//                    val audioUri: Uri? = data.data
//                    if (audioUri != null) {
//                        // Access the selected audio file using the URI
//                        val audioContentResolver = context?.contentResolver
//                        val audioInputStream = audioContentResolver?.openInputStream(audioUri)
//
//                        if (audioInputStream != null) {
//                            val audilFile= key?.let { createTempFile(audioInputStream, it) }
//                            // Use MediaPlayer to get the audio duration
//                            val mediaPlayer = MediaPlayer()
//                            mediaPlayer.setDataSource(audioContentResolver.openFileDescriptor(audioUri, "r")?.fileDescriptor)
//                            mediaPlayer.prepare()
//                            val duration = mediaPlayer.duration
//                            mediaPlayer.release()
//
//                            // Convert duration to a human-readable format (hh:mm:ss)
//                           val d  = String.format(
//                                "%02d:%02d",
//                                TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
//                                TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) % TimeUnit.MINUTES.toSeconds(1)
//                            )
//                            if (audilFile != null) {
//                                binding.voiceDuration.setText(d)
//                                Static.voice=audilFile.absolutePath
//                                audio_File= audilFile
//                            }
//                            // Display the duration in a TextView
//                        }
//                    }
//
//                } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
//                    // Handle captured image from the camera
//                    val imageBitmap = data.extras?.get("data") as Bitmap?
//                    Static.imageUri= imageBitmap?.let { bitmapToUri(it) }
//                    // Do something with the captured image (e.g., display it in an ImageView)
//                }
//
//                // Handle permissions granted for Android 11 and Android 12
//            } else if (requestCode == REQUEST_CODE_PERMISSIONS_ANDROID_6 && resultCode == RESULT_OK) {
//                if (requestCode == PICK_AUDIO_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
//                    val audioUri: Uri? = data.data
//                    if (audioUri != null) {
//                        requireContext().shortToast(audioUri.toString())
//                        // Access the selected audio file using the URI
//                        val audioContentResolver = context?.contentResolver
//                        val audioInputStream = audioContentResolver?.openInputStream(audioUri)
//
//                        if (audioInputStream != null) {
//                            val audioFile = key?.let { createTempFile(audioInputStream, it) }
//
//                            // Use MediaPlayer to get the audio duration asynchronously
//                            val mediaPlayer = MediaPlayer()
//                            mediaPlayer.setDataSource(audioContentResolver.openFileDescriptor(audioUri, "r")?.fileDescriptor)
//
//                            mediaPlayer.setOnPreparedListener { player ->
//                                val duration = player.duration
//
//                                // Convert duration to a human-readable format (hh:mm:ss)
//                                val d = String.format(
//                                    "%02d:%02d",
//                                    TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
//                                    TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) % TimeUnit.MINUTES.toSeconds(1)
//                                )
//
//                                // Update UI with the duration
//                                requireActivity().runOnUiThread {
//                                    binding.voiceDuration.text = d
//                                    Static.voice = audioFile?.absolutePath
//                                    audio_File = audioFile
//                                }
//                                mediaPlayer.release()
//                            }
//
//                            mediaPlayer.prepareAsync()
//                            // Display the duration in a TextView
//                        }
//                    }
//
//                } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
//                    // Handle captured image from the camera
//                    val imageBitmap = data.extras?.get("data") as Bitmap?
//                    Static.imageUri= imageBitmap?.let { bitmapToUri(it) }
//                    // Do something with the captured image (e.g., display it in an ImageView)
//                }
//
//                // Handle permissions granted for Android 6 and above
//            } else {
//                // Handle permissions denied
//            }
//        }catch (e:Exception){
//            requireContext().longToast(e.message.toString())
//            Log.d("Crash", "onActivityResult: "+e.message.toString())
//        }

    }
    fun bitmapToString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun createTempFile(inputStream: InputStream, fileName: String): File {
        val tempFile = File(requireContext().cacheDir, fileName)
        tempFile.deleteOnExit()
        try {
            FileOutputStream(tempFile).use { output ->
                inputStream.use { input ->
                    input.copyTo(output)
                }
            }
        } catch (e: IOException) {
            Log.d("Crash", "InputStream: "+e.message.toString())
        }
        return tempFile
    }
    fun bitmapToUri(bitmap: Bitmap): Uri? {
        val contentResolver: ContentResolver = requireContext().contentResolver
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        // Insert the bitmap into the MediaStore and get a content URI
        val uri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            try {
                val outputStream = contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                outputStream?.close()
                return uri
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }
    private fun emojiBottomSheet() {
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.emoji_bottom_sheet, null)
        val emojiBtn = view.findViewById<EmojiPickerView>(R.id.emojiPickerView)
        val cancelBtn = view.findViewById<CardView>(R.id.cancelBtn)
        val saveBtn = view.findViewById<CardView>(R.id.saveBtn)
        // Connect TabLayout and ViewPager2
        cancelBtn.setOnClickListener {
            // Handle the cancel button click
            dialog.dismiss() // Dismiss the dialog when the cancel button is clicked
        }
        emojiBtn.setOnEmojiPickedListener { emojiS->
            val description = binding.descriptionEd.text.toString()
            if (description.isEmpty()){
                binding.descriptionEd.setText(emojiS.emoji)
            }else{
                updated=description+emoji
                if (updated?.isEmpty()==true){
                    binding.descriptionEd.setText("")
                }else{
                    binding.descriptionEd.setText(updated)
                }
            }
            emoji=emojiS.emoji
        }
        saveBtn.setOnClickListener {
            dialog.dismiss()
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
    private fun getAllImages(): List<ImageItem> {
        val imageList = mutableListOf<ImageItem>()
        // Add your custom drawable image at the zero index
        val customDrawableResId = R.drawable.camera_open // Replace with your drawable resource ID
        val customDrawableUri = Uri.parse("android.resource://${requireContext().packageName}/$customDrawableResId")
        val customImageItem = ImageItem(customDrawableUri)
        imageList.add(customImageItem)

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val cursor: Cursor? = requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                imageList.add(ImageItem(contentUri))
            }
        }
        return imageList
    }
    private fun checkUserStatus() {
        val hasSeenBottomSheet = SharedPrefObj.getBoolean(requireContext(), MyConstants.KEY_HAS_SEEN_BOTTOM_SHEET, false)
        if (!hasSeenBottomSheet) {
            bottomSheetNoteViewStatus()
        }
    }
    private fun bottomSheetNoteViewStatus() {
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_noteview_status, null)
        val statusOne = view.findViewById<ImageView>(R.id.emojiOne)
        val statusTwo = view.findViewById<ImageView>(R.id.emojiTwo)
        val statusThree = view.findViewById<ImageView>(R.id.emojiThree)
        val statusFour = view.findViewById<ImageView>(R.id.emojiFour)
        val statusFive = view.findViewById<ImageView>(R.id.emojiFive)
        val statusSix = view.findViewById<ImageView>(R.id.emojiSix)
        val statusSeven = view.findViewById<ImageView>(R.id.emojiSeven)
        val statusEight = view.findViewById<ImageView>(R.id.emojiEight)
        val cancelBtn = view.findViewById<CardView>(R.id.cancelBtn)
        val saveBtn = view.findViewById<TextView>(R.id.saveBtn)
        // Connect TabLayout and ViewPager2
        saveBtn.setOnClickListener {
            status?.let { it1 -> binding.noteViewStatusImage.setImageResource(it1) }
            // Handle the cancel button click
            dialog.dismiss() // Dismiss the dialog when the cancel button is clicked
        }
        cancelBtn.setOnClickListener {
            // Handle the cancel button click
            dialog.dismiss() // Dismiss the dialog when the cancel button is clicked
            SharedPrefObj.saveBoolean(requireContext(), MyConstants.KEY_HAS_SEEN_BOTTOM_SHEET, true)
        }
        statusOne.setOnClickListener {
            statusOne.setBackgroundResource(R.drawable.circle_shape_pink)
            statusTwo.setBackgroundResource(android.R.color.transparent)
            statusThree.setBackgroundResource(android.R.color.transparent)
            statusFour.setBackgroundResource(android.R.color.transparent)
            statusFive.setBackgroundResource(android.R.color.transparent)
            statusSix.setBackgroundResource(android.R.color.transparent)
            statusSeven.setBackgroundResource(android.R.color.transparent)
            statusEight.setBackgroundResource(android.R.color.transparent)
            status=R.drawable.emoji
        }
        statusTwo.setOnClickListener {
            statusOne.setBackgroundResource(android.R.color.transparent)
            statusTwo.setBackgroundResource(R.drawable.circle_shape_pink)
            statusThree.setBackgroundResource(android.R.color.transparent)
            statusFour.setBackgroundResource(android.R.color.transparent)
            statusFive.setBackgroundResource(android.R.color.transparent)
            statusSix.setBackgroundResource(android.R.color.transparent)
            statusSeven.setBackgroundResource(android.R.color.transparent)
            statusEight.setBackgroundResource(android.R.color.transparent)
            status=R.drawable.emojitwo
        }
        statusThree.setOnClickListener {
            statusOne.setBackgroundResource(android.R.color.transparent)
            statusTwo.setBackgroundResource(android.R.color.transparent)
            statusThree.setBackgroundResource(R.drawable.circle_shape_pink)
            statusFour.setBackgroundResource(android.R.color.transparent)
            statusFive.setBackgroundResource(android.R.color.transparent)
            statusSix.setBackgroundResource(android.R.color.transparent)
            statusSeven.setBackgroundResource(android.R.color.transparent)
            statusEight.setBackgroundResource(android.R.color.transparent)
            status=R.drawable.emojithree
        }
        statusFour.setOnClickListener {
            statusOne.setBackgroundResource(android.R.color.transparent)
            statusTwo.setBackgroundResource(android.R.color.transparent)
            statusThree.setBackgroundResource(android.R.color.transparent)
            statusFour.setBackgroundResource(R.drawable.circle_shape_pink)
            statusFive.setBackgroundResource(android.R.color.transparent)
            statusSix.setBackgroundResource(android.R.color.transparent)
            statusSeven.setBackgroundResource(android.R.color.transparent)
            statusEight.setBackgroundResource(android.R.color.transparent)
            status=R.drawable.emojifour
        }
        statusFive.setOnClickListener {
            statusOne.setBackgroundResource(android.R.color.transparent)
            statusTwo.setBackgroundResource(android.R.color.transparent)
            statusThree.setBackgroundResource(android.R.color.transparent)
            statusFour.setBackgroundResource(android.R.color.transparent)
            statusFive.setBackgroundResource(R.drawable.circle_shape_pink)
            statusSix.setBackgroundResource(android.R.color.transparent)
            statusSeven.setBackgroundResource(android.R.color.transparent)
            statusEight.setBackgroundResource(android.R.color.transparent)
            status=R.drawable.emojifive
        }
        statusSix.setOnClickListener {
            statusOne.setBackgroundResource(android.R.color.transparent)
            statusTwo.setBackgroundResource(android.R.color.transparent)
            statusThree.setBackgroundResource(android.R.color.transparent)
            statusFour.setBackgroundResource(android.R.color.transparent)
            statusFive.setBackgroundResource(android.R.color.transparent)
            statusSix.setBackgroundResource(R.drawable.circle_shape_pink)
            statusSeven.setBackgroundResource(android.R.color.transparent)
            statusEight.setBackgroundResource(android.R.color.transparent)
            status=R.drawable.emojisix
        }
        statusSeven.setOnClickListener {
            statusOne.setBackgroundResource(android.R.color.transparent)
            statusTwo.setBackgroundResource(android.R.color.transparent)
            statusThree.setBackgroundResource(android.R.color.transparent)
            statusFour.setBackgroundResource(android.R.color.transparent)
            statusFive.setBackgroundResource(android.R.color.transparent)
            statusSix.setBackgroundResource(android.R.color.transparent)
            statusSeven.setBackgroundResource(R.drawable.circle_shape_pink)
            statusEight.setBackgroundResource(android.R.color.transparent)
            status=R.drawable.emojiseven
        }
        statusEight.setOnClickListener {
            statusOne.setBackgroundResource(android.R.color.transparent)
            statusTwo.setBackgroundResource(android.R.color.transparent)
            statusThree.setBackgroundResource(android.R.color.transparent)
            statusFour.setBackgroundResource(android.R.color.transparent)
            statusFive.setBackgroundResource(android.R.color.transparent)
            statusSix.setBackgroundResource(android.R.color.transparent)
            statusSeven.setBackgroundResource(android.R.color.transparent)
            statusEight.setBackgroundResource(R.drawable.circle_shape_pink)
            status=R.drawable.emojieight
        }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.setOnCancelListener {
            SharedPrefObj.saveBoolean(requireContext(),MyConstants.KEY_HAS_SEEN_BOTTOM_SHEET,true)
            // Handle the cancel listener if needed
        }
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}