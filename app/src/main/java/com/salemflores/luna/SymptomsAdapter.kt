package com.salemflores.luna
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import database.Symptom




class SymptomsAdapter(private var symptoms: List<Symptom>) : RecyclerView.Adapter<SymptomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymptomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_symptom, parent, false)
        return SymptomViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: SymptomViewHolder, position: Int) {
        val symptom = symptoms[position]
        holder.bind(symptom)
    }

    override fun getItemCount(): Int {
        return symptoms.size
    }

    fun updateSymptoms(newSymptoms: List<Symptom>) {
        Log.d("SymptomsFragment", "Symptom list: $newSymptoms")
        symptoms = newSymptoms
        notifyDataSetChanged()
    }

    fun setSymptoms(symptomsList: List<Symptom>) {
        this.symptoms = symptomsList
        notifyDataSetChanged()
    }

}


class SymptomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    //private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    private val symptomTextView: TextView = itemView.findViewById(R.id.symptomTextView)

    fun bind(symptom: Symptom) {
       // dateTextView.text = symptom.date
        symptomTextView.text = symptom.symptom
    }
}