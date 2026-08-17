package com.theekshana.mediapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar
import java.util.Locale

class Medicinemanage1Page : AppCompatActivity() {

    private lateinit var medicineNameInput: EditText
    private lateinit var medicineTimeInput: EditText
    private lateinit var medicineDateInput: EditText
    private lateinit var addButton: AppCompatButton
    private lateinit var updateButton: AppCompatButton
    private lateinit var medicinesRecyclerView: RecyclerView
    
    private lateinit var repository: MedicineRepository
    private lateinit var adapter: MedicineAdapter
    private var medicinesList = mutableListOf<Medicine>()
    
    private var editingMedicineId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_medicinemanage1_page)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.medicinemanage1Root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

        // Initialize Views
        medicineNameInput = findViewById(R.id.medicineNameInput)
        medicineTimeInput = findViewById(R.id.medicineTimeInput)
        medicineDateInput = findViewById(R.id.medicineDateInput)
        addButton = findViewById(R.id.addButton)
        updateButton = findViewById(R.id.updateButton)
        medicinesRecyclerView = findViewById(R.id.medicinesRecyclerView)

        // Make Date and Time inputs non-editable manually
        medicineTimeInput.isFocusable = false
        medicineTimeInput.isClickable = true
        medicineDateInput.isFocusable = false
        medicineDateInput.isClickable = true

        medicineTimeInput.setOnClickListener { showTimePicker() }
        medicineDateInput.setOnClickListener { showDatePicker() }

        repository = MedicineRepository(this)

        setupRecyclerView()

        // Observe real-time changes from Firebase
        repository.observeMedicines { medicines ->
            medicinesList.clear()
            medicinesList.addAll(medicines)
            adapter.notifyDataSetChanged()
        }

        addButton.setOnClickListener {
            val name = medicineNameInput.text.toString().trim()
            val time = medicineTimeInput.text.toString().trim()
            val date = medicineDateInput.text.toString().trim()

            if (name.isNotEmpty() && time.isNotEmpty() && date.isNotEmpty()) {
                val newMedicine = Medicine(name = name, time = time, date = date)
                repository.saveMedicine(newMedicine) { success ->
                    if (success) {
                        resetForm()
                        Toast.makeText(this, "Medicine added successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to add medicine", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        updateButton.setOnClickListener {
            val name = medicineNameInput.text.toString().trim()
            val time = medicineTimeInput.text.toString().trim()
            val date = medicineDateInput.text.toString().trim()

            if (editingMedicineId != null && name.isNotEmpty() && time.isNotEmpty() && date.isNotEmpty()) {
                val updatedMedicine = Medicine(id = editingMedicineId!!, name = name, time = time, date = date)
                repository.saveMedicine(updatedMedicine) { success ->
                    if (success) {
                        resetForm()
                        Toast.makeText(this, "Medicine updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to update medicine", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (editingMedicineId == null) {
                Toast.makeText(this, "Select a medicine to update", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
            medicineTimeInput.setText(formattedTime)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            medicineDateInput.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun setupRecyclerView() {
        adapter = MedicineAdapter(medicinesList,
            onEditClick = { medicine ->
                medicineNameInput.setText(medicine.name)
                medicineTimeInput.setText(medicine.time)
                medicineDateInput.setText(medicine.date)
                editingMedicineId = medicine.id
                
                // Highlight update button
                updateButton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.update_btn)
                updateButton.setTextColor(ContextCompat.getColor(this, R.color.white))
            },
            onDeleteClick = { medicine ->
                repository.deleteMedicine(medicine.id) { success ->
                    if (success) {
                        Toast.makeText(this, "Medicine deleted", Toast.LENGTH_SHORT).show()
                        if (editingMedicineId == medicine.id) {
                            resetForm()
                        }
                    } else {
                        Toast.makeText(this, "Failed to delete medicine", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
        medicinesRecyclerView.layoutManager = LinearLayoutManager(this)
        medicinesRecyclerView.adapter = adapter
    }

    private fun resetForm() {
        medicineNameInput.text.clear()
        medicineTimeInput.text.clear()
        medicineDateInput.text.clear()
        editingMedicineId = null
        updateButton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.disabled_btn_bg)
        updateButton.setTextColor(ContextCompat.getColor(this, R.color.grey_text))
    }
}