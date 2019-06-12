import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './Components/login/login.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ActivityComponent } from './Components/activity/activity.component';
import { CurrentExecutionComponent } from './Components/current-execution/current-execution.component';
import { ExecutionSummaryComponent } from './Components/execution-summary/execution-summary.component';
import { ProcessComponent } from './Components/process/process.component';
import { SubscriberComponent } from './Components/subscriber/subscriber.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule, MatListModule, MatInputModule, MatSelectModule, MatCardModule, MatTableModule, MatPaginatorModule, MatMenuModule, MatDialogModule, MatTreeModule, MatToolbarModule, MatIconModule, MatTabsModule, MatTooltipModule, MatSlideToggleModule } from '@angular/material';
import { UIRouterModule } from "@uirouter/angular";
import { JwtInterceptor } from './Services/common/jwtInterceptor';
import { ToastrModule } from 'ngx-toastr';
import { ForgotPasswordComponent } from './Components/forgot-password/forgot-password.component';
import { ExecutionStepOutcomePopupDialogComponent } from './Components/execution-step-outcome-popup-dialog/execution-step-outcome-popup-dialog.component';
import { CronEditorModule } from "cron-editor";
import { CreateUserComponent } from './Components/ManageUsers/create-user/create-user.component';
import { OrderModule } from 'ngx-order-pipe';
import { convertCaseFilter } from './Services/common/ConvertCaseFIlter';
import { NgxPaginationModule } from 'ngx-pagination';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { DialogComponent } from './Components/dialog/dialog.component';
import { ProcessManagementComponent } from './Components/process-management/process-management.component';
import { UserListComponent } from './Components/ManageUsers/user-list/user-list.component';
import { UpdateUserComponent } from './Components/ManageUsers/update-user/update-user.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ActivityComponent,
    CurrentExecutionComponent,
    ExecutionSummaryComponent,
    ProcessComponent,
    SubscriberComponent,
    ForgotPasswordComponent,
    ExecutionStepOutcomePopupDialogComponent,
    CreateUserComponent,
    convertCaseFilter,
    DialogComponent,
    ProcessManagementComponent,
    UserListComponent,
    UpdateUserComponent
  ],
  entryComponents: [ExecutionStepOutcomePopupDialogComponent, DialogComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatButtonModule,
    MatMenuModule,
    MatCardModule,
    MatToolbarModule,
    MatIconModule,
    MatTabsModule,
    MatTooltipModule,
    MatSlideToggleModule,
    UIRouterModule,
    ToastrModule.forRoot(),
    MatTreeModule,
    BrowserAnimationsModule,
    CronEditorModule,
    MatDialogModule,
    MatTableModule,
    MatPaginatorModule,
    OrderModule,
    MatListModule,
    MatSelectModule,
    MatInputModule,
    NgxPaginationModule

  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
  { provide: LocationStrategy, useClass: HashLocationStrategy }],

  bootstrap: [AppComponent]
})
export class AppModule { } 
