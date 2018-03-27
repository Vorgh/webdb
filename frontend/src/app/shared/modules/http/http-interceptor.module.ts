import {Injectable, NgModule} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {
    HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpHeaders,
    HttpErrorResponse
} from '@angular/common/http';

import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/finally';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/take';

import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";
import {ConnectionService} from "../../../services/connection.service";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {OAuthTokenResponse} from "../../../models/connection";
import {GeneralError} from "../../../models/rest/rest-errors";
import {GlobalErrorHandler} from "../../../services/error-handler.service";
import {environment} from "../../../../environments/environment";

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor
{
    private headers = new HttpHeaders().set('Content-Type', 'application/json');
    private isRefreshingToken: boolean = false;
    private tokenSubject: BehaviorSubject<string> = new BehaviorSubject<string>(null);

    constructor(private cookieService: CookieService,
                private connectionService: ConnectionService,
                private errorHandler: GlobalErrorHandler,
                private router: Router)
    {
    }

    addToken(req: HttpRequest<any>, token: string): HttpRequest<any>
    {
        /*if (this.cookieService.check("access_token"))
        {
            this.headers = this.headers.set('Authorization', 'Bearer ' + token);
        }*/
        this.headers = this.headers.set('Authorization', 'Bearer ' + token);
        return req.clone({headers: this.headers});
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>
    {
        if (req.url.startsWith("rest/oauth/") || req.url == "rest/connection/connectionAuth")
            return next.handle(req);

        if (req.url == "rest/connection/logout")
        {
            return next.handle(this.addToken(req, this.cookieService.get("access_token")))
                .catch(err =>
                {
                    this.errorHandler.handleError(err);
                    this.router.navigate(['/login']);
                    return Observable.throw(err);
                })
        }

        return next.handle(this.addToken(req, this.cookieService.get("access_token")))
                   .catch(err =>
                   {
                       if (err instanceof HttpErrorResponse)
                       {
                           switch (err.status)
                           {
                               case 400: return this.handle400Error(err);
                               case 401: return this.handle401Error(req, next);
                           }
                           /*return this.connectionService.refreshConnection()
                                      .then(response =>
                                      {
                                          console.log("refresh_token");
                                          this.cookieService.set("access_token", response.access_token, 60);
                                          return next.handle(dupReq);
                                      })
                                      .catch(error =>
                                      {
                                          console.log("refresh_token_fail");
                                          this.cookieService.delete("access_token");
                                          this.router.navigate(['/login']);

                                          return Promise.reject(error);
                                      })*/
                       }

                       return Observable.throw(err);
                   });
    }

    private handle400Error(error)
    {
        if (error && error.status === 400 && error.error && error.error.error === 'invalid_grant')
        {
            return this.connectionService.logout();
        }

        return Observable.throw(error);
    }

    private handle401Error(req: HttpRequest<any>, next: HttpHandler)
    {
        if (!this.isRefreshingToken)
        {
            this.isRefreshingToken = true;

            // Reset here so that the following requests wait until the token
            // comes back from the refreshToken call.
            this.tokenSubject.next(null);

            return this.connectionService.refreshConnection()
                       .switchMap((newTokenRes: OAuthTokenResponse) =>
                       {
                           if (newTokenRes.access_token)
                           {
                               this.tokenSubject.next(newTokenRes.access_token);
                               this.cookieService.set("access_token", newTokenRes.access_token, 1800, "/", environment.domain);
                               return next.handle(this.addToken(req, newTokenRes.access_token));
                           }

                           // If we don't get a new token, we are in trouble so logout.
                           return this.logout();
                       })
                       .catch(() =>
                       {
                           // If there is an exception calling 'refreshToken', bad news so logout.
                           return this.logout();
                       })
                       .finally(() =>
                       {
                           this.isRefreshingToken = false;
                       });
        }
        else
        {
            return this.tokenSubject
                       .filter(token => token != null)
                       .take(1)
                       .switchMap(token =>
                       {
                           return next.handle(this.addToken(req, token));
                       });
        }
    }

    private logout()
    {
        this.cookieService.delete("access_token");
        this.cookieService.delete("refresh_token");
        this.router.navigate(['/login']);

        return Observable.throw(new GeneralError("Refresh Token Error", "Couldn't refresh the access token."));
    }
}

@NgModule({
    providers: [
        {provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true}
    ]
})
export class HttpRequestInterceptorModule
{
}
