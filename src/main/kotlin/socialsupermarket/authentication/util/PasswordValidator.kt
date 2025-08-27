package socialsupermarket.authentication.util

/**
 * Utility class for validating password strength.
 * Implements common password security best practices.
 */
object PasswordValidator {
    
    private const val MIN_LENGTH = 8
    private const val MAX_LENGTH = 128
    
    /**
     * Validates a password against security requirements.
     * 
     * Requirements:
     * - At least 8 characters long
     * - Not longer than 128 characters
     * - Contains at least one uppercase letter
     * - Contains at least one lowercase letter
     * - Contains at least one digit
     * - Contains at least one special character
     * 
     * @param password The password to validate
     * @return A list of validation error messages, empty if valid
     */
    fun validate(password: String): List<String> {
        val errors = mutableListOf<String>()
        
        // Check length
        if (password.length < MIN_LENGTH) {
            errors.add("Password must be at least $MIN_LENGTH characters long")
        }
        
        if (password.length > MAX_LENGTH) {
            errors.add("Password must not be longer than $MAX_LENGTH characters")
        }
        
        // Check for uppercase letters
        if (!password.any { it.isUpperCase() }) {
            errors.add("Password must contain at least one uppercase letter")
        }
        
        // Check for lowercase letters
        if (!password.any { it.isLowerCase() }) {
            errors.add("Password must contain at least one lowercase letter")
        }
        
        // Check for digits
        if (!password.any { it.isDigit() }) {
            errors.add("Password must contain at least one digit")
        }
        
        // Check for special characters
        if (!password.any { !it.isLetterOrDigit() }) {
            errors.add("Password must contain at least one special character")
        }
        
        return errors
    }
    
    /**
     * Checks if a password is valid according to security requirements.
     * 
     * @param password The password to check
     * @return true if the password is valid, false otherwise
     */
    fun isValid(password: String): Boolean {
        return validate(password).isEmpty()
    }
}