package com.seniorcitizen.app.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.seniorcitizen.app.R
import com.seniorcitizen.app.data.model.Transaction
import kotlinx.android.synthetic.main.view_transaction_list_item.view.*
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

/**
 * Created by Nic Evans on 2020-01-02.
 */
class TransactionListAdapter(
    val viewModel: TransactionViewModel,
    val lifecycleOwner: LifecycleOwner,
    val transactionSelectedListener: TransactionSelectedListener
): RecyclerView.Adapter<TransactionListAdapter.TransactionViewHolder>() {

    var data = ArrayList<Transaction>()

    init {
        viewModel.getTransactions().observe(lifecycleOwner, Observer { transactions ->
            data.clear()
            if (transactions!=null){
                data.addAll(transactions)
                notifyDataSetChanged()
            }
        })
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_transaction_list_item, parent, false)
        return TransactionViewHolder(view, transactionSelectedListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemId(position: Int): Long {
        return data.get(position).transactionID!!.toLong()
    }

    class TransactionViewHolder(itemView: View,
        repoSelectedListener: TransactionSelectedListener
    ) : RecyclerView.ViewHolder(itemView) {

        private var transaction: Transaction? = null

        fun bind(transaction: Transaction) {
            this.transaction = transaction
            //set
            itemView.tv_store_name.text = transaction.business?.businessName
            itemView.tv_id_number.text = transaction.seniorCitizen?.idNumber
            itemView.tv_quantity.text = transaction.totalQuantity.toString()

            // convert birthday
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)//model.birthday.toString(), Locale.US
            val formatter = SimpleDateFormat("MM-dd-yyyy hh:mm a", Locale.US)
            val convertedDate = formatter.format(parser.parse(transaction.transactionDate.toString()))

            itemView.tv_date.text = convertedDate
        }

        init {
            itemView.setOnClickListener { v: View? ->
                if (transaction != null) {
                    repoSelectedListener.onTransactionSelected(transaction)
                }
            }
        }
    }
}