import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ENDPOINTS } from '../index.constants';

@Injectable({
  providedIn: 'root'
})
export class ProcessService { 

  constructor(private http: HttpClient) { }

  createProcess(processData: any): Observable<any> {
    console.log(processData)
    return this.http.post(ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.CreateProcess, processData)
  }

  updateProcess(data: any): Observable<any> {
    return this.http.post<any>(ENDPOINTS.SERVICE_ADDRESS, data);
  }
}
