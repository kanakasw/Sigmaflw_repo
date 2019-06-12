import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ENDPOINTS } from '../index.constants';
import { UtilityService } from './common/utility.service';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {
  currentUserRoleSubject: BehaviorSubject<any>;
  public currentUserRole: Observable<any>;


  constructor(private http: HttpClient,
    private utilityService: UtilityService
  ) {
    this.currentUserRoleSubject = new BehaviorSubject<any>(sessionStorage.getItem('role'));
    this.currentUserRole = this.currentUserRoleSubject.asObservable();
   }

  register(data: any): Observable<any> {
    data.createdDate = Date.now();
    data.modifiedDate = Date.now();
    return this.http.post<any>(ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.CreateUser, data);
  }

  userlist(): Observable<any> {
    return this.http.get<any>(ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.UserList);
  }

  userDetails(id: any): Observable<any> {
    var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.UserDetails
    url = this.utilityService.replaceTagWithValue(url, 'id', id);
    return this.http.get<any>(url);
  }

  updateUser(id: any, data: any): Observable<any> {
    var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.UpdateUser;
    url = this.utilityService.replaceTagWithValue(url, 'id', id);
    return this.http.put<any>(url, data);
  }

  /**
   *  Description : Deactivete user
   *  Method      : PUT 
   *  @param id 
   *  name        : deleteUser
   */
  deleteUser(id: any): Observable<any> {
    var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.DeleteUser;
    url = this.utilityService.replaceTagWithValue(url, 'id', id);
    return this.http.put<any>(url, null);
  }
 
  getLoggedInUserDetails(username: any): Observable<any> {
    var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.getUserByLogin;
    url = this.utilityService.replaceTagWithValue(url, 'name', username);
    return this.http.get<any>(url)
      .pipe(map(data => {
        localStorage.setItem('navigationMenu', JSON.stringify(data));
        sessionStorage.setItem("role", data.role);
        this.currentUserRoleSubject.next(data.role);
      })
      )
  }
  public get currentUserRoleValue(): any {
    return this.currentUserRoleSubject.value;
  }
}
