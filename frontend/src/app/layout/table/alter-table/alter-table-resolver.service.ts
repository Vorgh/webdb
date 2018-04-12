import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, Router} from '@angular/router';
import {Table} from "../../../models/rest/rest-models";
import {DatabaseService} from "../../../services/database.service";
import {isNullOrUndefined} from "util";
import {GlobalErrorHandler} from "../../../services/error-handler.service";

@Injectable()
export class AlterTableResolver implements Resolve<Table>
{

    constructor(private databaseService: DatabaseService,
                private router: Router,
                private errorHandler: GlobalErrorHandler)
    {
    }

    resolve(route: ActivatedRouteSnapshot): Promise<Table>
    {
        if (!isNullOrUndefined(route.queryParams['schema']) && !isNullOrUndefined(route.queryParams['table']))
        {
            return this.databaseService.getTable(route.queryParams['schema'], route.queryParams['table'])
                .then(table => table)
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
