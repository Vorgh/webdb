import {Injectable, isDevMode} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Table} from "../models/table";
import {Schema} from "../models/schema";
import {Observable} from "rxjs/Observable";
import {Subject} from "rxjs/Subject";

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

    getAllTables(schema: string): Promise<Table[]>
    {
        let params = new HttpParams().append('schema', schema);

        return this.http.get<Table[]>(`${this.urlPrefix}/table/metadata/all`, {params: params})
                   .toPromise()
                   .catch(error => Promise.reject(error));
    }

    getCurrentSchemaObservable()
    {
        return this._currentSchema.asObservable();
    }

    setCurrentSchema(value: string)
    {
        this._currentSchema.next(value)
    }
}
