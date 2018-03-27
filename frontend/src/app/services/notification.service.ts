import {Injectable} from "@angular/core";
import {Subject} from "rxjs/Subject";
import {Observable} from "rxjs/Observable";
import {PushNotification} from "../models/notification";

@Injectable()
export class NotificationService
{
    private notificationSubject: Subject<PushNotification> = new Subject();

    getNotification(): Observable<any>
    {
        return this.notificationSubject.asObservable();
    }

    notify(message: string, type: string, title=""): void
    {
        this.notificationSubject.next(new PushNotification(message, type, title))
    }

    info(message: string, title="")
    {
        let type = "info";
        this.notify(message, type, title);
    }

    success(message: string, title="")
    {
        let type = "success";
        this.notify(message, type, title);
    }

    warning(message: string, title="")
    {
        let type = "warning";
        this.notify(message, type, title);
    }

    error(message: string, title="")
    {
        let type = "danger";
        this.notify(message, type, title);
    }
}
