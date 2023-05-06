package edu.quinnipiac.workouttracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import edu.quinnipiac.workouttracker.databinding.FragmentWorkoutDetailsBinding

class WorkoutDetailsFragment : Fragment() {
    private val navigationArgs: WorkoutDetailsFragmentArgs by navArgs()

    private val viewModel: WorkoutViewModel by activityViewModels {
        WorkoutViewModelFactory(
            (activity?.application as WorkoutApplication).workoutDatabase.getWorkoutDao()
        )
    }
    private var _binding: FragmentWorkoutDetailsBinding? = null
    private val binding get() = _binding!!

    lateinit var workout: Workout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWorkoutDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        viewModel.retrieveWorkout(id).observe(this.viewLifecycleOwner) {  selectedItem ->
            workout = selectedItem
            bind(workout)
        }
    }

    private fun bind(workout: Workout){
        binding.apply {
            itemDate.text = workout.date
            itemName.text = workout.name
            itemType.text = workout.type
            itemDuration.text = workout.duration
            itemDescription.text = workout.description
        }
    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}