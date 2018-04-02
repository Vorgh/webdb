import {Injectable} from "@angular/core";
import {HeaderElement} from "../../../models/header-element";
import {Procedure, Table, Trigger, View} from "../../../models/rest/rest-models";

@Injectable()
export class PageHeaderService
{
    siteMap: HeaderElement[] = [
        {id: 'home', parent: null, title: 'Home', link: '/home', icon: 'fa-dashboard'},
        {id: 'dbhome', parent: 'home', title: 'Database', link: '/db', icon: 'fa-database'},
        {id: 'table', parent: 'dbhome', title: 'Table', link: '/table', icon: 'fa-table'},
        {id: 'create-table', parent: 'dbhome', title: 'New Table', link: '/table/create', icon: 'fa-table'},
        {id: 'modify-table', parent: 'dbhome', title: 'Modify Table', link: '/table/alter', icon: 'fa-table'},
        {id: 'view', parent: 'dbhome', title: 'Table', link: '/table', icon: 'fa-table'},
        {id: 'create-view', parent: 'dbhome', title: 'New View', link: '/view/create', icon: 'fa-table'},
        {id: 'modify-view', parent: 'dbhome', title: 'Modify View', link: '/view/alter', icon: 'fa-table'},
        {id: 'create-trigger', parent: 'dbhome', title: 'New Trigger', link: '/trigger/create', icon: 'fa-bolt'},
        {id: 'modify-trigger', parent: 'dbhome', title: 'Modify Trigger', link: '/trigger', icon: 'fa-bolt'},
        {id: 'create-procedure', parent: 'dbhome', title: 'New Procedure', link: '/procedure/create', icon: 'fa-superscript'},
        {id: 'modify-procedure', parent: 'dbhome', title: 'Modify Procedure', link: '/procedure', icon: 'fa-superscript'},
        {id: 'custom', parent: 'home', title: 'Custom SQL', link: null, icon: 'fa-file-text-o'},

    ];

    constructor()
    {
    }

    getHeaderByID(id: string): HeaderElement
    {
        return this.siteMap.find(elem => elem.id === id);
    }

    getPathFromID(id: string, schema: string = null, object: Table | View | Trigger | Procedure = null, path: HeaderElement[] = []): HeaderElement[]
    {
        let current: HeaderElement = this.getHeaderByID(id);

        if (current.parent == null)
        {
            path.push(current);
            return path.reverse();
        }

        if (object != null)
        {
            current.title = object.name;
            current.queryParams = null;
            current.link = null;

            path.push(current);
            return this.getPathFromID(current.parent, object.schema, null, path);
        }

        if (schema != null)
        {
            current.queryParams = {schema: schema};
            if (current.id == 'dbhome')
            {
                current.title = schema;
            }

            path.push(current);
            return this.getPathFromID(current.parent, schema, null, path);
        }

        path.push(current);
        return this.getPathFromID(current.parent, null, null, path);
    }
}
