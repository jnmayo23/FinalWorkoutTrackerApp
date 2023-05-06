package edu.quinnipiac.workouttracker
//import kotlinx.android.synthetic.main.fragment_interval_workout_list.name
//import kotlinx.android.synthetic.main.fragment_interval_workout_list.sets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.quinnipiac.workouttracker.databinding.FragmentWorkoutLogBinding


class WorkoutLogFragment : Fragment() {

    private val viewModel: WorkoutViewModel by activityViewModels {
        WorkoutViewModelFactory(
            (activity?.application as WorkoutApplication).workoutDatabase.getWorkoutDao()
        )
    }
    private lateinit var binding: FragmentWorkoutLogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWorkoutLogBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = WorkoutLogAdapter{
            val action = WorkoutLogFragmentDirections.actionWorkoutLogFragmentToWorkoutDetailsFragment(it.id)
            this.findNavController().navigate(action)

        }

        binding.workoutRv.adapter = adapter
        binding.workoutRv.layoutManager = LinearLayoutManager(this.context)
        viewModel.allWorkouts.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }

        }
        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_workoutLogFragment_to_createNewWorkoutFragment)
        }
    }

}
