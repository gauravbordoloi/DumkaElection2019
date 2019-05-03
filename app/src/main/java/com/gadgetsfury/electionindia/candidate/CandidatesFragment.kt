package com.gadgetsfury.electionindia.candidate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.util.dialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_candidates.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gadgetsfury.electionindia.announcement.CandidateViewModel
import com.gadgetsfury.electionindia.db.DatabaseCreator
import com.gadgetsfury.electionindia.util.LinearItemDecoration
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class CandidatesFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    //private lateinit var progressDialog: ProgressDialog

    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_candidates, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*stateSpinner.adapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_list_item_1, Array<String>(1){"Jharkhand"})
        val districts = resources.getStringArray(R.array.districts)
        districtSpinner.adapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_list_item_1, districts)*/

        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(LinearItemDecoration(18, 18, 36, 36))
        val adapter = CandidateAdapter(activity!!)
        recyclerView.adapter = adapter

        val candidateDao = DatabaseCreator().getAppDatabase(activity!!.applicationContext).candidateDao()
        //progressDialog = ProgressDialog(activity!!)


        doAsync {
            val list = candidateDao.getAll()
            uiThread {
                if (list.isNullOrEmpty()) {
                    //progressDialog.show()
                } else {
                    adapter.setList(list)
                }
            }
        }

        val model = ViewModelProviders.of(this).get(CandidateViewModel::class.java)
        model.getCandidates().observe(this, Observer<List<Candidate>>{
            /*if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }*/
            if (!it.isNullOrEmpty()) {
                adapter.setList(it)
                doAsync {
                    candidateDao.clearAll()
                    candidateDao.insertAll(it)
                }
            }
        })

        /*districtSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                adapter.filter.filter(districts[position])
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {

            }

        }*/

    }

    override fun onDestroy() {
        /*if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }*/
        super.onDestroy()
    }

}
