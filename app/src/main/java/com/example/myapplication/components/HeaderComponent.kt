package com.example.myapplication.components

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.myapplication.R

class HeaderComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val leftIconImageView: ImageView
    private val rightIconImageView: ImageView
    private var leftIntentClass: Class<*>? = null
    private var rightIntentClass: Class<*>? = null
    private var searchIconImageView : ImageView;
    private var searchIconIntentClass : Class<*>? = null;
    init {
        inflate(context, R.layout.header_layout, this)
        leftIconImageView = findViewById(R.id.leftIcon);
        rightIconImageView = findViewById(R.id.rightIcon);
        searchIconImageView = findViewById(R.id.searchIcon)

        searchIconImageView.setOnClickListener {
            searchIconIntentClass?.let { intentClass ->
                val intent = Intent(context, intentClass)
                context.startActivity(intent) }
        }

        leftIconImageView.setOnClickListener{
            leftIntentClass?.let { intentClass ->
                val intent = Intent(context, intentClass)
                context.startActivity(intent)
            }
        }
        rightIconImageView.setOnClickListener {
            rightIntentClass?.let { intentClass ->
                val intent = Intent(context, intentClass)
                context.startActivity(intent)
            }
        }
    }

   fun setIcons(leftIconResId: Int, rightIconResId: Int?, searchIconsResId : Int?, leftIntentClass: Class<*>?, rightIntentClass: Class<*>?, searchIntentClass: Class<*>?) {
        leftIconImageView.setImageResource(leftIconResId)
       searchIconsResId?.let {
           searchIconImageView.setImageResource(it)
       } ?: run {
           searchIconImageView.setImageResource(android.R.color.transparent)
       }
        rightIconResId?.let {
            rightIconImageView.setImageResource(it)
        } ?: run {
            rightIconImageView.setImageResource(android.R.color.transparent)
        }

        this.searchIconIntentClass =  searchIntentClass;
        this.leftIntentClass = leftIntentClass
        this.rightIntentClass = rightIntentClass
    }
}
