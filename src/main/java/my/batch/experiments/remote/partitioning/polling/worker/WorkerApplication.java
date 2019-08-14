package my.batch.experiments.remote.partitioning.polling.worker;

import my.batch.experiments.remote.partitioning.BrokerConfiguration;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@EnableBatchProcessing
@SpringBootApplication
@Import({BrokerConfiguration.class})

public class WorkerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }
}