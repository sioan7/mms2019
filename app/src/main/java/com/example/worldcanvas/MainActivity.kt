package com.example.worldcanvas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private val MY_REQUEST_CODE: Int = 1
    lateinit var providers: List<AuthUI.IdpConfig>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        providers = listOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build()
        )
        if (FirebaseAuth.getInstance().currentUser == null) {
            showSignInOptions()
        } else {
            setContentView(R.layout.activity_main)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this, user!!.email, Toast.LENGTH_SHORT).show()

                setContentView(R.layout.activity_main)
            } else {
                Toast.makeText(this, response!!.error!!.message, Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(), MY_REQUEST_CODE
        )
    }

    fun goToCanvas(view: View) {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun goToScene(view: View) {
        val intent = Intent(this, ArActivity::class.java)

        startActivity(intent)
        finish()
    }

    fun logout(view: View) {
        AuthUI.getInstance().signOut(this@MainActivity)
                .addOnCompleteListener {
                    showSignInOptions()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
    }


    override fun onBackPressed() {
        finish()
    }


}
