package com.technolins.springbatch;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;

@SpringBootApplication
public class SpringBatchApplication {
    private static final Logger log = LoggerFactory.getLogger(SpringBatchApplication.class);

	public static void main(String[] args) {
	    String strClassPath = System.getProperty("user.dir");
        System.out.println("strClassPath: "+strClassPath);
        DOMConfigurator.configure(strClassPath + File.separator + "src" + File.separator + "main"+ File.separator +"resources" + File.separator + "log4j.xml");

	    log.info("Running coy !");

//	    SpringApplication.run(SpringBatchApplication.class, args);
		SpringApplication app = new SpringApplication(SpringBatchApplication.class);
		ConfigurableApplicationContext ctx = app.run(args);

		SpringBatchApplication main = new SpringBatchApplication();
		try {
			main.runJob(ctx);
			System.out.println("SUCCESS");
		}catch (Exception e){
			log.error(e.getMessage());
			System.out.println("FAILED");
		}

		System.exit(0);
	}

	private void runJob(ConfigurableApplicationContext ctx) throws JobExecutionAlreadyRunningException,
			JobRestartException,
			JobInstanceAlreadyCompleteException,
			JobParametersInvalidException {
		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
		Job batchDemo = ctx.getBean("batchExportDb2ExcelJob", Job.class);
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("JobID", String.valueOf(System.nanoTime()))
				.toJobParameters();
		JobExecution jobExecution = jobLauncher.run(batchDemo,jobParameters);

		BatchStatus batchStatus = jobExecution.getStatus();
		while (batchStatus.isRunning()){
			log.info("**************** Batch Still Running . . .**************");
			try {
				Thread.sleep(5000);
			}catch (InterruptedException e){
				log.info(e.getMessage());
			}
		}

		ExitStatus exitStatus = jobExecution.getExitStatus();
		String exitCode = exitStatus.getExitCode();
		log.info("Exit Code : {}", exitCode);

		if(exitStatus.equals(ExitStatus.COMPLETED)){
			System.out.println("SUCCESS");
		}

		JobInstance jobInstance = jobExecution.getJobInstance();
		log.info("***** Name of the job {}", jobInstance.getJobName());
		log.info("***** Job instance Id: {}", jobInstance.getInstanceId());
		System.exit(0);

	}
}
