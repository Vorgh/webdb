export class Utils
{
    public static parseReference(reference: string): any[]
    {
        let regex = new RegExp('^[A-z]+[.][A-z]+[(][A-z]+[)]');
        if (!regex.test(reference))
        {
            console.exception('Wrong pattern! Please correct it to match: \'schema_name\'.\'table_name\'(column_name)');
            return null;
        }

        let parsed = [];
        parsed.push(reference.substring(0, reference.indexOf(".")));
        parsed.push(reference.substring(reference.indexOf(".")+1, reference.indexOf("(")));
        parsed.push(reference.substring(reference.indexOf("(")+1, reference.indexOf(")")));

        return parsed;
    }
}
