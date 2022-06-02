package com.github.toricor.clickfestival.top

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.toricor.clickfestival.R
import com.github.toricor.clickfestival.databinding.FragmentResultBinding

/**
 * show result
 */
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentResultBinding.inflate(inflater, container, false)

        binding.playAgain.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.ResultFragment) {
                findNavController().navigate(R.id.action_ResultFragment_to_GameFragment)
            }
        }

        binding.backHome.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.ResultFragment) {
                findNavController().navigate(R.id.action_ResultFragment_to_TopFragment)
            }
        }

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}