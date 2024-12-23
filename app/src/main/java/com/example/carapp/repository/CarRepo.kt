package com.example.carapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.carapp.model.Cars
import com.example.carapp.retrofit.CarApi
import com.example.carapp.uistate.NetworkResult
import javax.inject.Inject

class CarRepo @Inject constructor(private val carApi: CarApi) {
    private val _cars: MutableLiveData<NetworkResult<List<Cars>>> = MutableLiveData()
    val cars: LiveData<NetworkResult<List<Cars>>> get() = _cars

    private val _singleCar: MutableLiveData<NetworkResult<Cars>> = MutableLiveData()
    val sinlgleCar: LiveData<NetworkResult<Cars>> get() = _singleCar

    suspend fun getCars(){
        _cars.value = NetworkResult.Loading()
        try {
            val result = carApi.getCars()
            if (result.isSuccessful && result.body()!=null){
                Log.i("Responsssee","${result.body()}")
                //_cars.postValue(result.body())
                _cars.value = NetworkResult.Success(result.body()!!)
            }else{
                _cars.value = NetworkResult.Failure(result.message())
            }
        }catch (e: Exception){
            _cars.value = NetworkResult.Failure(e.message.toString())
        }

    }

     suspend fun updateCar(id: Int, cars: Cars){
        _singleCar.value = NetworkResult.Loading()
         try {
             val result = carApi.updateCar(id,cars)
             if (result.isSuccessful && result.body()!=null){
                 Log.i("UpdateResponsssee","${result.body()}")
                 //_singleCar.postValue(result.body())
                 _singleCar.value = NetworkResult.Success(result.body()!!)
             }else{
                 _singleCar.value = NetworkResult.Failure(result.message())
             }
         }catch (e: Exception){
             _singleCar.value = NetworkResult.Failure(e.message.toString())
         }

    }
    suspend fun addCar(cars: Cars){
        _singleCar.value = NetworkResult.Loading()
        try {
            val result = carApi.addCar(cars)
            if (result.isSuccessful && result.body()!=null){
                Log.i("AddCarResponsssee","${result.body()}")
                _singleCar.value = NetworkResult.Success(result.body()!!)
            }else{
                _singleCar.value = NetworkResult.Failure(result.message())
            }
        }catch (e: Exception){
            _singleCar.value = NetworkResult.Failure(e.message.toString())
        }
    }
    suspend fun deleteCar(id: Int){
        _singleCar.value = NetworkResult.Loading()
        try {
            val result = carApi.deleteCar(id)
            if (result.isSuccessful && result.body()!=null){
                Log.i("DeleteCarResponsssee","${result.body()}")
                _singleCar.value = NetworkResult.Success(result.body()!!)
            }else{
                _singleCar.value = NetworkResult.Failure(result.message())
            }
        }catch (e: Exception){
            _singleCar.value = NetworkResult.Failure(e.message.toString())
        }
    }

}