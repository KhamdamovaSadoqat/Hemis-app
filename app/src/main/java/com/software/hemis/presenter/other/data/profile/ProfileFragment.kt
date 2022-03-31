package com.software.hemis.presenter.other.data.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.hemis.R
import com.example.hemis.databinding.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.database.room.RoomDatabase
import com.software.hemis.presenter.help.HelpAdapter
import com.software.hemis.presenter.other.adapter.Info
import com.software.hemis.presenter.other.adapter.InformationRecyclerViewAdapter
import com.software.hemis.utils.ImageDownloader
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.data.SharedPref
import com.software.hemis.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BindingFragment<FragmentProfileBinding>(), View.OnClickListener {

    @Inject
    lateinit var sharedPref: SharedPref
    @Inject
    lateinit var roomDatabase: RoomDatabase
    private val viewModel: ProfileViewModel by viewModels()
    private var languageDialog: BottomSheetDialog? = null
    private var themeDialog: BottomSheetDialog? = null
    private var semesterDialog: BottomSheetDialog? = null
    private var logOutDialog: BottomSheetDialog? = null
    private lateinit var semesterAdapter: SemesterAdapter
    private lateinit var languageDialogBinding: DialogLanguageBinding
    private lateinit var themeDialogBinding: DialogNightModeBinding
    private lateinit var semesterDialogBinding: DialogSemestrBinding
    private lateinit var logOutDialogBinding: DialogLogOutBinding

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentProfileBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        profile()
    }

    private fun setUpViews() {
        languageDialogBinding = DialogLanguageBinding.inflate(LayoutInflater.from(requireContext()))
        themeDialogBinding = DialogNightModeBinding.inflate(LayoutInflater.from(requireContext()))
        semesterDialogBinding = DialogSemestrBinding.inflate(LayoutInflater.from(requireContext()))
        logOutDialogBinding = DialogLogOutBinding.inflate(LayoutInflater.from(requireContext()))

        setSemester()

        val adapter = InformationRecyclerViewAdapter { position ->
            when (position) {
                0 -> {
                    findNavController().navigate(R.id.editProfileFragment)
                }
                1 -> {
                    (semesterDialogBinding.root.parent as? ViewGroup)?.removeView(
                        semesterDialogBinding.root
                    )
                    showSemesterDialog()
                }
                2 -> {
                    (languageDialogBinding.root.parent as? ViewGroup)?.removeView(
                        languageDialogBinding.root
                    )
                    showLanguageDialog()
                }
                3 -> {
                    (themeDialogBinding.root.parent as? ViewGroup)?.removeView(themeDialogBinding.root)
                    showThemeDialog()
                }
                4 -> {
                    (logOutDialogBinding.root.parent as? ViewGroup)?.removeView(logOutDialogBinding.root)
                    showLogOutDialog()
                }
            }
        }
        binding.rv.adapter = adapter
        adapter.items = loadData()

        //Language Dialog
        languageDialogBinding.rbEnglish.setOnClickListener(this)
        languageDialogBinding.rbRussian.setOnClickListener(this)
        languageDialogBinding.rbUzbekKrl.setOnClickListener(this)
        languageDialogBinding.rbUzbekLat.setOnClickListener(this)

        //Theme Dialog
        themeDialogBinding.rbDay.setOnClickListener(this)
        themeDialogBinding.rbNight.setOnClickListener(this)

        //LogOut Dialog
        logOutDialogBinding.btnSubmit.setOnClickListener {
            sharedPref.token = ""
            sharedPref.refreshToken = ""
            sharedPref.universityUrl = ""
            Constants.universityUrl = ""
            dismissLogOutDialog()
            CoroutineScope(Dispatchers.IO).launch {
               roomDatabase.clearAllTables()
            }
            findNavController().navigate(R.id.action_otherFragment_to_universityFragment)
        }
        logOutDialogBinding.btnCancel.setOnClickListener { dismissLogOutDialog() }
    }

    private fun setSemester() {
        semesterAdapter = SemesterAdapter { selectedSemester ->
            sharedPref.currentSemester = selectedSemester.code.toInt()
            sharedPref.currentSemesterName = selectedSemester.name

            semesterAdapter.updateList(selectedSemester.code.toInt())
        }
        semesterDialogBinding.rvSemester.adapter = semesterAdapter

        viewModel.getSemester().observe(this) { semesterEntity ->
            semesterAdapter.changedSemester = sharedPref.currentSemester
            semesterAdapter.items = semesterEntity
        }
    }

    private fun loadData(): List<Info> = listOf(
        Info(R.drawable.ic_setting, "Profil", R.color.blue_light),
        Info(R.drawable.ic_access_time, "Semestr", R.color.blue_light),
        Info(R.drawable.ic_language, "Tilni o‘zgartirish", R.color.blue_light),
        Info(R.drawable.ic_dark_mode, "Dizayn", R.color.blue_light),
        Info(R.drawable.ic_logout, "Chiqish", R.color.blue_light),
    )

    private fun showSemesterDialog() {
        semesterDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
        semesterDialog!!.setContentView(semesterDialogBinding.root)
        semesterDialog!!.show()
    }

    private fun showLanguageDialog() {
        languageDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
        languageDialog!!.setContentView(languageDialogBinding.root)
        languageDialog!!.show()
    }

    private fun showThemeDialog() {
        themeDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
        themeDialog!!.setContentView(themeDialogBinding.root)
        themeDialog!!.show()
    }

    private fun showLogOutDialog() {
        logOutDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
        logOutDialog!!.setContentView(logOutDialogBinding.root)
        logOutDialog!!.show()
    }

    private fun dismissLogOutDialog() {
        if (logOutDialog!!.isShowing) {
            logOutDialog!!.dismiss()
        }
        logOutDialog!!.dismiss()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            languageDialogBinding.rbEnglish.id -> {}
            languageDialogBinding.rbRussian.id -> {}
            languageDialogBinding.rbUzbekKrl.id -> {}
            languageDialogBinding.rbUzbekLat.id -> {}

            themeDialogBinding.rbDay.id -> {}
            themeDialogBinding.rbNight.id -> {}

//            semestrDialogBinding.rbFirst.id -> {}
//            semestrDialogBinding.rbSecond.id -> {}
//            semestrDialogBinding.rbThird.id -> {}
//            semestrDialogBinding.rbForth.id -> {}
//            semestrDialogBinding.rbFifth.id -> {}
//            semestrDialogBinding.rbSixth.id -> {}
//            semestrDialogBinding.rbSeventh.id -> {}
//            semestrDialogBinding.rbEighth.id -> {}
        }
    }

    private fun profile() {
        viewModel.getProfile().observe(this){ profileEntity ->
            binding.clProfile.visible()
            binding.tvName.text = requireContext().getString(
                R.string.full_name,
                profileEntity.firstName,
                profileEntity.secondName
            )
            binding.tvGroup.text = requireContext().getString(
                R.string.group_course,
                profileEntity.groupName,
                profileEntity.levelName)
            Log.d("-------------", "handleStateChange: url: ${profileEntity.image}")
            ImageDownloader.loadImage(requireContext(),
                profileEntity.image,
                binding.ivProfile)
        }
    }
}