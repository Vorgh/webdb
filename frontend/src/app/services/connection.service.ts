import {Injectable, isDevMode} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {ConnectionAuthInfo, OAuthTokenResponse} from "../models/connection";

@Injectable()
export class ConnectionService
{
  urlPrefix: string;

  constructor(private http: HttpClient)
  {
    if (isDevMode())
    {
      this.urlPrefix = "api";
    }
    else
    {
      this.urlPrefix = "rest"
    }
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
                   .append('grant_type','password');

                 let headers = new HttpHeaders()
                   .append('Content-type', 'application/x-www-form-urlencoded')
                   .append('Authorization', 'Basic '+btoa(`${connAuth.username}:${connAuth.password}`));

                 return this.http.post<OAuthTokenResponse>(`${this.urlPrefix}/oauth/token`, params, {headers: headers})
                            .toPromise()
                            .catch(error => Promise.reject(error));
               })
               .catch(error => Promise.reject(error));
  }

  private encodeFormValues(data)
  {
    const formBody = [];
    for (const property in data)
    {
      if (data.hasOwnProperty(property))
      {
        const encodedKey = encodeURIComponent(property);
        const encodedValue = encodeURIComponent(data[property]);
        formBody.push(encodedKey + '=' + encodedValue);
      }
    }
    return formBody.join('&');
  }

}
