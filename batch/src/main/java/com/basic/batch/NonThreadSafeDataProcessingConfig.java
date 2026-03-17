package com.basic.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class NonThreadSafeDataProcessingConfig {
    private final AtomicInteger totalProcessedCount = new AtomicInteger(0);
    private final int TARGET_THRESHOLD = 100; // 처리 목표 수치 (Integer)

    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;

    @Bean
    public Job nonThreadSafeDataProcessingJob() {
        return new JobBuilder("nonThreadSafeDataProcessingJob", jobRepository)
                .start(initBatchStep())          // 1. 배치 초기화
                .next(processDataStep())         // 2. 데이터 처리 및 카운팅
                .next(verifyResultStep())        // 3. 최종 결과 검증
                .build();
    }

    @Bean
    public Step initBatchStep() {
        return new StepBuilder("initBatchStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("Batch initialization step started");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step processDataStep() {
        return new StepBuilder("processDataStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    // 비즈니스 로직 수행 후 카운트 증가 (예: 20개씩 처리 가정)
                    int currentBatchSize = 20;
                    int currentTotal = totalProcessedCount.addAndGet(currentBatchSize);

                    System.out.println(">> 데이터 처리 중... 현재 누적: " + currentTotal + " / " + TARGET_THRESHOLD);

                    // 목표치에 도달할 때까지 반복 수행
                    if (currentTotal < TARGET_THRESHOLD) {
                        return RepeatStatus.CONTINUABLE;
                    } else {
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean
    public Step verifyResultStep() {
        return new StepBuilder("verifyResultStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    int finalCount = totalProcessedCount.get();
                    System.out.println(">> 최종 검증 완료. 총 " + finalCount + "건의 데이터를 처리했습니다.");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
