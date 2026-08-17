package com.theekshana.mediapp

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

class Medicinemanage1Page : AppCompatActivity() {

    private lateinit var medicineNameInput: EditText
    private lateinit var medicineTimeInput: EditText
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
        addButton = findViewById(R.id.addButton)
        updateButton = findViewById(R.id.updateButton)
        medicinesRecyclerView = findViewById(R.id.medicinesRecyclerView)

        repository = MedicineRepository(this)
        medicinesList = repository.getMedicines()

        setupRecyclerView()

        addButton.setOnClickListener {
            val name = medicineNameInput.text.toString().trim()
            val time = medicineTimeInput.text.toString().trim()

            if (name.isNotEmpty() && time.isNotEmpty()) {
                val newMedicine = Medicine(name = name, time = time)
                medicinesList.add(newMedicine)
                repository.saveMedicines(medicinesList)
                adapter.notifyDataSetChanged()
                
                medicineNameInput.text.clear()
                medicineTimeInput.text.clear()
                Toast.makeText(this, "Medicine added successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter medicine name and time", Toast.LENGTH_SHORT).show()
            }
        }

        updateButton.setOnClickListener {
            val name = medicineNameInput.text.toString().trim()
            val time = medicineTimeInput.text.toString().trim()

            if (editingMedicineId != null && name.isNotEmpty() && time.isNotEmpty()) {
                val index = medicinesList.indexOfFirst { it.id == editingMedicineId }
                if (index != -1) {
                    medicinesList[index].name = name
                    medicinesList[index].time = time
                    repository.saveMedicines(medicinesList)
                    adapter.notifyDataSetChanged()
                    
                    resetForm()
                    Toast.makeText(this, "Medicine updated successfully", Toast.LENGTH_SHORT).show()
                }
            } else if (editingMedicineId == null) {
                Toast.makeText(this, "Select a medicine to update", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter medicine name and time", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = MedicineAdapter(medicinesList,
            onEditClick = { medicine ->
                medicineNameInput.setText(medicine.name)
                medicineTimeInput.setText(medicine.time)
                editingMedicineId = medicine.id
                
                // Highlight update button
                updateButton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.update_btn)
                updateButton.setTextColor(ContextCompat.getColor(this, R.color.white))
            },
            onDeleteClick = { medicine ->
                medicinesList.remove(medicine)
                repository.saveMedicines(medicinesList)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Medicine deleted", Toast.LENGTH_SHORT).show()
                if (editingMedicineId == medicine.id) {
                    resetForm()
                }
            }
        )
        medicinesRecyclerView.layoutManager = LinearLayoutManager(this)
        medicinesRecyclerView.adapter = adapter
    }

    private fun resetForm() {
        medicineNameInput.text.clear()
        medicineTimeInput.text.clear()
        editingMedicineId = null
        updateButton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.disabled_btn_bg)
        updateButton.setTextColor(ContextCompat.getColor(this, R.color.grey_text))
    }
}