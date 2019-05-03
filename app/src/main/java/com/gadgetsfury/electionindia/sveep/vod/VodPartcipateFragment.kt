package com.gadgetsfury.electionindia.sveep.vod

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import com.gadgetsfury.electionindia.settings.ServerResponse
import com.gadgetsfury.electionindia.util.dialog.ProgressDialog
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_vod_partcipate.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VodPartcipateFragment : Fragment() {

    private val PICK_IMAGE = 114
    private val TAG = this.javaClass.simpleName

    private var image: String? = null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vod_partcipate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialog(activity!!)

        btnSubmit.setOnClickListener {

            if (validate()) {

                val name = inputName.editText!!.text.toString()
                val content = inputContent.editText!!.text.toString()

                val map = HashMap<String, String>()
                map["name"] = name
                map["image"] = image!!
                map["content"] = content

                ApiClient.getClient().create(ApiInterface::class.java)
                    .participateInVOD(map)
                    .enqueue(object: Callback<ServerResponse?> {
                        override fun onFailure(call: Call<ServerResponse?>, t: Throwable) {
                            progressDialog.dismiss()
                            Toast.makeText(activity!!, "Some error occurred while uploading your content!", Toast.LENGTH_LONG).show()
                            Log.e(TAG, t.message)
                        }

                        override fun onResponse(call: Call<ServerResponse?>, response: Response<ServerResponse?>) {
                            progressDialog.dismiss()
                            if (response.body() != null) {
                                if (response.body()!!.code == "success") {
                                    Toast.makeText(activity!!, "Your content was submitted successfully", Toast.LENGTH_LONG).show()
                                    Glide.with(imageView).load(R.drawable.placeholder).into(imageView)
                                    imageView.visibility = View.GONE
                                    inputName.editText!!.text = null
                                    inputContent.editText!!.text = null
                                } else {
                                    Toast.makeText(activity!!, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(activity!!, "Some error occurred while uploading your content!", Toast.LENGTH_LONG).show()
                                Log.e(TAG, "Null response")
                            }
                        }
                    })

            }

        }

        btnUpload.setOnClickListener {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"
            val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"
            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
            startActivityForResult(chooserIntent, PICK_IMAGE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {

                progressDialog.show()
                val uri = data.data!!

                val ref = FirebaseStorage.getInstance().reference.child("${System.currentTimeMillis()}.jpg")
                val uploadTask = ref.putFile(uri)
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        progressDialog.dismiss()
                        Log.e(TAG, task.exception!!.message)
                        Toast.makeText(activity!!, "Some error occurred while uploading image!", Toast.LENGTH_SHORT).show()
                    }
                    return@Continuation ref.downloadUrl
                }).addOnCompleteListener { task ->
                    progressDialog.dismiss()
                    if (task.isSuccessful) {
                        image = task.result.toString()
                        Toast.makeText(activity!!, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
                        imageView.visibility = View.VISIBLE
                        Glide.with(imageView).load(uri).into(imageView)
                    } else {
                        Log.e(TAG, task.exception!!.message)
                        Toast.makeText(activity!!, "Some error occurred while uploading image!", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Log.e(TAG, "Null data received from image picker intent")
            }
        }
    }

    private fun validate():Boolean {

        var valid = true

        val name = inputName.editText!!.text.toString()
        val content = inputContent.editText!!.text.toString()

        if (name.isEmpty()) {
            inputName.error = "This field is required"
            valid = false
        } else {
            inputName.error = null
        }

        if (content.isEmpty()) {
            inputContent.error = "This field is required"
            valid = false
        } else {
            inputContent.error = null
        }

        if (image.isNullOrEmpty()) {
            Toast.makeText(activity!!, "Your image is required!", Toast.LENGTH_SHORT).show()
            valid = false
        }

        return valid

    }

    override fun onDestroy() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
        super.onDestroy()
    }

}
