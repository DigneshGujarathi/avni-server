package org.openchs.importer.batch.csv;

import org.openchs.service.BulkUploadS3Service;
import org.openchs.service.S3Service;
import org.openchs.service.S3Service.ObjectInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;

import static java.lang.String.format;

@Component
@JobScope
public class ErrorFileCreatorListener implements JobExecutionListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final S3Service s3Service;

    @Value("#{jobParameters['uuid']}")
    private String uuid;
    @Value("#{jobParameters['s3Key']}")
    private String s3Key;
    @Value("#{jobParameters['type']}")
    private String type;
    @Value("#{jobParameters['fileName']}")
    private String originalFileName;
    private File errorFile;
    private String jobInfo;
    private BulkUploadS3Service bulkUploadS3Service;

    @Autowired
    public ErrorFileCreatorListener(S3Service s3Service, BulkUploadS3Service bulkUploadS3Service) {
        this.s3Service = s3Service;
        this.bulkUploadS3Service = bulkUploadS3Service;
    }

    @PostConstruct
    public void init() {
        jobInfo = format("Job{type='%s',uuid='%s',fileName='%s'}", type, uuid, originalFileName);
        errorFile = bulkUploadS3Service.getLocalErrorFile(uuid);
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        try {
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(s3Service.getObjectContent(s3Key)));
            String headerRow = csvReader.readLine();
            csvReader.close();

            FileWriter writer = new FileWriter(errorFile, true);
            writer.append(headerRow);
            writer.append(',');
            writer.append("error");
            writer.append('\n');
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to create temp file to record failures");
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        logger.info(format("Bulkupload '%s'! %s", jobExecution.getStatus(), jobInfo));
        try {
            ObjectInfo metadata = bulkUploadS3Service.uploadErrorFile(errorFile, uuid);
            logger.info(format("Bulkupload '%s'! Check for errors at '%s'", jobExecution.getStatus(), metadata.getKey()));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to create error files in S3", e.getMessage());
        }
    }
}