package com.mk.diary.presentation.ui.boardpassing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.bumptech.glide.Glide
import com.mk.diary.adapters.recyclerview.LocalizationRecAdapter
import com.mk.diary.di.application.HiltApplication
import com.mk.diary.localization.ForLanguageSettingsClass
import com.mk.diary.localization.LangCountryModelClass
import com.mk.diary.localization.SharedPref
import com.mk.diary.presentation.ui.tabs.BottomNavActivity
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.appext.newScreen
import com.mk.diary.utils.companion.Static
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentSelectLanguageBinding


class SelectLanguageFragment : Fragment() {
    private val modelList :ArrayList<LangCountryModelClass> by lazy { ArrayList() }
    private val mAdapter by lazy { LocalizationRecAdapter(modelList) }
    lateinit var binding: FragmentSelectLanguageBinding
    private  val sp by lazy { SharedPref(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSelectLanguageBinding.inflate(layoutInflater,container,false)
        HiltApplication.showPasswordScreen = false
        if (Static.settLang=="SetLan"){
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                requireContext().newScreen(BottomNavActivity::class.java)
            }
            binding.backBtn.visibility=View.VISIBLE
        }else{
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
               if (SharedPrefObj.getEmail(requireContext())==null){
                   requireActivity().finishAffinity()
               }
            }
        }
        binding.backBtn.setOnClickListener {
            requireContext().newScreen(BottomNavActivity::class.java)
        }
        binding.nextBtn.setOnClickListener {
            if (Static.settLang=="SetLan"){

                requireContext().newScreen(BottomNavActivity::class.java)
            }else{
                requireContext().newScreen(BoardPassActivity::class.java)
            }
        }
        if (SharedPrefObj.getAppTheme(requireContext())!=null){
            val model= SharedPrefObj.getAppTheme(requireContext())
            Glide.with(requireContext()).load(model.theme)
                .into(binding.backGroundTheme)
        }
        recyclerView()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        modelList.clear()
        modelList.add(LangCountryModelClass(0,"English","en"))
        modelList.add(LangCountryModelClass(1,"Hindi","hi"))
        modelList.add(LangCountryModelClass(2,"Italian","it"))
        modelList.add(LangCountryModelClass(3,"Spanish","es"))
        modelList.add(LangCountryModelClass(4,"Chinese","zh"))
        mAdapter.notifyDataSetChanged()
    }
    private fun recyclerView() {

        binding.recyclerLocale.adapter=mAdapter
        mAdapter.recyclerClick(object :LocalizationRecAdapter.PassData{
            override fun clickFunction(modelClass: LangCountryModelClass) {
                sp.setLangName(modelClass.name)
                ForLanguageSettingsClass.setLocale(requireContext()
                ,modelClass.abr)
                mAdapter.notifyDataSetChanged()
            }
        })
    }
}