package net.noliaware.yumi.commun.util

class PaginationException(val errorType: ErrorType) : Exception(errorType.toString())
