package socialsupermarket.common


/** Result of a command execution that allows to give Feedback to the client to update. */
data class CommandResult(val identifier: String, val aggregateSequence: Long)
