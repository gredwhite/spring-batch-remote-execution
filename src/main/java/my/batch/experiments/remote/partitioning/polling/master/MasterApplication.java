package my.batch.experiments.remote.partitioning.polling.master;

import my.batch.experiments.remote.partitioning.BasicPartitioner;
import my.batch.experiments.remote.partitioning.BrokerConfiguration;
import org.apache.activemq.broker.BrokerService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import java.util.Date;

@EnableBatchProcessing
@SpringBootApplication
@Import({BasicPartitioner.class, BrokerConfiguration.class})
public class MasterApplication {

    @Value("${broker.url}")
    private String brokerUrl;

    public static void main(String[] args) {
        SpringApplication.run(MasterApplication.class, args);
    }


    @Autowired
    private Job remotePartitioningJob;
    @Autowired
    private JobLauncher jobLauncher;

    @PostConstruct
    public void init() throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector(brokerUrl);
        broker.start();
    }

    @EventListener
    public void refreshed(ContextRefreshedEvent event) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("some_key", "some_new_value_4")
                .addDate("date", new Date())
                .toJobParameters();
        jobLauncher.run(remotePartitioningJob, jobParameters);
    }

}