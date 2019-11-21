package com.example.android.sample.myapplication

import android.media.Image
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


import com.example.android.sample.myapplication.MasterFragment.OnListFragmentInteractionListener
import com.example.android.sample.myapplication.dummy.DummyContent.DummyItem
import io.realm.Realm.init
import io.realm.RealmResults

import kotlinx.android.synthetic.main.fragment_master.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyMasterRecyclerViewAdapter(
    private val mValues: RealmResults<TodoModel>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyMasterRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as DummyItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_master, parent, false)
        return RecyclerView.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.textViewTitle.text = mValues[position]?.title
        holder.textViewDeadline.text = mValues[position]?.deadline
        holder.imageStatus

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textViewTitle : TextView
        val textViewDeadline : TextView
        val imageStatus : ImageView

        var item : TodoModel = null

        init {
            textViewTitle = mView.findViewById(R.id.textViewTitle) as TextView
            textViewDeadline = mView.findViewById(R.id.textViewDeadline) as TextView
            imageStatus = mView.findViewById(R.id.imageStatus) as ImageView
        }
    }
}
