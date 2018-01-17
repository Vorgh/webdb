import {Injectable, isDevMode} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Table} from "../models/table";
import {Schema} from "../models/schema";
import {Observable} from "rxjs/Observable";
import {Subject} from "rxjs/Subject";
import {Column} from "../models/column";
import {RowRequest} from "../models/request/row-request";

@Injectable()
export class DatabaseService
{
    private urlPrefix: string = 'rest';
    private _currentSchema: Subject<string>;

    constructor(private http: HttpClient)
    {
        this._currentSchema = new Subject<string>();
    }

    getAllSchemas(): Promise<Schema[]>
    {
        return this.http.get<Schema[]>(`${this.urlPrefix}/schema/all`)
            .toPromise()
            .catch(error => Promise.reject(error));
    }

    getAllTables(schema: string, isView: boolean = false): Promise<Table[]>
    {
        let params = new HttpParams().append('schema', schema);

        return this.http.get<Table[]>(`${this.urlPrefix}/table/metadata/all`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    getTable(schema: string, table: string, isView: boolean = false)
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('table', table);

        return this.http.get<Table>(`${this.urlPrefix}/table/metadata/single`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    getColumns(schema: string, table: string): Promise<Column[]>
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('table', table);

        return this.http.get<Column[]>(`${this.urlPrefix}/table/metadata/columns`, {params: params})
            .toPromise()
            .catch(error => Promise.reject(error));
    }

    getRows(schema: string, table: string): Promise<any[]>
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('table', table)
            .append('column', '*');

        return this.http.get<any[]>(`${this.urlPrefix}/table/rows`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    //TODO: Deatiled row select
    /*getRows(schema: string, table: string, columns: string[]): Promise<any[]>
    {
        let request: RowRequest = {
            schemaName: schema,
            tableName: table,
            columnNames: columns
        };

        return this.http.get<any[]>(`${this.urlPrefix}/table/rows`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }*/

    getCurrentSchemaObservable()
    {
        return this._currentSchema.asObservable();
    }

    setCurrentSchema(value: string)
    {
        this._currentSchema.next(value)
    }
}
