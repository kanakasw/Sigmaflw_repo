import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-execution-step-outcome-popup-dialog',
  templateUrl: './execution-step-outcome-popup-dialog.component.html',
  styleUrls: ['./execution-step-outcome-popup-dialog.component.css']
})
export class ExecutionStepOutcomePopupDialogComponent implements OnInit {

  objectKeys = Object.keys;

  constructor(public dialogRef: MatDialogRef<ExecutionStepOutcomePopupDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public executionStepOutcome: any
  ) { }

  ngOnInit() {

  }
  closeDialog() {
    this.dialogRef.close();
  }
}
