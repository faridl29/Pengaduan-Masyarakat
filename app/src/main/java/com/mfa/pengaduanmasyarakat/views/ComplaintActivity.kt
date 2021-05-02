package com.mfa.pengaduanmasyarakat.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mfa.pengaduanmasyarakat.R
import com.mfa.pengaduanmasyarakat.adapters.ComplaintAdapter
import com.mfa.pengaduanmasyarakat.databinding.ActivityComplaintBinding
import com.mfa.pengaduanmasyarakat.utils.Constants
import com.mfa.pengaduanmasyarakat.view_models.ComplaintState
import com.mfa.pengaduanmasyarakat.view_models.ComplaintViewModel
import kotlinx.android.synthetic.main.activity_complaint.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class ComplaintActivity : AppCompatActivity() {
    private lateinit var activityComplaintBinding: ActivityComplaintBinding
    private lateinit var complaintViewModel: ComplaintViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComplaintBinding = ActivityComplaintBinding.inflate(layoutInflater)
        setContentView(activityComplaintBinding.root)
        
        complaintViewModel = ViewModelProviders.of(this).get(ComplaintViewModel::class.java)
        complaintViewModel.getComplaints().observe(this, Observer {
            rvComplaint.adapter?.let { adapter ->
                if (adapter is ComplaintAdapter) {
                    adapter.setComplaints(it)
                }
            }
        })
        
        activityComplaintBinding.fab.setOnClickListener {
            val intent = Intent(this, CreateComplaintActivity::class.java)
            startActivity(intent)
        }

        activityComplaintBinding.btnSignout.setOnClickListener {
            logout()
        }
        
        setupRecycler()
        hideShowFab()
        
        complaintViewModel.getState().observer(this, Observer {
            handleUIState(it)
        })
    }
    
    private fun hideShowFab(){
        rvComplaint.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fab.isShown) fab.hide()
            }
        
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) fab.show()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }
    
    private fun handleUIState(it: ComplaintState){
        when(it){
            is ComplaintState.IsLoading -> isLoading(it.state)
            is ComplaintState.IsError -> {
                toast(it.err)
                isLoading(false)
            }
            is ComplaintState.NotAuthenticated -> {
                logout()
            }
        }
    }

    private fun logout(){
        Constants.clearToken(this)
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }).also { finish() }
    }

    private fun toast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    private fun isLoading(state: Boolean){
        if(state){
            loading.visibility = View.VISIBLE
        }else {
            loading.visibility = View.GONE
        }
    }
    
    override fun onResume() {
        super.onResume()
        complaintViewModel.fetchAllComplaint(Constants.getToken(this))
    }
    
    fun setupRecycler(){
        rvComplaint.apply {
            layoutManager = LinearLayoutManager(this@ComplaintActivity)
            adapter = ComplaintAdapter(mutableListOf(), this@ComplaintActivity)
        }
    }
}