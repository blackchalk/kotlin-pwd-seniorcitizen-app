package com.seniorcitizen.app.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.seniorcitizen.app.R
import com.seniorcitizen.app.databinding.FragmentTransactionsBinding
import com.seniorcitizen.app.ui.base.BaseFragment
import javax.inject.Inject

/**
 * Created by Nic Evans on 2019-12-20.
 */
class TransactionFragment: BaseFragment<FragmentTransactionsBinding, TransactionViewModel>() {

	@Inject
	lateinit var viewmodel: TransactionViewModel

	override fun getLayoutRes(): Int = R.layout.fragment_transactions

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val view = inflater.inflate(R.layout.fragment_transactions, container, false)

		view.findViewById<FloatingActionButton>(R.id.floating_action_button).setOnClickListener {
			findNavController().navigate(R.id.action_transaction_dest_to_scan_dest)
		}
		return view
	}
}
