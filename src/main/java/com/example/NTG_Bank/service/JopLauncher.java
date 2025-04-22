package com.example.NTG_Bank.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class JopLauncher implements CommandLineRunner {
        private final JobLauncher jobLauncher;
        private final Job importDataJob;

        public JopLauncher(JobLauncher jobLauncher, Job importDataJob) {
            this.jobLauncher = jobLauncher;
            this.importDataJob = importDataJob;
        }

        @Override
        public void run(String... args) throws Exception {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(importDataJob, jobParameters);
            System.out.println("Job Status: " + execution.getStatus());
        }

}
