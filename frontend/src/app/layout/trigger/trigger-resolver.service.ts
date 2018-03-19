import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, Router} from '@angular/router';
import {DatabaseService} from "../../services/database.service";
import {isNullOrUndefined} from "util";
import {Trigger} from "../../models/rest/rest-models";
import {GlobalErrorHandler} from "../../services/error-handler.service";

@Injectable()
export class TriggerResolver implements Resolve<Trigger>
{

    constructor(private databaseService: DatabaseService,
                private router: Router,
                private errorHandler: GlobalErrorHandler)
    {
    }

    resolve(route: ActivatedRouteSnapshot): Promise<Trigger>
    {
        if (!isNullOrUndefined(route.queryParams['schema']) && !isNullOrUndefined(route.queryParams['trigger']))
        {
            return this.databaseService.getTrigger(route.queryParams['schema'], route.queryParams['trigger'])
                       .then(trigger => trigger)
                       .catch(error =>
                       {
                           this.errorHandler.handleError(error);
                           return null;
                       })
        }
        else
        {
            this.errorHandler.notFound();
            return null;
        }
    }
}
