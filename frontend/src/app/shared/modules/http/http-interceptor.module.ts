import {Injectable, NgModule} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {
    HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpHeaders,
    HttpErrorResponse
} from '@angular/common/http';

import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor
{
    private headers = new HttpHeaders().set('Content-Type', 'application/json');

    constructor(private cookieService: CookieService, private router: Router)
    {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>
    {
        if (req.url === "rest/oauth/token" || req.url === "rest/connection/connectionAuth")
            return next.handle(req);

        if (this.cookieService.check("access_token"))
        {
            let token = this.cookieService.get("access_token");
            this.headers = this.headers.set('Authorization', 'Bearer ' + token);
        }
        const dupReq = req.clone(
            {
                headers: this.headers
            });

        return next.handle(dupReq)
                   .catch(err =>
                   {
                       if (err instanceof HttpErrorResponse && err.status == 401)
                       {
                           this.cookieService.delete("access_token");
                           this.cookieService.delete("current_user");
                           this.router.navigate(['/login']);
                       }

                       return Observable.throw(err);
                   });
    };
}

@NgModule({
    providers: [
        {provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true}
    ]
})
export class HttpRequestInterceptorModule
{
}
