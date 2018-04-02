package com.maxwell.taxhelper.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.maxwell.projectfoundation.BaseActivity
import com.maxwell.taxhelper.R
import com.maxwell.taxhelper.bean.CityParams
import com.maxwell.taxhelper.busevent.CitySwitch
import de.greenrobot.event.EventBus
import org.apache.commons.lang3.StringUtils

/**
 * Created by maxwellma on 04/11/2017.
 */
class QuickIndexAdapter(context: Context, cityList: ArrayList<CityParams>) : RecyclerView.Adapter<QuickIndexAdapter.CityHolder>() {

    private val cityList = cityList
    private var context = context

    override fun onBindViewHolder(viewHolder: CityHolder, position: Int) {
        viewHolder.nameText.text = cityList[position].name
        viewHolder.indexText.text = cityList[position].pinyin[0].toString().toUpperCase()
        if (position > 0) {
            if (StringUtils.equalsIgnoreCase(cityList[position - 1].pinyin[0].toString(), cityList[position].pinyin[0].toString())) {
                viewHolder.indexText.visibility = View.GONE
            } else {
                viewHolder.indexText.visibility = View.VISIBLE
            }
        } else {
            viewHolder.indexText.visibility = View.VISIBLE
        }
        viewHolder.itemView.setOnClickListener { _ ->
            EventBus.getDefault().post(CitySwitch(cityList[position]))
            (context as BaseActivity).finish()
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CityHolder {
        return CityHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_city, viewGroup, false))
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameText: TextView = itemView.findViewById(R.id.tv_name) as TextView
        var indexText: TextView = itemView.findViewById(R.id.tv_index) as TextView
    }
}