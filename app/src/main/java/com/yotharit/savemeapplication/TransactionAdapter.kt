package com.yotharit.savemeapplication

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


public class TransactionAdapter(transactionList:ArrayList<Transaction>) : RecyclerView.Adapter<TransactionAdapter.MyViewHolder>() {

    private var transactionList: List<Transaction> = transactionList


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val transaction = transactionList.get(position)
        holder.tran.text = transaction.tran
        holder.kind.text = transaction.kind
        holder.amount.text = transaction.amount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.transaction_list_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tran: TextView = view.findViewById(R.id.transaction)
        var kind: TextView = view.findViewById(R.id.kind)
        var amount: TextView = view.findViewById(R.id.amount)

    }

}