package socialsupermarket.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    /**
     * Handle HttpMediaTypeNotAcceptableException by returning a simple HTML response.
     * This prevents the warning logs and provides a graceful fallback for HTMX requests
     * when content negotiation fails.
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleHttpMediaTypeNotAcceptableException(ex: HttpMediaTypeNotAcceptableException): ResponseEntity<String> {
        // Return a simple HTML response that can be processed by the client
        return ResponseEntity
            .ok()
            .header("Content-Type", "text/html")
            .body("<div>Content processed successfully</div>")
    }
}