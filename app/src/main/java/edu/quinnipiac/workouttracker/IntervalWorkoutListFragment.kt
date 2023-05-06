package edu.quinnipiac.workouttracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.quinnipiac.workouttracker.databinding.FragmentIntervalWorkoutListBinding


class IntervalWorkoutListFragment : Fragment() {

    private val viewModel: IntervalWorkoutViewModel by activityViewModels {
        IntervalWorkoutViewModelFactory(
            (activity?.application as WorkoutApplication).intervalWorkoutDatabase.getIntervalWorkoutDao()
        )
    }
    private lateinit var binding: FragmentIntervalWorkoutListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentIntervalWorkoutListBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = IntervalWorkoutListAdapter{
            val action = IntervalWorkoutListFragmentDirections.actionIntervalWorkoutListFragmentToIntervalTimerFragment(it.id)
            this.findNavController().navigate(action)

        }

        binding.intervalWorkoutRv.adapter = adapter
        binding.intervalWorkoutRv.layoutManager = LinearLayoutManager(this.context)
        viewModel.allIntervalWorkouts.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }

        }
        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_intervalWorkoutListFragment_to_createNewIntervalWorkoutFragment)
        }
    }

}
