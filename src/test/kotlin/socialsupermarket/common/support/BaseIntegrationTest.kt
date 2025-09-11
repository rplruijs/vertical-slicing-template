package socialsupermarket.common.support

import org.awaitility.Awaitility
import org.axonframework.config.EventProcessingConfiguration
import org.axonframework.eventhandling.EventProcessor
import org.axonframework.eventhandling.TrackingEventProcessor
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
abstract class BaseIntegrationTest {


    @Autowired
    protected lateinit var flyway: Flyway

    @org.junit.jupiter.api.BeforeEach
    fun resetDatabase() {
        flyway.clean()
        flyway.migrate()
    }


    companion object {
        @org.testcontainers.junit.jupiter.Container
        private val postgres =
            PostgreSQLContainer(DockerImageName.parse("postgres")).withReuse(true)

        @ServiceConnection
        @org.testcontainers.junit.jupiter.Container
        private val kafkaContainer =
            KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.3"))
                .withExposedPorts(9092)
                .withExposedPorts(9093)
                .withReuse(false)

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgres.jdbcUrl }
            registry.add("spring.flyway.url") { postgres.jdbcUrl }
            registry.add("spring.datasource.username") { "test" }
            registry.add("spring.datasource.password") { "test" }
            registry.add("spring.flyway.user") { "test" }
            registry.add("spring.flyway.password") { "test" }
            registry.add("spring.flyway.clean-disabled") { "false" }
            registry.add("spring.flyway.schemas") { "public" }
            // Make the projector synchronous and deterministic in tests
            registry.add("axon.processors.funding-balance.mode") { "subscribing" }
            registry.add("axon.eventhandling.processors.funding-balance.mode") { "subscribing" }
        }
    }
}


fun awaitUntilAsserted(fn: () -> Unit) {
    Awaitility.await().pollInSameThread().atMost(Duration.ofSeconds(15)).untilAsserted { fn() }
}


