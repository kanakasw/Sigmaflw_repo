import { Component, OnInit } from '@angular/core';
import { ApplicationStorageService } from 'src/app/Services/common/application-storage.service';
import { SubscriberService } from 'src/app/Services/subscriber.service';
import { ResponseHandlerService } from 'src/app/Services/common/response-handler.service';
import { ENDPOINTS } from 'src/app/index.constants';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { Location } from '@angular/common';
import { NotificationService } from 'src/app/Services/common/notification.service';
import { FormControl } from '@angular/forms';
import { ExecutionService } from 'src/app/Services/execution.service';
import { UserServiceService } from 'src/app/Services/user-service.service';
import { state } from '@angular/animations';
import { isNullOrUndefined } from 'util';
@Component({
  selector: 'app-process',
  templateUrl: './process.component.html',
  styleUrls: ['./process.component.css']
})
export class ProcessComponent implements OnInit {
  NavigationMenu: any;
  objectKeys = Object.keys;
  integrationProcessID = 0;
  integrationProcess: any;
  integrationProcessEnabled = false;
  responseData: any;
  currentState = this.router.url;
  selectedProcessTab: any
  selectedExecution: any;
  users: any;
  processOwner: any;
  submitIntegrationProcess = false;
  stateName: any;
  activitySelected = false;
  showProcess = true;
  activityId: any;
  selectedIndex: number = 0;
  processOwnername: any;
  LoggedInUserRole: any;
  currentProcessState : any;
  constructor(private applicationStorage: ApplicationStorageService,
    private subscriberSerice: SubscriberService,
    private toastr: NotificationService,
    private router: Router,
    private userService: UserServiceService
  ) {
  }

  ngOnInit() {

    this.LoggedInUserRole = sessionStorage.getItem('role');
    this.currentState = this.router.url;
    this.NavigationMenu = JSON.parse(localStorage.getItem('navigationMenu'));
    var noOfProcess = this.NavigationMenu.integrationProcesses.length;
    for (var j = 0; j < noOfProcess; j++) {
      this.stateName = "/process/" + this.getStateName(this.NavigationMenu.integrationProcesses[j].integrationProcessID);
      if (this.stateName == this.currentState) {
        this.integrationProcess = this.NavigationMenu.integrationProcesses[j];
        this.processOwner = this.integrationProcess.userId
        this.getCurrentProcessOwner(this.integrationProcess.userId);
        this.integrationProcessEnabled = this.NavigationMenu.integrationProcesses[j].enabled;
        this.integrationProcessID = this.integrationProcess.integrationProcessID;
      }
    }
    this.getProcessState();
    this.getActiveUsers();
  }

  getCurrentExecutionUiSref(processName) {
    this.selectedExecution = 'Current Execution Summary'
    return this.currentState + ".current";
  }

  getExecutionHistoryUiSref(processName) {
    this.selectedExecution = 'Execution History Summary'
    return this.currentState + ".history";
  }

  getProcessUiSref(processName, stateID) {
    return this.currentState + "." + this.getStateName(stateID);
  }

  getStateName(stateID) {
    return stateID;
  }

  //set current selected activityName 
  select(item) {
    this.selectedExecution = item;
    this.showProcess = false;
    this.activitySelected = true;
    this.activityId = item.integrationProcessID + '.' + item.activityID
  }

  //set current selected Execution Summary
  selectExecutionSummaryItem(item) {
    this.selectedExecution = item;
  }

  isActive(item) {
    return this.selectedExecution === item;
  }

  updateIntegrationProcessSetup() {
    this.submitIntegrationProcess = true;
    this.integrationProcess.enabled = this.integrationProcessEnabled;
    this.integrationProcess.userId = this.processOwner;
    this.subscriberSerice.updateIntegrationProcessSetup(ENDPOINTS, this.integrationProcessID, this.integrationProcess)
      .subscribe(Response => {
        this.responseData = Response;
        this.toastr.showSuccess("Integration process setup successfully updated");
      })
  }

  tabClick(tab) {
    if (tab.index == 1) {
      //set initial default selection : current execution summary when user clicks on Executions tab
      this.selectedExecution = 'Current Execution Summary';
      //set selected tab
      this.selectedProcessTab = tab.index;

    }
  }

  executeProcess() {
    this.subscriberSerice.executeProcessByIntegrationProcessID(this.integrationProcessID)
      .subscribe(Response => {
        this.toastr.showSuccess("Process executed successfully !!")
      },
        error => {
          this.toastr.showerror("Server error !!")
        })
    this.selectedExecution = 'Current Execution Summary';
    this.selectedIndex = 1;
  }
  redirectToProcessDetails() {
    this.activitySelected = false;
    this.showProcess = true;
  }

  getActiveUsers() {
    this.userService.userlist()
      .subscribe(Response => {
        var userdata = [];
        userdata = Response;
        this.users = userdata.filter(user =>
          user.status == "ACTIVE" && this.processOwner != user.userId
        )
      })
  }
  changeProcessOwner(selectedUser) {
    this.processOwner = selectedUser;
  }

  getCurrentProcessOwner(userid) {
    this.userService.userDetails(userid)
      .subscribe(response => {
        this.processOwnername = response.login
      })
  }
  getProcessState() {
    this.currentProcessState = sessionStorage.getItem('selectedProcessState');
  }
}
