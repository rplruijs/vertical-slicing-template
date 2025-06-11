package dinner_invitation.vertical_slicing.common

interface Query

interface QueryHandler<T : Query, U> {
    fun handleQuery(query: T): U
}
