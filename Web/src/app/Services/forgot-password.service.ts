import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ForgotPasswordService {

  constructor(private http : HttpClient) { }
 sendUserEmailId(data, ENDPOINTS) : Observable<any>{
	  
    var requestParam = data;
    var headersValues = {
      'Content-Type': 'application/json'
    };
    return this.http.post<any>(ENDPOINTS.FORGET_PPASSWORD_ENDPOINT, headersValues, requestParam);
  }
 
 checkResetPasswordUrl(data, ENDPOINTS){
	  
    var requestParam = data;
    var headersValues = {
      'Authorization': 'bearer ',
      'Content-Type': 'application/json'
    };
    return this.http.post<any>(ENDPOINTS.FORGET_PPASSWORD_ENDPOINT, headersValues, requestParam);

  }


}
