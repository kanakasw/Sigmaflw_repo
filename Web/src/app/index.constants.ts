
export const ENDPOINTS = {
    /* "SERVICE_ADDRESS": "http://192.168.1.161:8081/", */
    /*   "SERVICE_ADDRESS": "http://192.168.1.120:8080/", */
    "SERVICE_ADDRESS" : "http://localhost:8080/",
    "AUTH_ENDPOINT": "/oauth/token", 
    "FORGET_PASSWORD_ENDPOINT": "/users/password/sendResetPasswordLink",
    "LOGOUT_ENDPOINT": "/revoketoken",
    "GET_SUBSCRIBER_ENDPOINT": "/Integration/subscriber/username",
    "UPDATE_ACTIVITY_BY_ACTIVITY_ID": "/Integration/Activity/updateCron/",
    "EXECUTE_PROCESS": "/Integration/subscriber/process/{integrationProcessID}/execute",
    "GetCurrentIntegrationProcessExecution": "/Integration/subscriber/process/{integrationProcessID}/IntegrationProcessExecution/current",
    "GetAllActivityExecutions": "/Integration/IntegrationProcessExecution/{integrationProcessExecutionID}/ActivityExecutions/",
    "GetAllPreviousExecutionDetails": "/Integration/IntegrationProcess/{integrationProcessID}/IntegrationProcessExecutions",
    "GetProcessDetailsByProcessID": "/Integration/subscriber/process/{integrationProcessID}",
    "retryActivityExecution": "/Integration/Activity/ActivityExecution/{activityExecutionID}/ReExecute",
    "AverageExecutionTime": "/Integration/IntegrationProcess/{integrationProcessID}/AverageExecutionTime",
    "LastSuccessfulExecutionDetails": "/Integration/IntegrationProcess/{integrationProcessID}/IntegrationProcessExecution/lastSuccessful",
    "GetInputParameter": "/Integration/Activity/{activityID}/getInputParameters",
    "GetUniqueKeywords": "/Integration/subscriber/{subscriberID}/getUniqueKeywords",
    "UpdateInputParameter": "/Integration/Activity/{activityID}/updateInputParameters",
    "UpdateIntegrationProcessSetup": "/Integration/subscriber/process/{IntegrationProcessID}/update",
    "UpdateProcessingSpecification": "/Integration/Activity/{activityID}/updateProcessingSpecification",
    "UserList": "/Integration/users",
    "UserDetails": "/Integration/user/id/{id}",
    "UpdateUser": "/Integration/user/{id}/update",
    "CreateUser": "/Integration/user",
    "DeleteUser": "/Integration/user/{id}/deactivate",
    "CreateProcess": "/Integration/subscriber/process/create",
    "getUserByLogin": "/Integration/user/username/{name}"
}

export const STATUS_CODE = {
    "BAD_REQUEST": 400,
    "UN_AUTHORIZED": 401,
    "INTERNAL_ERROR": 500,
    "NOT_FOUND": 404,
    "OK": 200,
    "SERVICE_NOT_REACHABLE": -1
}

export const STATUS_MESSAGE = {
    "BAD_REQUEST": "ERROR : 400. Bad Request error. Something is missing.",
    "UN_AUTHORIZED": "ERROR : 401. User is not logged in. Please Login with your credentials.",
    "INTERNAL_ERROR": "ERROR : 500. Internal server error. Please contact admin.",
    "NOT_FOUND": "ERROR : 404. This url is not configured. Please contact admin.",
    "OK": "SUCCESS : 200. Response received from server.",
    "SERVICE_NOT_REACHABLE": "Service is unavailable,Please try after sometime."
}


/* export const PROCEESS_STATES = {
    "COMPLETED" : "Completed",
    "ERROR" : "Failed with error",
    "PROCESSING": "Processing",
    "READY" : "Ready to Excecute",
    "SCHEDULED" : "Scheduled",
    "SCHEDULED_UNDEFINED" : "Schedule undefined"

} */