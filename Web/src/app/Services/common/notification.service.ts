import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private toastr: ToastrService) { 
  }

  showSuccess(message) {
    this.toastr.success(message, 'Success');
  }

  showerror(message) {
    this.toastr.error(message, 'Error');
  }
}
