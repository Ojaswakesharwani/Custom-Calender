package com.example.mycalender

import android.app.DatePickerDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    lateinit var selected_date: TextView
    lateinit var show_calender_btn: Button
    private lateinit var mediaPlayer: MediaPlayer // MediaPlayer for sound
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize MediaPlayer with your sound file
        mediaPlayer = MediaPlayer.create(this, R.raw.scroll_sound)

        show_calender_btn = findViewById(R.id.show_calender_btn)
        selected_date = findViewById(R.id.tv_selected_date)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        show_calender_btn.setOnClickListener {
            showDatePickerDialog(selected_date)
        }

    }


    private fun showDatePickerDialog(dateField: TextView) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.date_picker_dialog, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(dialogView)

        val monthPicker = dialogView.findViewById<NumberPicker>(R.id.monthPicker)
        val dayPicker = dialogView.findViewById<NumberPicker>(R.id.dayPicker)
        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.yearPicker)
        val btnSelectDate = dialogView.findViewById<Button>(R.id.btnSelectDate)

        // Setup month names
        val months = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        monthPicker.minValue = 0
        monthPicker.maxValue = months.size - 1
        monthPicker.displayedValues = months


        // Setup days (1-31)
        dayPicker.minValue = 1
        dayPicker.maxValue = when (monthPicker.value) {
            1 -> 28
            3, 5, 8, 10 -> 30
            else -> 31
        }

        // Setup year range
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        yearPicker.minValue = 1990
        yearPicker.maxValue = currentYear // Disable future years


        // 🔹 Apply rotation effect on scroll
        applyRotationEffect(monthPicker)
        applyRotationEffect(dayPicker)
        applyRotationEffect(yearPicker)

        // Select button action
        btnSelectDate.setOnClickListener {
            val selectedDate =
                "${months[monthPicker.value]} ${dayPicker.value}, ${yearPicker.value}"
            dateField.text = selectedDate
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    //Rotating effect
    private fun applyRotationEffect(picker: NumberPicker) {
        picker.setOnValueChangedListener { _, _, _ ->
            playScrollSound() // Play sound on scroll

            // 🔹 Rotation effect logic
            picker.animate().rotationX(15f).setDuration(100).withEndAction {
                picker.animate().rotationX(0f).setDuration(100)
            }.start()
        }
    }

    /* // Earth rotation 3d effect
     private fun applyRotationEffect(picker: NumberPicker) {
         picker.setOnValueChangedListener { _, _, _ ->
             playScrollSound() // Play sound on scroll

             // 🔹 Earth rotation effect logic
             picker.animate()
                 .rotationYBy(360f) // Rotate 360 degrees around the Y-axis
                 .setDuration(500) // Duration of the rotation
                 .withEndAction {
                     picker.rotationY = 0f // Reset rotation after completion
                 }
                 .start()
         }
     }*/

    private fun playScrollSound() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(0) // Reset to avoid delay
        } else {
            mediaPlayer.start()
        }
    }

}