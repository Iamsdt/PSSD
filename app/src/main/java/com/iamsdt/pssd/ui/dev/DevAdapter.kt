package com.iamsdt.pssd.ui.dev

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.addStrK
import com.iamsdt.pssd.ext.gone
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

        val name: TextView = view.dev_name
        private val bio: TextView = view.dev_name
        val work: TextView = view.dev_name
        val type: TextView = view.dev_name
        private val devImage: ImageView = view.dev_image
        private val fb: AppCompatImageButton = view.dev_fb
        private val linkedin: AppCompatImageButton = view.dev_linkedin
        private val git: AppCompatImageButton = view.dev_git
        private val email: AppCompatImageButton = view.dev_email


        fun bind(model: DevModel, context: Context) {
            name.addStrK(model.name)
            bio.addStrK(model.bio)
            work.addStrK(model.workType)
            type.addStrK(model.type)

            @Suppress("DEPRECATION")
            devImage.setImageDrawable(model.image)

            fb.set(model.fb, context)
            git.set(model.fb, context)
            linkedin.set(model.fb, context)

            //email
            email.setOnClickListener {
                sendEmail(model.email, context)
            }
        }
    }

    private fun AppCompatImageButton.set(link: String, context: Context) {
        if (link.isNotEmpty()) {
            setOnClickListener {
                customTab(link, context)
            }
        } else {
            gone()
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