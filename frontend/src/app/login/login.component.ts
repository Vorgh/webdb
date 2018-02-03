import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {routerTransition} from '../router.animations';
import {ConnectionAuthInfo, OAuthTokenResponse} from "../models/connection";
import {ConnectionService} from "../services/connection.service";
import {isNullOrUndefined} from "util";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
    animations: [routerTransition()]
})
export class LoginComponent implements OnInit
{
    connAuth: ConnectionAuthInfo = new ConnectionAuthInfo();

    constructor(private connectionService: ConnectionService, private router: Router)
    {
    }

    ngOnInit()
    {
        if (!isNullOrUndefined(localStorage.getItem('access_token')))
        {
            this.router.navigate(['/home']);
        }
    }

    onLoggedin()
    {
        this.connectionService.connect(this.connAuth)
            .then((response: OAuthTokenResponse) =>
            {
                localStorage.setItem("access_token", response.access_token);
                this.router.navigate(['/home']);
            })
            .catch(error =>
            {
                console.log(error);
                localStorage.removeItem("current_user");
                this.router.navigate(['/access-denied']);
            });
    }
}
