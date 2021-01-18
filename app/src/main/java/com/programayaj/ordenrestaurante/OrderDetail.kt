package com.programayaj.ordenrestaurante

class OrderDetail {

    private var orderDetail = ArrayList<DataOrderDetail>()
    private var total: Double = 0.0
    //ESTA POSICIÓN SERÁ IGUAL AL ELEMENTO QUE ESTÁ PULSADO ACTUALMENTE EN EL RECYCLER DE ÓRDENES
    //CUANDO SE ELIMINA UN ELEMENTO POR COMPLETO O EL RECYCLER VIEW DE ÓRDENES ESTE VACIÓ ESTA SELECCIÓN VOLVERÁ A -1, DE AHÍ SIEMPRE ESTARÁ CON UN VALOR
    private var position = -1

    fun add(quantity: Int, name: String, priceTotal: Double, observation: String, price: Double): ArrayList<String> {
        //BUSCA SI EXISTE UN NOMBRE YA EXISTENTE EN LA LISTA SI ES ASÍ AUMENTA LA CANTIDAD EN UNO, SINO AGREGA UNA NUEVA ORDEN
        position = search(name)
        val message: String
        if(position != -1) {
            increase()
            message = "${orderDetail[position].name}: ${orderDetail[position].quantity}"
        }else {
            orderDetail.add(DataOrderDetail(quantity, name, priceTotal, observation, price))
            message = "${name}: $quantity"
            calculateTotal(price)
        }

        val auxArray = ArrayList<String>()
        auxArray.add(message)
        auxArray.add(position.toString())
        return auxArray
    }

    fun increase(): String{
        return if(position >= 0) {
            orderDetail[position].quantity += 1
            orderDetail[position].priceTotal += orderDetail[position].price
            calculateTotal(orderDetail[position].price)
            "${orderDetail[position].name}: ${this.orderDetail[position].quantity}"
        }else
            "Seleccione un elemento de la lista primero"
    }

    fun decrease(): String{
        if(position >= 0) {
            return when (orderDetail[position].quantity) {
                1 -> {
                    val nombre: String = orderDetail[position].name
                    calculateTotal(-orderDetail[position].price)
                    orderDetail.removeAt(position)
                    position = -1
                    "La orden de $nombre ha sido eliminada"
                }
                else -> {
                    orderDetail[position].quantity += - 1
                    orderDetail[position].priceTotal += - orderDetail[position].price
                    calculateTotal(-orderDetail[position].price)
                    "${orderDetail[position].name}: ${orderDetail[position].quantity}"
                }
            }
        }else
            return "Seleccione un elemento de la lista primero"
    }

    private fun search(name: String) : Int{
        for(i in orderDetail.indices){
            if(orderDetail[i].name.equals(name))
                return i
        }
        return -1
    }

    private fun calculateTotal(auxTotal: Double){
        total += auxTotal
    }

    fun setOrderDetail() = orderDetail

    fun size() = orderDetail.size

    fun getObservation() = orderDetail[position].observation

    fun setObservation(observation: String){
        orderDetail[position].observation = observation
    }

    fun getTotal() = total

    fun getPosition() = position

    fun setPosition(position: Int) {
        this.position = position
    }

    override fun toString(): String {
         var auxString = ""
         for (auxOrder in orderDetail){
            auxString += "Cantidad: ${auxOrder.quantity} + Nombre: ${auxOrder.name} + PrecioTotal: ${auxOrder.priceTotal} + Observacion: ${auxOrder.observation} + Precio Unitario: ${auxOrder.price} + \n"
         }
         return auxString
    }
}