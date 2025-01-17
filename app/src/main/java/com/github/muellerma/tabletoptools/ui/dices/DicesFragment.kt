package com.github.muellerma.tabletoptools.ui.dices

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.muellerma.tabletoptools.MainActivity
import com.github.muellerma.tabletoptools.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider

class DicesFragment : Fragment() {
    private lateinit var slider: Slider
    private lateinit var result: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dices, container, false)
        slider = root.findViewById(R.id.dices_slider)
        val sliderHint = root.findViewById<TextView>(R.id.dices_slider_hint)
        sliderHint.text = getString(R.string.dices_slider_hint, slider.value.toInt())
        slider.addOnChangeListener { _, value, _ ->
            sliderHint.text = getString(R.string.dices_slider_hint, value.toInt())
        }
        result = root.findViewById(R.id.dices_result_text)

        mapOf(
                R.id.dices_button_3 to 3,
                R.id.dices_button_4 to 4,
                R.id.dices_button_6 to 6,
                R.id.dices_button_8 to 8,
                R.id.dices_button_10 to 10,
                R.id.dices_button_12 to 12,
                R.id.dices_button_20 to 20,
                R.id.dices_button_100 to 100
        ).forEach { dice ->
            root.findViewById<MaterialButton>(dice.key).apply {
                setOnClickListener {
                    roll(dice.value)
                }
            }
        }

        root.findViewById<MaterialButton>(R.id.dices_button_10_2).apply {
            setOnClickListener {
                roll(10, 10)
            }
        }

        return root
    }

    override fun onResume() {
        (activity as MainActivity?)?.let {
            result.text = it.diceResults
        }
        super.onResume()
    }

    private fun roll(max: Int, multiplier: Int = 1) {
        val resultString = StringBuilder()
        val numberOfDices = slider.value.toInt()
        resultString.append("$numberOfDices x ${getString(R.string.dices_d_d, max)}: ")

        val firstDice = (1..max).shuffled().first().times(multiplier)
        Log.d(TAG, "Rolled $firstDice")
        resultString.append(firstDice).append(" ")
        var sum = firstDice

        for (i in 2..numberOfDices) {
            val rolledDice = (1..max).shuffled().first().times(multiplier)
            Log.d(TAG, "Rolled $rolledDice")
            sum += rolledDice
            resultString.append("+ ").append(rolledDice).append(" ")
        }
        if (numberOfDices > 1) {
            resultString.append("= $sum")
        }
        resultString.appendLine().append(result.text)
        resultString.toString().apply {
            (activity as MainActivity?)?.diceResults = this
            result.text = this
        }
    }

    companion object {
        private var TAG = DicesFragment::class.java.simpleName
    }
}