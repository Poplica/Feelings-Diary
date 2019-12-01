package com.example.feelingsdiary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SearchActivity : AppCompatActivity() {
    private var searchResults: LinearLayout? = null
    private var mAdapter: SearchActivityAdapter? = null
    private var lstView: ListView? = null
    private var userEntryList: ArrayList<JournalEntry>? = arrayListOf()
    private var resultList: ArrayList<JournalEntry>? = arrayListOf()

    private val root = FirebaseDatabase.getInstance().reference
    private val pointer = root.child(FirebaseAuth.getInstance().currentUser!!.uid)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchResults = findViewById<View>(R.id.search_layout) as LinearLayout

        val userInput = findViewById<View>(R.id.search_editText) as EditText
        userEntryList!!.clear()
        mAdapter = SearchActivityAdapter(applicationContext)
        lstView = ListView(applicationContext)
        lstView!!.setFooterDividersEnabled(true)
        lstView!!.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this@SearchActivity, ViewEntryActivity::class.java)
            intent.putExtra("entry", mAdapter!!.getItem(position))
            startActivityForResult(intent, 0)
        }
        lstView!!.adapter = mAdapter

        updateUserEntryList()

        val buttonSearch = findViewById<View>(R.id.search_button)
        buttonSearch.setOnClickListener {
            // hides keyboard after clicking search
            val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)

            val tagToSearch = userInput.text.toString()

            resultList!!.clear()

            for (entry in userEntryList!!) {
                if (entry.getTags().contains(tagToSearch)) {
                    resultList!!.add(entry)
                }
            }

            // empty string case
            if (tagToSearch == "" || resultList!!.size == 0) {
                setNoResults()
            } else {
                setResultsFound(resultList!!)
            }
        }
    }

    // updates userEntryList to contain
    private fun updateUserEntryList() {
        pointer.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userEntryList!!.clear()
                // iterating over all simpleDate entries of current user
                for (simpleDates in dataSnapshot.children) {
                    for (entry in simpleDates.children) {
                        userEntryList!!.add(entry.getValue(JournalEntry::class.java)!!)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun setNoResults() {
        searchResults!!.removeAllViews()
        mAdapter!!.clearList()
        val noResultsView = TextView(this)
        noResultsView.setTextColor(Color.WHITE)
        noResultsView.setText(R.string.no_search_results)
        searchResults!!.addView(noResultsView)
    }

    private fun setResultsFound(resultList: ArrayList<JournalEntry>) {
        searchResults!!.removeAllViews()
        mAdapter!!.clearList()
        mAdapter!!.addList(resultList)
        searchResults!!.addView(lstView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 2) {
            updateUserEntryList()
            val tagToSearch = findViewById<View>(R.id.search_editText) as EditText
            resultList!!.clear()

            for (entry in userEntryList!!) {
                if (entry.getTags().contains(tagToSearch.text.toString())) {
                    resultList!!.add(entry)
                }
            }

            if (resultList!!.size > 0) {
                setResultsFound(resultList!!)
            } else {
                setNoResults()
            }
        }
    }
}
