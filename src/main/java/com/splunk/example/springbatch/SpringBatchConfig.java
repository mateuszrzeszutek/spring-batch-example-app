package com.splunk.example.springbatch;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBatchConfig {
  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final RecordRepository recordRepository;

  @Autowired
  public SpringBatchConfig(
      JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory,
      RecordRepository recordRepository) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
    this.recordRepository = recordRepository;
  }

  @Bean
  Job job() {
    return jobBuilderFactory.get("testJob").start(step()).build();
  }

  @Bean
  Step step() {
    return stepBuilderFactory
        .get("testStep")
        .<Integer, Record>chunk(5)
        .reader(itemReader())
        .processor(itemProcessor())
        .writer(itemWriter())
        .build();
  }

  @Bean
  ItemReader<Integer> itemReader() {
    List<Integer> ids = IntStream.range(1, 11).boxed().collect(Collectors.toList());
    return new ListItemReader<>(ids);
  }

  @Bean
  ItemProcessor<Integer, Record> itemProcessor() {
    return new TodoDownloadProcessor();
  }

  @Bean
  ItemWriter<Record> itemWriter() {
    return recordRepository::saveAll;
  }
}
