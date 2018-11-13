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
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.*
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.utils.Constants.ADD.DES
import com.iamsdt.pssd.utils.Constants.ADD.DIALOG
import com.iamsdt.pssd.utils.Constants.ADD.WORD
import com.iamsdt.pssd.utils.SwipeUtil
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.add_dialog.view.*
import kotlinx.android.synthetic.main.content_add.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AddActivity : AppCompatActivity() {

    private val adapter: AddAdapter by inject()

    private val model: AddVM by viewModel()

    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_add)
        setSupportActionBar(toolbar)

        // complete: 8/24/18 add empty view

        addRcv.layoutManager = LinearLayoutManager(this)
        addRcv.adapter = adapter

        setSwipeForRecyclerView(addRcv)

//        val deco = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
//        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
//        deco.setDrawable(getDrawable(R.drawable.dercoration))
//        addRcv.addItemDecoration(deco)

        model.getWord().observe(this, Observer { list ->
            list?.let {
                if (it.isNotEmpty()) {
                    regularView()
                    adapter.submitList(it)
                } else {
                    emptyView()
                }
            }
        })

        model.dialogStatus.observe(this, Observer { model ->
            model?.let {
                if (it.status && it.title == DIALOG) {
                    Timber.i("Called")
                    if (::dialog.isInitialized && dialog.isShowing) {
                        dialog.dismiss()
                        showToast(ToastType.SUCCESSFUL, it.message)

                        //add analytics
                        val ana = FirebaseAnalytics.getInstance(this@AddActivity)
                        val bundle = Bundle()
                        bundle.putString("Data_added", "Data Put in the local database")
                        ana.logEvent("add_data", bundle)
                    }
                }
            }
        })

        fab.setOnClickListener {
            showDialog()
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
                .inflate(R.layout.add_dialog, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        val wordTV: TextInputEditText = dialogView.add_word
        val desTV: TextInputEditText = dialogView.add_des
        val button: AppCompatImageButton = dialogView.add_btn

        button.setOnClickListener {
            val word = wordTV.text ?: ""
            val des = desTV.text ?: ""

            model.addData(word.toCapFirst(), des.toCapFirst())
        }

        model.dialogStatus.observe(this, Observer { model ->
            model?.let {
                if (!it.status && it.title == WORD) {
                    wordTV.error = it.message
                } else if (!it.status && it.title == DES) {
                    desTV.error = it.message
                }
            }
        })

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

        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}
