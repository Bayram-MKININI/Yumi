package net.noliaware.yumi.commun.util

class PaginationException(val serviceError: ServiceError) : Exception(serviceError.toString())