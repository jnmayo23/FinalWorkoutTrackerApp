package edu.quinnipiac.workouttracker

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import edu.quinnipiac.workouttracker.databinding.FragmentIntervalTimerBinding
import edu.quinnipiac.workouttracker.databinding.FragmentWorkoutDetailsBinding

// Fragment has all the logic for interval timer
class IntervalTimerFragment : Fragment() {

    private val navigationArgs: WorkoutDetailsFragmentArgs by navArgs()

    private val viewModel: IntervalWorkoutViewModel by activityViewModels {
        IntervalWorkoutViewModelFactory(
            (activity?.application as WorkoutApplication).intervalWorkoutDatabase.getIntervalWorkoutDao()
        )
    }

    private val workoutViewModel: WorkoutViewModel by activityViewModels {
        WorkoutViewModelFactory(
            (activity?.application as WorkoutApplication).workoutDatabase.getWorkoutDao()
        )
    }

    private var _binding: FragmentIntervalTimerBinding? = null
    private val binding get() = _binding!!

    lateinit var intervalWorkout: IntervalWorkout

    var name = ""
    var numSets = 0
    var count = 0
    var warmUpTime = 0
    var highIntensityTime = 0
    var lowIntensityTime = 0
    var coolDownTime = 0
    var state = 0
    var lastPaused: Long = 0
    var isRunning: Boolean = false

    // Add key ints for interval state
    val WARM_UP = 0
    val HIGH_INTENSITY = 1
    val LOW_INTENSITY = 2
    val COOL_DOWN = 3
    val COMPLETE = 4

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentIntervalTimerBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        viewModel.retrieveIntervalWorkout(id).observe(this.viewLifecycleOwner) {  selectedItem ->
            intervalWorkout = selectedItem
            bind(intervalWorkout)
        }

        binding.saveButton.setEnabled(false)

        // Starts interval timer
        binding.startButton.setOnClickListener {
            if (state == WARM_UP && !isRunning || state == COMPLETE && !isRunning) {
                startWarmUpTimer(warmUpTime.toLong())
            } else if (state == HIGH_INTENSITY && !isRunning) {
                startHighIntensityTimer(highIntensityTime.toLong())
            } else if (state == LOW_INTENSITY && !isRunning) {
                startLowIntensityTimer(lowIntensityTime.toLong())
            } else if (state == COOL_DOWN && !isRunning) {
                startCoolDownTimer(coolDownTime.toLong())
            }
            isRunning = true
        }

        // Resets interval timer to beginning
        binding.resetButton.setOnClickListener {
            isRunning = false
            bind(intervalWorkout)
        }

