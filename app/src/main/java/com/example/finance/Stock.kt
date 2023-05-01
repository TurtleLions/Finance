package com.example.finance

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Stock(var ticker:String, var name:String, var exchange:String):Parcelable{

}
