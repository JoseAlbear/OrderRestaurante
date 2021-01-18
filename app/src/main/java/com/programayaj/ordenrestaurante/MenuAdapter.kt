package com.programayaj.ordenrestaurante

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_menu.view.*

class MenuAdapter (var dataMenu: List<DataMenu>,
                   private val listener: OnItemClickListenerMenu
):
        RecyclerView.Adapter<MenuAdapter.MenuHolder>() {

    var positionCurrent = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return MenuHolder(layoutInflater.inflate(R.layout.card_menu, parent, false))
    }

    override fun onBindViewHolder(holder: MenuHolder, position: Int) {
        holder.render(dataMenu.get(position))
    }

    override fun getItemCount(): Int = dataMenu.size


    inner class MenuHolder(var view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        fun render(dataMenu: DataMenu) {
            view.textHeader.text = dataMenu.name

            if(adapterPosition == positionCurrent)
                view.cardMenu.setCardBackgroundColor(Color.YELLOW)
            else
                view.cardMenu.setCardBackgroundColor(Color.WHITE)
        }

        init{
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClickMenu(adapterPosition)
            positionCurrent = adapterPosition
            notifyDataSetChanged()
        }

    }

    interface OnItemClickListenerMenu{
        fun onItemClickMenu(position: Int)
    }


}