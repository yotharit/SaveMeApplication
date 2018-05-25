package com.yotharit.savemeapplication

import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayList

class Wallet {

    lateinit var currentUser : User

    var transactionList: ArrayList<Transaction> = ArrayList()

    fun addTransaction(transaction: Transaction) {
        if(!(transaction.amount.toInt()>currentUser.currentMoney)){
            transactionList.add(transaction)
            currentUser.currentMoney -= transaction.amount.toInt()
            currentUser.currentUsage += transaction.amount.toInt()
        }
    }

    fun removeTransaction(transaction: Transaction) {
        transactionList.remove(transaction)
        currentUser.currentMoney += transaction.amount.toInt()
        currentUser.currentUsage -= transaction.amount.toInt()
    }


    fun exceedIntention() : Boolean {
        return currentUser.currentUsage > currentUser.intentUsing
    }

    fun setUser(user: User) {
        currentUser = user
    }


}
