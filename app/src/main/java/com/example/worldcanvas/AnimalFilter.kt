package com.example.worldcanvas

import android.widget.Filter


class AnimalFilter (private val animalName: ArrayList<String>, private val adapter: MyListAdapter) : Filter() {
    private val filteredAnimalName: MutableList<String>

    init {
        this.filteredAnimalName = ArrayList()
    }

     override fun performFiltering(constraint: CharSequence): FilterResults {
         filteredAnimalName.clear()
         val results = FilterResults()


         val query = ArrayList<String>()


         for (animal in animalName) {
             if (animal.toLowerCase().contains(constraint.toString().toLowerCase())) query.add(animal)
         }

         results.values = query
         results.count = query.size

         return results
     }


    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapter.filteredAnimalName = results.values as ArrayList<String>
        adapter.notifyDataSetChanged()
        adapter.notifyDataSetInvalidated()
    }

//    override fun publishResults(constraint: CharSequence, results: FilterResults) {
//        //adapter.setList(filteredContactList)
//        //adapter.notifyDataSetChanged()
//    }
}