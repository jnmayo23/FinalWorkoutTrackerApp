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
import edu.quinnipiac.workouttracker.databinding.FragmentCreateNewIntervalWorkoutBinding


class CreateNewIntervalWorkoutFragment : Fragment() {

    private val viewModel: IntervalWorkoutViewModel by activityViewModels {
        IntervalWorkoutViewModelFactory(
            (activity?.application as WorkoutApplication).intervalWorkoutDatabase.getIntervalWorkoutDao()
        )
    }
    lateinit var intervalWorkout: IntervalWorkout

    private var _binding: FragmentCreateNewIntervalWorkoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentCreateNewIntervalWorkoutBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addIntervalWorkoutButton.setOnClickListener {
            addNewIntervalWorkout()
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
            binding.newName.text.toString(),
            binding.newSets.text.toString(),
            binding.newWarmUpTime.text.toString(),
            binding.newHighIntensityTime.text.toString(),
            binding.newLowIntensityTime.text.toString(),
            binding.newCoolDownTime.text.toString()
        )
    }
    private fun addNewIntervalWorkout(){
        if(isEntryValid()){
            viewModel.addNewIntervalWorkout(
                binding.newName.text.toString(),
                binding.newSets.text.toString(),
                binding.newWarmUpTime.text.toString(),
                binding.newHighIntensityTime.text.toString(),
                binding.newLowIntensityTime.text.toString(),
                binding.newCoolDownTime.text.toString()
            )
            binding.newName.setText("")
            binding.newSets.setText("")
            binding.newWarmUpTime.setText("")
            binding.newHighIntensityTime.setText("")
            binding.newLowIntensityTime.setText("")
            binding.newCoolDownTime.setText("")
            Toast.makeText(requireActivity(), "Interval workout has been saved to Room Database.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireActivity(), "Interval workout could not be saved. Please enter values in all fields.", Toast.LENGTH_SHORT).show()
        }
    }

}