import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, Router} from '@angular/router';
import {DatabaseService} from "../../services/database.service";
import {isNullOrUndefined} from "util";
import {Procedure} from "../../models/rest/rest-models";
import {GlobalErrorHandler} from "../../services/error-handler.service";

@Injectable()
export class ProcedureResolver implements Resolve<Procedure>
{

    constructor(private databaseService: DatabaseService,
                private router: Router,
                private errorHandler: GlobalErrorHandler)
    {
    }

    resolve(route: ActivatedRouteSnapshot): Promise<Procedure>
    {
        if (!isNullOrUndefined(route.queryParams['schema']) && !isNullOrUndefined(route.queryParams['procedure']))
        {
            if (!isNullOrUndefined(route.queryParams['isFunction']) && route.queryParams['isFunction'] == 'true')
            {
                return this.databaseService.getProcedure(route.queryParams['schema'], route.queryParams['procedure'], "true")
                           .catch(error =>
                           {
                               this.router.navigate(["/home"]);
                               this.errorHandler.handleError(error);
                               return null;
                           })
            }
            else
            {
                return this.databaseService.getProcedure(route.queryParams['schema'], route.queryParams['procedure'], "false")
                           .catch(error =>
                           {
                               this.router.navigate(["/home"]);
                               this.errorHandler.handleError(error);
                               return null;
                           })
            }
        }
        else
        {
            this.errorHandler.notFound();
            return null;
        }
    }
}
