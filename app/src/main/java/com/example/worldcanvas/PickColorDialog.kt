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
        setContentView(R.layout.color_picker_dialog)
    }
}