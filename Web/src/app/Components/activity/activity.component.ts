import { Component, OnInit, Input, SimpleChange } from '@angular/core';
import { SubscriberService } from 'src/app/Services/subscriber.service';
import { ENDPOINTS } from 'src/app/index.constants';
import { ResponseHandlerService } from 'src/app/Services/common/response-handler.service';
import { ExecutionService } from 'src/app/Services/execution.service';
import { ApplicationStorageService } from 'src/app/Services/common/application-storage.service';
import { ToastrService } from "ngx-toastr";
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { CronOptions } from "cron-editor/cron-editor";
import { val } from '@uirouter/core';
import { NotificationService } from 'src/app/Services/common/notification.service';

@Component({
  selector: 'app-activity',
  templateUrl: './activity.component.html',
  styleUrls: ['./activity.component.css']
})
export class ActivityComponent implements OnInit {

  objectKeys = Object.keys;
  cron = {};
  defualtCronValue = '0 0 0 0 0/0 ? *';
  isCronDisabled = false;
  cronOptions: CronOptions = {

    formInputClass: 'form-control cron-editor-input',
    formSelectClass: 'form-control cron-editor-select',
    formRadioClass: 'cron-editor-radio',
    formCheckboxClass: 'cron-editor-checkbox',
    defaultTime: "00:00:00",
    hideAdvancedTab: false,
    use24HourTime: true,
    hideMinutesTab: false,
    hideHourlyTab: false,
    hideDailyTab: false,
    hideWeeklyTab: false,
    hideMonthlyTab: false,
    hideYearlyTab: false,
    hideSeconds: false,
    removeSeconds: false,
    removeYears: false

  };
  noOfProcess = 0;
  size = 0;
  //console.log($scope.state.activeTab);
  causesNewProcessExecutionValue = false;
  formdata: any;
  NavigationMenu: any;
  activity: any;
  cronExpression: any;
  hasProcessingSpecification = false;
  updatedInputParameter = {};

  triggerTypeList = ["MANUAL", "SCHEDULED"];
  workflowTypeList = ["Transformation", "Job"];
  selectedWorkFlowType = "Transformation";
  selectedTriggerType = "";
  dirty = {};
  submitProcessingSpec = false;
  processingspec: any;
  currentCron = '';
  currentState: any;

  submitInputParameter = false;
  subscriberId: any;
  states = [];
  errorInsubmitProcessingSpec: any;
  activityID: any;
  responseData: any;
  integrationProcess: any;
  newInputParameter: any;
  inputparameter: any;
  autocomplete_options: any;
  ac_option_delimited: any;
  tabname: any;
  currentProcessID: any;
  /*  integrationProcessID:any; */
  constructor(private subscriberService: SubscriberService,
    private responseHandler: ResponseHandlerService,
    private executionService: ExecutionService,
    private applicationStorage: ApplicationStorageService,
    private toastr: NotificationService,
    private router: Router,
    private route: ActivatedRoute) {
  }

  @Input() activityId: any;

