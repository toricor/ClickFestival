package com.github.toricor.clickfestival.top

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.toricor.clickfestival.R
import com.github.toricor.clickfestival.databinding.FragmentTopBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TopFragment : Fragment() {

    private var _binding: FragmentTopBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTopBinding.inflate(inflater, container, false)

        binding.buttonStart.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.TopFragment) {
                findNavController().navigate(R.id.action_TopFragment_to_GameFragment)
            }
        }

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}