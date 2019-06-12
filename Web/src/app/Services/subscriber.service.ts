import { Injectable } from '@angular/core';
import { HttpClient, HttpHandler, HttpHeaders } from '@angular/common/http';
import { UtilityService } from './common/utility.service';
import { Observable } from 'rxjs';
import { HttpHandlerService } from './common/http-handler.service';
import { ENDPOINTS } from '../index.constants';

@Injectable({
    providedIn: 'root'
})
export class SubscriberService {

    constructor(private http: HttpClient,
        private utilityService: UtilityService,
        private httpHandler: HttpHandlerService
    ) { }

    /**
         * Description : getSubscriberDetails service method. <br>
         * Method      : GET <br>
         * name        : Get Subscriber Details <br>
         * return      : promise <br>
         */
    getSubscriberDetails(ENDPOINTS, username): Observable<any> {
        /*  var header = new  HttpHeaders();
         header.append("Access-Control-Allow-Origin ","*") */
        /*  let headers = new Headers();
       headers.append("Access-Control-Allow-Origin","*");
       headers.append("Access-Control-Allow-Methods","GET, POST");
       headers.append("Content-Type","application/json");
*/
        var url = ENDPOINTS.SERVICE_ADDRESS
            + ENDPOINTS.GET_SUBSCRIBER_ENDPOINT
            + "/"
            + username;
        return this.http.get<any>(url);
    }

    getSubscriberByUser(ENDPOINTS, id): Observable<any> {
        /*  var header = new  HttpHeaders();
         header.append("Access-Control-Allow-Origin ","*") */
        /*  let headers = new Headers();
       headers.append("Access-Control-Allow-Origin","*");
       headers.append("Access-Control-Allow-Methods","GET, POST");
       headers.append("Content-Type","application/json");
*/
        var url = ENDPOINTS.SERVICE_ADDRESS
            + "/Integration/user/id/3";
        return this.http.get<any>(url);
    }

    /**
     * Description : Subscriber Logout service method. <br>   
     * Method      : GET <br>
     * name       : Logout <br>
     * return      : promise <br>  
     */
    logout(ENDPOINTS, accessToken): Observable<any> {
        var url = ENDPOINTS.SERVICE_ADDRESS
            + ENDPOINTS.LOGOUT_ENDPOINT
            + "/"
            + accessToken;
        return this.http.get<any>(url);
    }

    /**
     * Description : Execute Process by integration process ID.  <br>
     * Method      : GET <br>
     * name       : Execute Process By Integration ProcessID <br>
     * return      : promise <br>
     */
    executeProcessByIntegrationProcessID(integrationProcessID): Observable<any> {
        var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.EXECUTE_PROCESS;
        url = this.utilityService.
            replaceTagWithValue(url, 'integrationProcessID', integrationProcessID)
        return this.http.get<any>(url);
    }

    /**
     * Description : Update Activity by Activity ID <br> and body i.e {"cronExpression":"0 0/5 * * * ?"}. <br>
     * Method      : PUT <br>
     * name       : Update Activity By Activity ID <br>
     * return      : promise <br>
     */
    updateActivityByActivityID(ENDPOINTS, ActivityID, bodyJSON): Observable<any> {
        var url = ENDPOINTS.SERVICE_ADDRESS
            + ENDPOINTS.UPDATE_ACTIVITY_BY_ACTIVITY_ID
            + ActivityID;
        return this.http.put<any>(url, bodyJSON);
    }

    /**
     * Description : When token expires, refresh Token <br> (form-data : grant_type=refresh_token&refresh_token=THE_REFRESH_TOKEN). <br>
     * Method      : POST <br>
     * name       : Refresh Token <br>
     * return      : promise <br>
     */
    refreshToken(ENDPOINTS, refreshToken) {
        var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.AUTH_ENDPOINT;
        var formData = "scope=read+write&grant_type=refresh_token&refresh_token=" + encodeURIComponent(refreshToken);
        return this.httpHandler.OAuthRefreshToken(url, formData);
    }

    /**
     * Description : getInputparameter service method. <br>
     * Method      : GET <br>
     * name        : Get Input parameter Details <br>
     * return      : promise <br>
     */
    getInputParameter(ENDPOINTS, activityID): Observable<any> {

        var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.GetInputParameter;
        url = this.utilityService.
            replaceTagWithValue(url, 'activityID', activityID)
        return this.http.get<any>(url);
    }

    /**
     * Description : getUniqueKeywords service method. <br>
    * Method      : GET <br>
    * name        : Get Unique Keywords Details <br>
    * return      : promise <br>
    */
    getUniqueKeywords(ENDPOINTS, subscriberID): Observable<any> {
        var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.GetUniqueKeywords;
        url = this.utilityService.
            replaceTagWithValue(url, 'subscriberID', subscriberID)
        return this.http.get<any>(url);
    }

    /**
     * Description : updateInputParameter service method. <br>
     * Method      : PUT <br>
     * name        : Update Input Parameter <br>
     * return      : promise <br>
     *     */
    updateInputParameter(ENDPOINTS, activityID, bodyJSON): Observable<any> {
        var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.UpdateInputParameter;
        url = this.utilityService.
            replaceTagWithValue(url, 'activityID', activityID)
        return this.http.put<any>(url, activityID);
    }

    /**
     * Description : updateIntegrationProcessSetup service method. <br>
     * Method      : PUT <br>
     * name        : Update Integration Process Setup <br>
     * return      : promise <br>
     *     */
    updateIntegrationProcessSetup(ENDPOINTS, integrationProcessID, bodyJSON): Observable<any> {
        var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.UpdateIntegrationProcessSetup;
        url = this.utilityService.
            replaceTagWithValue(url, 'IntegrationProcessID', integrationProcessID)
        return this.http.put<any>(url, bodyJSON);
    }

    /**
     * Description : updateProcessingSpecification service method. <br>
     * Method      : PUT <br>
     * name        : Update Processing Specification <br>
     * return      : promise <br>
     **/
    updateProcessingSpecification(ENDPOINTS, activityID, bodyJSON): Observable<any> {
        var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.UpdateProcessingSpecification;
        url = this.utilityService.
            replaceTagWithValue(url, 'activityID', activityID)
        return this.http.put<any>(url, bodyJSON);
    }
}
