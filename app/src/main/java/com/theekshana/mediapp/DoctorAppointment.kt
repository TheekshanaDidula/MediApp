package com.theekshana.mediapp

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import java.util.Calendar
import java.util.Locale

class DoctorAppointment : AppCompatActivity() {

    private var selectedDoctor: MaterialCardView? = null
    private var selectedTime: TextView? = null
    private lateinit var repository: AppointmentRepository

    data class DoctorInfo(
        val name: String,
        val specialty: String,
        val imageRes: Int,
        val experience: String,
        val fee: String,
        val availability: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doctor_appointment)
        
        repository = AppointmentRepository(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }

        val dateInput = findViewById<EditText>(R.id.dateInput)
        
        dateInput.isFocusable = false
        dateInput.isClickable = true
        dateInput.setOnClickListener {
            showDatePicker(dateInput)
        }

        val doctorCards = listOf(
            findViewById<MaterialCardView>(R.id.doctor1),
            findViewById<MaterialCardView>(R.id.doctor2),
            findViewById<MaterialCardView>(R.id.doctor3),
            findViewById<MaterialCardView>(R.id.doctor4)
        )

        val doctorData = mapOf(
            R.id.doctor1 to DoctorInfo("Dr. Asanka Perera", "Cardiologist", R.drawable.image_81, "15 Years", "Rs. 2500", "12.00 PM - 5.00 PM"),
            R.id.doctor2 to DoctorInfo("Dr. kameesh Weern", "General Physician", R.drawable.image_82, "10 Years", "Rs. 2000", "09.00 AM - 1.00 PM"),
            R.id.doctor3 to DoctorInfo("Dr. Romesh Suranga", "Cardiologist", R.drawable.image_83, "12 Years", "Rs. 2500", "2.00 PM - 6.00 PM"),
            R.id.doctor4 to DoctorInfo("Dr. Rawanthi", "Pediatrician", R.drawable.image_84, "20 Years", "Rs. 2800", "12.00 PM - 5.00 PM")
        )

        doctorCards.forEach { card ->
            card.setOnClickListener {
                val info = doctorData[card.id]
                if (info != null) {
                    showDoctorDetailsDialog(info, card)
                }
            }
        }

        val timeSlots = listOf(
            findViewById<TextView>(R.id.time9am),
            findViewById<TextView>(R.id.time10am),
            findViewById<TextView>(R.id.time11am),
            findViewById<TextView>(R.id.time2pm),
            findViewById<TextView>(R.id.time3pm),
            findViewById<TextView>(R.id.time4pm)
        )

        timeSlots.forEach { slot ->
            slot.setOnClickListener {
                selectedTime?.setBackgroundResource(R.drawable.edittext_bg)
                slot.setBackgroundColor(Color.parseColor("#25DE9D"))
                selectedTime = slot
            }
        }

        findViewById<Button>(R.id.bookButton).setOnClickListener {
            val doctor = selectedDoctor?.let { doctorData[it.id] }
            val date = dateInput.text.toString().trim()
            val time = selectedTime?.text?.toString()

            if (doctor != null && date.isNotEmpty() && time != null) {
                val appointment = Appointment(
                    doctorName = doctor.name,
                    doctorSpecialty = doctor.specialty,
                    date = date,
                    time = time
                )

                repository.saveAppointment(appointment) { success ->
                    if (success) {
                        Toast.makeText(this, "Appointment Booked Successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, AppointmentSuccess::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to book appointment", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please select a doctor, date, and time", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDoctorDetailsDialog(info: DoctorInfo, card: MaterialCardView) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_doctor_details)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.findViewById<ShapeableImageView>(R.id.dialogDoctorImage).setImageResource(info.imageRes)
        dialog.findViewById<TextView>(R.id.dialogDoctorName).text = info.name
        dialog.findViewById<TextView>(R.id.dialogSpecialty).text = info.specialty
        dialog.findViewById<TextView>(R.id.dialogAvailability).text = "Available Today ${info.availability}"
        dialog.findViewById<TextView>(R.id.dialogFee).text = info.fee
        dialog.findViewById<TextView>(R.id.dialogExperience).text = "${info.experience} of Experience"

        dialog.findViewById<Button>(R.id.closeDialogButton).setOnClickListener {
            // Select the doctor
            selectedDoctor?.setCardBackgroundColor(Color.WHITE)
            card.setCardBackgroundColor(Color.parseColor("#E3F2FD"))
            selectedDoctor = card
            dialog.dismiss()
        }

        dialog.show()
        
        // Set dialog width to match parent with some margin
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun showDatePicker(dateInput: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            dateInput.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }
}
