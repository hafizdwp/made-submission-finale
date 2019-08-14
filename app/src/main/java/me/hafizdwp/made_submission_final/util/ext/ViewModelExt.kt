package me.hafizdwp.made_submission_final.util.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import me.hafizdwp.made_submission_final.util.ViewModelFactory

/**
 * @author hafizdwp
 * 10/07/19
 **/
fun <VM : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<VM>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

fun <VM : ViewModel> androidx.fragment.app.Fragment.obtainViewModel(viewModelClass: Class<VM>) =
        ViewModelProviders.of(requireActivity(), ViewModelFactory.getInstance(requireActivity().application)).get(viewModelClass)

inline fun <reified T : ViewModel> AppCompatActivity.obtainViewModel() =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(T::class.java)

inline fun <reified VM : ViewModel> androidx.fragment.app.Fragment.obtainViewModel() =
        ViewModelProviders.of(requireActivity(), ViewModelFactory.getInstance(requireActivity().application)).get(VM::class.java)