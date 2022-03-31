package com.software.hemis.presenter.other.data.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.hemis.R
import com.example.hemis.databinding.FragmentInformationBinding
import com.software.hemis.presenter.other.adapter.Info
import com.software.hemis.presenter.other.adapter.InformationRecyclerViewAdapter
import com.software.hemis.data.comman.base.BindingFragment

class InformationFragment : BindingFragment<FragmentInformationBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentInformationBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews()
    }

    private fun setUpViews() {
        val adapter = InformationRecyclerViewAdapter { position ->
            when(position){
                0 -> {findNavController().navigate(R.id.referenceFragment)}
                1 -> {findNavController().navigate(R.id.contractFragment)}
                2 -> {findNavController().navigate(R.id.studentDocumentFragment)}
                3 -> {findNavController().navigate(R.id.ratingNotebookFragment)}
                4 -> {findNavController().navigate(R.id.performanceFragment)}
                5 -> {findNavController().navigate(R.id.ordersFragment)}
                6 -> {}
            }
        }
        binding.rv.adapter = adapter
        adapter.items = loadData()
    }

    private fun loadData(): List<Info> = listOf(
        Info(R.drawable.ic_information, "Ma‘lumotnomalar", R.color.orange),
        Info(R.drawable.ic_contract, "Shartnomalar", R.color.electr),
        Info(R.drawable.ic_student_document, "Talaba hujjati", R.color.neon_blue),
        Info(R.drawable.ic_rating_book, "Reyting daftarcha", R.color.green_fern),
        Info(R.drawable.ic_mark, "O‘zlashtirish", R.color.blue_light),
        Info(R.drawable.ic_orders, "Buyruqlar", R.color.orange),
        Info(R.drawable.ic_absent, "Davomat", R.color.tomato),
    )
}