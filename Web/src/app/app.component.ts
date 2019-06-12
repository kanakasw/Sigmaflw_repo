
import { Component, OnInit, Input } from '@angular/core';
import { ApplicationStorageService } from 'src/app/Services/common/application-storage.service';
import { SubscriberService } from 'src/app/Services/subscriber.service';
import { ResponseHandlerService } from 'src/app/Services/common/response-handler.service';
import { ENDPOINTS } from 'src/app/index.constants';
import { Router, Route } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoginService } from './Services/login.service';
import { UserServiceService } from './Services/user-service.service';
import { NotificationService } from './Services/common/notification.service';
import { val } from '@uirouter/core';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'ksCloud';
  processHeader: any;
  currentuser: any;
  currentUserRole: string;
  activeTab: string;
  constructor(private applicationStorage: ApplicationStorageService,
    private subscriberSerice: SubscriberService,
    private router: Router,
    private toastr: NotificationService,
    private loginService: LoginService,
    private userService: UserServiceService
  ) {
    this.processHeader = this.applicationStorage.processHeader;
  }
  ngOnInit() {
    this.activeTab = sessionStorage.getItem('activeTab') == null ? 'dashboard' : sessionStorage.getItem('activeTab');
    this.loginService.currentuser.subscribe(x => {
      this.currentuser = x;
    })
    this.userService.currentUserRole.subscribe(x => {
      this.currentUserRole = x;
    })
  }

  logout() {
    this.activeTab = 'dashboard';
    var accessToken = this.applicationStorage.getAccessToken();
    this.toastr.showSuccess("Successfully Logged out...");
    this.loginService.currentUserSubject.next(null);
    this.userService.currentUserRoleSubject.next(null);
    this.applicationStorage.clear();
    this.subscriberSerice.logout(ENDPOINTS, accessToken)
      .subscribe(Response => {
        this.router.navigate(['login'])
      },
        error => {
          this.router.navigate(['login'])
        }
      )
  }

  setUrl(value) {
    var route = "";
    if (value == 'createUser')
      this.router.navigate(['register'])

    if (value == 'userList')
      this.router.navigate(['userList'])

    if (value == 'createProcess' || value == 'modifyProcess') {
      route = 'processManagement'
      this.router.navigate([route, value])
    }
  }

  setActiveTab(tab: string) {
    this.activeTab = tab;
    sessionStorage.setItem('activeTab', tab);
  }
}
