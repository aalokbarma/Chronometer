package com.example.stopwatch

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.NumberPicker
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var isRunning = false
    private var minutes: String? = "00.00.00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var lapseList = ArrayList<String>()
        var arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lapseList)
        binding.lapsList.adapter = arrayAdapter
        binding.lapButton.setOnClickListener {
            if(isRunning){
                lapseList.add(binding.chronometer.text.toString())
                arrayAdapter.notifyDataSetChanged()
            }
        }
        binding.clockIcon.setOnClickListener{
            var dialogue = Dialog(this)
            dialogue.setContentView(R.layout.dialogue)
            var numberPicker = dialogue.findViewById<NumberPicker>(R.id.numberPicker)
            numberPicker.minValue = 0
            numberPicker.maxValue = 5
            dialogue.findViewById<Button>(R.id.setTimeButton).setOnClickListener {
                minutes = numberPicker.value.toString()
                binding.stopwatchTimerText.text = dialogue.findViewById<NumberPicker>(R.id.numberPicker).value.toString() + " mins"
                dialogue.dismiss()
            }
            dialogue.show()
        }
        binding.runButton.setOnClickListener{
            if (!isRunning) {
                isRunning = true
                if(!minutes.equals("00.00.00")){
                    var totalTime = minutes!!.toInt()*60*1000L
                    var countdown = 1000L
                    binding.chronometer.base = SystemClock.elapsedRealtime()+totalTime
                    binding.chronometer.format = "%S %S"
                    binding.chronometer.onChronometerTickListener = Chronometer.OnChronometerTickListener {
                        var elapsedTime = SystemClock.elapsedRealtime()-binding.chronometer.base
                        if (elapsedTime>=totalTime){
                            binding.chronometer.stop()
                            isRunning = false
                            binding.runButton.text = "RUN"
                        }
                    }
                }
                else {
                    isRunning = true
                    binding.chronometer.base = SystemClock.elapsedRealtime()
                    binding.runButton.text = "STOP"
                    binding.chronometer.start()
                }
            } else {
                binding.chronometer.stop()
                isRunning = false
                binding.runButton.text = "RUN"
            }
        }
    }
}