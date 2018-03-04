import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, Router} from '@angular/router';
import {Table} from "../../../models/rest-models";
import {DatabaseService} from "../../../services/database.service";
import {isNullOrUndefined} from "util";

@Injectable()
export class AlterTableResolver implements Resolve<Table>
{

    constructor(private databaseService: DatabaseService,
                private router: Router)
    {
    }

    resolve(route: ActivatedRouteSnapshot): Promise<Table>
    {
        if (!isNullOrUndefined(route.queryParams['schema']) && !isNullOrUndefined(route.queryParams['table']))
        {
            return this.databaseService.getTable(route.queryParams['schema'], route.queryParams['table'])
                .then(table => table)
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
