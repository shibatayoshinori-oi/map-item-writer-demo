package com.example.mapitemwriterdemo

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component

@StepScope
@Component
class MapUsingTasklet(
    private val mapItemWriter: MapItemWriter<String, Int>
) : Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        println(mapItemWriter.writtenItems)

        return RepeatStatus.FINISHED
    }
}