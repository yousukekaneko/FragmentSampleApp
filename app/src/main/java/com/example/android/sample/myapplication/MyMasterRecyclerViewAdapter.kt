package com.example.android.sample.myapplication


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sample.myapplication.MasterFragment.OnListFragmentInteractionListener
import io.realm.RealmResults
import java.text.SimpleDateFormat
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener]
 */
class MyMasterRecyclerViewAdapter(
    private val mValues: RealmResults<TodoModel>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyMasterRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_master, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]!!
        holder.textViewTitle.text = mValues[position]?.title
        holder.textViewDeadline.text = MyApplication.appContext.getString(R.string.deadline) + " : " + mValues[position]?.deadline

        val changedDeadline = SimpleDateFormat("yyyy/MM/dd").parse(mValues[position]?.deadline)
        val today = Date()
        if (today >= changedDeadline) {
            holder.imageStatus.setImageResource(R.drawable.ic_warning_black_24dp)
        } else {
            holder.imageStatus.setImageResource(R.drawable.ic_work_black_24dp)
        }

        holder.mView.setOnClickListener{
            mListener?.onListItemClicked(holder.mItem!!)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val textViewTitle : TextView
        val textViewDeadline : TextView
        val imageStatus : ImageView

        var mItem : TodoModel? = null

        init {
            textViewTitle = mView.findViewById(R.id.textViewTitle) as TextView
            textViewDeadline = mView.findViewById(R.id.textViewDeadline) as TextView
            imageStatus = mView.findViewById(R.id.imageStatus) as ImageView
        }
    }
}
