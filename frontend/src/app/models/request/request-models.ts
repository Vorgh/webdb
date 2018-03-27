export class ModifyRequest<T>
{
    from: T;
    to: T;

    constructor(from: T, to: T)
    {
        this.from = from;
        this.to = to;
    }
}

export class RowRequest
{
    schemaName: string;
    tableName: string;
    columnNames: string[];
    conditions: string[];
    orderings: string[];
    groupings: string[];
}
