import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HttpHandlerService {

  constructor(private http: HttpClient) { }

  _buildUrl(url) {
    return url;
  }

  OAuthToken(url, requestParam, bodyData): Observable<any> {
    /* var request = {
      method: "POST",
      url: this._buildUrl(url+"?" + requestParam),
      data : bodyData
    };
     */
    url = this._buildUrl(url + "?" + requestParam)
    return this.http.post<any>(url, bodyData);
    /*  return _execute(request); */
  }

  OAuthRefreshToken(url, bodyData): Observable<any> {
    /*  var request = {
       method: "POST",
       url: this._buildUrl(url),
       data : bodyData
     }; */
    url = this._buildUrl(url)
    return this.http.post<any>(url, bodyData);
    /*   return _execute(request); */
  }

}
