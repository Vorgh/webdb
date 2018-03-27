import {Injectable, isDevMode} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {ConnectionAuthInfo, OAuthTokenResponse} from "../models/connection";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {Observable} from "rxjs/Observable";

import 'rxjs/add/observable/throw'
import 'rxjs/add/operator/catch'
import {environment} from "../../environments/environment";

@Injectable()
export class ConnectionService
{
    urlPrefix: string = 'rest';
    basicAuth: string;

    constructor(private http: HttpClient,
                private router: Router,
                private cookieService: CookieService)
    {
    }

    connect(connAuth: ConnectionAuthInfo): Promise<OAuthTokenResponse>
    {
        return this.http.post(`${this.urlPrefix}/connection/connectionAuth`, connAuth, {responseType: "text"})
                   .toPromise()
                   .then(() =>
                   {
                       this.basicAuth = btoa(`${connAuth.username}:${connAuth.password}`);

                       let params = new HttpParams()
                           .append('url', connAuth.url)
                           .append('username', connAuth.username)
                           .append('password', connAuth.password)
                           .append('grant_type', 'password');

                       let headers = new HttpHeaders()
                               .append('Content-type', 'application/x-www-form-urlencoded')
                               .append('Authorization', 'Basic ' + this.basicAuth);

                       return this.http.post<OAuthTokenResponse>(`${this.urlPrefix}/oauth/token`, params, {headers: headers})
                                  .toPromise()
                                  .then(response =>
                                  {
                                      this.cookieService.set("access_token", response.access_token, 1800, "/", environment.domain);
                                      this.cookieService.set("refresh_token", response.refresh_token, 43200, "/", environment.domain);
                                      return response;
                                  })
                                  .catch(error => Promise.reject(error));
                   })
                   .catch(error => Promise.reject(error));
    }

    refreshConnection(): Observable<OAuthTokenResponse>
    {
        let params = new HttpParams()
            .append('refresh_token', this.cookieService.get("refresh_token"))
            .append('grant_type', 'refresh_token');

        let headers;
        headers = new HttpHeaders()
            .append('Content-type', 'application/x-www-form-urlencoded')
            .append('Authorization', 'Basic ' + this.basicAuth);

        return this.http.post<OAuthTokenResponse>(`${this.urlPrefix}/oauth/token`, params, {headers: headers})
                   .catch(error => Observable.throw(error));
    }

    logout(): Promise<any>
    {
        return this.http.post(`${this.urlPrefix}/connection/logout`, [])
            .toPromise()
            .then(() =>
            {
                this.cookieService.delete('access_token');
                this.cookieService.delete('refresh_token');
                this.router.navigate(['/login']);
            })
            .catch(error => Promise.reject(error));
    }
}
