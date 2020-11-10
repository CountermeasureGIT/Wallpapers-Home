package ru.countermeasure.wallpapershome.domain.models

data class Filter(
    /** Search fuzzily for a tags/keywords*/
    val searchFuzzily: List<String> = emptyList(),

    /**  Tags, that must be in a result*/
    val tagsInclude: List<String> = emptyList(),

    /**  Tags, that must NOT be in a result*/
    val tagsExclude: List<String> = emptyList(),

    /** 100(default if not present)/101/111/etc (general/anime/people)*/
    val categories: List<Category>,

    /** Method of sorting results:
     * date_added(default if not present), relevance, random, views, favorites, toplist*/
    val sorting: Sorting = Sorting.RELEVANCE,

    /** Sorting order:
     * desc(default if not present), asc*/
    val order: Order = Order.DESC,

    /** Sorting MUST be set to 'toplist':
     * 1d, 3d, 1w, 1M(default if not present), 3M, 6M, 1y*/
    val topRange: TopRange? = null
) {
    companion object {
        private const val SEARCH_QUERY = "q"
        private const val CATEGORIES_QUERY = "categories"
        private const val SORTING_QUERY = "sorting"
        private const val ORDER_QUERY = "order"
        private const val TOP_RANGE_QUERY = "topRange"
        private const val PAGE_QUERY = "page"

        private const val FUZZ_SEARCH_DIVIDER = " "
        private const val TAG_INCLUSIVE_PREFIX = "+"
        private const val TAG_EXCLUSIVE_PREFIX = "-"

        fun createEmptyFilter() =
            Filter(categories = listOf(Category.ANIME, Category.GENERAL, Category.PEOPLE))
    }

    fun getQuery(page: Int) = buildQuery(page)
    fun getQuery() = buildQuery(null)

    private fun buildQuery(page: Int?): Map<String, String> {
        return mutableMapOf<String, String>().apply {
            val queryString = buildString {
                if (searchFuzzily.isNotEmpty())
                    append(searchFuzzily.joinToString(separator = FUZZ_SEARCH_DIVIDER))
                if (tagsInclude.isNotEmpty())
                    append(tagsInclude.joinToString(separator = "") {
                        "$TAG_INCLUSIVE_PREFIX$it"
                    })
                if (tagsExclude.isNotEmpty())
                    append(tagsExclude.joinToString(separator = "") {
                        "$TAG_EXCLUSIVE_PREFIX$it"
                    })
            }
            if (queryString.isNotEmpty())
                put(SEARCH_QUERY, queryString)

            val categoriesMask = categories.fold(0) { composition, element ->
                return@fold composition + element.value
            }
            put(CATEGORIES_QUERY, Integer.toBinaryString(categoriesMask).padStart(3, '0'))
            put(SORTING_QUERY, sorting.value)
            order?.let { put(ORDER_QUERY, it.value) }
            topRange?.let { put(TOP_RANGE_QUERY, it.value) }
            page?.let { put(PAGE_QUERY, page.toString()) }
        }
    }

    enum class Sorting(val value: String) {
        DATE_ADDED("date_added"),
        RELEVANCE("relevance"),
        RANDOM("random"),
        VIEWS("views"),
        FAVORITES("favorites"),
        TOPLIST("toplist"),
    }

    enum class Order(val value: String) {
        ASC("asc"),
        DESC("desc"),
    }

    enum class TopRange(val value: String) {
        D1("1d"),
        D3("3d"),
        W1("1w"),
        M1("1M"),
        M3("3M"),
        M6("6M"),
        Y1("1y"),
    }

    enum class Category(val value: Int) {
        PEOPLE(1),
        ANIME(2),
        GENERAL(4),
    }
}