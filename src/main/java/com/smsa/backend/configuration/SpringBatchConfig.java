package com.smsa.backend.configuration;

import java.io.*;
import java.util.logging.Logger;
import com.amazonaws.services.s3.AmazonS3;
import com.smsa.backend.model.ManifestData;
import com.smsa.backend.repository.ManifestDataRepository;
import com.smsa.backend.service.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;


@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {
    private static final Logger logger = Logger.getLogger(SpringBatchConfig.class.getName());

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private ManifestDataRepository manifestDataRepository;

    @Autowired
    private AmazonS3 s3Client;
    @Autowired
    private StorageService storageService;

    @Value("${smsa.file.location}")
    String sampleFileLocalLocation;

    @Bean
    public FlatFileItemReader<ManifestData> reader() {
        FlatFileItemReader<ManifestData> itemReader = new FlatFileItemReader<>();
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    @Bean
    public MultiResourceItemReader<ManifestData> multiResourceItemReader() {

        MultiResourceItemReader<ManifestData> multiResourceItemReader = new MultiResourceItemReader<>();

        try {
            // Log when setting up the MultiResourceItemReader
            logger.info("Setting up MultiResourceItemReader...");

            // Set the delegate reader
            multiResourceItemReader.setDelegate(reader());

            // Get the CSV resources
            Resource[] resources = getResources();

            // Log the number of resources found
            logger.info("Number of CSV resources found: " + resources.length);

            // Set the resources for the MultiResourceItemReader
            multiResourceItemReader.setResources(resources);

            // Log successful setup
            logger.info("MultiResourceItemReader setup completed successfully");
        } catch (Exception e) {
            // Log any exceptions that occur during setup
            logger.severe("Error setting up MultiResourceItemReader: " + e.getMessage());
            e.printStackTrace();
        }

        return multiResourceItemReader;
    }

    private LineMapper<ManifestData> lineMapper(){
        DefaultLineMapper<ManifestData> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("companyName", "mode", "shipmentMode", "encodeDesc", "loadingPortCode", "encodeDescSec", "destinationPort", "carrierCode", "flightNumber", "departureDate", "arrivalDate", "actualWeight", "dimWeight", "prefix", "manifestNumber", "blDate", "awb", "orderNumber", "customShipDate", "accountNumber", "weight", "amount", "shipmentCountry");

        BeanWrapperFieldSetMapper<ManifestData> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ManifestData.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public ManifestDataProcessor processor(){
        return new ManifestDataProcessor();
    }

    @Bean
    public RepositoryItemWriter<ManifestData> writer(){
        RepositoryItemWriter<ManifestData> writer = new RepositoryItemWriter<>();
        writer.setRepository(manifestDataRepository);
        writer.setMethodName("save");
        return writer;
    }

    public Step step1(){
            return stepBuilderFactory.get("csv-step")
                    .<ManifestData,ManifestData>chunk(1000)
                    .reader(multiResourceItemReader())
                    .processor(processor())
                    .writer(writer())
                    .build();
    }

    @Bean
    public Job runJob(){
        return jobBuilderFactory.get("ImportManifestData")
                .flow(step1())
                .end()
                .build();
    }

    public Resource[] getResources() throws IOException {
        String directoryPath = sampleFileLocalLocation + "/Manifest Data";
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        return resolver.getResources("file:" + directoryPath + "/*.csv");
    }
}
