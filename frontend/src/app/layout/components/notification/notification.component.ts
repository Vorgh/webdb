import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {NotificationService} from "../../../services/notification.service";
import {PushNotification} from "../../../models/notification";
import {animate, keyframes, query, style, transition, trigger} from "@angular/animations";

@Component({
    selector: 'app-notification',
    templateUrl: './notification.component.html',
    styleUrls: ['./notification.component.scss'],
    animations: [
        trigger('notificationPopup', [
            transition('* => *', [
                query(':enter', style({opacity: 0}), {optional: true}),
                query(':enter',
                    animate('500ms ease-in', keyframes([
                        style({opacity: 0, transform: 'translateY(-10px)', offset: 0}),
                        style({opacity: 0.5, transform: 'translateY(10px)', offset: 0.3}),
                        style({opacity: 1, transform: 'translateY(0)', offset: 1}),
                    ])), {optional: true}),
                query(':leave',
                    animate('300ms ease-in', keyframes([
                        style({opacity : 1, transform: 'translateY(0)'}),
                        style({opacity: 0, transform: 'translateY(-50px)'}),
                    ])), {optional: true})
            ])
        ])
    ]
})
export class NotificationComponent implements OnInit
{
    private readonly TIMEOUT: number = 10000;

    @ViewChild('notificationBody') notificationBody: ElementRef;
    private timer;
    notification: PushNotification;

    constructor(private notificationService: NotificationService)
    {
    }

    ngOnInit()
    {
        this.notificationService.getNotification().subscribe((notification: PushNotification) =>
        {
            if (!notification)
            {
                return;
            }

            this.notification = notification;

            if (this.timer) clearTimeout(this.timer);
            this.timer = setTimeout(()=>
            {
                this.onClose()
            }, this.TIMEOUT);

        })
    }

    onClose()
    {
        this.notification = null;
        clearTimeout(this.timer);
        this.timer = null;
    }

}
