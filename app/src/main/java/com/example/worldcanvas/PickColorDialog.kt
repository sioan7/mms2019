package com.example.worldcanvas

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import com.google.android.flexbox.FlexboxLayout
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class PickColorDialog
@JvmOverloads constructor(
    context: Context
): Dialog(context){

    override fun onCreate(savedInstanceState: Bundle?) {
//        return activity?.let {
//            // Use the Builder class for convenient dialog construction
//            val builder = AlertDialog.Builder(it)
//            builder.setMessage("Fire missels?")
//                .setPositiveButton("Fire",
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // FIRE ZE MISSILES!
//                    })
//                .setNegativeButton("Cancel",
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // User cancelled the dialog
//                    })
//            // Create the AlertDialog object and return it
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")

//        val id = R.id.color_picker_dialog
//        val lay = R.layout.color_picker_dialog
//
//        val cpDialog = findViewById<FlexboxLayout>(id)
//
//        for (colorButton in cpDialog.children) {
//            colorButton.setOnClickListener {
//                (context as CanvasActivity).changeColor(colorButton.id)
//            }
//        }
        setContentView(R.layout.color_picker_dialog)
    }
}