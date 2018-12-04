/*
 * Developed By Shudipto Trafder
 *  on 9/22/18 9:25 AM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iamsdt.pssd.R
import kotlinx.android.synthetic.main.fragment_main.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {

    private val viewModel: MainVM by viewModel()

    private lateinit var adapter: MainAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        view.mainRcv.layoutManager = LinearLayoutManager(context)
        //adapter = MainAdapter(context!!)
        view.mainRcv.adapter = adapter

        viewModel.liveData.observe(activity!!, Observer {
            adapter.submitList(it)
        })


        return view
    }

}