package com.madison.move.ui.profile


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.madison.move.R
import com.madison.move.data.model.User
import com.madison.move.databinding.FragmentProfileBinding
import com.madison.move.ui.base.BaseFragment


class ProfileFragment : BaseFragment<ProfilePresenter>(), ProfileContract.ProfileView {

    companion object {
        const val FULL_NAME_AT_LEAST_4_CHARS = "FN_4_CH"
        const val USER_NAME_CONTAINS_WHITE_SPACE = "USER_WS"
    }

    private lateinit var binding: FragmentProfileBinding
    private lateinit var user: User
    private var listState: ArrayList<String> =
        arrayListOf("None", "Ha Noi", "Da Nang", "Hue", "Ho Chi Minh", "Hai Phong")
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private val months = arrayOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
    )
    private val years = (1900..2030).map { it.toString() }.toMutableList()
    private var userNewProfile = User()
    override fun createPresenter(): ProfilePresenter = ProfilePresenter(this, userNewProfile)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val bundle = arguments
        user = bundle?.getParcelable<User>("user")!!

        presenter.apply {

        }

        binding.saveSettingBtn.setOnClickListener {
            presenter?.onSaveProfileClickPresenter(getNewProfile())
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleRadioButton()
        handleDropDownState()
        handleDropDownCountry()
        hideHintTextInputLayout()
        handleInputUserFullName()
        handleInputUserName()
        handleInputCity()
        setUserData(user)
        handleDropDownDob()
        handlePickerImage()

    }

    private fun getNewProfile(): User {
        val newUserName = binding.editUsername.text.toString().trim()
        val newFullName = binding.editProfileFullName.text.toString().trim()
        val newCountry = binding.dropdownCountryText.text.toString().trim()
        val newState = binding.dropdownStateText.text.toString().trim()
        val newCity = binding.editProfileCity.text.toString().trim()

        val newAddress = "$newCity-$newState-$newCountry"

        val newGender: String = if (binding.radioMale.isChecked) {
            binding.radioMale.text.toString()
        } else if (binding.radioFemale.isChecked) {
            binding.radioFemale.text.toString()
        } else {
            binding.radioRatherNotSay.text.toString()
        }
        val newDob =
            "${binding.dropdownDayText.text}/${binding.dropdownMonthText.text}/${binding.dropdownYearText.text}".trim()

        userNewProfile =
            User(
                1,
                newUserName,
                user.email,
                newFullName,
                user.password,
                user.avatar,
                1,
                newGender,
                newDob,
                1,
                newAddress
            )

        return userNewProfile

    }

    private fun handleInputUserName() {
        binding.editUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.editUsername.setBackgroundResource(R.drawable.custom_edittext)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun handleInputUserFullName() {
        binding.editProfileFullName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.editProfileFullName.setBackgroundResource(R.drawable.custom_edittext)
                binding.txtErrorFullName.visibility = View.GONE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val nameText = binding.editProfileFullName.text.toString()
                val countDotChar = nameText.count { it == '.' }
                if (countDotChar >= 2) {
                    return binding.editProfileFullName.setText(nameText.dropLast(1))
                }

                if (nameText.contains("  ") || nameText.contains(". ") || nameText.contains(" .")) {
                    return binding.editProfileFullName.setText(nameText.dropLast(1))
                }

                if (nameText.startsWith(" ")) {
                    return binding.editProfileFullName.setText(nameText.dropLast(1))
                }


            }

            override fun afterTextChanged(s: Editable?) {
                binding.editProfileFullName.setSelection(binding.editProfileFullName.length())
            }

        })
    }

    private fun handleInputCity() {
        binding.editProfileCity.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val cityText = binding.editProfileCity.text.toString()
                val matches = arrayOf("  ", "..", ",,", "--", " ,", " .", "- -")

                for (s in matches) {
                    if (cityText.contains(s)) {
                        return binding.editProfileCity.setText(cityText.dropLast(1))
                    }
                }

                if (cityText.startsWith(" ")) {
                    return binding.editProfileCity.setText(cityText.dropLast(1))
                }
            }

            override fun afterTextChanged(s: Editable?) {
                binding.editProfileCity.setSelection(binding.editProfileCity.length())
            }

        })
    }


    /*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK) {

                //Image Uri will not be null for RESULT_OK
                val uri: Uri? = data?.data
                binding.imgProfileUser.setImageURI(uri)

                Log.d("AVATAR", uri.toString())

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(
                    activity?.applicationContext,
                    ImagePicker.getError(data),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(activity?.applicationContext, "Task Cancelled", Toast.LENGTH_SHORT)
                    .show()
            }

        }*/

    private var mCameraUri: Uri? = null
    private var mGalleryUri: Uri? = null
    private var mProfileUri: Uri? = null


    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                mProfileUri = uri
                binding.imgProfileUser.setLocalImage(uri, false)
            } else {
                parseError(it)
            }
        }

    private fun parseError(activityResult: ActivityResult) {
        if (activityResult.resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(activity, ImagePicker.getError(activityResult.data), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handlePickerImage() {
        binding.txtProfileUpdatePicture.setOnClickListener {
            activity?.let { it1 ->
                ImagePicker.with(it1)
                    .crop()
                    .cropOval()
                    .maxResultSize(1000, 1000, false)
                    .provider(ImageProvider.BOTH) // Or bothCameraGallery()
                    .setDismissListener {
                        Log.d("ImagePicker", "onDismiss")
                    }
                    .createIntentFromDialog { launcher.launch(it) }
            }
        }
    }


    private fun setUserData(user: User) {

        binding.editUsername.setText(user.username)
        binding.editProfileEmail.setText(user.email)
        binding.editProfileFullName.setText(user.fullname)
        binding.editProfileCity.setText(user.address)

        binding.imgProfileUser.setImageResource(user.avatar)


        if (user.gender == "Male") {
            binding.radioMale.isChecked = true
        } else {
            binding.radioFemale.isChecked = true
        }

        binding.dropdownYearText.setText("2001")
        binding.dropdownMonthText.setText("Jan")
        binding.dropdownDayText.setText("1")

        val monthSelected = binding.dropDownProfileMonth.editText?.text.toString()
        val yearSelected = binding.dropDownProfileYear.editText?.text.toString()
        onHandleListOfDay(monthSelected, yearSelected)

    }

    override fun onShowLoading() {

    }

    override fun onSaveProfileClick() {
        binding.txtErrorFullName.visibility = View.GONE
        binding.txtErrorFullName.focusable = View.FOCUSABLE
        Toast.makeText(
            activity?.applicationContext,
            "Update Profile Successful",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onShowError(errorType: String) {
        when (errorType) {
            FULL_NAME_AT_LEAST_4_CHARS -> {
                binding.txtErrorFullName.apply {
                    visibility = View.VISIBLE
                    text = context.getString(R.string.error_fullname_chars)
                }
                binding.editProfileFullName.setBackgroundResource(R.drawable.custom_edittext_error)
            }
        }

    }


    private fun hideHintTextInputLayout() {
        binding.dropDownProfileCountry.isHintEnabled = false
        binding.dropDownProfileState.isHintEnabled = false
        binding.dropDownProfileDay.isHintEnabled = false
        binding.dropDownProfileMonth.isHintEnabled = false
        binding.dropDownProfileYear.isHintEnabled = false
    }

    private fun handleDropDownDob() {
        binding.dropdownMonthText.inputType = EditorInfo.TYPE_NULL
        binding.dropdownYearText.inputType = EditorInfo.TYPE_NULL

        onMonthSelected()
        onYearSelected()

        val monthAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, months)
        val yearAdapter =
            ArrayAdapter(requireContext(), R.layout.item_dropdown, years.sortedDescending())

        binding.dropdownMonthText.setAdapter(monthAdapter)
        binding.dropdownYearText.setAdapter(yearAdapter)


    }

    private fun onYearSelected() {
        binding.dropdownYearText.setOnItemClickListener { parent, _, position, _ ->
            val monthSelected = binding.dropDownProfileMonth.editText?.text.toString()
            val yearSelected: String = parent.getItemAtPosition(position).toString()
            onHandleListOfDay(monthSelected, yearSelected)
        }
    }

    private fun onMonthSelected() {
        binding.dropdownMonthText.setOnItemClickListener { parent, _, position, _ ->
            val yearSelected = binding.dropDownProfileYear.editText?.text.toString()
            val monthSelected: String = parent.getItemAtPosition(position).toString()
            if (yearSelected != "") {
                onHandleListOfDay(monthSelected, yearSelected)
            }

        }
    }


    private fun onHandleListOfDay(monthSelected: String, yearSelected: String) {
        binding.dropdownDayText.inputType = EditorInfo.TYPE_NULL
        val days: MutableList<String>
        if (isThirtyDaysMonth(monthSelected)) {
            days = (1..30).map { it.toString() }.toMutableList()
        } else if (monthSelected == "Feb") {
            days =
                if (isLeapYear(yearSelected.toInt())) {
                    (1..29).map { it.toString() }.toMutableList()
                } else {
                    (1..28).map { it.toString() }.toMutableList()
                }
        } else {
            days = (1..31).map { it.toString() }.toMutableList()
        }
        val dayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, days)
        binding.dropdownDayText.setAdapter(dayAdapter)
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)
    }

    private fun isThirtyDaysMonth(month: String): Boolean {
        return (month in arrayOf(
            "Apr",
            "Jun",
            "Sep",
            "Nov",
        ))
    }


    private fun handleDropDownState() {
        for (i in 1..3) {
            listState.addAll(arrayListOf("Ha Noi", "Da Nang", "Hue", "Ho Chi Minh", "Hai Phong"))
        }
        arrayAdapter = context?.let {
            ArrayAdapter(
                it.applicationContext,
                R.layout.item_dropdown,
                listState
            )
        }!!
        binding.dropdownStateText.setAdapter(arrayAdapter)
//        binding.dropdownStateText.inputType = EditorInfo.TYPE_NULL

        binding.dropdownStateText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val inputStateText = binding.dropdownStateText.text.toString().trim()
                if (inputStateText !in listState) {
                    binding.dropdownStateText.text.clear()
                }
            }
        }
    }

    private fun handleDropDownCountry() {
        for (i in 1..3) {
            listState.addAll(arrayListOf("Ha Noi", "Da Nang", "Hue", "Ho Chi Minh", "Hai Phong"))
        }
        arrayAdapter = context?.let {
            ArrayAdapter(
                it.applicationContext,
                R.layout.item_dropdown,
                listState
            )
        }!!
        binding.dropdownCountryText.setAdapter(arrayAdapter)
//        binding.dropdownCountryText.inputType = EditorInfo.TYPE_NULL


        binding.dropdownCountryText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val inputStateText = binding.dropdownCountryText.text.toString().trim()
                if (inputStateText !in listState) {
                    binding.dropdownCountryText.text.clear()
                }
            }
        }
    }

    private fun handleRadioButton() {
        binding.radioMale.setOnClickListener {
            onRadioButtonClicked(binding.radioMale)
        }

        binding.radioFemale.setOnClickListener {
            onRadioButtonClicked(binding.radioFemale)
        }

        binding.radioRatherNotSay.setOnClickListener {
            onRadioButtonClicked(binding.radioRatherNotSay)
        }
    }

    private fun onRadioButtonClicked(view: View) {
        val idRadioMale = binding.radioMale.id
        val idRadioFemale = binding.radioFemale.id
        val idRadioRather = binding.radioRatherNotSay.id

        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.id) {
                idRadioMale -> if (checked) {
                    binding.radioFemale.isChecked = false
                    binding.radioRatherNotSay.isChecked = false
                }
                idRadioFemale -> if (checked) {
                    binding.radioMale.isChecked = false
                    binding.radioRatherNotSay.isChecked = false
                }
                idRadioRather -> if (checked) {
                    binding.radioMale.isChecked = false
                    binding.radioFemale.isChecked = false
                }
            }
        }
    }


}