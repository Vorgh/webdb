import {Injectable} from "@angular/core";
import {Subject} from "rxjs/Subject";
import {Observable} from "rxjs/Observable";

@Injectable()
export class NotificationService
{
    private notificationSubject: Subject<String> = new Subject();

    getNotification(): Observable<any>
    {
        return this.notificationSubject.asObservable();
    }

    notify(message: string): void
    {
        this.notificationSubject.next(message)
    }

    info(message: string)
    {
        this.notify(message);
    }

    success(message: string)
    {
        this.notify(message);
    }

    warning(message: string)
    {
        this.notify(message);
    }

    error(message: string)
    {
        this.notify(message);
    }
}
