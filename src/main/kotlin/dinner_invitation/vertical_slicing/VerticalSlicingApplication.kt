package dinner_invitation.vertical_slicing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
class VerticalSlicingApplication

fun main(args: Array<String>) {
	runApplication<VerticalSlicingApplication>(*args)
}
