import {Procedure, Table, Trigger} from "./rest-models";

export class DbDataWrapper
{
    schema: string;
    tables: Table[];
    triggers: Trigger[];
    procedures: Procedure[];
}
