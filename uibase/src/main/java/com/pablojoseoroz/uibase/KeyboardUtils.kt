package com.pablojoseoroz.uibase

import android.R
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

/**
 * Created by Pablo on 29/04/2016.
 */
object KeyboardUtils {
    fun showKeyBoard(edt: View, vararg delayMilis: Long) {
        Handler().postDelayed({
            try {
                edt.requestFocus()
                val imm =
                    edt.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, if (delayMilis != null && delayMilis.size > 0) delayMilis[0] else 100)
    }

    fun hideKeyBoard(context: Context?, windowToken: IBinder?) {
        try {
            val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideKeyBoard(context: Context, edt: EditText) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(edt.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideKeyBoard(activity: Activity) {
        try {
            if (isShown(activity)) hideKeyBoard(
                activity,
                activity.findViewById<View>(R.id.content).windowToken
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideKeyBoard(fragment: Fragment) {
        try {
            val activity: Activity? = fragment.activity
            if (isShown(activity)) hideKeyBoard(
                activity,
                activity!!.findViewById<View>(R.id.content).windowToken
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isShown(view: View?): Boolean {
        if (view == null) {
            return false
        }
        try {
            val inputManager =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return inputManager.isActive(view)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun isShown(context: Context?): Boolean {
        try {
            val inputManager =
                context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return inputManager.isActive
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}