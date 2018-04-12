import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, Router} from '@angular/router';
import {isNullOrUndefined} from "util";
import {GlobalErrorHandler} from "../../services/error-handler.service";
import {DatabaseService} from "../../services/database.service";
import {DbDataWrapper} from "../../models/rest/db-data-wrapper";

@Injectable()
export class DBHomeResolver implements Resolve<DbDataWrapper>
{

    constructor(private databaseService: DatabaseService,
                private router: Router,
                private errorHandler: GlobalErrorHandler)
    {
    }

    resolve(route: ActivatedRouteSnapshot): Promise<DbDataWrapper>
    {
        if (!isNullOrUndefined(route.queryParams['schema']))
        {
            let schema = route.queryParams['schema'];
            return Promise.all([
                this.databaseService.getAllTables(schema),
                this.databaseService.getAllTriggers(schema),
                this.databaseService.getAllProcedures(schema)])
                .then(result =>
                {
                    let wrapper: DbDataWrapper = new DbDataWrapper();
                    wrapper.schema = schema;
                    wrapper.tables = result[0];
                    wrapper.triggers = result[1];
                    wrapper.procedures = result[2];

                    return wrapper;
                })
                .catch(error =>
                {
                    this.errorHandler.handleError(error);
                    this.router.navigate(["/home"]);
                    return null;
                });
        }
        else
        {
            this.errorHandler.notFound();
            return null;
        }
    }
}
