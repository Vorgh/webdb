import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {routerTransition} from '../router.animations';
import {ConnectionAuthInfo} from "../models/connection";
import {ConnectionService} from "../services/connection.service";
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
    loginErrorMessage: string;

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
            .then(() =>
            {
                this.router.navigate(['/home']);
                this.cookieService.set("user", this.connAuth.username)
            })
            .catch(error =>
            {
                this.loginErrorMessage = this.errorHandler.getErrorMessage(error)
                //this.cookieService.delete("access_token");
            });
    }
}
