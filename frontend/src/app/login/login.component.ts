import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {routerTransition} from '../router.animations';
import {ConnectionAuthInfo, OAuthTokenResponse} from "../models/connection";
import {ConnectionService} from "../services/connection.service";
import {isNullOrUndefined} from "util";
import {CookieService} from "ngx-cookie-service";
import {GlobalErrorHandler} from "../services/error-handler.service";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
    animations: [routerTransition()]
})
export class LoginComponent implements OnInit
{
    connAuth: ConnectionAuthInfo = new ConnectionAuthInfo();

    constructor(private connectionService: ConnectionService,
                private router: Router,
                private cookieService: CookieService,
                private errorHandler: GlobalErrorHandler)
    {
    }

    ngOnInit()
    {
        if (this.cookieService.check('access_token'))
        {
            this.router.navigate(['/home']);
        }
    }

    onLoggedin()
    {
        this.connectionService.connect(this.connAuth)
            .then((response: OAuthTokenResponse) =>
            {
                this.cookieService.set("access_token", response.access_token);
                this.router.navigate(['/home']);
            })
            .catch(error =>
            {
                this.errorHandler.handleError(error);
                this.cookieService.delete("current_user");
            });
    }
}
