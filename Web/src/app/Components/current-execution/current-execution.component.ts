import { Component, OnInit, ApplicationRef } from '@angular/core';
import { ApplicationStorageService } from 'src/app/Services/common/application-storage.service';
import { ExecutionService } from 'src/app/Services/execution.service';
import { ResponseHandlerService } from 'src/app/Services/common/response-handler.service';
import { ENDPOINTS } from 'src/app/index.constants';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { ExecutionStepOutcomePopupDialogComponent } from '../execution-step-outcome-popup-dialog/execution-step-outcome-popup-dialog.component';
import { Router } from '@angular/router';
import { NotificationService } from 'src/app/Services/common/notification.service';
import { isNullOrUndefined } from 'util';

@Component({
  selector: 'app-current-execution',
  templateUrl: './current-execution.component.html',
  styleUrls: ['./current-execution.component.css']
})
export class CurrentExecutionComponent implements OnInit {
  NavigationMenu = this.applicationStorage.navigationMenu;
  toggleAvgExecution = false;
  currentIntegrationProcessesExecution = {};
  activityDetailsCurrentExecution = {};
  toggleObject = {};
  message = "";
  lastExecutionDetails = {};
  averageExecutionTimeDetails = {};
  responseData: any;
  executionStepOutcome = {};
  objectKeys = Object.keys;

  constructor(private applicationStorage: ApplicationStorageService,
    private executionbService: ExecutionService,
    private toastr: NotificationService,
    private dialog: MatDialog,
    private router: Router
  ) {

  }

  ngOnInit() {
    this.NavigationMenu = JSON.parse(localStorage.getItem('navigationMenu'));
    var currentProcessID = this.getCurrentProcessID();
    this.getCurrentIntegrationProcessExecutionByIntegrationProcessID(currentProcessID);
    this.getAverageExecutionTimeByIntegrationProcessID(currentProcessID);
    this.getLastExecutionDetailsByIntegrationProcessID(currentProcessID);
    this.getAllActivityExecutionsByIntegrationProcessExecutionID(currentProcessID);
  }

  toggleSliding(integrationProcessExecutionID) {
    if (null == this.toggleObject[integrationProcessExecutionID]) {
      this.toggleObject[integrationProcessExecutionID] = true;
    } else {
      this.toggleObject[integrationProcessExecutionID] = !this.toggleObject[integrationProcessExecutionID];
    }
  }

  // get Average Execution Time By Integration Process ID
  getAverageExecutionTimeByIntegrationProcessID(integrationProcessID) {
    this.executionbService
      .getAverageExecutionTimeByIntegrationProcessID(ENDPOINTS, integrationProcessID)
      .subscribe(Response => {
        var responseData = Response
        this.averageExecutionTimeDetails = responseData;
      })
  }

  // retry Activity Execution By Activity Execution ID
  retryActivityExecutionByActivityExecutionID(integrationProcessExecutionID, integrationProcessID) {
    this.executionbService
      .retryActivityExecutionByActivityExecutionID(ENDPOINTS, integrationProcessID)
      .subscribe(Response => {
        this.responseData = Response
        this.toastr.showSuccess(this.responseData.message);
        //Make call to refresh all activities of current Process execution
        this.callToActivityExecutionsByIntegrationProcessExecutionID(integrationProcessExecutionID);
      }, error => {
        this.toastr.showerror("server error !!")
      })
  }

  //Get All Activity Executions By Integration Process Execution ID.
  getAllActivityExecutionsByIntegrationProcessExecutionID(integrationProcessExecutionID) {
    this.toggleSliding(integrationProcessExecutionID);
    if (!this.activityDetailsCurrentExecution.hasOwnProperty(integrationProcessExecutionID)) {
      //Make call to update all activities of Integration Process execution by integrationProcessExecutionID
      this.callToActivityExecutionsByIntegrationProcessExecutionID(integrationProcessExecutionID);
    }
  }

