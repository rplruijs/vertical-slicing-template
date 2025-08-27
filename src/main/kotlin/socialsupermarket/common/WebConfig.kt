package socialsupermarket.common

import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    /**
     * Configure content negotiation to handle HttpMediaTypeNotAcceptableException.
     * This ensures that when a client doesn't specify an Accept header or specifies an unsupported media type,
     * the server will default to HTML format for HTMX requests.
     */
    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer
            .defaultContentType(MediaType.TEXT_HTML)
            .favorParameter(false)
            .ignoreAcceptHeader(false)
            .mediaType("html", MediaType.TEXT_HTML)
            .mediaType("json", MediaType.APPLICATION_JSON)
    }
}