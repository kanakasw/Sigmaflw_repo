import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { UserServiceService } from 'src/app/Services/user-service.service';
import { AbstractControl } from '@angular/forms';
import { MatDialog } from '@angular/material';
import { NotificationService } from 'src/app/Services/common/notification.service';
import { values } from '@uirouter/core';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit {
  registerForm: FormGroup;
  loading = false;
  submitted = false;
  userIDForModification: any;
  p: number = 1;

  constructor(private formBuilder: FormBuilder,
    private router: Router,
    private userSerivice: UserServiceService,
    private route: ActivatedRoute,
    public dialog: MatDialog,
    private toastr: NotificationService
  ) {
  }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      login: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      status: ['', Validators.required],
      userPassword: ['', [Validators.required, Validators.minLength(6)]],
      cnfPassword: ['', Validators.required],
      role: ['', Validators.required]
    }, {
        validators: this.MatchPassword
      },
    );
  }

  get f() {
    return this.registerForm.controls;
  }

  onSubmit() {
    this.submitted = true
    if (this.registerForm.invalid)
      return;

    this.loading = true;
    this.userSerivice.register(this.registerForm.value)
      .subscribe(
        data => {
          this.toastr.showSuccess("User created successfully !!")
          this.router.navigate(['userList'])
          this.loading = false;
        },
        error => {
          if (error.error.status == 409)
            this.toastr.showerror("username already exists !!")
          this.loading = false;
        });
  }

  MatchPassword(val: AbstractControl) {
    let password = val.get('userPassword').value; // to get value in input tag
    let confirmPassword = val.get('cnfPassword').value;
    if (password != confirmPassword) {
      val.get('cnfPassword').setErrors({ MatchPassword: true })
    } else {
      return null;
    }
  }

  Cancel() {
    this.router.navigate(['userList'])
  }

}
