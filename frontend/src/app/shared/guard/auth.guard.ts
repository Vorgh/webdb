import {Injectable} from '@angular/core';
import {CanActivate} from '@angular/router';
import {Router} from '@angular/router';
import {CookieService} from "ngx-cookie-service";
import {isNullOrUndefined} from "util";

@Injectable()
export class AuthGuard implements CanActivate
{
    constructor(private router: Router, private cookieService: CookieService)
    {
    }

    canActivate()
    {
        if (this.cookieService.check('access_token'))
        {
            return true;
        }

        this.router.navigate(['/login']);
        return false;
    }
}
