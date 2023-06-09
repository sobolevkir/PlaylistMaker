package com.sobolevkir.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {

    lateinit var inputSearch: EditText
    private var savedSearchQueryText = ""

    companion object {
        private const val SEARCH_QUERY_TEXT = "SEARCH_QUERY_TEXT"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(
            SEARCH_QUERY_TEXT,
            savedSearchQueryText)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedSearchQueryText = savedInstanceState.getString(SEARCH_QUERY_TEXT,"")
        inputSearch.setText(savedSearchQueryText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        inputSearch = findViewById(R.id.inputSearch)
        val backButton = findViewById<ImageButton>(R.id.button_back)
        val clearButton = findViewById<ImageView>(R.id.clearButton)
        backButton.setOnClickListener{ finish() }
        clearButton.visibility = clearButtonVisibility(inputSearch.text)
        clearButton.setOnClickListener{
            inputSearch.setText("")
            closeKeyboard()
        }
        val searchInputTextWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }
            override fun afterTextChanged(s: Editable?) {
                savedSearchQueryText = inputSearch.text.toString()
            }
        }
        inputSearch.addTextChangedListener(searchInputTextWatcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun closeKeyboard() {
        this.currentFocus?.let {view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}