import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs/BehaviorSubject";

@Injectable()
export class DbModalService
{
    private refreshSidebarSchemas: BehaviorSubject<boolean> = new BehaviorSubject(null);

    constructor()
    {
    }

    getRefreshObservable()
    {
        return this.refreshSidebarSchemas.asObservable();
    }

    refreshSidebar()
    {
        this.refreshSidebarSchemas.next(true);
    }
}
