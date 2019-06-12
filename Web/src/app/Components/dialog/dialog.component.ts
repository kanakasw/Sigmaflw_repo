import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { UserServiceService } from 'src/app/Services/user-service.service';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { NotificationService } from 'src/app/Services/common/notification.service';
@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.css']
})
export class DialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<DialogComponent>,
    @Inject(MAT_DIALOG_DATA) public DialogData: any,
    private userService: UserServiceService,
    private router: Router,
    private toastr: NotificationService

  ) { }

  ngOnInit() {

  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onDelete() {
    this.userService.deleteUser(this.DialogData.userId)
      .subscribe(Response => {
        this.toastr.showSuccess("User deactivated successfully")
        this.onNoClick();
      },
      error => {
        this.toastr.showerror("server error")
      })
  }
}
