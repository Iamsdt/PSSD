/*
 * Developed By Shudipto Trafder
 *  on 8/24/18 9:25 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.gone
import com.iamsdt.pssd.ext.nextActivity
import com.iamsdt.pssd.ext.show
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.utils.SwipeUtil
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.content_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddActivity : AppCompatActivity() {

    private val adapter: AddAdapter by inject()

    private val model: AddVM by viewModel()

    private lateinit var dialog: AlertDialog

    lateinit var wordTV: TextInputLayout
    lateinit var desTV: TextInputLayout
    lateinit var refTV: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_add)
        setSupportActionBar(toolbar)

        addRcv.layoutManager = LinearLayoutManager(this)
        addRcv.adapter = adapter

        setSwipeForRecyclerView(addRcv)


        model.getWord().observe(this@AddActivity, Observer { list ->
            list?.let {
                if (it.isNotEmpty()) {
                    regularView()
                    adapter.submitList(it)
                } else {
                    emptyView()
                }
            }
        })

        fab.setOnClickListener {
            nextActivity<InsertActivity>()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun regularView() {
        regular_view.show()
        empty_view.gone()
    }

    private fun emptyView() {
        regular_view.gone()
        empty_view.show()
    }

    private fun setSwipeForRecyclerView(recyclerView: RecyclerView) {

        val swipeHelper = object : SwipeUtil(0, ItemTouchHelper.START or ItemTouchHelper.END, this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val swipedPosition = viewHolder.adapterPosition
                val adapter = recyclerView.adapter as AddAdapter
                adapter.pendingRemoval(swipedPosition)
            }

            override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val position = viewHolder.adapterPosition
                val adapter = recyclerView.adapter as AddAdapter
                return if (adapter.isPendingRemoval(position)) {
                    0
                } else super.getSwipeDirs(recyclerView, viewHolder)
            }
        }

        val mItemTouchHelper = ItemTouchHelper(swipeHelper)
        mItemTouchHelper.attachToRecyclerView(recyclerView)

        //set swipe label
        //swipeHelper.leftSwipeLabel = "Bookmark removed"
        //set swipe background-Color
        swipeHelper.leftColorCode = ContextCompat.getColor(this, R.color.red_300)

    }

    @SuppressLint("InflateParams")
    private fun showDialog() {
        val dialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_add, null)

        val builder = AlertDialog.Builder(this, R.style.MyDialogStyle)
        builder.setView(dialogView)

        wordTV = dialogView.add_word
        desTV = dialogView.add_des
        refTV = dialogView.refEditText
        val button: MaterialButton = dialogView.add_btn
        val close = dialogView.imageButton

        button.setOnClickListener {
            val word = wordTV.editText?.text ?: ""
            val des = desTV.editText?.text ?: ""
            val ref = refTV.editText?.text ?: "Added by user"

            //model.addData(word, des, ref)
        }

        dialog = builder.create()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val window = dialog.window
        val wlp = WindowManager.LayoutParams()

        wlp.copyFrom(dialog.window?.attributes)

        wlp.gravity = Gravity.BOTTOM
        wlp.width = displayMetrics.widthPixels
        dialog.window?.attributes = wlp

        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        close.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}
