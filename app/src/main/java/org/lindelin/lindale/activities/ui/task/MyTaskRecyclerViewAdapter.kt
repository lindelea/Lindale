package org.lindelin.lindale.activities.ui.task

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import org.lindelin.lindale.R


import org.lindelin.lindale.activities.ui.task.TaskFragment.OnListFragmentInteractionListener
import org.lindelin.lindale.activities.ui.task.dummy.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_task.view.*
import org.lindelin.lindale.models.Task
import org.lindelin.lindale.supports.onProgressChanged
import org.lindelin.lindale.supports.setImageFromUrl
import org.w3c.dom.Text

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyTaskRecyclerViewAdapter(
    private val mValues: List<Task>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyTaskRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Task
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_task, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.userPhotoView.setImageFromUrl(item.user?.photo)
        holder.userNameText.text = item.user?.name
        holder.statusText.text = item.status
        holder.titleText.text = item.title
        holder.progressText.text = "${item.progress}% Completed"
        holder.progressStatusText.text = item.subTaskStatus
        holder.progressBar.onProgressChanged(item.progress)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val userPhotoView: ImageView = mView.userPhotoView
        val userNameText: TextView = mView.userNameText
        val statusText: TextView = mView.statusText
        val titleText: TextView = mView.titleText
        val progressText: TextView = mView.progressText
        val progressStatusText: TextView = mView.progressStatusText
        val progressBar: ProgressBar = mView.progressBar

        override fun toString(): String {
            return super.toString() + " '" + titleText.text + "'"
        }
    }
}
