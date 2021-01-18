package com.programayaj.ordenrestaurante

import java.math.BigDecimal

data class DataOrderDetail (
    var quantity: Int,
    var name: String,
    var priceTotal: Double,
    var observation: String,
    var price: Double

)