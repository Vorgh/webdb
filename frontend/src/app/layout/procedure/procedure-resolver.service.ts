import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, Router} from '@angular/router';
import {DatabaseService} from "../../services/database.service";
import {isNullOrUndefined} from "util";
import {Procedure} from "../../models/rest-models";
import {GlobalErrorHandler} from "../../shared/error-handler/error-handler.service";

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
            return this.databaseService.getProcedure(route.queryParams['schema'], route.queryParams['procedure'])
                       .then(procedure => procedure)
                       .catch(error =>
                       {
                           this.errorHandler.handleError(error);
                           return null;
                       })
        }
        else
        {
            this.router.navigate(['/home']);
            return null;
        }
    }
}
