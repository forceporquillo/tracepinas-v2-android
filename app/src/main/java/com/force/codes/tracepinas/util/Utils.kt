/*
 * Created by Force Porquillo on 9/2/20 9:29 PM
 */

package com.force.codes.tracepinas.util

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.graphics.Color
import android.icu.text.DecimalFormat
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.force.codes.tracepinas.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utilities class that has some helper methods.
 * This class doesn't need an instance of a class itself.
 *
 * @author Force Porquillo
 */
object Utils {
  @JvmStatic
  fun getLongDate(
      milliseconds: Long
  ): String {
    val formatter = formatDate(
        "EEE, dd MMM yyyy hh:mm:ss aaa"
    )
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milliseconds
    return formatter.format(
        calendar.time
    )
  }

  @JvmStatic
  fun formatNumber(
      number: String
  ): String {
    if (number.isNotEmpty()) {
      val value = number.toDouble()
      return NumberFormat.getNumberInstance(
          Locale.US
      )
        .format(value)
    }
    return "0"
  }

  @JvmStatic
  val sDKInt: Int
    get() = VERSION.SDK_INT

  @JvmStatic
  fun requiresSdkInt(
      version: Int
  ): Boolean {
    return sDKInt > version
  }

  @JvmStatic
  val deviceModel: String
    get() = Build.MODEL

  /**
   * @return thread count by multiplying
   * device processors by 2
   */
  @JvmStatic
  val threadCount: Int
    get() = Runtime.getRuntime()
      .availableProcessors() * 2

  @JvmStatic
  val date: String
    get() {
      val calendar = Calendar
        .getInstance().time

      @SuppressLint("SimpleDateFormat")
      val dateFormat = formatDate(
          "yyyy-mm-dd"
      )
      return dateFormat.format(calendar)
    }

  /**
   * A helper method to manually align view
   * margin at runtime. This converts dp to px.
   *
   * @return pixel based on device dpi and resolution.
   */
  @JvmStatic
  fun dpToPx(
      context: Context?,
      marginWidth: Int?,
      useComplexUnit: Boolean?
  ): Int {
    val resources = context!!.resources
    useComplexUnit!!.let {
      if (it) {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            marginWidth!!.toFloat(),
            resources.displayMetrics
        )
          .toInt()
      }
      return resources
        .displayMetrics
        .widthPixels / marginWidth!!
    }
  }

  @SuppressLint("SimpleDateFormat")
  @JvmStatic
  fun formatDate(
      format: String?
  ): SimpleDateFormat {
    return SimpleDateFormat(format)
  }

  @JvmStatic
  val todayDate: Date
    get() = Date()

  @JvmStatic
  fun animationUtils(
      canAnimate: Boolean,
      context: Context?
  ): Animation {
    return if (canAnimate) {
      AnimationUtils
        .loadAnimation(
            context, R.anim.pop_up_slide_down
        )
    } else AnimationUtils
      .loadAnimation(
          context, R.anim.pop_up_slide_up
      )
  }

  @JvmStatic
  fun getDeviceWidth(
      context: Context?
  ): Int {
    return context!!
      .resources
      .displayMetrics
      .widthPixels
  }

  @JvmStatic
  fun customAnim(
      context: Context?,
      pushAnim: Int,
      pullAnim: Int
  ): ActivityOptions {
    return ActivityOptions.makeCustomAnimation(
          context,
          pushAnim,
          pullAnim
      )
  }

  fun getResColorId(
      context: Context?,
      colorId: Int
  ): Int {
    return ContextCompat.getColor(context!!, colorId)
  }

  @RequiresApi(api = VERSION_CODES.N)
  fun toPercent(
      num: Double,
      total: Double
  ): String {
    val df = DecimalFormat("##%")
    val percent = num / total
    return df.format(percent)
  }

  fun spannableString(
      color: String?,
      string: String
  ): SpannableString {
    val spannableString = SpannableString(string)
    val colorSpan: ForegroundColorSpan
    when (color) {
        "BLUE" -> {
            colorSpan = ForegroundColorSpan(
                Color.rgb(50, 120, 210)
            )
            spannableString.setSpan(
                colorSpan, 1,
                string.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        "RED" -> {
            colorSpan = ForegroundColorSpan(
                Color.rgb(255, 93, 93)
            )
            spannableString.setSpan(
                colorSpan, 1,
                string.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    return spannableString
  }
}