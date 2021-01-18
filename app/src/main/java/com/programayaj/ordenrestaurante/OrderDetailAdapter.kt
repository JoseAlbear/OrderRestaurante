package com.programayaj.ordenrestaurante

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_orders.view.*

class OrderDetailAdapter(var orderDetail: List<DataOrderDetail>,
                         private val listener: onItemClickListenerDetalleOrden): RecyclerView.Adapter<OrderDetailAdapter.OrderDetailHolder>(){

    var posisionCurrent = -1
    var funcionesGenerales = FunctionsGenerals()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return OrderDetailHolder(layoutInflater.inflate(R.layout.card_orders, parent, false))
    }

    override fun onBindViewHolder(holder: OrderDetailHolder, position: Int) {
        holder.render(orderDetail.get(position))

    }

    override fun getItemCount(): Int = orderDetail.size

    inner class OrderDetailHolder(var view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

        fun render(orderDetail: DataOrderDetail) {
            view.tvQuantity.text = orderDetail.quantity.toString()
            view.tvName.text = orderDetail.name
            view.tvPriceTotal.text = funcionesGenerales.formatoDouble(orderDetail.priceTotal)
            if(!orderDetail.observation.equals(""))
                view.tvName.setTextColor(Color.rgb(255,171, 145))
            else
                view.tvName.setTextColor(Color.BLACK)
            if (adapterPosition == posisionCurrent)
                view.setBackgroundColor(Color.GRAY)
            else
                view.setBackgroundColor(Color.WHITE)

        }

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClickDetalleOrden(adapterPosition)
            posisionCurrent = adapterPosition
            notifyDataSetChanged()
        }
    }

    interface onItemClickListenerDetalleOrden{
        fun onItemClickDetalleOrden(position: Int)
    }

}