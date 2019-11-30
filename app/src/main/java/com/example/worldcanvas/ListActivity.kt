package com.example.worldcanvas


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_list.*


class ListActivity : AppCompatActivity() {

    lateinit var listOfAnimals: Array<String>
    lateinit var listOfResources: Array<Int>
    lateinit var providers: List<AuthUI.IdpConfig>
    private val REQUEST_CODE: Int = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        providers = listOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build()
        )
        setupActionBar()
        searchBar.setHint("Search...")
        searchBar.setSpeechMode(true)

        setupArray()
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfAnimals)
        mListView.adapter = adapter

        searchBar.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(charSequence)
            }
        })

        mListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ -> goToCanvas(position) }
    }


    private fun setupActionBar() {
        findViewById<Toolbar>(R.id.toolbar).apply {
            setSupportActionBar(this)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setTitle("List of animals")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener {
                    showSignInOptions()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }

        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this, user!!.email, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, response!!.error!!.message, Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun goToCanvas( position: Int) {
        val intent = Intent(this, CanvasActivity::class.java)
        val elementClicked = listOfResources[position]
        intent.putExtra("Canvas",elementClicked)
        intent.putExtra("Position", position)
        startActivity(intent)
    }

    private fun showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(), REQUEST_CODE
        )
    }


    private fun setupArray() {
        listOfAnimals = arrayOf(
                "Bear",
                "Cat",
                "Cow",
                "Dog",
                "Elephant",
                "Ferret",
                "Hippopotamus",
                "Horse",
                "Koala bear",
                "Lion",
                "Reindeer",
                "Wolverine"
        )

        listOfResources = arrayOf(
                R.raw.bear_image,
                R.raw.cat_image,
                R.raw.cow_image,
                R.raw.dog_image,
                R.raw.elephant_image,
                R.raw.ferret_image,
                R.raw.hippopotamus_image,
                R.raw.horse_image,
                R.raw.koala_bear_image,
                R.raw.lion_bmp,
                R.raw.reindeer_image,
                R.raw.wolverine_image
        )
    }


}


