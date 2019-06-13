import { Component, OnInit } from '@angular/core';
import { Route } from '@angular/compiler/src/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CronOptions } from "cron-editor/cron-editor";
import { ProcessService } from 'src/app/Services/process.service';
import { UserServiceService } from 'src/app/Services/user-service.service';
import { processState } from 'src/app/Models/processStates';
import { filePath } from 'src/app/Models/ProcessFilePaths';
import { process } from 'src/app/Models/Process';
import { NotificationService } from 'src/app/Services/common/notification.service';
@Component({
  selector: 'app-process-management',
  templateUrl: './process-management.component.html',
  styleUrls: ['./process-management.component.css']
})
export class ProcessManagementComponent implements OnInit {
  showdiv: any;
  processForm: FormGroup;
  loading = false;
  submitted = false;
  processmodifying: any;
  selectedTriggerType: 'SCHEDULED'
  cron = {};
  cronExpression: any;
  defualtCronValue = '0 0 0 0 0/0 ? *';
  isCronDisabled = false;
  currentCron: any;
  integrationProcessEnabled: boolean;
  users = [];
  selectedIndex: number = 0;

  selectedFiles = {
    inputFile: '',
    outputFile: '',
    ktrFile: ''
  }
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

  constructor(private router: Router,
    private route: ActivatedRoute,
    private formbuilder: FormBuilder,
    private processService: ProcessService,
    private userService: UserServiceService,
    private notificationService : NotificationService) {

    this.route.params.subscribe(params => {
      this.showdiv = params['input']
      this.ngOnInit();
    })
  }

  ngOnInit() {
    if (this.showdiv == 'createProcess') {
      this.processmodifying = false;
      this.integrationProcessEnabled = false;
      this.selectedTriggerType = 'SCHEDULED'
      this.userService.userlist()
        .subscribe(Response => {
          var userdata = [];
          userdata = Response
          this.users = Response
          this.users = userdata.filter(user =>
            user.status == "ACTIVE"
          )
        })


      this.processForm = this.formbuilder.group({
        proecessName: ['', Validators.required],
        processOwner: ['', Validators.required],
        activityType: ['', Validators.required],
        /*   inputFile: ['', Validators.required],
          outputFile: ['', Validators.required],
          ktrFile: ['', Validators.required] */
      })

      if (this.selectedTriggerType == 'SCHEDULED')
        this.cronExpression = this.defualtCronValue;
    }

    if (this.showdiv == 'modifyProcess') {
      this.processmodifying = true;
      this.processForm = this.formbuilder.group({
        proecessName: ['', Validators.required],
        processOwner: ['', Validators.required],
        status: ['', Validators.required]
      })
    }
  }

  get f() {
    return this.processForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.processForm.invalid)
      return;

    this.loading = true;

    var processSpecs = {
      ActivityExecutionID: "${ActivityExecutionID}",
      workflowFilePath: filePath.workflowFilePath + this.processForm.value.proecessName + ".ktr",
      StepToTrigger: 2,
      workflowType: "Transformation",
      inputParameters: {
        siteFilePath: filePath.siteFilePath + this.processForm.value.proecessName + '_input' + '.csv' /* this.selectedFiles.inputFile */,
        wastageFilePath: filePath.siteFilePath + this.processForm.value.proecessName + '_input' + '.csv'/*  this.selectedFiles.inputFile */
      },
      outputParameters: []
    }
    var cronExp = {
      cronExpression: this.cronExpression,
    }

    var processdata = new process();
    processdata.activities = [{
      activityID: null,
      activitykey: null,
      activityName: this.processForm.value.proecessName,
      activityOrderIndex: 1,
      activityType: this.processForm.value.activityType,
      causesNewProcessExecution: true,
      createdDate: Date.now(),
      eventGroupName: null,
      eventGroupOrderIndex: 1,
      modifiedDate: Date.now(),
      processingSpecification: JSON.stringify(processSpecs),
      scheduleSetup: JSON.stringify(cronExp),
      triggerType: this.selectedTriggerType
    }, {
      activityID: null,
      activitykey: null,
      activityName: "End execution",
      activityOrderIndex: 2,
      activityType: "END",
      causesNewProcessExecution: true,
      createdDate: Date.now(),
      eventGroupName: "END_EXECUTION",
      eventGroupOrderIndex: 1,
      modifiedDate: Date.now(),
      processingSpecification: "{}",
      scheduleSetup: null,
      triggerType: "MANUAL"
    }];


    processdata.createdDate = Date.now(),
      processdata.enabled = this.integrationProcessEnabled, // process status
      processdata.fileEncryptionKey = null,
      processdata.integrationProcessExecutions = [],
      processdata.integrationProcessName = this.processForm.value.proecessName,
      processdata.integrationProcessUniqueReference = 'sfklsdgjg-vdgb-fdgh',
      processdata.modifiedDate = Date.now(),
      processdata.subscriberID = 1,
      processdata.userId = this.processForm.value.processOwner
      
    this.loading = false;
    this.processService.createProcess(processdata)
      .subscribe(Response => {
        console.log("Process created successfully !"),
          this.loading = false
        this.router.navigate(['home'])
      }
        , error => {
          this.loading = false;
        })
  }

  updateProcess() {
  }

  Cancel() {
  this.notificationService.setActiveTab('dashboard');
    this.router.navigate(['home'])
  }

  changeTriggerType(selectedTriggerType) {
    this.selectedTriggerType = selectedTriggerType
  }

  getIntialValue(value) {
    var jsonvalue = JSON.parse(value);
    if (jsonvalue) {
      this.cronExpression = this.defualtCronValue;
      this.currentCron = jsonvalue['cronExpression'];
    } else {
      this.cronExpression = this.defualtCronValue;
    }
  }


  tabClick(tab) {
    if (tab.index == 0)
      this.selectedIndex = 0;
    if (tab.index == 1)
      this.selectedIndex = 1;
  }
  getInputFileName(fileInput, filetype) {
    if (filetype == 'inputFile')
      this.selectedFiles.inputFile = fileInput.target.files[0].name;

    if (filetype = 'outputFile')
      this.selectedFiles.outputFile = fileInput.target.files[0].name;

    if (filetype = 'ktrFile')
      this.selectedFiles.ktrFile = fileInput.target.files[0].name;
    console.log(this.selectedFiles)
  }
}
