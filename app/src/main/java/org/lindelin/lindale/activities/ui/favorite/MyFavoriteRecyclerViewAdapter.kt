package org.lindelin.lindale.activities.ui.favorite

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.lindelin.lindale.R


import org.lindelin.lindale.activities.ui.favorite.FavoriteFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_favorite.view.*
import org.lindelin.lindale.models.Project
import org.lindelin.lindale.supports.setImageFromUrl

/**
 * [RecyclerView.Adapter] that can display a [Project] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyFavoriteRecyclerViewAdapter(
    private val mValues: List<Project>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyFavoriteRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Project
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_favorite, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.typeText.text = item.type
        holder.titleText.text = item.title
        holder.progressText.text = "${item.progress}%"
        holder.taskStatusText.text = item.taskStatus
        holder.todoStatusText.text = item.todoStatus
        holder.imageView.setImageFromUrl(item.image)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val typeText: TextView = mView.userNameText
        val titleText: TextView = mView.titleText
        val progressText: TextView = mView.progressText
        val taskStatusText: TextView = mView.taskStatusText
        val todoStatusText: TextView = mView.todoStatusText
        val imageView: ImageView = mView.image

        override fun toString(): String {
            return super.toString() + " '" + titleText.text + "'"
        }
    }
}
