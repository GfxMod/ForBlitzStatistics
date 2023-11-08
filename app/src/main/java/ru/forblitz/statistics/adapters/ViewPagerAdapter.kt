package ru.forblitz.statistics.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.forblitz.statistics.AchievementsFragment
import ru.forblitz.statistics.ClanFragment
import ru.forblitz.statistics.StatisticsFragment
import ru.forblitz.statistics.TanksFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, private val tabCount: Int):
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                StatisticsFragment()
            }
            1 -> {
                AchievementsFragment()
            }
            2 -> {
                ClanFragment()
            }
            else -> {
                TanksFragment()
            }
        }
    }

    override fun getCount(): Int {
        return tabCount
    }

}
