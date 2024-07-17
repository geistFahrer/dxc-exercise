package org.bishu;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParametersRequest;
import software.amazon.awssdk.services.ssm.model.GetParametersResponse;
import software.amazon.awssdk.services.ssm.model.Parameter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExerciseLambda implements RequestHandler<String, String> {

    public String handleRequest(String event, Context context) {
        LambdaLogger logger = context.getLogger();

        logger.log("Creating ssm client");
        SsmClient ssmClient = SsmClient.builder().region(Region.US_EAST_1).build();
        GetParametersRequest getParametersRequest = GetParametersRequest.builder()
                .names("Name", "Value").build();
        logger.log("SSM parameter reqeust" + getParametersRequest.toString());
        GetParametersResponse response = null;
        try {
            response = ssmClient.getParameters(getParametersRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<Parameter> parameters = response.parameters();
        Map<String, String> parameterMap =
        parameters.stream().collect(Collectors.toMap(Parameter::name, Parameter::value));

        String fileName = "parameters.txt";
        logger.log("Creating s3 client");
        S3Client s3Client = S3Client.builder().region(Region.US_EAST_1).build();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("dxc-exercise-parameter").key(fileName).build();
        logger.log("putting file in s3");
        s3Client.putObject(putObjectRequest, RequestBody.fromString(parameterMap.toString()));
        return "Uploaded parameters";
    }
}