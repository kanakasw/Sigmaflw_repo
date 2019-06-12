import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpEvent, HttpHandler } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginService } from '../login.service';
import { ApplicationStorageService } from './application-storage.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private loginService: LoginService,
    private applicationStorageService: ApplicationStorageService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    var tokenInfo = this.applicationStorageService.getTokenInfo();
    if (tokenInfo && tokenInfo.access_token) {
      console.log('bearer')
      request = request.clone({
        headers: request.headers.set('Authorization', 'Bearer ' + tokenInfo.access_token)
      });
    }
    if (tokenInfo == null) {
      console.log('basic')
      request = request.clone({
        headers: request.headers.set('Authorization', 'Basic MTcyZDQyNjAtNDgwMi0xMWU2LWJlYjgtOWU3MTEyOGNhZTc3OmU0ZmVlYjY4LTQ4MDEtMTFlNi1iZWI4LTllNzExMjhjYWU3Nw==')
      });
    }
    else {
      request = request.clone({
        headers: request.headers
      });
    }
    return next.handle(request);
  }
}

