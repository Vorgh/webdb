import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, Router} from '@angular/router';
import {View} from "../../../models/rest/rest-models";
import {DatabaseService} from "../../../services/database.service";
import {isNullOrUndefined} from "util";
import {GlobalErrorHandler} from "../../../services/error-handler.service";

@Injectable()
export class AlterViewResolver implements Resolve<View>
{

    constructor(private databaseService: DatabaseService,
                private router: Router,
                private errorHandler: GlobalErrorHandler)
    {
    }

    resolve(route: ActivatedRouteSnapshot): Promise<View>
    {
        if (!isNullOrUndefined(route.queryParams['schema']) && !isNullOrUndefined(route.queryParams['view']))
        {
            return this.databaseService.getView(route.queryParams['schema'], route.queryParams['view'])
                       .catch(error =>
                       {
                           this.router.navigate(["/home"]);
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
