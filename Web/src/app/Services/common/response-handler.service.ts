import { Injectable } from '@angular/core';
import { STATUS_CODE, STATUS_MESSAGE } from 'src/app/index.constants';

@Injectable({
  providedIn: 'root'
})
export class ResponseHandlerService {

  constructor() { }

  getResponseMessageByStatus(promise) {
    var message;
    if (promise.status == STATUS_CODE.OK) {
      return true;
    }
    else if (promise.status == STATUS_CODE.BAD_REQUEST) {
      message = STATUS_MESSAGE.BAD_REQUEST;
      /*  toastr.error(message); */
    }
    else if (promise.status == STATUS_CODE.UN_AUTHORIZED) {
      message = STATUS_MESSAGE.UN_AUTHORIZED;
      /* toastr.error(message); */
      /*  ApplicationStorage.clear(); */
      this.redirectToLogin();
    }
    else if (promise.status == STATUS_CODE.NOT_FOUND) {
      //message = promise.status + " : Not found";
      //console.log(message);
      message = STATUS_MESSAGE.NOT_FOUND;
      /* toastr.error(message); */
    }
    else if (promise.status == STATUS_CODE.INTERNAL_ERROR) {
      message = STATUS_MESSAGE.INTERNAL_ERROR;
      /*  toastr.error(message); */
    }
    else if (promise.status == STATUS_CODE.SERVICE_NOT_REACHABLE) {
      message = STATUS_MESSAGE.SERVICE_NOT_REACHABLE;
      /*    toastr.error(message); */
      /*  ApplicationStorage.clear(); */
      this.redirectToLogin();
    }
    return false;
  }

  getResponseMessageByData(data) {
    if (data.status == STATUS_CODE.OK) {
      console.log("Successfully updated Process details :  " + data.data.message);
      /*  toastr.success(data.data.message); */
    } else {
      console.log("Error occured while executing process : " + data.data.message);
      /*   toastr.error(" ERROR : "+ data.status +" "+ data.data.message); */
    }
  }

  redirectToLogin() {
    /*  $state.go('loginState'); */
  }
}