  // on Input change reload component...
  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    this.ngOnInit();
  }

  ngOnInit() {
    this.NavigationMenu = JSON.parse(localStorage.getItem('navigationMenu'));
    this.noOfProcess = this.NavigationMenu.integrationProcesses.length;
    this.subscriberId = this.NavigationMenu.userId;
    this.currentState = this.router.url + '/activity/' + this.activityId;
    for (var j = 0; j < this.noOfProcess; j++) {
      this.size = Object.keys(this.NavigationMenu.integrationProcesses[j].activities).length;
      for (var i = 0; i < this.size; i++) {
        var integrationProcessID = this.NavigationMenu.integrationProcesses[j].integrationProcessID;
        this.currentProcessID = integrationProcessID;
        var activityIDvar = this.NavigationMenu.integrationProcesses[j].activities[i].activityID;
        var stateName = this.router.url + '/activity/' + this.getStateName(integrationProcessID) + "." + this.getStateName(activityIDvar);
        if (stateName == this.currentState) {
          this.activityID = this.NavigationMenu.integrationProcesses[j].activities[i].activityID;
          this.activity = this.NavigationMenu.integrationProcesses[j].activities[i];
          this.tabname = this.NavigationMenu.integrationProcesses[j].activities[i].activityName;
          var str = this.NavigationMenu.integrationProcesses[j].activities[i].processingSpecification;
          this.processingspec = JSON.parse(str);
          if (this.processingspec.hasOwnProperty('ActivityExecutionID')) {
            this.hasProcessingSpecification = true
          }
          this.inputparameter = this.processingspec.inputParameters;
          this.integrationProcess = this.NavigationMenu.integrationProcesses[j];
          this.getInputParameter(this.activityID);
          this.selectedTriggerType = this.activity.triggerType
          if (this.selectedTriggerType == 'SCHEDULED')
            this.getIntialValue(this.activity['scheduleSetup'])
        }
      }
    }
  }

  updateProcessingSpecification() {
    this.states.forEach(item => {
      console.log(this.processingspec)
      if (!(this.states.indexOf(this.processingspec["ActivityExecutionID"]) > -1)) {
        this.processingspec["ActivityExecutionID"] = "";
        this.errorInsubmitProcessingSpec = false;
        this.submitProcessingSpec = true;
      }
      else {
        this.errorInsubmitProcessingSpec = true;
      }
    });
    if (this.errorInsubmitProcessingSpec) {
      var bodyJSON = JSON.stringify(this.processingspec);
      this.subscriberService.updateProcessingSpecification(ENDPOINTS, this.activityID, bodyJSON)
        .subscribe(Response => {
          var responseData = Response;
          console.log("Processing Specification updated successfully : " + responseData);
          this.toastr.showSuccess("Processing Specification updated successfully");
          //to update navigation menu self.NavigationMenu.integrationProcesses[i].activities[j].processingSpecification
          console.log("Current process: ", this.integrationProcess);
          var processID = this.integrationProcess.integrationProcessID;
          this.noOfProcess = Object.keys(this.NavigationMenu.integrationProcesses).length;
          for (var j = 0; j < this.noOfProcess; j++) {
            this.size = Object.keys(this.NavigationMenu.integrationProcesses[j].activities).length;
            for (var i = 0; i < this.size; i++) {
              if (this.NavigationMenu.integrationProcesses[j].activities[i].activityID == this.activityID) {
                this.NavigationMenu.integrationProcesses[j].activities[i].processingSpecification = this.processingspec;
              }
            }
          }
        });
    }
  }

  /**
  * call to reschedule process
  * Requires Crone expression
  *
  */
  reschedule(activityID) {
    var bodyJSON = { "cronExpression": this.cronExpression };
    this.subscriberService.updateActivityByActivityID(ENDPOINTS, activityID, bodyJSON)
      .subscribe(Response => {
        var responseData = Response;
        this.currentCron = this.cronExpression;
        this.toastr.showSuccess("Activity successfully updated");
      })
  }

  /**
   * IF activty in error state then User can retry activity execution.
   * Requires ActivityExecutionID
   *
   */
  retryActivityExecution(activityExecutionID) {
    this.executionService.retryActivityExecutionByActivityExecutionID(ENDPOINTS, activityExecutionID)
      .subscribe(Response => {
        this.responseData = Response;
        this.toastr.showSuccess("Activity successfully updated");
      })
  }

  /* updateSubscriberDetails() {
    var username = this.applicationStorage.getUsername();
    var promise = this.subscriberService.getSubscriberDetails(ENDPOINTS, username)
      .subscribe(Response => {
        if (this.responseHandler.getResponseMessageByStatus(Response)) {
          this.responseData = Response
          console.log(this.responseData);
          this.applicationStorage.setValue("NavigationMenu", this.responseData);
        }
      })
  } */

  getStateName(stateID) {
    return stateID;
  }

  initTriggerType(value) {
    this.selectedTriggerType = value;
  }

  changeTriggerType(selectedTriggerType) {
    this.selectedTriggerType = selectedTriggerType;
  }

  changeWorkFlowType(selectedWorkFlowType) {
    this.selectedWorkFlowType = selectedWorkFlowType; 
  }

  initWorkFlowType(value) {
    this.selectedWorkFlowType = value;
  }

  getCron(value) {
    if (value == null) return null;
    return JSON.parse(value).cronExpression;
  }

  getIntialValue(value) {
    var jsonvalue = JSON.parse(value);
    if (jsonvalue) {
      this.cronExpression = this.defualtCronValue;
      this.currentCron = jsonvalue['cronExpression'];
    } else {
      //if empty
      this.cronExpression = this.defualtCronValue;
    }
  }

  /*function iscausesNewProcessExecution(value) {
      console.log("causesNewProcessExecutionValue :: " + value);
      self.causesNewProcessExecutionValue = value;
      }
      */

  setActiveTabIndex(selectedTab) {
    return 0;
  }

  //function to get input parameter
  getInputParameter(activityID) {
    if (this.activity.activityType == "INTEGRATION") {
      this.subscriberService.getInputParameter(ENDPOINTS, activityID)
        .subscribe(Response => {
          if (Response) {
            this.newInputParameter = Response.InputParamList;
            this.newInputParameter.forEach(item => {
              if (!this.inputparameter.hasOwnProperty(this.newInputParameter[item].ParameterName))
                this.inputparameter[this.newInputParameter[item].ParameterName] = "";
            });
          }
        },
          error => {
            console.log(error)
          })
      this.subscriberService.getUniqueKeywords(ENDPOINTS, this.subscriberId)
        .subscribe(Response => {
          this.states = Response;
          this.loadFuzzySearch();
        })
    }
  }


  updateInputParameter(activityID, inputparameter) {
    this.inputparameter.forEach(element => {
      if (!(this.states.indexOf(element) > -1)) {
        this.inputparameter[element] = "";
        return false;
      }
    })
    var bodyJSON = JSON.stringify(inputparameter);
    this.subscriberService.updateInputParameter(ENDPOINTS, activityID, bodyJSON)
      .subscribe(Response => {
        if (this.responseHandler.getResponseMessageByStatus(Response)) {
          this.responseData = Response;
          console.log("Input parameters updated successfully : " + this.responseData);
          this.toastr.showSuccess("Input parameters updated successfully");
        }
      })
  }


  loadFuzzySearch() {
    function suggest_state(term) {
      var q = term.toLowerCase().trim();
      var results = [];
      // Find first 10 states that start with `term`.
      for (var i = 0; i < this.states.length && results.length < 10; i++) {
        var state = this.states[i];
        if (state.toLowerCase().slice(2).indexOf(q) === 0)
          results.push({ label: state, value: state });
      }

      return results;
    }

    this.autocomplete_options = {
      suggest: suggest_state
    };

    function suggest_state_delimited(term) {
      var ix = term.lastIndexOf('\\'),
        lhs = term.substring(0, ix + 1),
        rhs = term.substring(ix + 1),
        suggestionsListForSlash = suggest_state(rhs);

      var ixs = term.lastIndexOf('_'),
        lhs = term.substring(0, ixs + 1),
        rhs = term.substring(ixs + 1),
        suggestionsListForUnderscore = suggest_state(rhs);

      suggestionsListForSlash.forEach(function (s) {
        s.value = lhs + s.value;
      });

      suggestionsListForUnderscore.forEach(function (s) {
        s.value = lhs + s.value;
      });

      if (suggestionsListForSlash.length == 0)
        return suggestionsListForUnderscore;
      else
        return suggestionsListForSlash;
    };

    this.ac_option_delimited = {
      suggest: suggest_state_delimited
    };
  }

}
