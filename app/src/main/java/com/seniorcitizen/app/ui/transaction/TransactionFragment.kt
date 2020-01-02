package com.seniorcitizen.app.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.seniorcitizen.app.R
import com.seniorcitizen.app.data.model.Transaction
import com.seniorcitizen.app.databinding.FragmentTransactionsBinding
import com.seniorcitizen.app.ui.base.BaseFragment
import com.seniorcitizen.app.ui.home.HomeActivityViewModel
import kotlinx.android.synthetic.main.fragment_transactions.*
import timber.log.Timber

/**
 * Created by Nic Evans on 2019-12-20.
 */
class TransactionFragment: BaseFragment<FragmentTransactionsBinding, TransactionViewModel>(), TransactionSelectedListener {

	// uses parent activity view model
	private val homeActivityViewModel by lazy {
		activity?.let {
			ViewModelProviders.of(it, viewModelFactory).get(HomeActivityViewModel::class.java)
		}
	}
	private val viewModel: TransactionViewModel by viewModels {
		viewModelFactory
	}

	override fun getLayoutRes(): Int = R.layout.fragment_transactions

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		recyclerView.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
		recyclerView.adapter = TransactionListAdapter(viewModel,this,this)
		recyclerView.layoutManager = LinearLayoutManager(context)

		activity?.let { homeActivityViewModel?.seniorCitizenResult?.observe(it, Observer { user ->
			if (user.isNotEmpty()){
				viewModel.initTransactions(user[0])
			}
		}) }

		getTransactionObservableViewModel()
	}

	// Observe transactions of this user and fill the list
	private fun getTransactionObservableViewModel() {
		viewModel.getTransactions().observe(this, Observer {
			if (it.isNotEmpty()){
				recyclerView.visibility = View.VISIBLE
			}
		})

		viewModel.getError().observe(this, Observer {
			if (it != null)
				if (it){
					tv_error.visibility = View.VISIBLE
					recyclerView.visibility = View.GONE
					tv_error.text = "An Error Occurred While Loading Data!"
				}else{
					tv_error.visibility = View.GONE
					tv_error.text = null
				}
		})

		viewModel.getLoading().observe(this, Observer {
			if(it != null){
				if (it){
					loading_view.visibility = View.VISIBLE
					tv_error.visibility = View.GONE
					recyclerView.visibility = View.GONE
				}else{
					loading_view.visibility = View.GONE
				}
			}
		})
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

	override fun onTransactionSelected(transaction: Transaction?) {
		//TODO show a dialog of items
		Timber.i("%s",transaction)
	}
}
