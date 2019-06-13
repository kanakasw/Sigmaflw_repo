import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Observable, BehaviorSubject } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  currentactiveTabSubject: BehaviorSubject<any>;
  public activeTab: Observable<any>;

  constructor(private toastr: ToastrService) {
    this.currentactiveTabSubject = new BehaviorSubject<any>(sessionStorage.getItem('activeTab'));
    this.activeTab = this.currentactiveTabSubject.asObservable();
  }

  showSuccess(message) {
    this.toastr.success(message, 'Success');
  }

  showerror(message) {
    this.toastr.error(message, 'Error');
  }

  setActiveTab(tab) {
    sessionStorage.setItem('activeTab', tab);
    this.currentactiveTabSubject.next(tab);
  }
}
