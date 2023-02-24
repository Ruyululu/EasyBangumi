package com.heyanle.easybangumi4.ui.home.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.heyanle.bangumi_source_api.api.ISearchParser

/**
 * Created by HeYanLe on 2023/2/21 23:42.
 * https://github.com/heyanLE
 */
class ExploreViewModel : ViewModel() {

    private val viewModelOwnerStore = hashMapOf<ExplorePage, ViewModelStore>()

    fun getViewModelStoreOwner(page: ExplorePage) = ViewModelStoreOwner {
        var viewModelStore = viewModelOwnerStore[page]
        if (viewModelStore == null) {
            viewModelStore = ViewModelStore()
            viewModelOwnerStore[page] = viewModelStore
        }
        viewModelStore
    }

    override fun onCleared() {
        super.onCleared()
        viewModelOwnerStore.asIterable().forEach {
            it.value.clear()
        }
        viewModelOwnerStore.clear()
    }

}