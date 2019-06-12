import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';
import { ENDPOINTS } from '../index.constants';
import { ApplicationStorageService } from './common/application-storage.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  tokenInfo: any;
  currentUserSubject: BehaviorSubject<any>;
  public currentuser: Observable<any>;


  constructor(private http: HttpClient,
    private applicationStorageService: ApplicationStorageService) {
    this.currentUserSubject = new BehaviorSubject<any>(sessionStorage.getItem('currentuser'));
    this.currentuser = this.currentUserSubject.asObservable();
  }


  login(username: string, password: string): Observable<any> {
    var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.AUTH_ENDPOINT;
    var requestParameter = "grant_type=password&scope=read+write";
    url = url + '?' + requestParameter;
    let body = new FormData();
    body.append('username', username);
    body.append('password', password);
    return this.http.post<any>(url, body)
      .pipe(map(data => {
        this.tokenInfo = data;
        sessionStorage.setItem('currentuser', username)
        this.applicationStorageService.setTokenInfo(data);
        this.applicationStorageService.setUsername(username);
        this.currentUserSubject.next(username);

      }))
  }
 
  public get currentUserValue(): any {
    return this.currentUserSubject.value;
  }

}
