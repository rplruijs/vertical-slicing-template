package socialsupermarket.assesssupportrequest

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.CronTask
import org.springframework.scheduling.config.ScheduledTaskRegistrar

@Configuration
class WaitingForFundingSchedulingConfiguration(val supportWaitingForFundingProcessor: SupportWaitingForFundingProcessor) : SchedulingConfigurer {
    @Value("\${app.scheduling.request-waiting-for-funding:*/1 * * * * *}")
    private lateinit var requestWaitingForFundingProcessorCron: String
    
    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        taskRegistrar.addCronTask(
            CronTask(
                { supportWaitingForFundingProcessor.run() },
                requestWaitingForFundingProcessorCron
            )
        )
    }
}