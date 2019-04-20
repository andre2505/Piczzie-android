package com.ziggy.kdo.utils

import androidx.annotation.Nullable
import androidx.databinding.InverseMethod
import java.text.SimpleDateFormat
import java.util.*

object ConverterDate {

    @JvmStatic
    @InverseMethod("stringToDate")
    @Nullable
    fun DateToString( date: Date?): String? {
       if(date != null){
        return SimpleDateFormat("dd MMMM YYYY", Locale.getDefault()).format(date)
       }
        return null
    }

    @JvmStatic
    fun stringToDate(value: String): Date? {
        return Date()
    }

    @JvmStatic
    @InverseMethod("stringToDouble")
    fun doubleToString(price: Double?): String?{
        if(price != null){
            return price.toString()
        }
        return null
    }

    @JvmStatic
    fun stringToDouble(price: String?):Double? {
        if(price != null){
            return price.toDouble()
        }
        return null
    }



}