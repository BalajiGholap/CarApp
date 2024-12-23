package com.example.carapp.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carapp.R
import com.example.carapp.adapters.CarListAdapter
import com.example.carapp.model.Cars
import com.example.carapp.networkinfo.NetworkStatus
import com.example.carapp.progressloader.ShowLoader
import com.example.carapp.uistate.NetworkResult
import com.example.carapp.viewmodels.CarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var carViewModel: CarViewModel
    lateinit var recyclerview: RecyclerView
    lateinit var llCarList: LinearLayout
    lateinit var llRefresh: LinearLayout
    lateinit var llRefresh1: LinearLayout
    lateinit var imgRefresh: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_blue)
        // Code to initialize UI elements(Views)
        recyclerview = findViewById(R.id.recyclerview) as RecyclerView
        llCarList = findViewById(R.id.ll_car_list) as LinearLayout
        llRefresh = findViewById(R.id.ll_refresh) as LinearLayout
        llRefresh1 = findViewById(R.id.ll_refresh1) as LinearLayout
        imgRefresh = findViewById(R.id.img_main_refresh) as ImageView
        imgRefresh.setImageResource(R.drawable.baseline_refresh)
        recyclerview.layoutManager = LinearLayoutManager(this)

        carViewModel = ViewModelProvider(this).get(CarViewModel::class.java)
        // Set observer to update the UI state
        carViewModel.cars.observe(this) { response ->
            when(response){
                is NetworkResult.Failure -> {
                    ShowLoader.hideProgress()
                    setListVisibility(false)
                    Toast.makeText(this,resources.getString(R.string.error_msg),Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Loading -> {
                    ShowLoader.showProgress(this,resources.getString(R.string.wait),resources.getString(R.string.loading_list))
                }
                is NetworkResult.Success -> {
                    ShowLoader.hideProgress()
                    setListVisibility(true)
                    showCarList(response)
                }
            }
        }
        llRefresh1.setOnClickListener {
            if (NetworkStatus.isNetworkAvailable(this)){
                setListVisibility(false)
                carViewModel.getCars()
            }else{
                networkStatusDialog()
            }
        }
    }

    // Function to get car list
    private fun showCarList(response: NetworkResult.Success<List<Cars>>) {
        val adapter = response.data?.let {
            CarListAdapter(this, it)
        }
        recyclerview.adapter = adapter

        //Code to click on car list item to show car details
        if (adapter != null) {
            adapter.setOnClickListener(object : CarListAdapter.OnClickListener{
                override fun onClick(position: Int, model: Cars) {
                    Log.i("CarItemmm","Clicked ${model.carname} ${model.price}")
                    showCarDetailsPage(model)
                }
            })
        }
        Log.i("CarAdapterSize","${adapter?.itemCount}")
        if (adapter?.itemCount!! > 1){
            CarDetailsActivity.isCarCount = false
        }else{
            CarDetailsActivity.isCarCount = true
        }
    }

    // Navigate to display car details
    private fun showCarDetailsPage(model: Cars) {
        val intent = Intent(this@MainActivity, CarDetailsActivity::class.java)
        intent.putExtra("carname", model.carname)
        intent.putExtra("model", model.model)
        intent.putExtra("price", model.price.toString())
        intent.putExtra("image", model.image)
        intent.putExtra("id", model.id)
        startActivity(intent)
    }
    private fun networkStatusDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog_layout)

        val  txtTitle = dialog.findViewById(R.id.txt_di_title) as TextView
        val  txtMessage = dialog.findViewById(R.id.txt_message) as TextView
        val  btnCancel = dialog.findViewById(R.id.btn_cancel) as Button
        val  btnOk = dialog.findViewById(R.id.btn_ok) as Button
        btnCancel.setVisibility(View.GONE)
        txtTitle.text = resources.getString(R.string.no_internet)
        txtMessage.text = resources.getString(R.string.no_internet_msg)

        btnOk.setOnClickListener {
            setListVisibility(false)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setListVisibility(isListVisible: Boolean) {
        if (isListVisible){
            llCarList.visibility = View.VISIBLE
            llRefresh.visibility = View.GONE
        }else{
            llCarList.visibility = View.GONE
            llRefresh.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("CarNamme","onResumeCalled")
        //ShowLoader.hideProgress()
        if (NetworkStatus.isNetworkAvailable(this)){
            carViewModel.getCars()
        }else{
            networkStatusDialog()
        }
    }
}