package com.basic.batch.jobparameter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MonthlySettlementJobConfiguration {

    @Bean
    public Job monthlySettlementJob(
            JobRepository jobRepository,
            Step settlementExecutionStep) {
        return new JobBuilder("monthlySettlementJob", jobRepository)
                .start(settlementExecutionStep)
                .build();
    }

    @Bean
    public Step settlementExecutionStep(
            JobRepository jobRepository,
            Tasklet settlementTasklet
    ) {
        return new StepBuilder("settlementExecutionStep", jobRepository)
                .tasklet(settlementTasklet, null) // 트랜잭션 매니저가 필요 없는 경우 null 또는 PlatformTransactionManager 주입
                .build();
    }

    @Bean
    @StepScope
    public Tasklet settlementTasklet(
            @Value("#{jobParameters['targetDate']}") LocalDate targetDate,
            @Value("#{jobParameters['executionTime']}") LocalDateTime executionTime
    ) {
        return (contribution, chunkContext) -> {
            log.info("정기 정산 및 배치 작업 시작");
            log.info("정산 대상일: {}", targetDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            log.info("배치 실행 시각: {}", executionTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            log.info("단계별 데이터 검증 및 처리 프로세스 가동...");

            LocalDateTime processTime = executionTime;
            String[] steps = {
                    "대상 데이터 원장(Master) 로드",
                    "거래 내역 대조 및 정산 검증",
                    "정산 결과 DB 반영 및 업데이트",
                    "아카이빙 및 실행 이력 기록"
            };

            for (int i = 0; i < steps.length; i++) {
                processTime = processTime.plusMinutes(15);
                log.info("[Step {}] {} | 처리 시각: {}",
                        i + 1,
                        steps[i],
                        processTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            }

            log.info("정기 정산 작업 완료: 데이터 정합성 확인 완료");
            log.info("전체 프로세스 종료 시각: {}", processTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            return RepeatStatus.FINISHED;
        };
    }
}
