import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Constraint, Index, Procedure, Row, Table, Trigger, View} from "../models/rest/rest-models";
import {Schema} from "../models/rest/rest-models";
import {Column} from "../models/rest/rest-models";
import {ModifyRequest} from "../models/request/request-models";
import {environment} from "../../environments/environment";

@Injectable()
export class DatabaseService
{
    private urlPrefix: string = environment.apiPrefix;

    constructor(private http: HttpClient)
    {}

    getAllSchemas(): Promise<Schema[]>
    {
        return this.http.get<Schema[]>(`${this.urlPrefix}/schema/all`)
            .toPromise()
            .catch(error => Promise.reject(error));
    }

    createSchema(schema: string): Promise<any>
    {
        let params = new HttpParams().append('schema', schema);

        return this.http.post(`${this.urlPrefix}/schema/create`, null, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    dropSchema(schema: string): Promise<any>
    {
        let params = new HttpParams().append('schema', schema);

        return this.http.delete(`${this.urlPrefix}/schema/drop`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    getAllTables(schema: string): Promise<Table[]>
    {
        let params = new HttpParams().append('schema', schema);

        return this.http.get<Table[]>(`${this.urlPrefix}/table/metadata/all`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    createTable(schema: string, tableName: string, columnDefs: Column[], foreignKeys: Constraint[]): Promise<any>
    {
        let body = {
            schemaName: schema,
            tableName: tableName,
            columns: columnDefs,
            foreignKeys: foreignKeys
        };

        return this.http.post(`${this.urlPrefix}/table/create`, body)
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    alterTable(schema: string, table: string, changes: any): Promise<any>
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('table', table);

        return this.http.put(`${this.urlPrefix}/table/alter`, changes, {params: params})
            .toPromise()
            .catch(error => Promise.reject(error));
    }

    getTable(schema: string, table: string)
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('table', table);

        return this.http.get<Table>(`${this.urlPrefix}/table/metadata/single`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    dropTable(schema: string, table: string)
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('table', table);

        return this.http.delete(`${this.urlPrefix}/table/drop`, {params: params})
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

    getRows(schema: string, table: string): Promise<Row[]>
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('table', table)
            .append('column', '*');

        return this.http.get<Row[]>(`${this.urlPrefix}/table/rows`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    modifyRows(schema: string, table: string, rowChanges: any)
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('table', table);

        return this.http.post(`${this.urlPrefix}/table/rows/modify`, rowChanges, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    getAllViews(schema: string): Promise<View[]>
    {
        let params = new HttpParams().append('schema', schema);

        return this.http.get<View[]>(`${this.urlPrefix}/view/metadata/all`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    getView(schema: string, view: string)
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('view', view);

        return this.http.get<View>(`${this.urlPrefix}/view/metadata/single`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    createView(request: View)
    {
        return this.http.post(`${this.urlPrefix}/view/create`, request)
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    alterView(request: ModifyRequest<View>)
    {
        return this.http.put(`${this.urlPrefix}/view/alter`, request)
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    dropView(schema: string, view: string)
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('view', view);

        return this.http.delete(`${this.urlPrefix}/view/drop`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    getAllTriggers(schema: string): Promise<Trigger[]>
    {
        let params = new HttpParams().append('schema', schema);

        return this.http.get<Trigger[]>(`${this.urlPrefix}/trigger/all`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    getTrigger(schema: string, trigger: string)
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('trigger', trigger);

        return this.http.get<Trigger>(`${this.urlPrefix}/trigger/single`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    createTrigger(schema: string, trigger: Trigger): Promise<any>
    {
        let params = new HttpParams()
            .append('schema', schema);

        return this.http.post(`${this.urlPrefix}/trigger/create`, trigger, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    modifyTrigger(request: ModifyRequest<Trigger>): Promise<any>
    {
        return this.http.put(`${this.urlPrefix}/trigger/modify`, request)
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    dropTrigger(schema: string, triggerName: string): Promise<any>
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('trigger', triggerName);

        return this.http.delete(`${this.urlPrefix}/trigger/drop`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    getAllProcedures(schema: string): Promise<Procedure[]>
    {
        let params = new HttpParams().append('schema', schema);

        return this.http.get<Procedure[]>(`${this.urlPrefix}/procedure/all`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    getProcedure(schema: string, procedure: string): Promise<Procedure>
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('procedure', procedure);

        return this.http.get<Procedure>(`${this.urlPrefix}/procedure/single`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    createProcedure(procedure: Procedure): Promise<any>
    {
        return this.http.post(`${this.urlPrefix}/procedure/create`, procedure)
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    modifyProcedure(request: ModifyRequest<Procedure>): Promise<any>
    {
        return this.http.put(`${this.urlPrefix}/procedure/modify`, request)
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    dropProcedure(schema: string, procedure: string): Promise<any>
    {
        let params = new HttpParams()
            .append('schema', schema)
            .append('procedure', procedure);

        return this.http.delete(`${this.urlPrefix}/procedure/drop`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    executeCustomSql(sql: string): Promise<Row[]>
    {
        return this.http.post<Row[]>(`${this.urlPrefix}/custom`, sql)
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    //TODO: Deatiled row select
    /*getRows(schema: string, table: string, columns: string[]): Promise<Row[]>
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
