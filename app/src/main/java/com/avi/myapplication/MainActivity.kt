package com.avi.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    private lateinit var propertyTypeChipGroup: ChipGroup
    private lateinit var tenantTypeChipGroup: ChipGroup
    private lateinit var bedroomsChipGroup: ChipGroup
    private lateinit var washroomsChipGroup: ChipGroup
    private lateinit var balconyChipGroup: ChipGroup
    private lateinit var floorChipGroup: ChipGroup
    private lateinit var areaChipGroup: ChipGroup

    private lateinit var budgetSlider: RangeSlider
    private lateinit var budgetDisplay: TextView
    private lateinit var areaSlider: RangeSlider
    private lateinit var areaDisplay: TextView

    private lateinit var liftService: SwitchMaterial
    private lateinit var generatorService: SwitchMaterial
    private lateinit var gasService: SwitchMaterial
    private lateinit var securityService: SwitchMaterial
    private lateinit var parkingService: SwitchMaterial

    private lateinit var clearFilterButton: Button
    private lateinit var searchButton: Button
    private lateinit var areaInput: EditText
    private lateinit var addAreaButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.expBlue)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupSliders()
        setupButtons()
        setupAreaChipGroup()
    }

    private fun initializeViews() {
        propertyTypeChipGroup = findViewById(R.id.property_type_chip_group)
        tenantTypeChipGroup = findViewById(R.id.tenant_type_chip_group)
        bedroomsChipGroup = findViewById(R.id.bedrooms_chip_group)
        washroomsChipGroup = findViewById(R.id.washrooms_chip_group)
        balconyChipGroup = findViewById(R.id.balcony_chip_group)
        floorChipGroup = findViewById(R.id.floor_chip_group)
        areaChipGroup = findViewById(R.id.area_chip_group)

        budgetSlider = findViewById(R.id.budget_slider)
        budgetDisplay = findViewById(R.id.budget_display)
        areaSlider = findViewById(R.id.area_slider)
        areaDisplay = findViewById(R.id.area_display)

        liftService = findViewById(R.id.lift_service)
        generatorService = findViewById(R.id.generator_service)
        gasService = findViewById(R.id.gas_service)
        securityService = findViewById(R.id.security_service)
        parkingService = findViewById(R.id.parking_service)

        clearFilterButton = findViewById(R.id.clear_filter_button)
        searchButton = findViewById(R.id.search_button)
        areaInput = findViewById(R.id.area_input)
        addAreaButton = findViewById(R.id.add_area_button)
    }

    private fun setupSliders() {
        budgetSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            budgetDisplay.text = "₹${values[0].toInt()} - ₹${values[1].toInt()}"
        }

        areaSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            areaDisplay.text = "${values[0].toInt()} Sqft - ${values[1].toInt()} Sqft"
        }
    }

    private fun setupButtons() {
        clearFilterButton.setOnClickListener {
            clearAllFilters()
        }

        searchButton.setOnClickListener {
            applyFilters()
        }
    }

    private fun setupAreaChipGroup() {
        addAreaButton.setOnClickListener {
            val areaName = areaInput.text.toString().trim()
            if (areaName.isNotEmpty()) {
                addAreaChip(areaName)
                areaInput.text.clear()
            }
        }
    }

    private fun addAreaChip(areaName: String) {
        val chip = Chip(this)
        chip.text = areaName
        chip.isCloseIconVisible = true
        chip.setChipBackgroundColorResource(R.color.expBlue)
        chip.setTextColor(ContextCompat.getColor(this, R.color.white))
        chip.setCloseIconTintResource(R.color.white)

        chip.setOnCloseIconClickListener {
            areaChipGroup.removeView(chip)
        }

        areaChipGroup.addView(chip)
    }

    private fun clearAllFilters() {
        propertyTypeChipGroup.clearCheck()
        tenantTypeChipGroup.clearCheck()
        bedroomsChipGroup.clearCheck()
        washroomsChipGroup.clearCheck()
        balconyChipGroup.clearCheck()
        floorChipGroup.clearCheck()
        areaChipGroup.removeAllViews()

        budgetSlider.values = listOf(100f, 10000f)
        areaSlider.values = listOf(0f, 10000f)

        liftService.isChecked = false
        generatorService.isChecked = false
        gasService.isChecked = false
        securityService.isChecked = false
        parkingService.isChecked = false
    }

    private fun applyFilters() {
        // Collect all filter values
        val propertyType = getSelectedChipText(propertyTypeChipGroup)
        val tenantType = getSelectedChipText(tenantTypeChipGroup)
        val bedrooms = getSelectedChipText(bedroomsChipGroup)
        val washrooms = getSelectedChipText(washroomsChipGroup)
        val balcony = getSelectedChipText(balconyChipGroup)
        val floor = getSelectedChipText(floorChipGroup)
        val areas = getAreaChips()

        val budgetRange = budgetSlider.values
        val areaRange = areaSlider.values

        val services = mutableListOf<String>()
        if (liftService.isChecked) services.add("Lift")
        if (generatorService.isChecked) services.add("Generator")
        if (gasService.isChecked) services.add("Gas")
        if (securityService.isChecked) services.add("Security Guard")
        if (parkingService.isChecked) services.add("Parking")

        println("Filters applied:")
        println("Property Type: $propertyType")
        println("Tenant Type: $tenantType")
        println("Bedrooms: $bedrooms")
        println("Washrooms: $washrooms")
        println("Balcony: $balcony")
        println("Floor: $floor")
        println("Areas: $areas")
        println("Budget Range: ₹${budgetRange[0].toInt()} - ₹${budgetRange[1].toInt()}")
        println("Area Range: ${areaRange[0].toInt()} Sqft - ${areaRange[1].toInt()} Sqft")
        println("Services: $services")
    }

    private fun getSelectedChipText(chipGroup: ChipGroup): String? {
        val selectedChipId = chipGroup.checkedChipId
        return if (selectedChipId != -1) {
            findViewById<Chip>(selectedChipId).text.toString()
        } else {
            null
        }
    }

    private fun getAreaChips(): List<String> {
        return (0 until areaChipGroup.childCount)
            .map { areaChipGroup.getChildAt(it) as Chip }
            .map { it.text.toString() }
    }
}