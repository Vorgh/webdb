import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Table} from "../../models/table";
import {DatabaseService} from "../../services/database.service";

@Injectable()
export class DBHomeResolver implements Resolve<Table[]>
{
    constructor(private databaseService: DatabaseService)
    {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<Table[]>
    {
        let schema = route.parent.paramMap.get('schema');
        return this.databaseService.getAllTables(schema);
    }
}
