package fi.tuni.finalprojectandroid

import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * Paging source for fetching users.
 *
 * @param pageSize The number of users to fetch per page.
 * @param getUsers The suspend function to fetch users.
 */
class UserPagingSource(
    private val pageSize: Int,
    val getUsers: suspend (Int, Int) -> List<User>
) : PagingSource<Int, User>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        try {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize

            val users = getUsers(pageSize, pageNumber)

            val prevKey = if (pageNumber > 1) pageNumber - 1 else null
            val nextKey = pageNumber + 1

            return LoadResult.Page(
                data = users,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return null
    }
}