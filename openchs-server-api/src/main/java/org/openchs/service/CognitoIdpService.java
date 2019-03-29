package org.openchs.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;
import org.openchs.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Profile({"default", "live", "dev", "test"})
public class CognitoIdpService {
    private final Logger logger;

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    @Value("${cognito.poolid}")
    private String userPoolId;

    private Regions REGION = Regions.AP_SOUTH_1;

    private final String TEMPORARY_PASSWORD = "password";

    private AWSCognitoIdentityProvider cognitoClient;

    private boolean isDev;
    private Environment environment;

    @Autowired
    public CognitoIdpService(Environment environment) {
        this.environment = environment;
        this.isDev = isDev();
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @PostConstruct
    public void init() {
        cognitoClient = AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(getCredentialsProvider())
                .withRegion(REGION)
                .build();
        logger.info("Initialize CognitoIDP client");
    }

    private boolean isDev() {
        String[] activeProfiles = environment.getActiveProfiles();
        return activeProfiles.length == 1 && (activeProfiles[0].equals("dev") || activeProfiles[0].equals("test"));
    }

    private AWSStaticCredentialsProvider getCredentialsProvider() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey));
    }

    private AdminCreateUserRequest prepareCreateUserRequest(User user) {
        return new AdminCreateUserRequest()
                .withUserPoolId(userPoolId)
                .withUsername(user.getName())
                .withUserAttributes(
                    new AttributeType().withName("email").withValue(user.getEmail()),
                    new AttributeType().withName("phone_number").withValue(user.getPhoneNumber()),
                    new AttributeType().withName("email_verified").withValue("true"),
                    new AttributeType().withName("phone_number_verified").withValue("true"),
                    new AttributeType().withName("custom:userUUID").withValue(user.getUuid())
                )
                .withTemporaryPassword(TEMPORARY_PASSWORD);
    }

    public UserType createUser(User user) {
        if (isDev) {
            logger.info("Skipping Cognito CREATE in dev mode...");
            return null;
        }
        AdminCreateUserRequest createUserRequest = prepareCreateUserRequest(user);
        logger.info(String.format("Initiating CREATE cognito-user request | username '%s' | uuid '%s'", user.getName(), user.getUuid()));
        AdminCreateUserResult createUserResult =  cognitoClient.adminCreateUser(createUserRequest);
        logger.info(String.format("Created cognito-user | username '%s' | '%s'", user.getName(), createUserResult.toString()));
        return createUserResult.getUser();
    }

    private AdminUpdateUserAttributesRequest prepareUpdateUserRequest(User user) {
        return new AdminUpdateUserAttributesRequest()
                .withUserPoolId(userPoolId)
                .withUsername(user.getName())
                .withUserAttributes(
                    new AttributeType().withName("email").withValue(user.getEmail()),
                    new AttributeType().withName("phone_number").withValue(user.getPhoneNumber()),
                    new AttributeType().withName("custom:userUUID").withValue(user.getUuid())
                );
    }

    public void updateUser(User user) {
        if (isDev) {
            logger.info("Skipping Cognito UPDATE in dev mode...");
            return;
        }
        AdminUpdateUserAttributesRequest updateUserRequest = prepareUpdateUserRequest(user);
        logger.info(String.format("Initiating UPDATE cognito-user request | username '%s' | uuid '%s'", user.getName(), user.getUuid()));
        cognitoClient.adminUpdateUserAttributes(updateUserRequest);
        logger.info(String.format("Updated cognito-user | username '%s'", user.getName()));
    }

}