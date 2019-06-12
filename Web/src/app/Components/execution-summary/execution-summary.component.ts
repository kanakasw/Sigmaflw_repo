import { Component, OnInit, Input } from '@angular/core';
import { ApplicationStorageService } from 'src/app/Services/common/application-storage.service';
import { ExecutionService } from 'src/app/Services/execution.service';
import { ResponseHandlerService } from 'src/app/Services/common/response-handler.service';
import { ENDPOINTS } from 'src/app/index.constants';
import { MatDialog } from '@angular/material';
import { ExecutionStepOutcomePopupDialogComponent } from '../execution-step-outcome-popup-dialog/execution-step-outcome-popup-dialog.component';

@Component({
  selector: 'app-execution-summary',
  templateUrl: './execution-summary.component.html',
  styleUrls: ['./execution-summary.component.css']
})
export class ExecutionSummaryComponent implements OnInit {

  NavigationMenu = this.applicationStorage.navigationMenu;
  integrationProcessExecutions = [];

  currentProcessID: any;
  toggleObject = {};
  activityDetailsExecution: any;
  executionStepOutcome: any;
  isIntegrationProcessExecution: Boolean;
  activityExecutions = {};

  constructor(private applicationStorage: ApplicationStorageService,
    private executionService: ExecutionService,
    private dialog: MatDialog) { }

  ngOnInit() {
    this.NavigationMenu = JSON.parse(localStorage.getItem('navigationMenu'));
    this.currentProcessID = this.getCurrentProcessID();
    this.getPreviousExecutionDetailsByIntegrationProcessID();
    /*     this.getAllActivityExecutionsByIntegrationProcessExecutionID(this.currentProcessID); */
  }

  //Toggle slides on execution page
  toggleSliding(integrationProcessExecutionID) {
    if (null == this.toggleObject[integrationProcessExecutionID]) {
      this.toggleObject[integrationProcessExecutionID] = true;
    } else {
      this.toggleObject[integrationProcessExecutionID] = !this.toggleObject[integrationProcessExecutionID];
    }
  }

  //Get All Activity Executions By Integration Process Execution ID
  getAllActivityExecutionsByIntegrationProcessExecutionID(integrationProcessExecutionID) {
    this.toggleSliding(integrationProcessExecutionID)

    this.executionService
      .getAllActivityExecutionsByIntegrationProcessExecutionID(ENDPOINTS, integrationProcessExecutionID)
      .subscribe(Response => {
        var responseData = Response;
        // execution details of first activity
        this.activityDetailsExecution = responseData[0];
        this.displayOutcomePopup(integrationProcessExecutionID)
      },
        error => {
          console.log(error);
        })
  }

  getPreviousExecutionDetailsByIntegrationProcessID() {
    var currentIntegrationProcessID = this.getCurrentProcessID();
    this.executionService
      .getPreviousExecutionDetailsByIntegrationProcessID(ENDPOINTS, currentIntegrationProcessID)
      .subscribe(Response => {
        var responseData = Response;
        this.integrationProcessExecutions = responseData;
        this.isIntegrationProcessExecution = (this.integrationProcessExecutions.length == 0) ? false : true;
      })
  }

  getCurrentProcessID() {
    var currentProcessID = "";
    var noOfProcess = Object.keys(this.NavigationMenu.integrationProcesses).length;
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

  displayOutcomePopup(integrationProcessExecutionID) {
    this.executionStepOutcome = this.extractExecutionOutcome(integrationProcessExecutionID, 16);
    let dialog = this.dialog.open(ExecutionStepOutcomePopupDialogComponent, {
      data: this.executionStepOutcome
    });
  }

  extractExecutionOutcome(integrationProcessExecutionID, activityExecutionID) {
    /*   activityExecutionsPerProcessExecution.forEach(activityExecution => {
        if (activityExecutionID === activityExecution.activityExecutionID) {
          var executionStepOutcomeString = activityExecution.executionStepOutcome;
          executionStepOutcome = JSON.parse(executionStepOutcomeString);
        }
      }); */
    var executionStepOutcome = {};
    var executionStepOutcomeString = this.activityDetailsExecution.executionStepOutcome;
    executionStepOutcome = JSON.parse(executionStepOutcomeString);
    return executionStepOutcome;
  }

  getExecutionOutcomeContents(executionStepOutcome) {
    if (!executionStepOutcome || Object.keys(executionStepOutcome).length == 0) {
      return "No Execution Outcome is available..."
    }
    console.log(JSON.stringify(executionStepOutcome));
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

}
