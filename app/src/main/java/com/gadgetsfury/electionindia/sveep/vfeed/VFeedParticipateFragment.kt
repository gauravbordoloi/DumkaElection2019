package com.gadgetsfury.electionindia.sveep.vfeed

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
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
import com.gadgetsfury.electionindia.util.WYSISYGeditorActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_vfeed_participate.*
import kotlinx.android.synthetic.main.fragment_vfeed_participate.imageView
import kotlinx.android.synthetic.main.fragment_vfeed_participate.textViewContent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VFeedParticipateFragment : Fragment() {

    private val PICK_IMAGE = 112
    private val TAG = this.javaClass.simpleName
    private var EDITOR_CODE = 111

    private var image: String? = null
    private var content: String? = null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vfeed_participate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialog(activity!!)

        btnAddContent.setOnClickListener {
            val intent = Intent(activity!!, WYSISYGeditorActivity::class.java)
            if (!content.isNullOrEmpty()) {
                intent.putExtra("content", content)
            }
            startActivityForResult(intent, EDITOR_CODE)
        }

        btnUpload.setOnClickListener {

            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"
            val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"
            val chooserIntent = Intent.createChooser(getIntent, getString(R.string.select_image))
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
            startActivityForResult(chooserIntent, PICK_IMAGE)
        }

        btnSubmit.setOnClickListener {
            if (validate()) {

                val name = inputName.editText!!.text.toString()

                val map = HashMap<String, String>()
                map["name"] = name
                if (!image.isNullOrEmpty()) {
                    map["image"] = image!!
                }
                if (!content.isNullOrEmpty()) {
                    map["content"] = content!!
                }

                progressDialog.show()

                ApiClient.getClient().create(ApiInterface::class.java)
                    .postVotersFeed(map)
                    .enqueue(object : Callback<ServerResponse?> {
                        override fun onFailure(call: Call<ServerResponse?>, t: Throwable) {
                            progressDialog.dismiss()
                            Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_LONG).show()
                            Log.e(TAG, t.message)
                        }

                        override fun onResponse(call: Call<ServerResponse?>, response: Response<ServerResponse?>) {
                            progressDialog.dismiss()
                            if (response.body() != null) {
                                if (response.body()!!.code == "success") {
                                    Toast.makeText(activity!!, getString(R.string.content_submitted_successfully), Toast.LENGTH_LONG).show()
                                    inputName.editText!!.text = null
                                    textViewContent.text = null
                                    textViewContent.visibility = View.GONE
                                    Glide.with(imageView).load(R.drawable.placeholder).into(imageView)
                                    imageView.visibility = View.GONE
                                } else {
                                    Toast.makeText(activity!!, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_LONG).show()
                                Log.e(TAG, "Null response")
                            }
                        }
                    })

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDITOR_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                content = data.getStringExtra("content")
                textViewContent.visibility = View.VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewContent.text = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    textViewContent.text = Html.fromHtml(content)
                }
            } else {
                Log.e(TAG, "Null data received from content intent")
            }
        }

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
                        Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                    }
                    return@Continuation ref.downloadUrl
                }).addOnCompleteListener { task ->
                    progressDialog.dismiss()
                    if (task.isSuccessful) {
                        image = task.result.toString()
                        Toast.makeText(activity!!, getString(R.string.image_uploaded_successfully), Toast.LENGTH_SHORT).show()
                        imageView.visibility = View.VISIBLE
                        Glide.with(imageView).load(uri).into(imageView)
                    } else {
                        Log.e(TAG, task.exception!!.message)
                        Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Log.e(TAG, "Null data received from image picker intent")
            }
        }
    }

    private fun validate(): Boolean {

        var valid = true

        val name = inputName.editText!!.text.toString()

        if (name.isEmpty()) {
            inputName.error = getString(R.string.this_field_is_required)
            valid = false
        } else {
            inputName.error = null
        }

        if (image.isNullOrEmpty() && content.isNullOrEmpty()) {
            Toast.makeText(activity!!, getString(R.string.image_or_content_is_required), Toast.LENGTH_SHORT).show()
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
