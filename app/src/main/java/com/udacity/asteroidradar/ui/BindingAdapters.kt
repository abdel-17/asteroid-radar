package com.udacity.asteroidradar.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.network.ImageOfTheDay

@BindingAdapter("imageOfTheDay")
fun ImageView.bindImageOfTheDay(imageOfTheDay: ImageOfTheDay?) {
    if (imageOfTheDay == null || !imageOfTheDay.isImage) return
    Picasso.get()
        .load(imageOfTheDay.url)
        .placeholder(R.drawable.placeholder_picture_of_day)
        .into(this)
}

@BindingAdapter("statusIcon")
fun ImageView.bindStatusIcon(isHazardous: Boolean) {
    setImageResource(
        if (isHazardous)
            R.drawable.ic_status_potentially_hazardous
        else
            R.drawable.ic_status_normal
    )
}

@BindingAdapter("statusImage")
fun ImageView.bindStatusImage(isHazardous: Boolean) {
    setImageResource(
        if (isHazardous)
            R.drawable.asteroid_hazardous
        else
            R.drawable.asteroid_safe
    )
}

@BindingAdapter("imageOfTheDayDescription")
fun ImageView.bindImageOfTheDayDescription(imageOfTheDay: ImageOfTheDay?) {
    contentDescription = when (imageOfTheDay) {
        null -> context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
        else -> context.getString(R.string.nasa_picture_of_day_content_description_format, imageOfTheDay.title)
    }
}

@BindingAdapter("statusImageDescription")
fun ImageView.bindStatusImageDescription(isHazardous: Boolean) {
    contentDescription = if (isHazardous)
        context.getString(R.string.potentially_hazardous_asteroid_image)
    else
        context.getString(R.string.non_hazardous_asteroid_image)
}

@BindingAdapter("statusDescription")
fun ImageView.bindStatusDescription(isHazardous: Boolean) {
    contentDescription = if (isHazardous)
        context.getString(R.string.potentially_hazardous_asteroid)
    else
        context.getString(R.string.non_hazardous_asteroid)
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