  callToActivityExecutionsByIntegrationProcessExecutionID(integrationProcessExecutionID) {
    this.executionbService
      .getAllActivityExecutionsByIntegrationProcessExecutionID(ENDPOINTS, integrationProcessExecutionID)
      .subscribe(Response => {
        var responseData = Response;
        this.activityDetailsCurrentExecution[integrationProcessExecutionID] = responseData;
      })
  }

  //Get Current Integration Process Execution By Integration Process ID.
  getCurrentIntegrationProcessExecutionByIntegrationProcessID(currentProcessID) {
    this.executionbService
      .getCurrentIntegrationProcessExecutionByIntegrationProcessID(ENDPOINTS, currentProcessID)
      .subscribe(Response => {
        var responseData = Response;
        console.log(responseData)
        this.currentIntegrationProcessesExecution = responseData;
        this.message = "";
      }, error => {
        this.toastr.showerror('server error!!')
      })
  }

  getCurrentProcessID() {
    var currentProcessID = "";
    var noOfProcess = this.NavigationMenu.integrationProcesses.length;
    var integrationProcessName = this.applicationStorage.getValue('ProcessHeader');
    for (var j = 0; j < noOfProcess; j++) {
      var dummyintegrationProcessName = this.NavigationMenu.integrationProcesses[j].integrationProcessName;
      if (dummyintegrationProcessName == integrationProcessName) {
        currentProcessID = this.NavigationMenu.integrationProcesses[j].integrationProcessID;
        break;
      }
    }
    return currentProcessID;
  }

  containerClick() {
    this.message = "";
  }

  redirectToLogin() {
    this.router.navigate([''])
  }

  displayCurrentExecutionOutcomePopup(ev, integrationProcessExecutionID, activityExecutionID) {
    this.executionStepOutcome = this.extractExecutionOutcome(integrationProcessExecutionID, activityExecutionID);
    let dialog = this.dialog.open(ExecutionStepOutcomePopupDialogComponent, {
      data: this.executionStepOutcome
    });
  }

  extractExecutionOutcome(integrationProcessExecutionID, activityExecutionID) {
    var executionStepOutcome = {};
    var activityExecutionsPerProcessExecution = this.activityDetailsCurrentExecution[integrationProcessExecutionID];

    activityExecutionsPerProcessExecution.forEach(activityExecution => {
      if (activityExecutionID === activityExecution.activityExecutionID) {
        var executionStepOutcomeString = activityExecution.executionStepOutcome;
        executionStepOutcome = JSON.parse(executionStepOutcomeString);
      }
    });
    return executionStepOutcome;
  }

  getExecutionOutcomeContents(executionStepOutcome) {
    if (!executionStepOutcome || Object.keys(executionStepOutcome).length == 0) {
      return "No Execution Outcome is available..."
    }
    var logEntries = "LOG Entries : \n";
    var etlStepOutcome = "ETL Step Runtime : \n";
    var outputParam = "OutputParam : \n";

    executionStepOutcome.logEntries.forEach(item => {
      logEntries = logEntries + item.key + " : " + item.value + "\n";
    });
    executionStepOutcome.etlStepRuntime.inputParameters.forEach(item => {
      etlStepOutcome = etlStepOutcome + item.key + " : " + item.value + "\n";
    });

    etlStepOutcome = etlStepOutcome + "ktrFilePath : " + executionStepOutcome.etlStepRuntime.ktrFilePath + "\n";

    executionStepOutcome.outputParameters.forEach(item => {
      outputParam = outputParam + item.key + " : " + item.value + "\n";
    });
    return logEntries + etlStepOutcome + outputParam;
  }

  getLastExecutionDetailsByIntegrationProcessID(integrationProcessID) {
    this.executionbService
      .getLastExecutionDetailsByIntegrationProcessID(ENDPOINTS, integrationProcessID)
      .subscribe(Response => {
        var responseData = Response
        if (isNullOrUndefined(this.currentIntegrationProcessesExecution))
          this.currentIntegrationProcessesExecution = Response;
        if (responseData == null) {
          this.lastExecutionDetails = {
            "Last Execution Details": "Not Available"
          };
        }
        else
          this.lastExecutionDetails = responseData;
      })
  }
}
