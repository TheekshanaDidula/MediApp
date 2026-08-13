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
        medicinesList = repository.getMedicines()

        medNameInput = findViewById(R.id.medNameInput)
        timeInput = findViewById(R.id.timeInput)
        updateButton = findViewById(R.id.updateButton)
        cancelButton = findViewById(R.id.cancelButton)

        setupRecyclerView()

        // In this UI, the 'updateButton' acts as both Add and Update based on state
        // To strictly follow the professors instruction of "Add New Medicine form creates a new record"
        // I will implement the logic where if editingMedicineId is null, it's a CREATE.
        
        updateButton.setOnClickListener {
            val name = medNameInput.text.toString().trim()
            val time = timeInput.text.toString().trim()

            if (name.isNotEmpty() && time.isNotEmpty()) {
                if (editingMedicineId == null) {
                    // CREATE
                    val newMed = Medicine(name = name, time = time)
                    medicinesList.add(newMed)
                    Toast.makeText(this, "Medicine Added", Toast.LENGTH_SHORT).show()
                } else {
                    // UPDATE
                    val index = medicinesList.indexOfFirst { it.id == editingMedicineId }
                    if (index != -1) {
                        medicinesList[index].name = name
                        medicinesList[index].time = time
                        Toast.makeText(this, "Medicine Updated", Toast.LENGTH_SHORT).show()
                    }
                    editingMedicineId = null
                    updateButton.text = getString(R.string.update) // Reset text if changed
                }
                
                repository.saveMedicines(medicinesList)
                adapter.notifyDataSetChanged()
                clearInputs()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            clearInputs()
            editingMedicineId = null
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.medicinesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = MedicineAdapter(medicinesList, 
            onEditClick = { medicine ->
                // POPULATE FORM FOR MODIFICATION (Update)
                medNameInput.setText(medicine.name)
                timeInput.setText(medicine.time)
                editingMedicineId = medicine.id
                updateButton.text = "Save" // Change text to indicate editing
            },
            onDeleteClick = { medicine ->
                // DELETE
                medicinesList.remove(medicine)
                repository.saveMedicines(medicinesList)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Medicine Deleted", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter = adapter
    }

    private fun clearInputs() {
        medNameInput.text.clear()
        timeInput.text.clear()
    }
}