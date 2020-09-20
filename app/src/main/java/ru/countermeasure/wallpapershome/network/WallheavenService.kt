package ru.countermeasure.wallpapershome.network

import io.reactivex.Single
import retrofit2.http.*
import ru.countermeasure.wallpapershome.model.WallpapersDataHolder

interface WallheavenService {

    @GET("search")
    fun getList(
        @QueryMap queryMap: Map<String, String>
    ): Single<WallpapersDataHolder>

    @FormUrlEncoded
    @GET("search")
    fun getList(
        @FieldMap queryMap: Map<String, String>,
        @Query("page") page: Int
    ): Single<WallpapersDataHolder>

    @GET("search?sorting=toplist&order=desc")
    fun getTopList(
        @Query("page") page: Int
    ): Single<WallpapersDataHolder>

    companion object {
        const val ITEMS_IN_PAGE = 24
    }

//    companion object {
//        private const val SEARCH_QUERY = "q"
//        private const val CATEGORIES_QUERY = "categories"
//        private const val SORTING_QUERY = "sorting"
//        private const val ORDER_QUERY = "order"
//        private const val TOP_RANGE_QUERY = "topRange"
//        private const val PAGE_QUERY = "page"
//
//        private const val FUZZ_SEARCH_DIVIDER = " "
//        private const val TAG_INCLUSIVE_PREFIX = "+"
//        private const val TAG_EXCLUSIVE_PREFIX = "-"
//
//        fun buildQuery(
//            searchFuzzily: Array<String> = emptyArray(),
//            tagsInclude: Array<String> = emptyArray(),
//            tagsExclude: Array<String> = emptyArray(),
//            categories: Array<Category>,
//            sorting: Sorting,
//            order: Order,
//            topRange: TopRange? = null,
//            page: Int,
//        ): Map<String, Any> {
//            return mutableMapOf<String, Any>().apply {
//                val queryString = buildString {
//                    append(searchFuzzily.joinToString(separator = FUZZ_SEARCH_DIVIDER))
//                    append(tagsInclude.joinToString(prefix = TAG_INCLUSIVE_PREFIX))
//                    append(tagsExclude.joinToString(prefix = TAG_EXCLUSIVE_PREFIX))
//                }
//                put(SEARCH_QUERY, queryString)
//
//                val categoriesMask = categories.fold(0) { composition, element ->
//                    return@fold composition and element.value
//                }
//                put(CATEGORIES_QUERY, Integer.toBinaryString(categoriesMask))
//                put(SORTING_QUERY, sorting.value)
//                put(ORDER_QUERY, order.value)
//                topRange?.let { put(TOP_RANGE_QUERY, it.value) }
//                put(PAGE_QUERY, page)
//            }
//        }
//    }
}
//
//enum class Sorting(val value: String) {
//    DATE_ADDED("date_added"),
//    RELEVANCE("relevance"),
//    RANDOM("random"),
//    VIEWS("views"),
//    FAVORITES("favorites"),
//    TOPLIST("toplist"),
//}
//
//enum class Order(val value: String) {
//    ASC("asc"),
//    DESC("desc"),
//}
//
//enum class TopRange(val value: String) {
//    D1("1d"),
//    D3("3d"),
//    W1("1w"),
//    M1("1M"),
//    M3("3M"),
//    M6("6M"),
//    Y1("1y"),
//}
//
//enum class Category(val value: Int) {
//    PEOPLE(1),
//    ANIME(2),
//    GENERAL(4),
//}