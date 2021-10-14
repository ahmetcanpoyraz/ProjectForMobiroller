package com.example.mobirollerodev.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mobirollerodev.view.CoatsFragment
import com.example.mobirollerodev.view.ShoesFragment
import com.example.mobirollerodev.view.TshirtsFragment

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
            0->{
                TshirtsFragment()
            }
            1->{
                CoatsFragment()
            }
            2->{
                ShoesFragment()
            }
            else->{
                Fragment()
            }
        }
    }
}