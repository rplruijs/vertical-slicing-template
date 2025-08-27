package socialsupermarket.authentication.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtProperties {
    var secretKey: String = "defaultSecretKeyWhichShouldBeChangedInProduction"
    var tokenValidityInMinutes: Long = 60 // 1 hour
    var cookieName: String = "jwt_token"
    var issuer: String = "social-supermarket"
}