import {Component, OnInit} from '@angular/core';
import {Router, NavigationEnd} from '@angular/router';
import {ConnectionService} from "../../../services/connection.service";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit
{
    pushRightClass: string = 'push-right';

    constructor(private connectionService: ConnectionService, private router: Router)
    {
        this.router.events.subscribe(val =>
        {
            if (val instanceof NavigationEnd && window.innerWidth <= 992 && this.isToggled())
            {
                this.toggleSidebar();
            }
        });
    }

    ngOnInit()
    {
    }

    isToggled(): boolean
    {
        const dom: Element = document.querySelector('body');
        return dom.classList.contains(this.pushRightClass);
    }

    toggleSidebar()
    {
        const dom: any = document.querySelector('body');
        dom.classList.toggle(this.pushRightClass);
    }

    onLoggedout()
    {
        this.connectionService.logout()
            .then(() => localStorage.removeItem('access_token'))
            .catch(error => console.log(error));

    }
}
