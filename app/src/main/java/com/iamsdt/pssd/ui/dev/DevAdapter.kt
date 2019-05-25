package com.iamsdt.pssd.ui.dev

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.iamsdt.androidextension.addText
import com.iamsdt.androidextension.gone
import com.iamsdt.pssd.R
import com.iamsdt.pssd.utils.model.DevModel
import kotlinx.android.synthetic.main.dev_item.view.*

class DevAdapter(val context: Context, var list: List<DevModel>) : RecyclerView.Adapter<DevAdapter.DevVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevVH {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.dev_item, parent, false)
        return DevVH(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DevVH, position: Int) {
        val model = list[position]
        holder.bind(model, context)
    }


    inner class DevVH(val view: View) : RecyclerView.ViewHolder(view) {

        val name: AppCompatTextView = view.dev_name
        private val bio: AppCompatTextView = view.dev_bio
        val work: AppCompatTextView = view.dev_work_type
        val type: AppCompatTextView = view.dev_type
        private val devImage: ImageView = view.dev_image
        private val fb: AppCompatImageButton = view.dev_fb
        private val linkedin: AppCompatImageButton = view.dev_linkedin
        private val git: AppCompatImageButton = view.dev_git
        private val email: AppCompatImageButton = view.dev_email


        fun bind(model: DevModel, context: Context) {
            name.addText(model.name)
            bio.addText(model.bio)
            work.addText(model.workType)
            type.addText(model.type)

            @Suppress("DEPRECATION")
            devImage.setImageDrawable(model.image)

            if (model.fb.isNotEmpty()) {
                fb.set(model.fb, context)
            } else {
                fb.gone()
            }

            if (model.git.isNotEmpty()) {
                git.set(model.fb, context)
            } else {
                git.gone()
            }

            if (model.linkedin.isNotEmpty()) {
                linkedin.set(model.fb, context)
            } else {
                linkedin.gone()
            }


            //email
            email.setOnClickListener {
                sendEmail(model.email, context)
            }
        }
    }

    private fun AppCompatImageButton.set(link: String, context: Context) {
        setOnClickListener {
            customTab(link, context)
        }
    }

    fun sendEmail(email: String, context: Context) {

        val subject = "Contact from Soil Science Dictionary"

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.type = "text/plain"
        intent.data = Uri.parse("mailto:$email")
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        context.startActivity(Intent.createChooser(intent, "Send Email"))
    }

    private fun customTab(link: String, context: Context) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(R.attr.colorPrimary)
        builder.setShowTitle(true)
        builder.addDefaultShareMenuItem()
        //builder.setCloseButtonIcon(BitmapFactory.decodeResource(
        //resources, R.drawable.dialog_back))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(link))
    }
}