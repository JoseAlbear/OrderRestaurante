package com.programayaj.ordenrestaurante

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(),
        MenuAdapter.OnItemClickListenerMenu,
        DishAdapter.OnItemClickListenerDish,
        OrderDetailAdapter.onItemClickListenerDetalleOrden{
    // PARA EL DETALLE DE LA ORDEN
    private var orderDetail: OrderDetail = OrderDetail()
    private var adapterOrders = OrderDetailAdapter(orderDetail.setOrderDetail(),this)
    // PARA EL MENÚ O CATEGORÍA
    private var dataMenu:MutableList<DataMenu> = ArrayList()
    private var adapterMenu = MenuAdapter(dataMenu, this)
    // PARA LOS PLATOS DEL MENÚ
    private var dataDishOrginal: MutableList<DataDish> = ArrayList()
    private var dataDish: MutableList<DataDish> = ArrayList()
    private var adapterDish = DishAdapter(dataDish, this)
    ////////////////////////////////////////////////
    private var seleccion: ArrayList<ArrayList<Int>> = ArrayList()
    private var toastDetails: Toast? = null
    private val functionsGenerals = FunctionsGenerals()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerOrder()
        ///////////////////
        getMenu()
        initRecyclerMenu()
        //////////////////
        getDisher()
        initRecyclerDisher()
        //
        editTextObservation.setOnEditorActionListener{ v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                val inputMethodManager: InputMethodManager = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
                v.clearFocus()
                if(orderDetail.getPosition() >= 0) {
                    orderDetail.setObservation(v.text.toString())
                    mensaje(orderDetail.getObservation())

                    adapterOrders.notifyDataSetChanged()
                }else
                    mensaje("Seleccione un elemento de la lista Órdenes para ingresar su observación")
                true
            }
            else
                false
        }
    }

    private fun getMenu(){

        dataMenu.add(DataMenu("PLATO FUERTE"))
        dataMenu.add(DataMenu("SOPAS"))
        dataMenu.add(DataMenu("POSTRES"))
        dataMenu.add(DataMenu("BEBIDAS"))
        adapterMenu.notifyDataSetChanged()
    }

    private fun getDisher() {
        //CREA UN ARRAY  DE PLATOS DISPONIBLES
        dataDishOrginal.add(DataDish("BORREGO", 5.00))
        dataDishOrginal.add(DataDish("TRUCHA", 5.00))
        dataDishOrginal.add(DataDish("HABAS CON CHOCLO", 3.50))
        dataDishOrginal.add(DataDish("FRITADA", 4.50))
        dataDishOrginal.add(DataDish("MOTE CON CHICHARRÓN", 2.50))
        dataDishOrginal.add(DataDish("POLLO A LA PLANCHA", 4.75))
        ///
        dataDishOrginal.add(DataDish("YAGUARLOCRO", 5.50))
        dataDishOrginal.add(DataDish("LOCRO DE QUESO", 3.50))
        dataDishOrginal.add(DataDish("CALDO DE GALLINA", 2.50))
        //
        dataDishOrginal.add(DataDish("CERVEZA", 0.50))
        dataDishOrginal.add(DataDish("AGUA", 1.51))
        dataDishOrginal.add(DataDish("CHICHA", 3.50))
        //
        //CREA UN ARRAY BIDIMENSIONAL DE SELECCIÓN
        //EL PRIMER ELEMENTO ES LA CANTIDAD DE PLATOS QUE VA A TENER CADA MENÚ EL PRIMERO TIENE SEIS, EL SEGUNDO ELEMENTO SERÁ EL INICIO
        //LA CANTIDAD Y EL INICIO ES PARA TOMAR LOS ELEMENTOS DEL dataDishOriginal Y PASAR ESE RANGO DE DATOS A UN dataDish que posteriormente será pasado a un recyclerView
        //SEGÚN SE VAYA ESCOGIENDO DEL MENÚ ESTE DATADISH SE REINICIARÁ Y TOMARA SUS NUEVOS VALORES CORRESPONDIENTES
        //SON EN TOTAL CUATRO MENÚS, EL SEGUNDO ARREGLO O TERCER MENÚ VA SER CERO A PROPÓSITO
        //NOTA: ESTE ALGORITMO ES MEJORABLE INCLUYENDOLO DENTRO DE UN FOR, TAMBIÉN INCLUYE UN ERROR QUE NO TOMA CORRECTAMENTE EL RANGO

        var auxSeleccion : ArrayList<Int> = ArrayList()
        auxSeleccion.add(6)
        auxSeleccion.add(0)
        seleccion.add(auxSeleccion)
        //
        auxSeleccion = ArrayList()
        auxSeleccion.add(4)
        auxSeleccion.add(seleccion[0][0])
        seleccion.add(auxSeleccion)
        //
        auxSeleccion = ArrayList()
        auxSeleccion.add(0)
        var auxNumeroAux = 0
        for (auxNumero in seleccion){
            auxNumeroAux += auxNumero[0]
        }
        auxSeleccion.add(auxNumeroAux - 1)
        seleccion.add(auxSeleccion)
        //
        auxSeleccion = ArrayList()
        auxSeleccion.add(3)
        auxNumeroAux = 0
        for (auxNumero in seleccion){
            auxNumeroAux += auxNumero[0]
        }
        auxSeleccion.add(auxNumeroAux - 1)
        seleccion.add(auxSeleccion)
        dataDish.addAll(dataDishOrginal.subList(seleccion[0][1], seleccion[0][0] + seleccion[0][1]))

    }

    private fun initRecyclerMenu() {
        recyclerMenu.layoutManager = LinearLayoutManager(this)
        recyclerMenu.adapter = adapterMenu
        recyclerMenu.hasFixedSize()
    }

    private fun initRecyclerDisher() {
        recyclerDishes.layoutManager = GridLayoutManager(this,3)
        recyclerDishes.adapter = adapterDish
        recyclerDishes.hasFixedSize()
    }

    private fun initRecyclerOrder() {
        recyclerOrders.layoutManager = LinearLayoutManager(this)
        recyclerOrders.adapter = adapterOrders
        recyclerOrders.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerOrders.hasFixedSize()
    }


    override fun onItemClickMenu(position: Int) {
        //SEGÚN SE PULSE UNA OPCIÓN DEL MENÚ SE ACTUALIZA EL dataDish
        dataDish.clear()
        if(seleccion[position][0] > 0)
            dataDish.addAll(dataDishOrginal.subList(seleccion[position][1], seleccion[position][0] + seleccion[position][1]))
        adapterDish.notifyDataSetChanged()
    }

    override fun onItemClickDish(position: Int) {
        val auxArray = orderDetail.add(1, dataDish[position].name, dataDish[position].price, "", dataDish[position].price)
        calculateTotal()
        val auxIndice  = auxArray[1].toInt()
        mensaje(auxArray[0])
        adapterOrders.notifyDataSetChanged()
        //SE DESPLEZA AUTOMÁTICAMENTE AL LUGAR DEL ELEMENTO INGRESADO  EN EL RECYCLERVIEW Y
        //SELECCIONA EL ELEMENTO DEL DETALLE DE ORDEN AUTOMÁTICAMENTE
        if(auxIndice == -1) {
            recyclerOrders.scrollToPosition(orderDetail.size() - 1)
            adapterOrders.posisionCurrent = orderDetail.size() - 1
            orderDetail.setPosition(orderDetail.size() - 1)
        }else {
            recyclerOrders.scrollToPosition(auxIndice)
            adapterOrders.posisionCurrent = auxIndice
        }
        editTextObservation.setText(orderDetail.getObservation())
        adapterOrders.notifyDataSetChanged()
    }

    override fun onItemClickDetalleOrden(position: Int) {
        orderDetail.setPosition(position)
        editTextObservation.setText(orderDetail.getObservation())
        if(!orderDetail.getObservation().equals(""))
            mensaje(orderDetail.getObservation())
        else
            toastDetails?.cancel()

    }

    fun insertItem(view: View) {
        mensaje(orderDetail.increase())
        adapterOrders.notifyDataSetChanged()
        calculateTotal()
    }

    fun deleteItem(view: View){
        mensaje(orderDetail.decrease())
        calculateTotal()
        adapterOrders.posisionCurrent = orderDetail.getPosition()
        adapterOrders.notifyDataSetChanged()
    }

    private fun calculateTotal(){
        textTotal.text = functionsGenerals.formatoDouble(orderDetail.getTotal())
    }

    private fun mensaje(auxMensaje: String){
        toastDetails?.cancel()
        toastDetails = Toast.makeText(applicationContext, auxMensaje, Toast.LENGTH_SHORT)
        toastDetails?.show()
    }


}


