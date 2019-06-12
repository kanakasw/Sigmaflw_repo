import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SubscriberComponent } from './Components/subscriber/subscriber.component';
import { LoginComponent } from './Components/login/login.component';
import { ForgotPasswordComponent } from './Components/forgot-password/forgot-password.component';
import { ProcessComponent } from './Components/process/process.component';
import { AppComponent } from './app.component';
import { ActivityComponent } from './Components/activity/activity.component';
import { CreateUserComponent } from './Components/ManageUsers/create-user/create-user.component';
import { AuthGuard } from './Services/common/AuthGuard';
import { ProcessManagementComponent } from './Components/process-management/process-management.component';
import { AdminAuthGuard } from './Services/common/AdminAuthGuard';
import { UpdateUserComponent } from './Components/ManageUsers/update-user/update-user.component';
import { UserListComponent } from './Components/ManageUsers/user-list/user-list.component';

const routes: Routes = [
  { path: 'home', component: SubscriberComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'forgotPassword', component: ForgotPasswordComponent },
  { path: 'register', component: CreateUserComponent, canActivate: [AuthGuard, AdminAuthGuard] },
  { path: 'processManagement/:input', component: ProcessManagementComponent, canActivate: [AuthGuard], },
  { path: 'process/:ProcessId', component: ProcessComponent, canActivate: [AuthGuard] },
  { path: 'userList', component: UserListComponent, canActivate: [AuthGuard, AdminAuthGuard] },
  { path: 'updateUser', component: UpdateUserComponent, canActivate: [AuthGuard, AdminAuthGuard] }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }
