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
import com.google.android.material.switchmaterial.SwitchMaterial
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity(), HistogramRangeSlider.OnRangeChangeListener {

    private lateinit var propertyTypeChipGroup: ChipGroup
    private lateinit var tenantTypeChipGroup: ChipGroup
    private lateinit var bedroomsChipGroup: ChipGroup
    private lateinit var washroomsChipGroup: ChipGroup
    private lateinit var balconyChipGroup: ChipGroup
    private lateinit var floorChipGroup: ChipGroup
    private lateinit var areaChipGroup: ChipGroup

    private lateinit var histogramRangeSlider: HistogramRangeSlider
    private lateinit var histogramRangeSliderArea: HistogramRangeSlider
    private lateinit var minPriceTextView: TextView
    private lateinit var maxPriceTextView: TextView
    private lateinit var minAreaTextView: TextView
    private lateinit var maxAreaTextView: TextView
    private lateinit var maxAreaUnitTextView: TextView

    private lateinit var liftService: SwitchMaterial
    private lateinit var generatorService: SwitchMaterial
    private lateinit var gasService: SwitchMaterial
    private lateinit var securityService: SwitchMaterial
    private lateinit var parkingService: SwitchMaterial

    private lateinit var clearFilterButton: Button
    private lateinit var searchButton: Button
    private lateinit var areaInput: EditText
    private lateinit var addAreaButton: Button




    private val maxAllowedPrice = 50000f
    private val maxAllowedArea = 3000f
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    private val numberFormat = NumberFormat.getNumberInstance(Locale.US)

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
        setupHistogramRangeSlider()
        setupHistogramRangeSliderArea()
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

        histogramRangeSlider = findViewById(R.id.histogramRangeSlider)
        histogramRangeSliderArea = findViewById(R.id.histogramRangeSliderArea)
        minPriceTextView = findViewById(R.id.minPriceTextView)
        maxPriceTextView = findViewById(R.id.maxPriceTextView)
        minAreaTextView = findViewById(R.id.minAreaTextView)
        maxAreaTextView = findViewById(R.id.maxAreaTextView)
        maxAreaUnitTextView = findViewById(R.id.maxAreaUnitTextView)

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

    private fun setupHistogramRangeSlider() {
        histogramRangeSlider.onRangeChangeListener = this

        // Sample data for budget slider
        val sampleData = listOf(
            5f, 8f, 12f, 18f, 25f, 35f, 48f, 64f, 85f, 110f,
            140f, 175f, 215f, 260f, 310f, 365f, 425f, 490f, 560f, 635f,
            715f, 800f, 890f, 985f, 1000f, 985f, 890f, 800f, 715f, 635f,
            560f, 490f, 425f, 365f, 310f, 260f, 215f, 175f, 140f, 110f,
            85f, 64f, 48f, 35f, 25f, 18f, 12f, 8f, 5f, 3f,
            // Extended tail
            2f, 2f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f
        )
        histogramRangeSlider.setHistogramData(sampleData)
        histogramRangeSlider.setValueRange(5000f, 50000f)
    }

    private fun setupHistogramRangeSliderArea() {
        histogramRangeSliderArea.onRangeChangeListener = object : HistogramRangeSlider.OnRangeChangeListener {
            override fun onRangeChanged(minValue: Float, maxValue: Float) {
                // Format the area values to display as integers with thousands separator
                val numberFormat = NumberFormat.getNumberInstance(Locale.US)
                val formattedMinArea = numberFormat.format(minValue.toInt())
                val formattedMaxArea = numberFormat.format(maxValue.toInt())

                // Set the text for the minAreaTextView and maxAreaTextView
                minAreaTextView.text = formattedMinArea
                maxAreaTextView.text = formattedMaxArea
            }
        }

        // Sample data for built up area slider
        val areaData = listOf(
            100f, 150f, 200f, 250f, 300f, 350f, 400f, 450f, 500f, 600f,
            700f, 800f, 900f, 1000f, 1200f, 1400f, 1600f, 1800f, 2000f, 2200f,
            2400f, 2600f, 2800f, 3000f, 2800f, 2600f, 2400f, 2200f, 2000f, 1800f,
            1600f, 1400f, 1200f, 1000f, 900f, 800f, 700f, 600f, 500f, 400f
        )
        histogramRangeSliderArea.setHistogramData(areaData)
        histogramRangeSliderArea.setValueRange(800f, 3000f)
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

        histogramRangeSlider.resetSlider()
        histogramRangeSliderArea.resetSlider()

        liftService.isChecked = false
        generatorService.isChecked = false
        gasService.isChecked = false
        securityService.isChecked = false
        parkingService.isChecked = false
    }

    private fun applyFilters() {
        val propertyType = getSelectedChipText(propertyTypeChipGroup)
        val tenantType = getSelectedChipText(tenantTypeChipGroup)
        val bedrooms = getSelectedChipText(bedroomsChipGroup)
        val washrooms = getSelectedChipText(washroomsChipGroup)
        val balcony = getSelectedChipText(balconyChipGroup)
        val floor = getSelectedChipText(floorChipGroup)
        val areas = getAreaChips()

        val budgetRange = histogramRangeSlider.getSelectedRange()
        val areaRange = histogramRangeSliderArea.getSelectedRange()

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
        println("Budget Range: ₹${budgetRange.first.toInt()} - ₹${budgetRange.second.toInt()}")
        println("Area Range: ${areaRange.first.toInt()} Sq.ft - ${areaRange.second.toInt()} Sq.ft")
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

    override fun onRangeChanged(minPrice: Float, maxPrice: Float) {
        currencyFormat.maximumFractionDigits = 0
        val formattedMinPrice = currencyFormat.format(minPrice.toInt())
        val formattedMaxPrice = if (maxPrice >= maxAllowedPrice) {
            "${currencyFormat.format(maxPrice.toInt())}+"
        } else {
            currencyFormat.format(maxPrice.toInt())
        }

        minPriceTextView.text = formattedMinPrice
        maxPriceTextView.text = formattedMaxPrice
    }


}