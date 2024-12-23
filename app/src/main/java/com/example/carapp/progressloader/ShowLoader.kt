package com.example.carapp.progressloader

import android.app.ProgressDialog
import android.content.Context

class ShowLoader {
    companion object{
        private lateinit var mProgressDialog: ProgressDialog
        fun showProgress(context: Context, title: String, message: String){
            mProgressDialog = ProgressDialog(context)
            mProgressDialog.setTitle(title)
            mProgressDialog.setMessage(message)
            mProgressDialog.show()
        }
        fun hideProgress(){
            if (mProgressDialog!=null){
                mProgressDialog.dismiss()
            }

        }

    }
}