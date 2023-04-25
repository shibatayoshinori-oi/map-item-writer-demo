package com.example.mapitemwriterdemo

import org.springframework.batch.core.ItemReadListener
import org.springframework.batch.core.ItemWriteListener
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.support.ListItemReader
import org.springframework.batch.item.support.PassThroughItemProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableBatchProcessing
class BatchConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val mapUsingTasklet: MapUsingTasklet
) : DefaultBatchConfigurer() {

    @Bean
    fun job(): Job {
        return jobBuilderFactory.get("sampleJob")
            .start(step1())
            .next(step2())
            .build()
    }

    @Bean
    fun step1(): Step {
        return stepBuilderFactory.get("sampleStep1")
            .chunk<Int, Int>(2)
            .reader(itemReader1())
            .processor(PassThroughItemProcessor())
            .writer(itemWriter1())
            .listener(itemReadListener1())
            .listener(itemWriteListener1())
            .build()
    }

    @StepScope
    @Bean
    fun itemReader1(): ItemReader<Int> {
        return ListItemReader(IntRange(1, 10).toList())
    }

    @Bean
    fun itemReadListener1(): ItemReadListener<Int> {
        return object : ItemReadListener<Int> {
            override fun beforeRead() {
                //NOOP
            }

            override fun afterRead(item: Int) {
                println("read $item")
            }

            override fun onReadError(ex: Exception) {
                //NOOP
            }
        }
    }

    @JobScope
    @Bean
    fun itemWriter1(): MapItemWriter<String, Int> {
        return MapItemWriter { it.toString() }
    }

    @Bean
    fun itemWriteListener1(): ItemWriteListener<String> {
        return object : ItemWriteListener<String> {
            override fun beforeWrite(items: MutableList<out String>) {
                //NOOP
            }

            override fun afterWrite(items: MutableList<out String>) {
                println("wrote $items")
            }

            override fun onWriteError(ex: Exception, items: MutableList<out String>) {
                //NOOP
            }
        }
    }

    @Bean
    fun step2(): Step {
        return stepBuilderFactory.get("sampleStep2")
            .tasklet(mapUsingTasklet)
            .build()
    }
}