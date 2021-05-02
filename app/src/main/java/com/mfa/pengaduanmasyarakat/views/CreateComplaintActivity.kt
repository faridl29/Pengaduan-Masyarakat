package com.mfa.pengaduanmasyarakat.views

import android.Manifest
import android.R.string
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mfa.pengaduanmasyarakat.BuildConfig
import com.mfa.pengaduanmasyarakat.R
import com.mfa.pengaduanmasyarakat.adapters.ComplaintAdapter
import com.mfa.pengaduanmasyarakat.view_models.ComplaintState
import com.mfa.pengaduanmasyarakat.view_models.ComplaintViewModel
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_create_complaint.*


class CreateComplaintActivity : AppCompatActivity() {
    private var filePath: String? = null
    private var fileName: String? = null
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private var mImageUri: Uri? = null
    private var category: String? = null
    
    private lateinit var complaintViewModel: ComplaintViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_complaint)
    
        llSelectPhoto.setOnClickListener {
            addPhoto()
        }
    
        verifyStoragePermissions(this)
        
        setSpinnerValue()
    
        ibSubmit.setOnClickListener {
            validate_field()
        }
    
        complaintViewModel = ViewModelProviders.of(this).get(ComplaintViewModel::class.java)
    
        complaintViewModel.getState().observer(this, Observer {
            handleUIState(it)
        })
    }
    
    private fun handleUIState(it: ComplaintState){
        when(it){
            is ComplaintState.Reset -> {
                setComplaintError(null)
            }
            is ComplaintState.IsError -> {
                isLoading(false)
                toast(it.err)
            }
            is ComplaintState.ShowToast -> {
                toast(it.message)
            }
            is ComplaintState.Validate -> {
                it.complaint.let {
                    setComplaintError(it)
                }
            }
            is ComplaintState.IsSuccess -> {
                startActivity(Intent(this, ComplaintActivity::class.java)).also {
                    finish()
                }
            }
            is ComplaintState.IsLoading -> {
                isLoading(it.state)
            }
        }
    }
    
    private fun validate_field(){
        val complaint = etComplaint.text.toString().trim()
        if (complaintViewModel.validate(complaint)){
            complaintViewModel.create(filePath, complaint, category)
        }
    }
    
    fun setSpinnerValue(){
        val arrayList: ArrayList<String> = ArrayList()
        arrayList.add("Permohonan")
        arrayList.add("Pelaporan")
        arrayList.add("Lain-Lain")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
        spinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                category = parent.getItemAtPosition(position).toString()
            }
        
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }
    
    fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission: Int = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE)
        }
    }
    
    fun addPhoto() {
        CropImage.activity()
                  .setGuidelines(CropImageView.Guidelines.ON)
                  .setAutoZoomEnabled(true)
                  .setAspectRatio(1, 1)
                  .setCropShape(CropImageView.CropShape.OVAL)
                  .setRequestedSize(1280, 1280)
                  .start(this)
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                filePath = resultUri.path
                fileName = filePath?.lastIndexOf("/")?.plus(1)?.let { filePath?.substring(it) }
                mImageUri = resultUri
                Picasso.get().load(mImageUri)
                          .networkPolicy(NetworkPolicy.NO_CACHE)
                          .memoryPolicy(MemoryPolicy.NO_CACHE)
                          .error(R.mipmap.ic_launcher)
                          .into(ivLogo)
                Picasso.get().invalidate(mImageUri)
                tvLogoName.text = fileName
                ivLogo.setPadding(0, 0, 0, 0)
                tvLogoName.setError(null)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                if (BuildConfig.DEBUG) error.printStackTrace()
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setComplaintError(err: String?){
        etComplaint.error = err
    }
    
    private fun isLoading(state: Boolean){
        if (state){
            ibSubmit.isEnabled = false
            loading.isIndeterminate = true
        }else {
            loading.apply {
                isIndeterminate = false
                progress = 0
            }
            ibSubmit.isEnabled = true
        }
    }
    
    private fun toast(message: String?){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}