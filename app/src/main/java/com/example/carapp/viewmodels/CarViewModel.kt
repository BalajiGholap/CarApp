package com.example.carapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.model.Cars
import com.example.carapp.repository.CarRepo
import com.example.carapp.uistate.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(private val carRepo: CarRepo): ViewModel(){
    val cars: LiveData<NetworkResult<List<Cars>>>
        get() = carRepo.cars
    val singleCar: LiveData<NetworkResult<Cars>>
        get() = carRepo.sinlgleCar

    fun getCars(){
        viewModelScope.launch {
            delay(1000)
            carRepo.getCars()
        }
    }
    fun addCar(cars: Cars){
        viewModelScope.launch {
            delay(1000)
            carRepo.addCar(cars)
        }
    }
    fun updateCar(id: Int, cars: Cars){
        viewModelScope.launch {
            carRepo.updateCar(id,cars)
        }
    }
    fun deleteCar(id: Int){
        viewModelScope.launch {
            carRepo.deleteCar(id)
        }
    }
}