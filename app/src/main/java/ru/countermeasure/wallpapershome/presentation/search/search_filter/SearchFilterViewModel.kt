package ru.countermeasure.wallpapershome.presentation.search.search_filter

import androidx.hilt.lifecycle.ViewModelInject
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import ru.countermeasure.wallpapershome.domain.models.Filter
import ru.countermeasure.wallpapershome.presentation._system.Event
import ru.countermeasure.wallpapershome.presentation._system.acceptEvent
import ru.countermeasure.wallpapershome.presentation._system.base.BaseViewModel

class SearchFilterViewModel @ViewModelInject constructor(
    private val searchFilterPublisher: SearchFilterPublisher
) : BaseViewModel() {

    private var searchText: String? = null
    private lateinit var tagsIncluded: MutableList<String>
    private lateinit var tagsExcluded: MutableList<String>

    private val tagsAddRelay = BehaviorRelay.create<Event<Pair<String, Boolean>>>()
    private val tagsDeleteRelay = BehaviorRelay.create<Event<Triple<Int, String, Boolean>>>()
    private val resetRelay = BehaviorRelay.create<Event<Filter>>()

    val tagsAddObservable: Observable<Event<Pair<String, Boolean>>> = tagsAddRelay.hide()
    val tagsDeleteObservable: Observable<Event<Triple<Int, String, Boolean>>> =
        tagsDeleteRelay.hide()
    val resetObservable: Observable<Event<Filter>> = resetRelay.hide()

    val filter: Filter
        get() = createCurrentFilterRepresentation()

    private fun parseFilter(filter: Filter) {
        searchText = filter.searchFuzzily.joinToString(separator = " ")
        tagsIncluded = filter.tagsInclude.toMutableList()
        tagsExcluded = filter.tagsExclude.toMutableList()
    }

    init {
        parseFilter(searchFilterPublisher.currentFilter)
    }

    fun onSearchTextChanged(newSearchString: String?) {
        val trimmedSearchString = newSearchString?.trim()
        searchText = if (trimmedSearchString.isNullOrBlank())
            null
        else trimmedSearchString
    }

    fun onTagAddClicked(newTag: String?, included: Boolean) {
        val trimmedTag = newTag?.trim()
        if (trimmedTag.isNullOrBlank()
            || included && tagsIncluded.contains(trimmedTag)
            || !included && tagsExcluded.contains(trimmedTag)
        )
            return

        if (included)
            tagsIncluded.add(trimmedTag)
        else
            tagsExcluded.add(trimmedTag)

        tagsAddRelay.acceptEvent(trimmedTag to included)
    }

    fun onTagDeleteClicked(id: Int, name: String, isInclude: Boolean) {
        if (isInclude)
            tagsIncluded.remove(name.trim())
        else
            tagsExcluded.remove(name.trim())
        tagsDeleteRelay.acceptEvent(Triple(id, name, isInclude))
    }

    fun onApplyButtonClicked() {
        val newFilter = createCurrentFilterRepresentation()
        val currentFilter = searchFilterPublisher.currentFilter

        if (newFilter != currentFilter)
            searchFilterPublisher.acceptFilter(newFilter)
    }

    fun onResetButtonClicked() {
        val defaultFilter = searchFilterPublisher.sharedDefaultFilter

        parseFilter(defaultFilter)
        searchFilterPublisher.acceptFilter(defaultFilter)
        resetRelay.acceptEvent(defaultFilter)
    }

    private fun createCurrentFilterRepresentation(): Filter {
        val searchTextList = searchText?.split(' ').orEmpty()
        val tagsIncludedCopy = tagsIncluded.toList()
        val tagsExcludedCopy = tagsExcluded.toList()

        return searchFilterPublisher.currentFilter.copy(
            searchFuzzily = searchTextList,
            tagsInclude = tagsIncludedCopy,
            tagsExclude = tagsExcludedCopy
        )
    }
}