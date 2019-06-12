import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { UserServiceService } from 'src/app/Services/user-service.service';
import { MatDialog } from '@angular/material';
import { NotificationService } from 'src/app/Services/common/notification.service';

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css']
})
export class UpdateUserComponent implements OnInit {
  userIDForModification: any;
  updateForm: FormGroup;
  loading = false;
  submitted = false;
  userDetails: any;
  constructor(private formBuilder: FormBuilder,
    private router: Router,
    private userSerivice: UserServiceService,
    private route: ActivatedRoute,
    public dialog: MatDialog,
    private toastr: NotificationService) { }

  ngOnInit() {
    this.updateForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      role: ['', Validators.required],
      status: ['', Validators.required],
      login: ['', Validators.required]
    })
    this.route.queryParams.subscribe(params => {
      this.userIDForModification = params['userId']
    })
    this.userSerivice.userDetails(this.userIDForModification)
      .subscribe(Response => {
        this.userDetails = Response;
        this.updateForm = this.formBuilder.group({
          firstName: [this.userDetails.firstName, Validators.required],
          lastName: [this.userDetails.lastName, Validators.required],
          email: [this.userDetails.email, Validators.required],
          role: [this.userDetails.role, Validators.required],
          status: [this.userDetails.status, Validators.required],
          login: [this.userDetails.login, Validators.required]
        })
      })
  }
  get f() {
    return this.updateForm.controls;
  }
  Cancel() {
    this.router.navigate(['userList'])
  }

  updateUser() {
    this.submitted = true;
    if (this.updateForm.invalid)
      return;
 
    this.loading = true;
    this.userSerivice.updateUser(this.userIDForModification, this.updateForm.value)
      .subscribe(Response => {
        this.toastr.showSuccess("user updated successfully !!")
        this.router.navigate(['userList'])
      },
        error => {
          this.loading = false;
          if (error.error.status == 500)
            this.toastr.showerror("Username already exists !!")
        }
      )
  }
}
