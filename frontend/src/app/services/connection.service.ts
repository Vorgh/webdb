import {Injectable, isDevMode} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {ConnectionAuthInfo, OAuthTokenResponse} from "../models/connection";

@Injectable()
export class ConnectionService
{
    urlPrefix: string = 'rest';

    constructor(private http: HttpClient)
    {
    }

    connect(connAuth: ConnectionAuthInfo): Promise<OAuthTokenResponse>
    {
        return this.http.post(`${this.urlPrefix}/connection/connectionAuth`, connAuth, {responseType: "text"})
                   .toPromise()
                   .then(() =>
                   {
                       let params = new HttpParams()
                           .append('url', connAuth.url)
                           .append('username', connAuth.username)
                           .append('password', connAuth.password)
                           .append('grant_type', 'password');

                       let headers = new HttpHeaders()
                           .append('Content-type', 'application/x-www-form-urlencoded')
                           .append('Authorization', 'Basic ' + btoa(`${connAuth.username}:${connAuth.password}`));

                       return this.http.post<OAuthTokenResponse>(`${this.urlPrefix}/oauth/token`, params, {headers: headers})
                                  .toPromise()
                                  .catch(error => Promise.reject(error));
                   })
                   .catch(error => Promise.reject(error));
    }

    logout(): Promise<any>
    {
        return this.http.post(`${this.urlPrefix}/connection/logout`, [])
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }
}
