package ru.countermeasure.wallpapershome.presentation.search.search_filter

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.view_filter.*
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.domain.models.Filter
import ru.countermeasure.wallpapershome.presentation._system.base.BaseFragment
import ru.countermeasure.wallpapershome.presentation._system.subscribeToEvent

@AndroidEntryPoint
class SearchFilterFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.view_filter
    private val viewModel: SearchFilterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderFilter(viewModel.filter, false)

        wordsTextInputLayout.editText?.doAfterTextChanged {
            viewModel.onSearchTextChanged(it?.toString())
        }

        applyButton.setOnClickListener {
            viewModel.onApplyButtonClicked()
        }

        resetButton.setOnClickListener {
            viewModel.onResetButtonClicked()
        }

        addTagButton.setOnClickListener {
            viewModel.onTagAddClicked(addTagTextInputLayout.editText?.text?.toString(), true)
        }

        removeTagButton.setOnClickListener {
            viewModel.onTagAddClicked(addTagTextInputLayout.editText?.text?.toString(), false)
        }
    }

    private fun renderFilter(initialFilter: Filter, shouldClearScreen: Boolean) {
        if (shouldClearScreen)
            clearScreen()
        wordsTextInputLayout.editText?.setText(initialFilter.searchFuzzily.joinToString(separator = " "))
        initialFilter.tagsInclude.forEach { addTag(it, true) }
        initialFilter.tagsExclude.forEach { addTag(it, false) }
    }

    private fun clearScreen() {
        wordsTextInputLayout.editText?.text = null
        addTagTextInputLayout.editText?.text = null
        TransitionManager.beginDelayedTransition(tagsChipGroup)
        tagsChipGroup.removeAllViews()
    }

    override fun onResume() {
        super.onResume()
        with(viewModel) {
            disposeOnPause(
                tagsAddObservable.subscribeToEvent { (tagName, isIncluded) ->
                    addTag(tagName, isIncluded)
                },
                tagsDeleteObservable.subscribeToEvent { (id, _, _) ->
                    deleteTag(id)
                },
                resetObservable.subscribeToEvent {
                    renderFilter(it, true)
                }
            )
        }
    }

    private fun deleteTag(id: Int) {
        tagsChipGroup.findViewById<Chip>(id)?.let { chip ->
            TransitionManager.beginDelayedTransition(tagsChipGroup, Fade())
            tagsChipGroup.removeView(chip)
        }

    }

    private fun addTag(tagName: String, isInclude: Boolean) {
        val newChip = createChip(tagsChipGroup.context, tagName, isInclude)
        TransitionManager.beginDelayedTransition(tagsChipGroup, Fade())
        tagsChipGroup.addView(newChip)
        addTagTextInputLayout.editText?.text = null
    }

    private fun createChip(chipContext: Context, name: String, isInclude: Boolean): Chip {
        return Chip(chipContext).apply {
            id = View.generateViewId()

            if (isInclude)
                setChipIconResource(R.drawable.ic_add)
            else
                setChipIconResource(R.drawable.ic_remove)
            setChipIconSizeResource(R.dimen.chip_icon_size)
            setCloseIconResource(R.drawable.ic_clear)
            isCloseIconVisible = true

            text = name

            setOnCloseIconClickListener {
                viewModel.onTagDeleteClicked(id, name, isInclude)
            }
        }
    }
}