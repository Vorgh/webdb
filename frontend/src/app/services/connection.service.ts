import {Injectable, isDevMode} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {ConnectionAuthInfo, OAuthTokenResponse} from "../models/connection";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";

@Injectable()
export class ConnectionService
{
    urlPrefix: string = 'rest';

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
                       let params = new HttpParams()
                           .append('url', connAuth.url)
                           .append('username', connAuth.username)
                           .append('password', connAuth.password)
                           .append('grant_type', 'password');

                       let headers = new HttpHeaders()
                           .append('Content-type', 'application/x-www-form-urlencoded')
                           .append('Authorization', 'Basic ' + btoa(`${connAuth.username}:${connAuth.password}`));

                       this.cookieService.set("current_user", connAuth.username, 3600);

                       return this.http.post<OAuthTokenResponse>(`${this.urlPrefix}/oauth/token`, params, {headers: headers})
                                  .toPromise()
                                  .catch(error => Promise.reject(error));
                   })
                   .catch(error => Promise.reject(error));
    }

    logout()
    {
        this.http.post(`${this.urlPrefix}/connection/logout`, [])
            .toPromise()
            .then(() =>
            {
                this.cookieService.delete('access_token');
                this.cookieService.delete('current_user');
                this.router.navigate(['/login']);
            })
            .catch(error => Promise.reject(error));
    }
}
