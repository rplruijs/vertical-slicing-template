package socialsupermarket.registration

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import socialsupermarket.authentication.util.PasswordValidator
import socialsupermarket.common.CommandException
import socialsupermarket.domain.commands.member.CreateMemberAccountCommand
import java.time.LocalDate
import java.util.UUID

data class RegistrationForm(
    @field:NotBlank(message = "First name is required")
    val firstName: String = "",

    @field:NotBlank(message = "Last name is required")
    val lastName: String = "",

    @field:Email(message = "Invalid email")
    @field:NotBlank(message = "Email is required")
    val email: String = "",

    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String = "",

    @field:NotNull(message = "Birth date is required")
    @field:Past(message = "Birth date must be in the past")
    val birthDate: LocalDate? = null,

    val terms: Boolean = false
)

@Controller
@RequestMapping("/registration")
class RegistrationController(private val commandGateway: CommandGateway) {

    private val logger = KotlinLogging.logger {}


    @PostMapping("/register")
    fun handleForm(
        @Valid @ModelAttribute("form") form: RegistrationForm,
        bindingResult: BindingResult,
        model: Model,
        response: HttpServletResponse
    ): String? {
        // Log the provided information
        logger.info { "New account registration: firstName=${form.firstName}, lastName=${form.lastName}, email=${form.email}, birthDate=${form.birthDate}, terms=${form.terms}" }
        logger.info { "Password provided = ${form.password} (not logging actual value for security reasons)" }

        // Validate password strength
        val passwordErrors = PasswordValidator.validate(form.password)
        if (passwordErrors.isNotEmpty()) {
            logger.warn { "Password validation failed: ${passwordErrors.joinToString(", ")}" }
            for (error in passwordErrors) {
                bindingResult.rejectValue("password", "password.strength", error)
            }
        }

        // Check terms acceptance
        if (!form.terms) {
            bindingResult.rejectValue("terms", "terms.required", "You must accept the terms and conditions")
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form)
            return "registration"
        }

        try {
            commandGateway.sendAndWait<CreateMemberAccountCommand>(
                CreateMemberAccountCommand(
                    memberId = UUID.randomUUID(),
                    firstName = form.firstName,
                    lastName = form.lastName,
                    email = form.email,
                    password = form.password,
                    birthDate = form.birthDate!!,
                )
            )

            // Redirect to success page or home page
            response.setHeader("HX-Redirect", "/login")
            return null // Skip view rendering
        } catch (ex: Exception) {
            // Extract CommandException if it's wrapped in another exception
            val commandException = when {
                ex is CommandException -> ex
                ex.cause is CommandException -> ex.cause as CommandException
                else -> null
            }

            // Log the error
            if (commandException != null) {
                logger.error { "Registration failed: ${commandException.message}" }
            } else {
                logger.error(ex) { "Registration failed with unexpected error" }
            }

            // Get appropriate error message
            val errorMessage = commandException?.message ?: ex.message ?: "An unexpected error occurred"

            // Add global error
            bindingResult.reject("registration.failed", errorMessage)
            model.addAttribute("form", form)
            return "registration"
        }
    }
}
