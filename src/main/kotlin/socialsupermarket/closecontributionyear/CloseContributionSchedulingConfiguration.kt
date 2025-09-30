package socialsupermarket.closecontributionyear

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.CronTask
import org.springframework.scheduling.config.ScheduledTaskRegistrar

@Configuration
class CloseContributionSchedulingConfiguration(val closeContributionProcessor: CloseContributionProcessor) : SchedulingConfigurer {
    @Value("\${app.scheduling.close-contribution:*/1 * * * * *}")
    private lateinit var closeContributionCron: String
    
    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        taskRegistrar.addCronTask(
            CronTask(
                { closeContributionProcessor.closeContribution() },
                closeContributionCron
            )
        )
    }
}