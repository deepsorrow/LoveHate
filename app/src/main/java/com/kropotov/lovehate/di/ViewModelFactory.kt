package com.kropotov.lovehate.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * Служебный класс представляющий реализацию фабрики отвечающей за создание джереник экземпляров вью-моделей
 * @param VIEW_MODEL тип предоставляемой вью-модели
 */
class ViewModelFactory<VIEW_MODEL : ViewModel> @Inject constructor(
    private val viewModel: Provider<VIEW_MODEL>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <VIEW_MODEL : ViewModel> create(modelClass: Class<VIEW_MODEL>): VIEW_MODEL {
        return viewModel.get() as VIEW_MODEL
    }
}