package edu.quinnipiac.workouttracker

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import edu.quinnipiac.workouttracker.databinding.FragmentCreateNewWorkoutBinding


class CreateNewWorkoutFragment : Fragment() {

    private val viewModel: WorkoutViewModel by activityViewModels {
        WorkoutViewModelFactory(
            (activity?.application as WorkoutApplication).workoutDatabase.getWorkoutDao()
        )
    }
    lateinit var workout: Workout

    private var _binding: FragmentCreateNewWorkoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentCreateNewWorkoutBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addWorkoutButton.setOnClickListener {
            addNewWorkout()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
    private fun isEntryValid(): Boolean{
        return viewModel.isEntryValid(
            binding.newDate.text.toString(),
            binding.newName.text.toString(),
            binding.newType.text.toString(),
            binding.newDuration.text.toString(),
            binding.newDescription.text.toString()
        )
    }
    private fun addNewWorkout(){
        if(isEntryValid()){
            viewModel.addNewWorkout(
                binding.newDate.text.toString(),
                binding.newName.text.toString(),
                binding.newType.text.toString(),
                binding.newDuration.text.toString(),
                binding.newDescription.text.toString()
            )
            binding.newDate.setText("")
            binding.newName.setText("")
            binding.newType.setText("")
            binding.newDuration.setText("")
            binding.newDescription.setText("")
            Toast.makeText(requireActivity(), "Workout has been saved to Room Database.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireActivity(), "Workout could not be saved. Please enter values in all fields.", Toast.LENGTH_SHORT).show()
        }

    }

}