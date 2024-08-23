package com.example.localdatabaseproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.localdatabaseproject.databinding.ItemExerciseBinding
import com.example.localdatabaseproject.models.exercises.ExerciseResult
import com.example.localdatabaseproject.utils.CommonUtils.loadGif

class ExerciseAdapter(private val list: List<ExerciseResult>) :
    RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ExerciseResult) {

            binding.apply {
                tvExerciseName.text = data.name
                tvEquipment.text = "Equipment: ${data.equipment}"
                tvTargetMuscle.text = "Target: ${data.target}"

                // Handling Instructions
                val instructionsText = StringBuilder("Instructions: ")
                data.instructions?.let {
                    for (i in it.indices) {
                        val instruction = it[i]
                        instructionsText.append("\n${i + 1}. $instruction")
                    }
                }
                tvInstructions.text = instructionsText.toString()

                // Handling Secondary Muscles
                val secondaryMusclesText = StringBuilder("Secondary: ")
                data.secondaryMuscles?.let {
                    for (i in it.indices) {
                        val secondaryMuscle = it[i]
                        if (i == 0) {
                            secondaryMusclesText.append(secondaryMuscle)
                        } else {
                            secondaryMusclesText.append(", $secondaryMuscle")
                        }
                    }
                }
                tvSecondaryMuscle.text = secondaryMusclesText.toString()

                // Loading GIF
                ivExerciseGif.loadGif(data.gifUrl ?: "")
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseAdapter.ViewHolder {
        return ViewHolder(
            ItemExerciseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}