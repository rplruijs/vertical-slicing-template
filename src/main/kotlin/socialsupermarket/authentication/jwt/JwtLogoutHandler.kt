package socialsupermarket.authentication.jwt

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Component

/**
 * Custom LogoutHandler that clears the JWT token cookie when a user logs out.
 */
@Component
class JwtLogoutHandler(private val jwtService: JwtService) : LogoutHandler {
    
    override fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication?
    ) {
        // Clear the JWT token cookie
        jwtService.clearTokenCookie(response)
    }
}