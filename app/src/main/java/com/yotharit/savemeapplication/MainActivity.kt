package com.yotharit.savemeapplication

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
import br.com.bloder.magic.view.MagicButton


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val transactionList: ArrayList<Transaction> = ArrayList()

    lateinit var recyclerView: RecyclerView
    lateinit var transactionAdapter: TransactionAdapter
    lateinit var addButton: MagicButton
    lateinit var snackbar: Snackbar
    lateinit var wallet: Wallet
    lateinit var tvName: TextView
    lateinit var tvInfo: TextView
    lateinit var tvBalance: TextView
    lateinit var tvSum: TextView
    lateinit var tvStatus: TextView
    lateinit var circularProgress: CircularProgressIndicator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareData()
        initInstance()
    }

    private fun initInstance() {

        circularProgress = findViewById(R.id.circular_progress)
        tvName = findViewById(R.id.tvName)
        tvInfo = findViewById(R.id.tvInfo)
        tvBalance = findViewById(R.id.tvBalance)
        tvSum = findViewById(R.id.tvSum)
        tvStatus = findViewById(R.id.tvStatus)
        updateText()


        tvName.text = wallet.currentUser.callingName
        tvInfo.text = wallet.currentUser.firstName + " " + wallet.currentUser.surname + " Income : " + wallet.currentUser.income.toInt()
        circularProgress.setMaxProgress(wallet.currentUser.allMoney.toInt());
        circularProgress.setCurrentProgress(wallet.currentUser.currentUsage.toInt());

        addButton = findViewById(R.id.addButton)
        addButton.setMagicButtonClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val builder = AlertDialog.Builder(this@MainActivity)
                val inflater = this@MainActivity.getLayoutInflater()
                var dialogview: View = inflater.inflate(R.layout.dialog_add_transaction, null)
                builder.setView(dialogview)
                        .setPositiveButton("Add", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {

                                var transactionDest: EditText = dialogview.findViewById(R.id.transactionDes)
                                var transactionKind: EditText = dialogview.findViewById(R.id.transactionType)
                                var transactionAmount: EditText = dialogview.findViewById(R.id.transactionAmount)

                                if (transactionDest.toString() != "" &&
                                        transactionKind.toString() != "" &&
                                        transactionAmount.text.toString() != "") {
                                    var transaction = Transaction(transactionDest.text.toString()
                                            , transactionKind.text.toString(),
                                            transactionAmount.text.toString())
                                    val beforeSize: Int = wallet.transactionList.size
                                    wallet.addTransaction(transaction)
                                    val afterSize: Int = wallet.transactionList.size
                                    if (beforeSize != afterSize) {
                                        Toast.makeText(this@MainActivity, "Add Successful!", Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(this@MainActivity, "Invalid Amount! Add Failed", Toast.LENGTH_LONG).show()
                                    }
                                    transactionAdapter.notifyDataSetChanged()
                                    updateText()
                                }

                            }

                        })
                        .setNegativeButton("Cancle", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                Toast.makeText(this@MainActivity, "You didn't enter transaction", Toast.LENGTH_LONG).show()
                            }
                        });
                var alertDialog: Dialog = builder.create()
                alertDialog.show()


            }
        })

        recyclerView = findViewById(R.id.recycler_view)
        transactionAdapter = TransactionAdapter(wallet.transactionList)
        val tranLayManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = tranLayManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16))

        recyclerView.addOnItemTouchListener(RecycleTouchListener(applicationContext, recyclerView, object : RecycleTouchListener.ClickListener {

            override fun onClick(view: View, position: Int) {
            }

            override fun onLongClick(view: View, position: Int) {
                val transaction = wallet.transactionList.get(position)
                snackbar = Snackbar.make(this@MainActivity.findViewById(R.id.myCoordinatorLayout),
                        transaction.tran + " : " + transaction.tran, Snackbar.LENGTH_SHORT)
                snackbar.setAction("Remove", RemoveListener(transaction))
                snackbar.show()
            }
        }))
        recyclerView.adapter = transactionAdapter
    }

    private fun updateText() {
        tvBalance.text = "Balance : " + wallet.currentUser.currentMoney + " Baht"
        tvSum.text = "You've used " + wallet.currentUser.currentUsage + " Baht so far"
        if (wallet.exceedIntention()) tvStatus.text = "Exceed your intention!!!"
        else tvStatus.text = "Not exceed your intention!!!"
        circularProgress.setCurrentProgress(wallet.currentUser.currentUsage.toInt());
    }

    private fun prepareData() {
        var user = User("Tharit", "Pongsaneh", "It's me yo", 80
                , 10000.0, 10000.0, 8000.0, 0.0, 10000.0)
        wallet = Wallet()
        wallet.setUser(user)
    }

    override fun onClick(v: View) {

    }

    inner class RemoveListener(var transaction: Transaction) : View.OnClickListener {

        override fun onClick(v: View) {
            wallet.removeTransaction(transaction)
            transactionAdapter.notifyDataSetChanged()
            updateText()
        }
    }

}
