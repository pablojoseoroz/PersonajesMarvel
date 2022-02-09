package com.pablojoseoroz.marvel.ui.detail

import android.os.Bundle
import android.os.Parcelable
import com.pablojoseoroz.marvelapi.dto.ResourceSummary
import com.pablojoseoroz.uibase.ListFragment
import java.util.ArrayList

/**
 * Detail list fragment encargado de mostrar un tipo de recurso pasado por parametro de un personaje Marvel
 *
 * @constructor Create empty Detail list fragment
 */
class DetailListFragment : ListFragment<DetailAdapter>() {

    override fun getAdapter(): DetailAdapter {
        return DetailAdapter(
            requireArguments().getParcelableArrayList<ResourceSummary>(
                EXTRA_RESOURCES
            )
        )
    }

    companion object {
        val EXTRA_RESOURCES = "extra_resources"

        @JvmStatic
        fun newInstance(data: List<ResourceSummary>) =
            DetailListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(EXTRA_RESOURCES, data as ArrayList<out Parcelable>)
                }
            }
    }

}