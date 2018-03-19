import {Procedure, Trigger} from "../rest/rest-models";

export class RowRequest
{
    schemaName: string;
    tableName: string;
    columnNames: string[];
    conditions: string[];
    orderings: string[];
    groupings: string[];
}

export class ModifyTriggerRequest
{
    from: Trigger;
    to: Trigger;

    constructor(from: Trigger, to: Trigger)
    {
        this.from = from;
        this.to = to;
    }
}

export class ModifyProcedureRequest
{
    from: Procedure;
    to: Procedure;

    constructor(from: Procedure, to: Procedure)
    {
        this.from = from;
        this.to = to;
    }
}
