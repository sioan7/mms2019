package com.example.worldcanvas

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView


class CustomAdapter(private val context: Activity, animalName: ArrayList<String>, private val mapToimage: LinkedHashMap<String, Int>)
    : BaseAdapter(), Filterable {


    var filteredAnimalName: ArrayList<String> = animalName
    var originalData: ArrayList<String> = animalName


    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_animal_list, null, true)

        val titleText = rowView.findViewById(R.id.text_item) as TextView
        val imageView = rowView.findViewById(R.id.image_item) as ImageView


        titleText.text = filteredAnimalName[position]
        mapToimage[filteredAnimalName[position]]?.let { imageView.setImageResource(it) }

        return rowView
    }

    override fun getCount() = filteredAnimalName.size

    override fun getItem(position: Int) = filteredAnimalName[position]
    override fun getItemId(p0: Int): Long {
        return 0
    }


    override fun getFilter() = AnimalFilter(originalData, this)


}
