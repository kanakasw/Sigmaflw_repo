package com.data.integration.service.vo;

import com.data.integration.data.Activity;
import com.data.integration.data.ActivityExecution;
import com.data.integration.data.IntegrationProcess;
import com.data.integration.data.IntegrationProcessExecution;
import com.data.integration.data.Subscriber;

/**
 * This class holds Setup Objects (i.e. Subscriber,IntegrationProcess etc.) for
 * Activity being executed.
 * 
 * @author Aniket
 *
 */
public class ActivityExecutionContextVO {

    private Subscriber subscriber;

    private IntegrationProcess integrationProcess;

    private IntegrationProcessExecution integrationProcessExecution;

    private ActivityExecution activityExecution;

    private Activity activity;

    private ExecuteIntegrationProcessVO executeIntegrationProcessVO;

    public ActivityExecutionContextVO(Subscriber subscriber,
            IntegrationProcess integrationProcess,
            IntegrationProcessExecution integrationProcessExecution,
            ActivityExecution activityExecution, Activity activity,
            ExecuteIntegrationProcessVO executeIntegrationProcessVO) {
        super();
        this.subscriber = subscriber;
        this.integrationProcess = integrationProcess;
        this.integrationProcessExecution = integrationProcessExecution;
        this.activityExecution = activityExecution;
        this.activity = activity;
        this.executeIntegrationProcessVO = executeIntegrationProcessVO;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public IntegrationProcess getIntegrationProcess() {
        return integrationProcess;
    }

    public void setIntegrationProcess(IntegrationProcess integrationProcess) {
        this.integrationProcess = integrationProcess;
    }

    public IntegrationProcessExecution getIntegrationProcessExecution() {
        return integrationProcessExecution;
    }

    public void setIntegrationProcessExecution(
            IntegrationProcessExecution integrationProcessExecution) {
        this.integrationProcessExecution = integrationProcessExecution;
    }

    public ActivityExecution getActivityExecution() {
        return activityExecution;
    }

    public void setActivityExecution(ActivityExecution activityExecution) {
        this.activityExecution = activityExecution;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ExecuteIntegrationProcessVO getExecuteIntegrationProcessVO() {
        return executeIntegrationProcessVO;
    }

    public void setExecuteIntegrationProcessVO(
            ExecuteIntegrationProcessVO executeIntegrationProcessVO) {
        this.executeIntegrationProcessVO = executeIntegrationProcessVO;
    }
}
