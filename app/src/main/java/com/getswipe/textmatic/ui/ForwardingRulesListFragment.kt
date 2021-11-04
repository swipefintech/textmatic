package com.getswipe.textmatic.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.getswipe.textmatic.R
import com.getswipe.textmatic.databinding.FragmentForwardingRulesListBinding

class ForwardingRulesListFragment : Fragment() {

    private var _binding: FragmentForwardingRulesListBinding? = null

    private val binding get() = _binding!!

    private val viewModel: ForwardingRuleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_forwarding_rules_list, container, false)
    }
}
