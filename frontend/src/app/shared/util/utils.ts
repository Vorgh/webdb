export class Utils
{
    public static dataTypes = [
        'BIT', 'TINYINT', 'BOOLEAN', 'SMALLINT', 'MEDIUMINT', 'INT', 'BIGINT', 'DECIMAL', 'FLOAT', 'DOUBLE',
        'DATE', 'DATETIME', 'TIMESTAMP', 'TIME', 'YEAR',
        'CHAR', 'VARCHAR', 'BINARY', 'VARBINARY', 'TINYBLOB', 'TINYTEXT', 'BLOB', 'TEXT', 'MEDIUMBLOB', 'MEDIUMTEXT', 'LONGBLOB', 'LONGTEXT',
        'ENUM', 'SET'
    ];
    public static NO_LENGTH_TYPES = ["BOOLEAN", "DATE", "TINYBLOB", "TINYTEXT", "MEDIUMBLOB", "MEDIUMTEXT", "LONGBLOB", "LONGTEXT"];
    public static OPTIONAL_LENGTH_TYPES = ["BIT", "TINYINT", "SMALLINT", "MEDIUMINT", "INT", "BIGINT", "DECIMAL", "FLOAT", "DOUBLE",
    "DATETIME", "TIMESTAMP", "TIME", "YEAR",
    "CHAR", "BINARY", "BLOB", "TEXT"];
    public static REQUIRED_LENGTH_TYPES = ["VARCHAR", "VARBINARY"];
    public static ANY_NUMBER_ARGS_TYPES = ["ENUM", "SET"];

    public static parseColumnReference(reference: string): any[]
    {
        let regex = new RegExp('^[A-z]+[.][A-z]+[(][A-z]+[)]');
        if (!regex.test(reference))
        {
            console.exception('Wrong pattern! Please correct it to match: \'schema_name\'.\'table_name\'(column_name)');
            return null;
        }

        let parsed = [];
        parsed.push(reference.substring(0, reference.indexOf(".")));
        parsed.push(reference.substring(reference.indexOf(".") + 1, reference.indexOf("(")));
        parsed.push(reference.substring(reference.indexOf("(") + 1, reference.indexOf(")")));

        return parsed;
    }

    public static parseTableReference(reference: string): any[]
    {
        let regex = new RegExp('^[A-z]+[.][A-z]+');
        if (!regex.test(reference))
        {
            console.exception('Wrong pattern! Please correct it to match: schema_name.table_name(column_name)');
            return null;
        }

        return reference.split(".");;
    }
}
