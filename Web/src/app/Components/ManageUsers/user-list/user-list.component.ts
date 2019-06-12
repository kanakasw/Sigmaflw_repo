import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserServiceService } from 'src/app/Services/user-service.service';
import { MatDialog } from '@angular/material';
import { NotificationService } from 'src/app/Services/common/notification.service';
import { DialogComponent } from '../../dialog/dialog.component';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  dataSource: any;
  selectedUser: any;
  showUserAction: any;

  constructor(private router: Router,
    private userSerivice: UserServiceService,
    public dialog: MatDialog,
    private toastr: NotificationService) { }

  ngOnInit() {
    this.userSerivice.userlist()
      .subscribe(Response => {
        this.dataSource = Response;
      })

  }
  selectUser(user) {
    this.showUserAction = true;
    this.selectedUser = user;
  }

  modifyUserRequest() {
    this.router.navigate(['updateUser'], {
      queryParams: {
        userId: this.selectedUser.userId
      }
    })
  }
  openDialog(): void {
    if (this.selectedUser.role == 'ADMIN') {
      this.toastr.showerror("Unauthorized, Admin user cannot be deactivated !!")
      return;
    }
    var data = {
      userId: this.selectedUser.userId,
      deleteUser: true
    }
    const dialogRef = this.dialog.open(DialogComponent, {
      data: data,

    });
    dialogRef.afterClosed().subscribe(result => {
      this.userSerivice.userlist()
        .subscribe(Response => {
          this.dataSource = Response;
        })
    });
  }
}
