import { Component, OnInit } from '@angular/core';
import { ForgotPasswordService } from 'src/app/Services/forgot-password.service';
import { ENDPOINTS } from 'src/app/index.constants';
import { timeout } from 'q';
import { Router } from '@angular/router';
import { any } from '@uirouter/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {
  username: FormGroup;
  emailIdSuccessMsg = false;
  emailIdErrorMsg = false;
  emailId = {
    email: any
  }
  submitted: boolean;
  constructor(private forgotPasswordServcie: ForgotPasswordService,
    private router: Router,
    private formBuilder: FormBuilder, ) { }

  ngOnInit() {
    this.username = this.formBuilder.group({
      emailid: ['', Validators.required]
    });
  }

  
  CheckValidMailId() {
    this.submitted = true;
    if (this.username.invalid) {
      return;
    }

    //$scope.showDialogOfProcessing();
    this.emailId.email = this.f.emailid.value;
    var len = this.emailId.email.length;
    if (this.emailId.email != undefined) {
      console.log(this.emailId);
      this.forgotPasswordServcie.sendUserEmailId(this.emailId, ENDPOINTS)
        .subscribe(Response => {
            console.log(Response.data.link);
            this.emailIdSuccessMsg = true;
            this.emailIdErrorMsg = false;
            setTimeout(() => {
              this.redirectToLoginPage();
            }, 5000);       
        })
    }
    else {
      this.emailIdErrorMsg = false;
    }
  }

  get f() {
    return this.username.controls;
  }

  hideErrorMsg() {
    this.emailIdErrorMsg = false;
  }

  redirectToLoginPage() {
    /*   $state.go('loginState'); */
    this.router.navigate(['login'])
  }

}
