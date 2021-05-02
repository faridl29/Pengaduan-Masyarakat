package com.mfa.pengaduanmasyarakat.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfa.pengaduanmasyarakat.R
import com.mfa.pengaduanmasyarakat.models.Complaint
import com.mfa.pengaduanmasyarakat.views.CreateComplaintActivity
import com.mfa.pengaduanmasyarakat.views.DetailComplaintActivity
import kotlinx.android.synthetic.main.item_complaint_list.view.*

class ComplaintAdapter(private var complaints: MutableList<Complaint>, private var context: Context) : RecyclerView.Adapter<ComplaintAdapter.ViewHolder>() {
    
    fun setComplaints(r: List<Complaint>){
        complaints.clear()
        complaints.addAll(r)
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_complaint_list, parent, false))
    }
    
    override fun onBindViewHolder(holder: ComplaintAdapter.ViewHolder, position: Int) {
        return holder.bind(complaints[position], context)
    }
    
    override fun getItemCount(): Int = complaints.size
    
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(complaint: Complaint, context: Context){
            itemView.tvComplaint.setText(complaint.complaint)
            itemView.tvCategory.setText(complaint.category)
            itemView.tvCreatedAt.setText(complaint.created_at)

            when(complaint.category){
                "Permintaan" -> itemView.lytCategory.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                "Pelaporan" -> itemView.lytCategory.setBackgroundColor(context.resources.getColor(R.color.red))
                "Lain-lain" -> itemView.lytCategory.setBackgroundColor(context.resources.getColor(R.color.green))
            }

            itemView.setOnClickListener {
                val intent = Intent(context, DetailComplaintActivity::class.java)
                intent.putExtra("complaint", complaint)
                context.startActivity(intent)
            }
        }
    }
    
}