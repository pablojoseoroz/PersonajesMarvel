package com.pablojoseoroz.marvel.ui.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.tabs.TabLayoutMediator
import com.pablojoseoroz.marvel.R
import com.pablojoseoroz.marvel.bus.FavoriteEvent
import com.pablojoseoroz.marvel.databinding.FragmentDetailBinding
import com.pablojoseoroz.marvel.glide.GlideApp
import com.pablojoseoroz.marvelapi.MarvelViewModel
import com.pablojoseoroz.marvelapi.dto.Character
import com.pablojoseoroz.marvelapi.responses.Result
import com.pablojoseoroz.uibase.LoadingFragment
import org.greenrobot.eventbus.EventBus

/**
 * Detail fragment encargado de mostrar el detalle de un personaje Marvel
 * Tiene las opciones de hacer favorito un personaje o de compartir su imagen
 *
 * @constructor Create empty Detail fragment
 */
class DetailFragment : LoadingFragment() {

    private var mId: Int = 0
    private lateinit var mFavoriteMenuItem: MenuItem
    private var mIsFavorite: Boolean = false

    private lateinit var mBinding: FragmentDetailBinding
    private val mViewModel: MarvelViewModel by activityViewModels()
    private val mFavoriteViewModel: FavoriteViewModel by activityViewModels()
    private lateinit var mCharacter: Character

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mId = DetailFragmentArgs.fromBundle(requireArguments()).id
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.detail, menu)
        mFavoriteMenuItem = menu.findItem(R.id.favorite)
    }

    /**
     * Check favorite item menu modifying his icon depending is favorite or not
     *
     */
    private fun checkFavoriteItemMenu() {
        if (!mFavoriteMenuItem.isVisible) mFavoriteMenuItem.isVisible = true
        mFavoriteMenuItem.setIcon(
            if (mIsFavorite == true) R.drawable.ic_baseline_favorite_24
            else R.drawable.ic_baseline_favorite_border_24
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                onFavoriteClick()
                return true
            }
            R.id.share -> {
                onShareClick()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onFavoriteClick() {
        if (mIsFavorite)
            mFavoriteViewModel.removeFavorite(mId).observe(viewLifecycleOwner, {
                mIsFavorite = false
                checkFavoriteItemMenu()
                EventBus.getDefault().post(FavoriteEvent())
            })
        else
            mFavoriteViewModel.addFavorite(mId).observe(viewLifecycleOwner, {
                mIsFavorite = true
                checkFavoriteItemMenu()
                EventBus.getDefault().post(FavoriteEvent())
            })
    }

    private fun onShareClick() {
        GlideApp.with(this)
            .asBitmap()
            .load(mCharacter.thumbnail.path + "." + mCharacter.thumbnail.extension)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(
                    @NonNull image: Bitmap,
                    @Nullable transition: Transition<in Bitmap?>?
                ) {
                    startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                        type = "image/png"
                        putExtra(
                            Intent.EXTRA_STREAM, Uri.parse(
                                MediaStore.Images.Media.insertImage(
                                    requireActivity().contentResolver,
                                    image,
                                    "title",
                                    null
                                )
                            )
                        )
                    }, "Share"))
                }

                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
            })
    }

    override fun getContentLayout(): Int {
        return R.layout.fragment_detail
    }

    override fun setupBinding(view: View) {
        super.setupBinding(view)
        mBinding = FragmentDetailBinding.bind((view as ViewGroup).getChildAt(1))
    }

    override fun setupObservers(view: View) {
        super.setupObservers(view)
        // check if character is favorite
        mFavoriteViewModel
            .isFavorite(mId)
            .observe(viewLifecycleOwner, {
                mIsFavorite = it
                checkFavoriteItemMenu()
            })

        // get character details
        mViewModel
            .getCharacter(mId)
            .observe(viewLifecycleOwner, {
                when (it) {
                    is Result.Loading -> {
                        mostrarCargando()
                    }
                    is Result.Success -> {
                        mostrarContenido()
                        mCharacter = it.data?.data?.results?.get(0)!!
                        with(mBinding) {
                            GlideApp.with(requireContext())
                                .load(mCharacter.thumbnail.path + "." + mCharacter.thumbnail.extension)
                                .into(ivPhoto)
                            tvName.text = mCharacter.name
                            tvDescription.text =
                                if (mCharacter.description.isNullOrEmpty()) getString(R.string.sin_descripcion) else mCharacter.description
                            vp.adapter = DetailAdapter(this@DetailFragment)
                            TabLayoutMediator(tl, vp) { tab, position ->
                                tab.text = when (position) {
                                    1 -> getString(R.string.eventos)
                                    2 -> getString(R.string.historias)
                                    3 -> getString(R.string.series)
                                    else -> getString(R.string.comics)
                                }
                            }.attach()
                        }
                    }
                    is Result.Error -> {
                        mostrarPlaceholder()
                    }
                }
            })
    }

    private inner class DetailAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment.requireActivity()) {

        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                1 -> DetailListFragment.newInstance(mCharacter.events.items)
                2 -> DetailListFragment.newInstance(mCharacter.stories.items)
                3 -> DetailListFragment.newInstance(mCharacter.series.items)
                else -> DetailListFragment.newInstance(mCharacter.comics.items)
            }
        }

    }

}