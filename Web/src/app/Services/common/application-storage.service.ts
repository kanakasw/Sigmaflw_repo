import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class ApplicationStorageService {
    tokenInfo: any;
    navigationMenu: any;
    processHeader: any;
    constructor() {
    }

    /**
       * {
       *  access_token : "bdbc2613-4901-4a1e-8851-048ad2609464",
       *  expires_in : 41097,
       *  refresh_token : "4e973df7-4156-489a-89c0-cd9a294348a5",
       *  scope : "read write",
       *  token_type : "bearer"
       * }
       */
    setTokenInfo(tokenInfo) {
        sessionStorage.setItem("tokenInfo", JSON.stringify(tokenInfo));
        this.tokenInfo = tokenInfo;
    }

    clear() {
        /* sessionStorage.removeItem("tokenInfo");
        sessionStorage.removeItem('username');
        sessionStorage.removeItem('currentuser');
        sessionStorage.removeItem('access_token');
        sessionStorage.removeItem('ProcessHeader');
        localStorage.removeItem('navigationMenu');
        sessionStorage.removeItem('role'); */
        sessionStorage.clear();
        this.navigationMenu = {};
        this.tokenInfo = {} 
    }

    getRefreshToken() {
        if (this.tokenInfo != undefined) {
            this.tokenInfo.refresh_token
        }
        return null;
    }

    getTokenInfo() {
        return JSON.parse(sessionStorage.getItem("tokenInfo"));

    }

    getValue(key) {
        return sessionStorage.getItem(key);
    }

    setValue(key, value) {
        sessionStorage.setItem(key, value);
    }

    getAccessToken() {
        var tokenInfo = sessionStorage.getItem("tokenInfo");
        if (tokenInfo != null)
            return;
        return this.tokenInfo.access_token;
    }

    setAccessToken(access_token) {
        sessionStorage.setItem("access_token", access_token)
        this.tokenInfo.access_token = access_token;
    }

    getUsername() {

        return sessionStorage.getItem("username");

        return this.tokenInfo.username;
    }

    setUsername(username) {

        sessionStorage.setItem("username", username);

        this.tokenInfo.username = username;
    }

    islocalStorageSupported() {
        return localStorage.isSupported;
    }

}
