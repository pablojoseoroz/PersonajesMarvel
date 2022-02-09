package com.pablojoseoroz.marvel.ui.detail

import android.view.View
import android.view.ViewGroup
import com.pablojoseoroz.uibase.BaseVH
import com.pablojoseoroz.marvel.R
import com.pablojoseoroz.marvel.databinding.ItemDetailBinding
import com.pablojoseoroz.marvelapi.dto.ResourceSummary
import com.pablojoseoroz.uibase.BaseRVAdapter
import java.util.ArrayList

/**
 * Detail adapter encargado de mostrar un resumen de cada recurso de un personaje Marvel
 *
 * @constructor
 *
 * @param items
 */
class DetailAdapter(items: ArrayList<ResourceSummary>?) : BaseRVAdapter<ResourceSummary, DetailAdapter.ViewHolder>(items) {

    override fun crearItem(viewGroup: ViewGroup, i: Int): DetailAdapter.ViewHolder {
        return ViewHolder(inflateItem(viewGroup, R.layout.item_detail))
    }

    inner class ViewHolder(itemView: View) : BaseVH<ResourceSummary>(itemView) {
        val binding: ItemDetailBinding

        init {
            binding = ItemDetailBinding.bind(itemView)
        }

        override fun bind(item: ResourceSummary) {
            binding.root.text = item.name
        }
    }

}