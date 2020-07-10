package com.technolins.springbatch.job;

import com.technolins.springbatch.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class SpringBatchJob extends DefaultBatchConfigurer {
    private static final Logger log = LoggerFactory.getLogger(SpringBatchJob.class);
    private static final String JOB_NAME = "Batch Export Db to Excel";

    private static final String STEP = "Read Data from Database and Export to Excel File";
//    private static final String STEP_EXPORT_TO_EXCEL = "Export to Excel File";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobExplorer jobExplorer;

    @PreDestroy
    public void destroy() throws NoSuchJobException{
        jobExplorer.getJobNames().forEach(name -> log.info("job name: {}", name));
        jobExplorer.getJobInstances(JOB_NAME, 0, jobExplorer.getJobInstanceCount(JOB_NAME)).forEach(
             jobInstance -> {
                 log.info("job instance id {}", jobInstance.getInstanceId());
             }
        );
    }

    @Bean
    public Job batchExportDb2ExcelJob() throws Exception {
        log.info("run job: {}", JOB_NAME);
        return jobBuilderFactory.get(JOB_NAME)
                .start(step())
                .build();
    }

    @Bean
    public Step step() throws Exception{
        log.info("run step: {}", STEP);
        return stepBuilderFactory.get(STEP)
                .<Employee, Employee> chunk(0)
                .reader(reader())
                .writer(writer())
                .listener(logProcessListener())
                .build();
    }


    @StepScope
    @Bean
    public ItemReader reader() throws Exception{
        log.info("run reader part of {}", JOB_NAME);
        return new DataReader();
    }

    @StepScope
    @Bean
    public ItemWriter<Employee> writer() {
        log.info("run writer part of {}",JOB_NAME);
        return new DataWriter() ;
    }

    @Bean
    public Object logProcessListener() {
        System.out.println("logProcessListener . . .");
        return "logProcessListener . . .";
    }

}
