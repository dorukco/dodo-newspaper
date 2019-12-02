package com.dodo.newspaper.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class ArticleSearchException(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message)
