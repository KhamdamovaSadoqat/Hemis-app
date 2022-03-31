package com.software.hemis.presenter.auth.introduction

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.example.hemis.R
import com.example.hemis.databinding.ItemIntroductionBinding

class ViewPagerAdapter(var itemList: List<Intro>, var context: Context) :
    PagerAdapter() {

    override fun getCount(): Int {
        return itemList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val binding = ItemIntroductionBinding.inflate(inflater, null, false)
        binding.apply {
            ivIntro.setImageDrawable(ContextCompat.getDrawable(context, itemList[position].image))
            tvHeader.text = context.getString(itemList[position].header)
            tvSubHeader.text = context.getString(itemList[position].subHeader)
        }
        container.addView(binding.root, 0)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}

data class Intro(
    @DrawableRes
    val image: Int,
    @StringRes
    val header: Int,
    @StringRes
    val subHeader: Int
)