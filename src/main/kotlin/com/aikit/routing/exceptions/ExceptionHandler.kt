package com.aikit.routing.exceptions

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.io.Serializable

@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(RouteNotPossibleException::class)
    @ResponseStatus(BAD_REQUEST)
    fun handleGamesNotFoundException(ex: RouteNotPossibleException): ResponseEntity<ErrorMessage> =
        ResponseEntity(ErrorMessage(ex.message), BAD_REQUEST)

    @ExceptionHandler(ResponseException::class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    fun handleSocketTimeoutException(ex: ResponseException): ResponseEntity<ErrorMessage> =
        ResponseEntity(ErrorMessage(ex.message), INTERNAL_SERVER_ERROR)


    @ExceptionHandler(InvalidCountryCodeException::class)
    @ResponseStatus(BAD_REQUEST)
    fun handleSocketTimeoutException(ex: InvalidCountryCodeException): ResponseEntity<ErrorMessage> =
        ResponseEntity(ErrorMessage(ex.message), BAD_REQUEST)
}

data class ErrorMessage(
    val message: String?
) : Serializable
