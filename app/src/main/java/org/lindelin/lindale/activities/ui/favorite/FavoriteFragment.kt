package org.lindelin.lindale.activities.ui.favorite

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import org.lindelin.lindale.R
import org.lindelin.lindale.activities.ui.home.HomeViewModel
import org.lindelin.lindale.models.Project

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [FavoriteFragment.OnListFragmentInteractionListener] interface.
 */
class FavoriteFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite_list, container, false)
        homeViewModel = activity?.run {
            ViewModelProviders.of(this)[HomeViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                view.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
                homeViewModel.getProfile().observe(this@FavoriteFragment, Observer {
                    adapter = MyFavoriteRecyclerViewAdapter(it.projects.favorites, listener)
                })
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Project?)
    }

    companion object {

        const val ARG_COLUMN_COUNT = "favorite-column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
