package com.mfa.pengaduanmasyarakat.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mfa.pengaduanmasyarakat.R
import com.mfa.pengaduanmasyarakat.models.Complaint
import kotlinx.android.synthetic.main.activity_detail_complaint.*


class DetailComplaintActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_complaint)

        val complaint: Complaint = intent.getParcelableExtra<Complaint>("complaint")!!

        tvComplaint.setText(complaint.complaint)

    }
}