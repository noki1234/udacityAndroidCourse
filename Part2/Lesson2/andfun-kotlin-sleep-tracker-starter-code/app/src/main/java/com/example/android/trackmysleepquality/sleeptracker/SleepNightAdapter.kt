package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1 //define different value of id because RecyclerView use them to figure out when view holders are different

class SleepNightAdapter(val clickListener: SleepNightListener) : ListAdapter</*SleepNight, SleepNightAdapter.ViewHolder*/ DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    //No need for ListAdapter (ListAdapter will take care of keeping track of the list or us)
//    var data = listOf<SleepNight>()
//        set(value) {
//            field = value
//            notifyDataSetChanged() //redraw new data
//        }

    //override fun getItemCount(): Int =  data.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // return ViewHolder.from(parent)
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //holder.bind(getItem(position)!!, clickListener)
        when (holder) {
            is ViewHolder -> {
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(nightItem.sleepNightItem, clickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map {DataItem.SleepNightItem(it)}
            }
            withContext(Dispatchers.Main) {
                submitList(items)  //update the UI on Main thread
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

        //No need when we use BindingAdapters where we define those functions in BindingUtils.kt
//        fun bind(item: SleepNight) {
//            val res = itemView.context.resources
//            binding.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
//            binding.sleepQuality.text = convertNumericQualityToString(item.sleepQuality, res)
//
//            binding.qualityImage.setImageResource(when (item.sleepQuality) {
//                0 -> R.drawable.ic_sleep_0
//                1 -> R.drawable.ic_sleep_1
//                2 -> R.drawable.ic_sleep_2
//                3 -> R.drawable.ic_sleep_3
//                4 -> R.drawable.ic_sleep_4
//                5 -> R.drawable.ic_sleep_5
//                else -> R.drawable.ic_sleep_active
//            })
//        }

        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            binding.sleep = item
            binding.clickListener = clickListener
            binding.executePendingBindings() //execute Pending bindings when we use binding adapters in RecyclerView,
            // This must be run on the UI thread. It can be slightly faster to size the views
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                //  val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    //Implement a new listener class at the top level
    class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
        fun onClick(night: SleepNight) = clickListener(night.nightId) //when user click on item - onClick() will be triggered
    }

    sealed class DataItem {
        abstract val id: Long//item callback needs to know the id of each item, that why we declare id

        data class SleepNightItem(val sleepNightItem: SleepNight) : DataItem() {
            override val id = sleepNightItem.nightId
        }

        object Header : DataItem() { //since header has no actual data, we declare it as object - created only ONE instance of header
            override val id = Long.MIN_VALUE
        }
    }

