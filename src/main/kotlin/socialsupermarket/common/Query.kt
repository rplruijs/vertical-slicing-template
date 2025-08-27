package socialsupermarket.common

interface Query

interface QueryHandler<T : Query, U> {
    fun handleQuery(query: T): U
}
