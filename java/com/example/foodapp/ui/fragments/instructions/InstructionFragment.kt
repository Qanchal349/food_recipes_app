package com.example.foodapp.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.foodapp.R
import com.example.foodapp.models.Result
import kotlinx.android.synthetic.main.fragment_instruction.view.*


class InstructionFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_instruction, container, false)
        val args = arguments
        val myBundle:Result? = args?.getParcelable("recipesBundle")


        view.instructions_webView.webViewClient= object:WebViewClient(){}
        myBundle?.let {
            val websiteUrl : String = myBundle.sourceUrl
            view.instructions_webView.loadUrl(websiteUrl)
        }

        return view

    }


}