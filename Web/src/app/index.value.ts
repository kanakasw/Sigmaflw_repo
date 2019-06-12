
 var data  = {
activities: [{
    activityID: null,
    activityName: "test_data",
    activityOrderIndex: 1,
    activityType: "INTEGRATION",
    activitykey: null,
    causesNewProcessExecution: null,
    createdDate: 1558013548162,
    eventGroupName: null,
    eventGroupOrderIndex: 1,
    modifiedDate: 1558013548162,
    processingSpecification:
    {
        ActivityExecutionID: "${ActivityExecutionID}",
        workflowFilePath: "${BaseFolderPath}\\Sub_${SubscriberID}\\Proc_${IntegrationProcessID}\\PentahoFiles\\2StepTransformation.ktr",
        StepToTrigger: 2,
        workflowType: "Transformation",
        inputParameters:
        {
            siteFilePath: "${BaseFolderPath}\\Sub_${SubscriberID}\\Proc_${IntegrationProcessID}\\Input\\xmlOutput.xml.xml",
            wastageFilePath: "${BaseFolderPath}\\Sub_${SubscriberID}\\Proc_${IntegrationProcessID}\\Input\\xmlOutput.xml.xml"
        },
        outputParameters: []
    },
    scheduleSetup: { cronExpression: "0 0 0 1 1 ? *" },
    triggerType: "SCHEDULED",
   // createdDate: 1558013548162,
    enabled: true
}],
fileEncryptionKey: null,
integrationProcessExecutions:
[{
executionFinishTime: null,
executionStartTime: null,
integrationProcessExecutionID: null,
status: "READY"
}],
integrationProcessName: "test_data",
integrationProcessUniqueReference: "sfklsdgjg-vdgb-fdgh",
modifiedDate: 1558013548163,
subscriberID: 1,
userId: "1"
}