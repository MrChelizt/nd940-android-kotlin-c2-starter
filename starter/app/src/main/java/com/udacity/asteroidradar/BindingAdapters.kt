package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.ListViewAdapter

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = imageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = imageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("pictureOfTheDay")
fun bindImage(imgView: ImageView, pictureOfDay: PictureOfTheDay?) {
    pictureOfDay?.let {
        if (it.mediaType == "image"){
            val imageUri = pictureOfDay.imageUrl.toUri().buildUpon().scheme("https").build()
            Picasso.with(imgView.context)
                .load(imageUri)
                .placeholder(R.drawable.placeholder_picture_of_day).into(imgView)
            imgView.contentDescription = pictureOfDay.title
        }
        else{
            imgView.setImageResource(R.drawable.ic_broken_image)
            imgView.contentDescription = imgView.context.getString(R.string.image_of_the_day_not_found)
        }
    }
}

@BindingAdapter("pictureOfTheDayTitle")
fun bindTitle(textView: TextView, pictureOfDay: PictureOfTheDay?){
    pictureOfDay?.let {
        if (it.mediaType == "image") {
            textView.text = pictureOfDay.title
            textView.contentDescription = pictureOfDay.title
        } else {
            textView.text = textView.context.getString(R.string.image_of_the_day_not_found)
            textView.contentDescription = textView.context.getString(R.string.image_of_the_day_not_found)
        }
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as ListViewAdapter
    adapter.submitList(data)
}
