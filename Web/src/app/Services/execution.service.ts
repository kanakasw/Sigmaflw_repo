import { Injectable } from '@angular/core';
import { UtilityService } from './common/utility.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
        providedIn: 'root'
})
export class ExecutionService {

        constructor(private utilityService: UtilityService,
                private http: HttpClient) { }

        /**
           * Description : Get Process Details By ProcessID <br>
           * Method      : GET <br>
           * Input       : ENDPOINTS, integrationProcessID <br>
           * return      : promise <br>
           */

        getProcessDetailsByProcessID(ENDPOINTS, integrationProcessID): Observable<any> {
                var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.GetProcessDetailsByProcessID;
                url = this.utilityService.
                        replaceTagWithValue(url, 'integrationProcessID', integrationProcessID);
                return this.http.get<any>(url);
        }

        /**
              *  Description : fetch Current Integration Process Execution by Integration Process ID <br>
              *  Method      : GET <br>
              *  Input       : ENDPOINTS, integrationProcessID <br>
              *  return      : promise        <br>
              */

        getCurrentIntegrationProcessExecutionByIntegrationProcessID(ENDPOINTS, integrationProcessID): Observable<any> {
                var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.GetCurrentIntegrationProcessExecution;
                url = this.utilityService.
                        replaceTagWithValue(url, 'integrationProcessID', integrationProcessID);
                return this.http.get<any>(url);
        }

        /**
          *  Description : Fetch All Activity Executions By Intgeration Process Execution ID <br>
          *  Method      : GET <br>
          *  Input       : ENDPOINTS,  integrationProcessExecutionID <br>
          *  return      : promise <br>
          */
        getAllActivityExecutionsByIntegrationProcessExecutionID(ENDPOINTS, integrationProcessExecutionID) {
                var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.GetAllActivityExecutions;
                url = this.utilityService.
                        replaceTagWithValue(url, 'integrationProcessExecutionID', integrationProcessExecutionID);
                return this.http.get<any>(url);
        }

        /**
         *  Description : Get all previous Execution details By Integration Process ID <br>
         *  Method      : GET <br>
         *  Input       : ENDPOINTS,   integrationProcessID <br>
         *  return      : promise <br>
         */
        getPreviousExecutionDetailsByIntegrationProcessID(ENDPOINTS, integrationProcessID): Observable<any> {

                var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.GetAllPreviousExecutionDetails;
                url = this.utilityService.
                        replaceTagWithValue(url, 'integrationProcessID', integrationProcessID);
                return this.http.get<any>(url);
        }

        /**
        * Description : retry Activity Execution By Activity Execution ID <br>
        * Method      : GET <br>
        * Input       : ENDPOINTS, activityExecutionID <br>
        * return      : promise <br>
        */
        retryActivityExecutionByActivityExecutionID(ENDPOINTS, integrationProcessID): Observable<any> {
                var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.EXECUTE_PROCESS;
                url = this.utilityService.
                        replaceTagWithValue(url, 'integrationProcessID', integrationProcessID);
                return this.http.get<any>(url);
        }

        /**
        * Description : Get Average Execution Time By integrationProcessID <br>
        * Method      : GET <br>
        * Input       : ENDPOINTS, activityExecutionID <br>
        * return      : promise <br>
        * Response    : {<br>
            *					"AverageExecutionTime" : "0 seconds"<br>
            *				 }<br>
        */
        getAverageExecutionTimeByIntegrationProcessID(ENDPOINTS, integrationProcessID): Observable<any> {
                var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.AverageExecutionTime;
                url = this.utilityService.
                        replaceTagWithValue(url, 'integrationProcessID', integrationProcessID);
                return this.http.get<any>(url);
        }

        /**
         *Description : Get Last Execution Details By integrationProcessID <br>
         * Method      : GET <br>
         * Input       : ENDPOINTS, integrationProcessID <br>
         * return      : promise <br>
         * Response    : {<br>
	     *					"Last Execution": "<Last Execution will be here..>",<br>
                        "Date and Time": "<Last Execution Date and Time will be here..>"<br>
         */
        getLastExecutionDetailsByIntegrationProcessID(ENDPOINTS, integrationProcessID): Observable<any> {
                var url = ENDPOINTS.SERVICE_ADDRESS + ENDPOINTS.LastSuccessfulExecutionDetails;
                url = this.utilityService.
                        replaceTagWithValue(url, 'integrationProcessID', integrationProcessID);
                return this.http.get<any>(url);
        }
}
