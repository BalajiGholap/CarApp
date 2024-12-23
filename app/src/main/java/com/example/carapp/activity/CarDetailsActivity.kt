package com.example.carapp.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.carapp.R
import com.example.carapp.model.Cars
import com.example.carapp.progressloader.ShowLoader
import com.example.carapp.uistate.NetworkResult
import com.example.carapp.viewmodels.CarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CarDetailsActivity : AppCompatActivity() {
    lateinit var carViewModel: CarViewModel
    private var isUpdate: Boolean = true
    private lateinit var imgCar: ImageView
    private lateinit var imgAdd: ImageView
    private lateinit var imgEdit: ImageView
    private lateinit var imgDelete: ImageView
    private lateinit var txtCarName: TextView
    private lateinit var txtCarModel: TextView
    private lateinit var txtCarPrice: TextView
    private lateinit var llDeleteCar: LinearLayout
    lateinit var carPrice: String
    companion object{
        var isCarCount: Boolean = false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_car_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_blue)
        // Code to initialize UI elements(Views)
        carViewModel = ViewModelProvider(this).get(CarViewModel::class.java)
        imgCar = findViewById(R.id.img_car)
        imgAdd = findViewById(R.id.img_add)
        imgAdd.setImageResource(R.drawable.add_circle)
        imgEdit = findViewById(R.id.img_edit)
        imgEdit.setImageResource(R.drawable.edit_square)
        imgDelete = findViewById(R.id.img_delete)
        imgDelete.setImageResource(R.drawable.delete_forever)
        txtCarName = findViewById(R.id.txt_car_name)
        txtCarModel = findViewById(R.id.txt_car_model)
        txtCarPrice = findViewById(R.id.txt_car_price)
        llDeleteCar = findViewById(R.id.ll_delete_car)

        val intent: Intent = getIntent()
        carPrice = intent.getStringExtra("price").toString()
        var cars: Cars = Cars(intent.getStringExtra("carname").toString(),intent.getStringExtra("id").toString(),
            intent.getStringExtra("image").toString(),intent.getStringExtra("model").toString(),carPrice.toInt())

        txtCarName.text = "Name: ${cars.carname}"
        txtCarModel.text = "Model: ${cars.model}"
        txtCarPrice.text = "Price: ${cars.price} INR"
        Glide.with(this)
            .load(cars.image)
            .placeholder(R.drawable.directions_car)
            .error(R.drawable.directions_car)
            .circleCrop()
            .thumbnail(0.5f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgCar)

        // Set observer to update the UI state
        carViewModel.singleCar.observe(this, Observer {
            when(it){
                is NetworkResult.Failure -> {
                    ShowLoader.hideProgress()
                    Toast.makeText(this,resources.getString(R.string.error_msg),Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Loading -> {
                    ShowLoader.showProgress(this,resources.getString(R.string.wait),resources.getString(R.string.loading_list))
                }
                is NetworkResult.Success -> {
                    ShowLoader.hideProgress()
                    txtCarName.text = "Name: ${it.data?.carname}"
                    txtCarModel.text = "Model: ${it.data?.model}"
                    txtCarPrice.text = "Price: ${it.data?.price} INR"
                }
            }
            if (!isUpdate){
                ShowLoader.hideProgress()
                onBackPressed()
            }

        })
        if (CarDetailsActivity.isCarCount){
            llDeleteCar.visibility = View.GONE
        }else{
            llDeleteCar.visibility = View.VISIBLE
        }
        imgAdd.setOnClickListener {
            isUpdate = false
            editCarDetails(cars)
        }
        imgEdit.setOnClickListener {
            isUpdate = true
            editCarDetails(cars)
        }
        imgDelete.setOnClickListener {
            isUpdate = false
            deleteCar(cars.id.toInt())
        }
    }

    // Function to Edit car details and add new car in the list
    private fun editCarDetails(cars: Cars) {
        val builder = AlertDialog.Builder(this, androidx.appcompat.R.style.Base_ThemeOverlay_AppCompat_Dialog_Alert)
            .create()
        val view = layoutInflater.inflate(R.layout.dialog_item_edit_car_details,null)
        val  txtTitle = view.findViewById(R.id.edit_title) as TextView
        val  editName = view.findViewById(R.id.edit_name) as EditText
        val  editModel = view.findViewById(R.id.edit_model) as EditText
        val  editPrice = view.findViewById(R.id.edit_price) as EditText
        val  btnUpdate = view.findViewById(R.id.btn_uodate_car) as Button
        if (isUpdate){
            txtTitle.setText(resources.getString(R.string.update_car_details))
            editName.setText(cars.carname)
            editModel.setText(cars.model)
            editPrice.setText(cars.price.toString())
            btnUpdate.setText(resources.getString(R.string.update))
        }else{
            txtTitle.setText(resources.getString(R.string.add_new_car))
            editName.setText("")
            editModel.setText("")
            editPrice.setText("")
            btnUpdate.setText(resources.getString(R.string.add_car))
        }

        builder.setView(view)
        btnUpdate.setOnClickListener {
            if (editName.text.toString().trim().isNotEmpty() && editName.text.toString().trim().isNotBlank() &&
                editModel.text.toString().trim().isNotEmpty() && editModel.text.toString().trim().isNotBlank() &&
                editPrice.text.toString().trim().isNotEmpty() && editPrice.text.toString().trim().isNotBlank()){

                val price = editPrice.text.toString()
                if (isUpdate){
                    val updatedCar: Cars = Cars(editName.text.toString(),cars.id.toString(),cars.image.toString(),editModel.text.toString(),price.toInt())
                    carViewModel.updateCar(updatedCar.id.toInt(),updatedCar)
                }else{
                    val imgUrl: String = "https://w0.peakpx.com/wallpaper/233/1012/HD-wallpaper-rolls-royce-car-cars-ghost-life-luxury-rich-roll-vintage-white.jpg"
                    val addCar: Cars = Cars(editName.text.toString(),"",imgUrl,editModel.text.toString(),price.toInt())
                    carViewModel.addCar(addCar)
                }
                builder.dismiss()

            }else{
                Toast.makeText(this, resources.getString(R.string.mandatory_fields), Toast.LENGTH_LONG).show()
            }

        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    // Function to Delete the Car Item
    private fun deleteCar(id: Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog_layout)

        val  txtTitle = dialog.findViewById(R.id.txt_di_title) as TextView
        val  txtMessage = dialog.findViewById(R.id.txt_message) as TextView
        val  btnCancel = dialog.findViewById(R.id.btn_cancel) as Button
        val  btnOk = dialog.findViewById(R.id.btn_ok) as Button
        txtTitle.text = resources.getString(R.string.remove_car)
        txtMessage.text = resources.getString(R.string.remove_car_msg)

        btnOk.setOnClickListener {
            carViewModel.deleteCar(id)
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}