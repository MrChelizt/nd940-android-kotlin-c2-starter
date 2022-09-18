package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.databinding.ListViewItemBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        ).get(MainViewModel::class.java)
    }

    private var viewModelAdapter: ListViewAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.asteroids.observe(viewLifecycleOwner) { asteroids ->
            asteroids?.apply {
                viewModelAdapter?.asteroids = asteroids
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        binding.asteroidRecycler.adapter = ListViewAdapter(ListViewAdapter.OnClickListener {
            viewModel.displayAsteroidDetails(it)
        })

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailsComplete()
            }
        })

//        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
//            viewModelAdapter?.submitList(it)
//        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(when(item.itemId) {
            R.id.show_today_menu -> AsteroidFilter.TODAY
            R.id.show_week_menu -> AsteroidFilter.WEEK
            else -> AsteroidFilter.SAVED
        })
        return true
    }
}
