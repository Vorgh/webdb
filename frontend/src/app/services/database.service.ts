import {Injectable, isDevMode} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Table} from "../models/table";

@Injectable()
export class DatabaseService
{
  urlPrefix: string;

  constructor(private http: HttpClient)
  {
    if (isDevMode())
    {
      this.urlPrefix = "api";
    }
    else
    {
      this.urlPrefix = "rest"
    }
  }

  getAllTables(): Promise<Table[]>
  {
    return this.http.get<Table[]>(`${this.urlPrefix}/db/tables`)
               .toPromise()
               .catch(error => Promise.reject(error));
  }

}
