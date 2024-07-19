# dxc-exercise
This is the assignment repo for dxc technology

This repository contains Lambda handler code and cloud formation template to create the lambda
function.

## Pre-requisites
 - AWS account
 - Maven
 - Java SDK 17

## How to run
 1. Create s3 bucket to store the jar file for lambda handler and upload the jar in the bucket.
 2. Create SSM parameters with key as **Name** and **Value**
 3. Create s3 bucket to which lambda function will store the ssm parameters key and value.
 4. Use the dxc-exercise-cloudformation-template.yaml to create the stack
 5. Once the lambda function is created test it from console. 
