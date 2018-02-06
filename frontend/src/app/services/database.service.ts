import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Constraint, Index, Table} from "../models/rest-models";
import {Schema} from "../models/rest-models";
import {Column} from "../models/rest-models";

@Injectable()
export class DatabaseService
{
    private urlPrefix: string = 'rest';

    constructor(private http: HttpClient)
    {}

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

    createTable(schema: string, tableName: string, columnDefs: Column[], foreignKeys: Constraint[]): Promise<any>
    {
        let params = new HttpParams()
            .append('schema', schema);

        let body = {
            tableName: tableName,
            columns: columnDefs,
            foreignKeys: foreignKeys
        };

        return this.http.post(`${this.urlPrefix}/table/create`, body, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    alterTable(schema: string, table: string, changes: any): Promise<any>
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('table', table);

        return this.http.post(`${this.urlPrefix}/table/alter`, changes, {params: params})
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

    getForeignKeys(schema: string, table: string): Promise<Constraint[]>
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('table', table);

        return this.http.get<Constraint[]>(`${this.urlPrefix}/table/foreign`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    getIndexes(schema: string, table: string): Promise<Index[]>
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('table', table);

        return this.http.get<Index[]>(`${this.urlPrefix}/table/indexes`, {params: params})
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
}
