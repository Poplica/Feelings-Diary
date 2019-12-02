package com.example.feelingsdiary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.graphics.Color.parseColor
import android.R.id.message
import android.graphics.Color


class SearchActivityAdapter(private val context: Context) : BaseAdapter() {
    private var entryList = ArrayList<JournalEntry>()

    internal class ViewHolder {
        var dateTime: TextView? = null
        var moodRating: TextView? = null
        var entry: TextView? = null
        var tags: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val newView: View
        val holder: ViewHolder

        val currEntry = entryList[position]

        if (null == convertView) {
            holder = ViewHolder()

            newView = LayoutInflater.from(context).inflate(R.layout.entry_list, parent, false)

            holder.dateTime = newView.findViewById(R.id.datetime)
            holder.moodRating = newView.findViewById(R.id.mood)
            holder.tags = newView.findViewById(R.id.tags_string)
            holder.entry = newView.findViewById(R.id.message_string)
            newView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            newView = convertView
        }

        holder.dateTime!!.text = "Date and Time: " + currEntry.getDate()
        holder.moodRating!!.text = "Mood: " + currEntry.getRating()
        holder.tags!!.text = "Tags: " + currEntry.getTags().joinToString(",")
        holder.entry!!.text = "Thoughts: " + currEntry.getEntry()

        val rating = Integer.parseInt(currEntry.getRating())
        when {
            rating >= 4 -> {
                holder.dateTime!!.setTextColor(parseColor("#FFFFFF"))
                holder.entry!!.setTextColor(parseColor("#FFFFFF"))
                holder.moodRating!!.setTextColor(parseColor("#FFFFFF"))
                holder.tags!!.setTextColor(parseColor("#FFFFFF"))
            }
            rating > 2 -> {
                holder.dateTime!!.setTextColor(parseColor("#C2D5EE"))
                holder.entry!!.setTextColor(parseColor("#C2D5EE"))
                holder.moodRating!!.setTextColor(parseColor("#C2D5EE"))
                holder.tags!!.setTextColor(parseColor("#C2D5EE"))
            }
            else -> {
                holder.dateTime!!.setTextColor(parseColor("#97B4D7"))
                holder.entry!!.setTextColor(parseColor("#97B4D7"))
                holder.moodRating!!.setTextColor(parseColor("#97B4D7"))
                holder.tags!!.setTextColor(parseColor("#97B4D7"))
            }
        }

        return newView
    }

    override fun getItem(position: Int): JournalEntry {
        return entryList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return entryList.size
    }

    fun clearList() {
        entryList.clear()
        notifyDataSetInvalidated()
    }

    fun addList(lst: List<JournalEntry>) {
        entryList.addAll(lst)
        notifyDataSetInvalidated()
    }
}