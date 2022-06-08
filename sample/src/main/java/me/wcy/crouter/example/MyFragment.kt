package me.wcy.crouter.example

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import me.wcy.router.annotation.Router

/**
 * Created by wangchenyan.top on 2022/6/8.
 */
@Router("/fragment/my")
class MyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text: TextView = view.findViewById(R.id.text)
        val button: Button = view.findViewById(R.id.button)
        val map = mutableMapOf<String, Any?>()
        arguments?.keySet()?.forEach {
            map.put(it, arguments?.get(it))
        }
        text.text = map.toString()
        button.setOnClickListener {
            val data = Intent()
            data.putExtra("key", "value from MyFragment")
            activity?.setResult(Activity.RESULT_OK, data)
            activity?.finish()
        }
    }
}