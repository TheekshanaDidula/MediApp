package com.theekshana.mediapp

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MedicinemanagePage : AppCompatActivity() {

    private lateinit var repository: MedicineRepository
    private lateinit var adapter: MedicineAdapter
    private var medicinesList = mutableListOf<Medicine>()
    
    private lateinit var medNameInput: EditText
    private lateinit var timeInput: EditText
    private lateinit var dateInput: EditText
    private lateinit var updateButton: AppCompatButton
    private lateinit var cancelButton: AppCompatButton
    
    private var editingMedicineId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_medicinemanage_page)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.medManagerRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        repository = MedicineRepository(this)

        medNameInput = findViewById(R.id.medNameInput)
        timeInput = findViewById(R.id.timeInput)
        dateInput = findViewById(R.id.dateInput)
        updateButton = findViewById(R.id.updateButton)
        cancelButton = findViewById(R.id.cancelButton)

        setupRecyclerView()
        
        repository.observeMedicines { medicines ->
            medicinesList.clear()
            medicinesList.addAll(medicines)
            adapter.notifyDataSetChanged()
        }

        updateButton.setOnClickListener {
            val name = medNameInput.text.toString().trim()
            val time = timeInput.text.toString().trim()
            val date = dateInput.text.toString().trim()

            if (name.isNotEmpty() && time.isNotEmpty() && date.isNotEmpty()) {
                val medicineToSave = if (editingMedicineId == null) {
                    Medicine(name = name, time = time, date = date)
                } else {
                    Medicine(id = editingMedicineId!!, name = name, time = time, date = date)
                }

                repository.saveMedicine(medicineToSave) { success ->
                    if (success) {
                        val message = if (editingMedicineId == null) "Medicine Added" else "Medicine Updated"
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        clearInputs()
                        editingMedicineId = null
                        updateButton.text = getString(R.string.update)
                    } else {
                        Toast.makeText(this, "Failed to save medicine", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            clearInputs()
            editingMedicineId = null
            updateButton.text = getString(R.string.update)
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.medicinesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = MedicineAdapter(medicinesList, 
            onEditClick = { medicine ->
                medNameInput.setText(medicine.name)
                timeInput.setText(medicine.time)
                dateInput.setText(medicine.date)
                editingMedicineId = medicine.id
                updateButton.text = getString(R.string.save)
            },
            onDeleteClick = { medicine ->
                repository.deleteMedicine(medicine.id) { success ->
                    if (success) {
                        Toast.makeText(this, "Medicine Deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to delete medicine", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
        recyclerView.adapter = adapter
    }

    private fun clearInputs() {
        medNameInput.text.clear()
        timeInput.text.clear()
        dateInput.text.clear()
    }
}