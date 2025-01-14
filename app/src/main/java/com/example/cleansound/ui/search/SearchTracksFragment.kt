package com.example.cleansound.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cleansound.MainApplication
import com.example.cleansound.R
import com.example.cleansound.adapter.SearchTracksAdapter
import com.example.cleansound.adapter.VerticalSpaceItemDecoration
import com.example.cleansound.databinding.FragmentSearchTracksBinding
import com.example.cleansound.ui.home.HomeViewModel
import com.example.cleansound.ui.home.SpotifyViewModelFactory

class SearchTracksFragment : Fragment() {

    private val spotifyViewModel : HomeViewModel by viewModels {
        val spotifyRepository = (requireActivity().application as MainApplication).spotifyRepository
        SpotifyViewModelFactory(spotifyRepository)
    }

    private var _binding: FragmentSearchTracksBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter : SearchTracksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchInput()
        setupRecyclerView()

        spotifyViewModel.searchResult.observe(viewLifecycleOwner) {tracks ->
            (binding.rvSearchResult.adapter as SearchTracksAdapter).submitList(tracks)
        }
    }

    private fun setupRecyclerView() {
        binding.rvSearchResult.layoutManager = LinearLayoutManager(context)
        adapter = SearchTracksAdapter { trackId ->
            val action = SearchTracksFragmentDirections.actionNavigationDashboardToTrackDetailFragment(trackId)
            findNavController().navigate(action)
        }
        val vertical = resources.getDimensionPixelSize(R.dimen.spacing_4)
        binding.rvSearchResult.addItemDecoration(VerticalSpaceItemDecoration(vertical))
        binding.rvSearchResult.adapter = adapter

    }

    private fun setupSearchInput() {
        binding.svTrackSearch.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { spotifyViewModel.searchTracks(query) }
                return true
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}