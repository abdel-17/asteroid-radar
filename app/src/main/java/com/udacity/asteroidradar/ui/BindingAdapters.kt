package com.udacity.asteroidradar.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.ImageOfTheDay

@BindingAdapter("statusIcon")
fun ImageView.bindAsteroidStatusImage(isHazardous: Boolean) {
    setImageResource(
        if (isHazardous)
            R.drawable.ic_status_potentially_hazardous
        else
            R.drawable.ic_status_normal
    )
}

@BindingAdapter("statusIconDescription")
fun ImageView.bindAsteroidStatusImageDescription(isHazardous: Boolean) {
    contentDescription = if (isHazardous)
        context.getString(R.string.potentially_hazardous_asteroid_image)
    else
        context.getString(R.string.non_hazardous_asteroid_image)
}

@BindingAdapter("asteroidStatusImage")
fun ImageView.bindDetailsStatusImage(isHazardous: Boolean) {
    setImageResource(
        if (isHazardous)
            R.drawable.asteroid_hazardous
        else
            R.drawable.asteroid_safe
    )
}

@BindingAdapter("asteroidImageOfTheDayDescription")
fun ImageView.bindImageOfTheDayDescription(imageOfTheDay: ImageOfTheDay?) {
    contentDescription = when (imageOfTheDay) {
        null -> context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
        else -> context.getString(R.string.nasa_picture_of_day_content_description_format, imageOfTheDay.title)
    }
}

@BindingAdapter("astronomicalUnitText")
fun TextView.bindTextViewToAstronomicalUnit(number: Double) {
    text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun TextView.bindTextViewToKmUnit(number: Double) {
    text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun TextView.bindTextViewToDisplayVelocity(number: Double) {
    text = String.format(context.getString(R.string.km_s_unit_format), number)
}
