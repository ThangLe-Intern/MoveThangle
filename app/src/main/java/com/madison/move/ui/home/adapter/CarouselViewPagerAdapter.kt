package com.madison.move.ui.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.madison.move.R
import com.madison.move.ui.home.FeaturedFragment
import com.madison.move.ui.offlinechannel.CommentFragment

class CarouselViewPagerAdapter(
    var listFragment: ArrayList<FeaturedFragment>,
    private val viewPager2: ViewPager2
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(fragment: FeaturedFragment) {
            itemView.findViewById<ConstraintLayout>(R.id.layout_feature_fragment).setOnClickListener {
                val activity: AppCompatActivity = it.context as AppCompatActivity
                val commentFragment = CommentFragment()
                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame_main, commentFragment)
                    .commit()

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_featured, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == listFragment.lastIndex) {
            viewPager2.post(runnable)
        }

        (holder as ViewHolder).onBind(listFragment[position])

    }


    private val runnable = Runnable {
//        viewPager2.currentItem = 0
        listFragment.addAll(listFragment)
        notifyDataSetChanged()
    }
}