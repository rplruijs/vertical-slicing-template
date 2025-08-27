package socialsupermarket

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.commandhandling.gateway.IntervalRetryScheduler
import org.axonframework.commandhandling.gateway.RetryScheduler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.listener.MessageListenerContainer
import org.apache.kafka.clients.consumer.Consumer
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableKafka
@EnableScheduling
class VerticalSlicingApplication

@Configuration
class KafkaConfig {

	@Bean
	fun kafkaListenerContainerFactory(
		consumerFactory: ConsumerFactory<String, Any>
	): ConcurrentKafkaListenerContainerFactory<String, Any> {
		val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
		factory.consumerFactory = consumerFactory

		// Set CommonErrorHandler to handle exceptions without retrying
		factory.setCommonErrorHandler(object : DefaultErrorHandler() {
			override fun handleRemaining(
				thrownException: Exception,
				records: MutableList<ConsumerRecord<*, *>>,  // Changed from List to MutableList with correct nullability
				consumer: Consumer<*, *>,
				container: MessageListenerContainer
			) {
				// Don't retry for checked exceptions
				if (thrownException !is RuntimeException &&
					thrownException.cause !is RuntimeException) {
					return  // Don't propagate for retry
				}

				// Default handling for other exceptions
				super.handleRemaining(thrownException, records, consumer, container)
			}
		})
		return factory
	}
}

fun main(args: Array<String>) {
	runApplication<VerticalSlicingApplication>(*args)
}
