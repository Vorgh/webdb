import {Injectable, NgModule} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpHeaders} from '@angular/common/http';

import {HTTP_INTERCEPTORS} from '@angular/common/http';

@Injectable()
export class HttpsRequestInterceptor implements HttpInterceptor
{
  private headers = new HttpHeaders().set('Content-Type','application/json');

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>
  {
    if (req.url === "rest/oauth/token")
      return next.handle(req);

    let token = localStorage.getItem("access_token");
    if (token)
    {
      this.headers = this.headers.set('Authorization', 'Bearer ' + token);
    }
    const dupReq = req.clone(
      {
        headers: this.headers
      });

    return next.handle(dupReq);
  }
}

@NgModule({
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: HttpsRequestInterceptor, multi: true}
  ]
})
export class HttpInterceptorModule
{
}
