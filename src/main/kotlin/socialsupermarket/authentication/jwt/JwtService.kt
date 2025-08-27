package socialsupermarket.authentication.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import socialsupermarket.authentication.config.JwtProperties
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(private val jwtProperties: JwtProperties) {
    
    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtProperties.secretKey.toByteArray())
    }
    
    fun generateToken(email: String, memberId: UUID): String {
        val now = Date()
        val validity = Date(now.time + jwtProperties.tokenValidityInMinutes * 60 * 1000)
        
        return Jwts.builder()
            .setSubject(memberId.toString())
            .claim("email", email)
            .setIssuer(jwtProperties.issuer)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }
    
    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            return true
        } catch (e: Exception) {
            return false
        }
    }
    
    fun getEmailFromToken(token: String): String? {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body
            
            claims["email"] as String?
        } catch (e: Exception) {
            null
        }
    }
    
    fun getMemberIdFromToken(token: String): UUID? {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body
            
            UUID.fromString(claims.subject)
        } catch (e: Exception) {
            null
        }
    }
    
    fun addTokenCookie(response: HttpServletResponse, token: String) {
        val cookie = Cookie(jwtProperties.cookieName, token).apply {
            isHttpOnly = true  // Prevents JavaScript access
            secure = true      // Sends only over HTTPS
            path = "/"         // Available across the entire site
            maxAge = (jwtProperties.tokenValidityInMinutes * 60).toInt()
        }
        response.addCookie(cookie)
    }
    
    fun getTokenFromCookies(request: HttpServletRequest): String? {
        val cookies = request.cookies ?: return null
        return cookies.find { it.name == jwtProperties.cookieName }?.value
    }


    
    fun clearTokenCookie(response: HttpServletResponse) {
        val cookie = Cookie(jwtProperties.cookieName, "").apply {
            isHttpOnly = true
            secure = true
            path = "/"
            maxAge = 0  // Expires immediately
        }
        response.addCookie(cookie)
    }

    fun getEmailOfUser(request: HttpServletRequest) : String? {
        return getTokenFromCookies(request)?.let {
            getEmailFromToken(it)
        }
    }
}