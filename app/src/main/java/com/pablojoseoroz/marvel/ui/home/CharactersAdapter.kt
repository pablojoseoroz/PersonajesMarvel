package com.pablojoseoroz.marvel.ui.home

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.chip.Chip
import com.pablojoseoroz.marvel.R
import com.pablojoseoroz.marvel.databinding.ItemCharacterBinding
import com.pablojoseoroz.marvel.glide.GlideApp
import com.pablojoseoroz.marvel.room.CharacterFavorite
import com.pablojoseoroz.marvelapi.dto.Character
import com.pablojoseoroz.uibase.BaseRVAdapter
import com.pablojoseoroz.uibase.BaseVH

/**
 * Characters adapter encargado de mostrar los personajes de Marvel
 * Dependinedo el modo carga un layout u otro
 * Al pinchar en un personaje avisa para que se muestre su detalle
 *
 * @property mListener
 * @constructor Create empty Characters adapter
 */
class CharactersAdapter(val mListener: (Character) -> Unit) :
    BaseRVAdapter<Character, CharactersAdapter.ViewHolder>() {

    private interface Payload {
        companion object {
            const val FAVORITE = "favorite"
        }
    }

    private var mFavorites: List<CharacterFavorite>? = null
    lateinit var mRequestManager: RequestManager
    var mIsModeGrid = false
    val mPaletteCache = hashMapOf<String, Int>()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRequestManager = GlideApp.with(recyclerView.context)
    }

    override fun onBindViewHolder(
        holder: BaseVH<Character>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }
        val payload = payloads[0].toString()
        when (payload) {
            Payload.FAVORITE -> {
                if (mFavorites != null && mFavorites!!.contains(CharacterFavorite(getItem(position).id))) {
                    (holder as ViewHolder).binding.ivFav.visibility = View.VISIBLE
                } else {
                    (holder as ViewHolder).binding.ivFav.visibility = View.GONE
                }
            }
        }
    }

    inner class ViewHolder(itemView: View) : BaseVH<Character>(itemView) {
        val binding: ItemCharacterBinding

        init {
            binding = ItemCharacterBinding.bind(itemView)
            binding.root.setOnClickListener {
                mListener(getItem(adapterPosition))
            }
        }

        override fun bind(item: Character) {
            // name
            binding.tvName.text = item.name

            // check if it is favorite
            if (mFavorites != null && mFavorites!!.contains(CharacterFavorite(item.id))) {
                binding.ivFav.visibility = View.VISIBLE
            } else {
                binding.ivFav.visibility = View.GONE
            }

            // chips to show comics, events, stories and series: onlly on mode grid
            if (!mIsModeGrid) {
                binding.chips.removeAllViews()
                val layoutInflater = LayoutInflater.from(context)

                (layoutInflater.inflate(R.layout.chip_character, null) as Chip).apply {
                    text = item.comics.items.size.toString() + " comics"
                    binding.chips.addView(this)
                }

                (layoutInflater.inflate(R.layout.chip_character, null) as Chip).apply {
                    text = item.events.items.size.toString() + " eventos"
                    binding.chips.addView(this)
                }

                (layoutInflater.inflate(R.layout.chip_character, null) as Chip).apply {
                    text = item.stories.items.size.toString() + " historias"
                    binding.chips.addView(this)
                }

                (layoutInflater.inflate(R.layout.chip_character, null) as Chip).apply {
                    text = item.series.items.size.toString() + " series"
                    binding.chips.addView(this)
                }
            }

            // load image with listener to apply palette
            val url = item.thumbnail.path + "." + item.thumbnail.extension
            mRequestManager
                .asBitmap()
                .load(url)
                .placeholder(null)
                .centerInside()
                .dontAnimate()
                .dontTransform()
                .listener(object : RequestListener<Bitmap?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return true
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (resource != null) {
                            if (mPaletteCache.containsKey(url)) {
                                binding.root.setStrokeColor(
                                    ColorStateList.valueOf(
                                        mPaletteCache.get(
                                            url
                                        )!!
                                    )
                                )
                                binding.chips.children.forEach {
                                    (it as Chip).setChipBackgroundColor(
                                        ColorStateList.valueOf(mPaletteCache.get(url)!!)
                                    )
                                }
                            } else {
                                // Asynchronous
                                Palette.from(resource)
                                    .maximumColorCount(16) // 24
                                    .generate { p ->
                                        try {
                                            val swatch = p!!.darkVibrantSwatch
                                            mPaletteCache.put(url, swatch!!.rgb)
                                            binding.root.setStrokeColor(swatch.rgb)
                                            binding.chips.children.forEach {
                                                (it as Chip).setChipBackgroundColor(
                                                    ColorStateList.valueOf(swatch.rgb)
                                                )
                                            }
                                        } catch (e: Exception) {
                                        }
                                    }
                            }
                        }
                        return false
                    }
                }).into(binding.ivPhoto)
        }
    }

    override fun crearItem(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            inflateItem(
                viewGroup,
                if (mIsModeGrid) R.layout.item_character_grid else R.layout.item_character
            )
        )
    }

    fun setFavorites(favorites: List<CharacterFavorite>?) {
        mFavorites = favorites
    }

    fun updateFavorites(favorites: List<CharacterFavorite>?) {
        mFavorites = favorites
        notifyItemRangeChanged(0, getItemCount(), Payload.FAVORITE);
    }

}