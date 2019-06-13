import { Component, OnInit } from '@angular/core';
import { ApplicationStorageService } from 'src/app/Services/common/application-storage.service';
import { SubscriberService } from 'src/app/Services/subscriber.service';
import { ResponseHandlerService } from 'src/app/Services/common/response-handler.service';
import { ENDPOINTS } from 'src/app/index.constants';
import { Router } from '@angular/router';
import { NotificationService } from 'src/app/Services/common/notification.service';
import { UserServiceService } from 'src/app/Services/user-service.service';
import { first } from 'rxjs/operators';
import * as _ from 'lodash';
import { processState } from 'src/app/Models/processStates';
import { ExecutionService } from 'src/app/Services/execution.service';
import { isNullOrUndefined } from 'util';
import { Observable } from 'rxjs';


@Component({
  selector: 'app-subscriber',
  templateUrl: './subscriber.component.html',
  styleUrls: ['./subscriber.component.css']
})
export class SubscriberComponent implements OnInit {
  NavigationMenu: any;
  flag = true;
  flag1 = false;
  disabledProcess = false;
  activeProcess = false;
  disableExecuteProcessButton = false;
  responseData: any;
  selected: any;
  processHeader: any;
  integrationProcesses: any;
  processForExecution: any;
  showprocessDiv = false;
  currentuser: any;
  integrationProcessID: string = 'integrationProcessID';
  p: number = 1;
  isProcessAvailable: boolean;
  IsProcesssing: boolean;
  constructor(private applicationStorage: ApplicationStorageService,
    private subscriberSerice: SubscriberService,
    private responseHandler: ResponseHandlerService,
    private router: Router,
    private toastr: NotificationService,
    private userService: UserServiceService,
    private executionbService: ExecutionService) { }

  ngOnInit() {
    this.getSubscriberDetails();
  }
  /*  openNav() {
 
     if (this.flag == false) {
       document.getElementById("mySidenav").style.width = "0";
       document.getElementById("main").style.marginLeft = "0";
       if (this.flag1 == true) {
         this.flag = true;
       }
       this.flag1 = true;
     }
 
     if (this.flag == true) {
       this.flag = false;
       this.flag1 = false;
       document.getElementById("mySidenav").style.width = "300px";
       document.getElementById("main").style.marginLeft = "300px";
     }
   } */

  getSubscriberDetails() {
    var username = sessionStorage.getItem('username')
    this.userService.getLoggedInUserDetails(username)
      .pipe(first())
      .subscribe(Response => {
        this.NavigationMenu = JSON.parse(localStorage.getItem('navigationMenu'));
        this.integrationProcesses = this.NavigationMenu.integrationProcesses;
        this.setProcessStates(this.integrationProcesses);
        this.isProcessAvailable = (this.integrationProcesses.length == 0) ? false : true;
        this.applicationStorage.navigationMenu = this.NavigationMenu;
      },
        error => {
          this.toastr.showerror("server error !")
        })
  }

  processDetailsExecution() {
    this.disableExecuteProcessButton = true;
    var integrationProcessID = document.querySelector('#value_integrationProcessID')[0].innerText;
    this.subscriberSerice.executeProcessByIntegrationProcessID(integrationProcessID)
      .subscribe(Response => {
        this.disableExecuteProcessButton = false;
        this.responseData = Response;
        this.toastr.showSuccess("Successfully updated Process details.");
      },
        error => {
          this.toastr.showerror("Server error !!");
        }
      )
  }

  select(item) {
    this.selected = item;
    this.applicationStorage.setValue('ProcessHeader', item.integrationProcessName)
    this.applicationStorage.processHeader = item.integrationProcessName;
  }

  isActive(item) {
    return this.selected === item;
  }

