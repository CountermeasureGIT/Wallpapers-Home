package ru.countermeasure.wallpapershome.presentation.search.search_filter

import com.jakewharton.rxrelay2.BehaviorRelay
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.reactivex.Observable
import ru.countermeasure.wallpapershome.domain.models.Filter
import javax.inject.Inject

@ActivityRetainedScoped
class SearchFilterPublisher @Inject constructor() {
    var sharedDefaultFilter: Filter = Filter.createEmptyFilter()

    private val filterRelay = BehaviorRelay.createDefault(sharedDefaultFilter)
    val currentFilter
        get() = filterRelay.value!!
    val filterObservable: Observable<Filter> = filterRelay.hide()

    fun acceptFilter(newFilter: Filter) {
        filterRelay.accept(newFilter)
    }
}