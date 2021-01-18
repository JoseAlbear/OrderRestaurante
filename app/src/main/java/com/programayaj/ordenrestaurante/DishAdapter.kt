package com.programayaj.ordenrestaurante

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_dishes.view.*

class DishAdapter (var dataDish: List<DataDish>,
                    private val listener: OnItemClickListenerDish
                   ): RecyclerView.Adapter<DishAdapter.DishHolder>() {

    var positionCurrent = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return DishHolder(layoutInflater.inflate(R.layout.card_dishes, parent, false))
    }

    override fun onBindViewHolder(holder: DishHolder, position: Int) {
        holder.render(dataDish.get(position))
    }

    override fun getItemCount(): Int = dataDish.size


    inner class DishHolder(var view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        fun render(dataDish: DataDish){
            view.textNameDish.text = dataDish.name
            view.textoPriceDish.text = dataDish.price.toString()


            view.setOnTouchListener{v, event ->
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> {
                        view.cardDishes.setCardBackgroundColor(Color.GREEN)
                    }
                    MotionEvent.ACTION_UP -> {
                        view.cardDishes.setCardBackgroundColor(Color.WHITE)
                    }
                }
                v?.onTouchEvent(event)?: true
            }

        }

        init{
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClickDish(adapterPosition)
        }

    }

    interface OnItemClickListenerDish{
        fun onItemClickDish(position: Int)
    }
}