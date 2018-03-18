export class Schema
{
    name: string;
}

export class Table
{
    schema: string;
    name: string;
    type: string;
    engine: string;
    creationDate: Date;
    collation: String;
}

export class Row
{
    deleted?: boolean;
    added?: boolean;
    [key: string]: any;
}

export class Column
{
    tableSchema: string;
    tableName: string;
    name: string;
    position: number;
    defaultValue: string;
    nullable: boolean;
    unique: boolean;
    dataType: string;
    maxCharLength: number;
    octetLength: number;
    numericPrecision: number;
    numericScale: number;
    datePrecision: number;
    charSet: string;
    columnType: string;
    primaryKey: boolean;
    autoIncrement: boolean;
    deleted?: boolean;
    added?: boolean;

    static defaultColumn(schema: string, table: string): Column
    {
        let col: Column = new Column();
        col.tableSchema = schema;
        col.tableName = table;
        col.name = "";
        col.columnType = "";
        col.defaultValue = "";
        col.primaryKey = false;
        col.unique = false;
        col.nullable = true;
        col.autoIncrement = false;
        col.deleted = false;
        col.added = true;

        return col;
    }
}

export class Constraint
{
    constraintName: string;
    schema: string;
    table: string;
    column: string;
    refSchema: string;
    refTable: string;
    refColumn: string;
    updateRule: string;
    deleteRule: string;
    deleted?: boolean;
    added?: boolean;
}

export class Index
{
    indexSchema: string;
    indexName: string;
    schema: string;
    table: string;
    column: string;
    unique: boolean;
    nullable: boolean;
    deleted?: boolean;
    added?: boolean;
}

export class Trigger
{
    schema: string;
    name: string;
    eventType: string;
    eventSchema: string;
    eventTable: string;
    triggerBody: string;
    timing: string;
    createdAt: Date;
}

export class Procedure
{
    schema: string;
    name: string;
    type: string;
    returnType: string;
    body: string;
    modified: Date;
    paramList: Parameter[];
}

export class Parameter
{
    schema: string;
    procedureName: string;
    mode: string;
    name: string;
    type: string;
    deleted?: boolean;
    added?: boolean;
}