  createProcessStateDefinition() {
    var processStateName;

    if (this.NavigationMenu.hasOwnProperty("integrationProcesses")) {
      var noOfProcesses = Object.keys(this.NavigationMenu.integrationProcesses).length;
      for (var j = 0; j < noOfProcesses; j++) {
        var processStateName = this.getStateName(this.NavigationMenu.integrationProcesses[j].integrationProcessID);

        //Create Process States
        this.prepareStates(
          this.getStateName(this.NavigationMenu.integrationProcesses[j].integrationProcessID),
          this.prepareUrl("process/" + this.NavigationMenu.integrationProcesses[j].integrationProcessID),
          'app/subscriber/process.html',
          'ProcessController as processCTRL',
          'homepageState.'
        );

        //create activities states
        if (this.NavigationMenu.integrationProcesses[j].hasOwnProperty("activities")) {
          var size = Object.keys(this.NavigationMenu.integrationProcesses[j].activities).length;
          for (var i = 0; i < size; i++) {
            this.prepareStates(
              this.getStateName(this.NavigationMenu.integrationProcesses[j].activities[i].activityID),
              this.prepareUrl("activity/" + this.NavigationMenu.integrationProcesses[j].activities[i].activityID),
              'app/subscriber/activity.html',
              'ActivityController as activityCTRL',
              'homepageState.' + processStateName + '.'
            );
          }
        }

        //current Execution Summary States  
        this.prepareStates(
          "current",
          this.prepareUrl("executions/" + "current"),
          'app/subscriber/current.execution.html',
          'CurrentExecutionController as currentExecutionCTRL',
          'homepageState.' + processStateName + '.'
        );

        //history Execution Summary States
        this.prepareStates(
          "history",
          this.prepareUrl("executions/" + "previous"),
          'app/subscriber/executionSummary.html',
          'ExecutionSummaryController as executionSummaryCTRL',
          'homepageState.' + processStateName + '.'
        );
      }
    }
  }

  //helper method for preparing routing state data
  prepareStates(stateName, urlName, templateurl, controllername, parentState) {
    var StateDefinition = {
      state: stateName,
      url: urlName,
      templateURL: templateurl,
      controller: controllername,
      parentState: parentState
    }
  }

  getUiSref() {
    var stateName = this.selected.integrationProcessID
    var state = this.getStateName(stateName);
    this.router.navigate(['process', state])
  }

  getStateName(stateID) {
    return stateID;
  }

  prepareUrl(stateName) {
    stateName = stateName.toLowerCase().replace(/\b[a-z]/g, function (letter) {
      return letter.toUpperCase();
    });
    stateName = stateName.charAt(0).toLowerCase() + stateName.substr(1);
    stateName = stateName.replace(/ /g, "");
    return stateName;
  }

  loadProcessForExecution(process) {
    sessionStorage.setItem('selectedProcessState', process.state);
    if (process.state == "Processing")
      this.IsProcesssing = true;
    else
      this.IsProcesssing = false;

    this.showprocessDiv = true;
    this.processForExecution = process.integrationProcessName;
  }

  setProcessStates(integrationProcesses) {
    var ScheduleState;
    integrationProcesses.forEach(process => {

      process.activities.forEach(activity => {
        if (activity.activityType == "INTEGRATION") {
          if (activity.triggerType == "SCHEDULED") {
            var cron = JSON.parse(activity.scheduleSetup)
            if (cron.cronExpression == "0 0 0 0 0/0 ? *")
              ScheduleState = "Schedule_undefined"
            else
              ScheduleState = processState.Scheduled
          }
          else
            if (activity.triggerType == "MANUAL")
              ScheduleState = processState.Manual
        }
      });
      if (ScheduleState == processState.Scheduled)
        process.state = "Scheduled";
      else
        if (ScheduleState == processState.Manual)
          process.state = "Ready to Execute";
        else
          if (ScheduleState == "Schedule_undefined")
            process.state = "Schedule Not Defined";
          else
            process.state = "Not Available"

      this.executionbService.getCurrentIntegrationProcessExecutionByIntegrationProcessID(ENDPOINTS, process.integrationProcessID)
        .subscribe(Response => {
          if (Response != null) {
            var currentState = Response.status
            if (currentState == "PROCESSING")
              process.state = "Processing"
            else
              if (ScheduleState == processState.Scheduled && currentState != "PROCESSING")
                process.state = "Scheduled";
          }
        })
      ScheduleState = "";
    });
  }
}
