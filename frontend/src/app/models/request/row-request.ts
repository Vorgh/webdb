export class RowRequest
{
    schemaName: string;
    tableName: string;
    columnNames: string[];
    conditions: string[];
    orderings: string[];
    groupings: string[];
}
