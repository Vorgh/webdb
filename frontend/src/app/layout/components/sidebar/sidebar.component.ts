import {Component, OnInit} from '@angular/core';
import {Router, NavigationEnd} from '@angular/router';
import {DatabaseService} from "../../../services/database.service";
import {Schema} from "../../../models/rest-models";
import {ConnectionService} from "../../../services/connection.service";

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit
{
    isActive: boolean = false;
    showMenu: string = '';
    pushRightClass: string = 'push-right';
    schemas: Schema[];

    constructor(private databaseService: DatabaseService,
                private connectionService: ConnectionService,
                private router: Router)
    {
        this.router.events.subscribe(val =>
        {
            if (
                val instanceof NavigationEnd &&
                window.innerWidth <= 992 &&
                this.isToggled()
            )
            {
                this.toggleSidebar();
            }
        });
    }

    ngOnInit()
    {
        this.databaseService.getAllSchemas()
            .then(schemas => this.schemas = schemas)
            .catch(promise =>
            {
                this.router.navigate(["/error"],
                    {queryParams: {code: promise.status, message: promise.statusText}});
            })
    }

    addExpandClass(element: any)
    {
        if (element === this.showMenu)
        {
            this.showMenu = '0';
        }
        else
        {
            this.showMenu = element;
        }
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