        binding.saveButton.setOnClickListener {
            workoutViewModel.addNewWorkout(
                getDate(),
                binding.intervalWorkoutName.text.toString(),
                "Interval Workout",
                getDuration(),
                getDescription()
            )
            Toast.makeText(requireActivity(), "Interval Workout has been saved to Workout Log.", Toast.LENGTH_SHORT).show()
        }

    }

    fun startWarmUpTimer(time: Long) {
        state = WARM_UP
        binding.intervalState.text = "Warm Up"
        val warmUpTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Used for formatting digit to be in 2 digits only
                val f: NumberFormat = DecimalFormat("00")
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                if (state == WARM_UP && isRunning) {
                    binding.intervalTime.setText((f.format(min)).toString() + ":" + f.format(sec))
                }
            }
            override fun onFinish() {
                binding.intervalTime.setText("00:00")
                startHighIntensityTimer(highIntensityTime.toLong())
            }
        }
        warmUpTimer.start()
    }

    fun startHighIntensityTimer(time: Long) {
        state = HIGH_INTENSITY
        binding.intervalState.text = "High Intensity"
        var highIntensityTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Used for formatting digit to be in 2 digits only
                val f: NumberFormat = DecimalFormat("00")
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                if (state == HIGH_INTENSITY && isRunning) {
                    binding.intervalTime.setText((f.format(min)).toString() + ":" + f.format(sec))
                }
            }
            override fun onFinish() {
                binding.intervalTime.setText("00:00")
                startLowIntensityTimer(lowIntensityTime.toLong())
            }
        }
        highIntensityTimer.start()
    }

    fun startLowIntensityTimer(time: Long) {
        state = LOW_INTENSITY
        binding.intervalState.text = "Low Intensity"
        var lowIntensityTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Used for formatting digit to be in 2 digits only
                val f: NumberFormat = DecimalFormat("00")
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                if (state == LOW_INTENSITY && isRunning) {
                    binding.intervalTime.setText((f.format(min)).toString() + ":" + f.format(sec))
                }
            }
            override fun onFinish() {
                binding.intervalTime.setText("00:00")
                count += 1
                if (count > numSets) {
                    startCoolDownTimer(coolDownTime.toLong())
                } else {
                    binding.setCount.text = "Set " + count + "/" + intervalWorkout.sets.toString()
                    startHighIntensityTimer(highIntensityTime.toLong())
                }
            }
        }
        lowIntensityTimer.start()
    }

    fun startCoolDownTimer(time: Long) {
        state = COOL_DOWN
        binding.intervalState.text = "Cool Down"
        var coolDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Used for formatting digit to be in 2 digits only
                val f: NumberFormat = DecimalFormat("00")
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                if (state == COOL_DOWN && isRunning) {
                    binding.intervalTime.setText((f.format(min)).toString() + ":" + f.format(sec))
                }
            }
            override fun onFinish() {
                binding.intervalTime.setText("00:00")
                state = COMPLETE
                binding.intervalState.text = "Workout Complete!"
                binding.saveButton.setEnabled(true)
            }
        }
        coolDownTimer.start()
    }

    /**
     * Deletes the current item and navigates to the list fragment.
     */
    private fun deleteItem() {
        //delete from database
        findNavController().navigateUp()
    }

    private fun bind(intervalWorkout: IntervalWorkout){
        binding.apply {
            intervalWorkoutName.text = intervalWorkout.name
            state = WARM_UP
            intervalState.text = "Warm Up"
            warmUpTime = intervalWorkout.warmUpTime * 1000
            highIntensityTime = intervalWorkout.highIntensityTime * 1000
            lowIntensityTime = intervalWorkout.lowIntensityTime * 1000
            coolDownTime = intervalWorkout.coolDownTime * 1000
            val f: NumberFormat = DecimalFormat("00")
            val min = warmUpTime / 60000 % 60
            val sec = warmUpTime / 1000 % 60
            intervalTime.text = f.format(min).toString() + ":" + f.format(sec)
            numSets = intervalWorkout.sets
            count = 1
            setCount.text = "Set " + count + "/" + intervalWorkout.sets.toString()
        }
    }

    fun getDate(): String {
        var dateString = ""
        val f: NumberFormat = DecimalFormat("00")
        val c = Calendar.getInstance()
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val year = c.get(Calendar.YEAR)
        dateString = f.format(month + 1) + "-" + f.format(day) + "-" + year.toString()
        return dateString
    }

    fun getDuration(): String {
        var durationString = ""
        val totalTime = warmUpTime + ((highIntensityTime + lowIntensityTime) * numSets) + coolDownTime
        val f: NumberFormat = DecimalFormat("00")
        val min = totalTime / 60000 % 60
        val sec = totalTime / 1000 % 60
        durationString = f.format(min).toString() + ":" + f.format(sec)
        return durationString
    }

    fun getDescription(): String {
        val descriptionString = "Sets: " + numSets +
                "\nWarm Up Time (in seconds): " + warmUpTime / 1000 +
                "\nHigh Intensity Time (in seconds: " + highIntensityTime / 1000 +
                "\nLow Intensity Time (in seconds: " + lowIntensityTime / 1000 +
                "\nCool Down Time (in seconds: " + coolDownTime / 1000
        return descriptionString
    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}