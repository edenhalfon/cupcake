package com.eden.cupcake.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel: ViewModel() {

    private var _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private var _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    private var _pickupDate = MutableLiveData<String>()
    val pickupDate: LiveData<String> = _pickupDate

    private var _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    val dateOptions = getPickupOptions()

    init {
        resetOrder()
    }

    fun setQuantity(quantity: Int) {
        _quantity.value = quantity
        updatePrice()
    }

    fun setFlavor(flavor: String) {
        _flavor.value = flavor
    }

    fun setPickupDate(pickupDate: String) {
        _pickupDate.value = pickupDate
        updatePrice()
    }

    fun hasNoFlavorSet(): Boolean = _flavor.value.isNullOrEmpty()

    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }

        return options
    }

    private fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _pickupDate.value = dateOptions[1]
        _price.value = 0.0
    }

    private fun updatePrice() {
        var price = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        if (dateOptions[0] == _pickupDate.value) {
            price += PRICE_FOR_SAME_DAY_PICKUP
        }

        _price.value = price
    }
}