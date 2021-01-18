package com.programayaj.ordenrestaurante

import java.text.DecimalFormat

class FunctionsGenerals {

    fun formatoDouble(valor :Double): String{
        val df: DecimalFormat = DecimalFormat("#0.00")
        return df.format(valor)
    }

}