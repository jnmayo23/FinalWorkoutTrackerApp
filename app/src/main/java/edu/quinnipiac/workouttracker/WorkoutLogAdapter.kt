package edu.quinnipiac.workouttracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.quinnipiac.workouttracker.Workout

import edu.quinnipiac.workouttracker.databinding.WorkoutRvItemBinding


class WorkoutLogAdapter(private val onItemClicked: (Workout) -> Unit):
    ListAdapter<Workout, WorkoutLogAdapter.WorkoutViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(
            WorkoutRvItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class WorkoutViewHolder(private var binding: WorkoutRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: Workout) {
            binding.apply {
                itemDate.text = workout.date
                itemName.text = workout.name
            }
        }
    }

    companion object {
        private  val DiffCallback = object : DiffUtil.ItemCallback<Workout>(){
            override fun areItemsTheSame(oldWorkout: Workout, newWorkout: Workout): Boolean {
                return oldWorkout == newWorkout
            }

            override fun areContentsTheSame(oldWorkout: Workout, newWorkout: Workout): Boolean {
                return oldWorkout.name == newWorkout.name
            }

        }
    }
}