package com.msuslo.firebasewithmvvm.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.msuslo.firebasewithmvvm.ui.note.NoteListingFragment
import com.msuslo.firebasewithmvvm.ui.profile.ProfileFragment
import com.msuslo.firebasewithmvvm.ui.queue.QueueFragment
import com.msuslo.firebasewithmvvm.ui.task.TaskListingFragment
import com.msuslo.firebasewithmvvm.utils.FireDatabase.QUEUE
import com.msuslo.firebasewithmvvm.utils.HomeTabs


class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = HomeTabs.values().size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            HomeTabs.QUEUE.index -> QueueFragment.newInstance(HomeTabs.QUEUE.name)
            HomeTabs.NOTES.index -> NoteListingFragment.newInstance(HomeTabs.NOTES.name)
            HomeTabs.TASKS.index -> TaskListingFragment.newInstance(HomeTabs.TASKS.name)
            HomeTabs.PROFILE.index -> ProfileFragment.newInstance(HomeTabs.PROFILE.name)
            else -> throw IllegalStateException("Fragment not found")
        }
    }
}