import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, Router} from '@angular/router';
import {DatabaseService} from "../../services/database.service";
import {isNullOrUndefined} from "util";
import {Trigger} from "../../models/rest-models";

@Injectable()
export class TriggerResolver implements Resolve<Trigger>
{

    constructor(private databaseService: DatabaseService,
                private router: Router)
    {
    }

    resolve(route: ActivatedRouteSnapshot): Promise<Trigger>
    {
        if (!isNullOrUndefined(route.queryParams['schema']) && !isNullOrUndefined(route.queryParams['trigger']))
        {
            return this.databaseService.getTrigger(route.queryParams['schema'], route.queryParams['trigger'])
                .then(trigger => trigger)
                .catch(()=>
                {
                    this.router.navigate(['/home']);
                    return null;
                });
        }
        else
        {
            this.router.navigate(['/home']);
            return null;
        }
    }
}
