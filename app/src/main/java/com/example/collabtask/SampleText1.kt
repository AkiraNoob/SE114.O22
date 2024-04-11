package com.example.collabtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.collabtask.databinding.SampleText1Binding

class SampleText1 : Fragment() {
    private var _binding: SampleText1Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SampleText1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.SampleText1Next.setOnClickListener()
        {
            findNavController().navigate(R.id.action_sampleText1_to_sampleText2)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}