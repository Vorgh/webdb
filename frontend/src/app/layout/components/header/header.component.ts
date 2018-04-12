import {Component, OnInit} from '@angular/core';
import {Router, NavigationEnd} from '@angular/router';
import {ConnectionService} from "../../../services/connection.service";
import {NotificationService} from "../../../services/notification.service";
import {PushNotification} from "../../../models/notification";
import {animate, keyframes, query, style, transition, trigger} from "@angular/animations";
import {CookieService} from "ngx-cookie-service";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss'],
    animations: [
        trigger('notificationList', [
            transition('* => *', [
                query(':enter', style({opacity: 0}), {optional: true}),
                query(':enter',
                    animate('500ms ease-in', keyframes([
                        style({opacity: 0, transform: 'translateX(-10px)', offset: 0}),
                        style({opacity: 0.5, transform: 'translateX(10px)', offset: 0.3}),
                        style({opacity: 1, transform: 'translateX(0)', offset: 1}),
                    ])), {optional: true}),
                query(':leave',
                    animate('300ms ease-in', keyframes([
                        style({opacity : 1, transform: 'translateX(0)'}),
                        style({opacity: 0, transform: 'translateX(-50px)'}),
                    ])), {optional: true})
            ])
        ])
    ]
})
export class HeaderComponent implements OnInit
{
    pushRightClass: string = 'push-right';
    notificationList: PushNotification[] = [];
    //isNotificationsShowing = false;
    user: string;

    constructor(private connectionService: ConnectionService,
                private notificationService: NotificationService,
                private cookieService: CookieService,
                private router: Router)
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
        this.notificationService.getNotification().subscribe((notification: PushNotification) =>
        {
            if (notification != null)
            {
                this.notificationList.push(notification);
            }
        });
        this.user = this.cookieService.check("user") ? this.cookieService.get("user") : "User";
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

    /*toggleNotificationList()
    {
        console.log(!this.isNotificationsShowing);
        this.isNotificationsShowing = !this.isNotificationsShowing;
    }*/

    removeNotification(index: number)
    {
        this.notificationList.splice(index, 1);
    }

    getColorClass(type: string): string
    {
        switch (type)
        {
            case "info": return "bg-info";
            case "success": return "bg-success";
            case "warning": return "bg-warning";
            case "danger": return "bg-danger";
        }
    }

    getHourMinFromDate(date: Date): string
    {
        return date.toLocaleTimeString(navigator.language, {hour: '2-digit', minute:'2-digit'});
    }

    onLoggedout()
    {
        this.connectionService.logout();
    }
}
