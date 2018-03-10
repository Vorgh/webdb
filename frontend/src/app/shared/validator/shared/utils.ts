import {Utils} from "../../util/utils";

export function isColumnTypeValid(type: string)
{
    let foundType: string;
    let wrongType: boolean = true;

    for (let t of Utils.dataTypes)
    {
        if (type.toUpperCase().startsWith(t))
        {
            foundType = t;
            wrongType = false;
            break;
        }
    }

    if (wrongType)
    {
        return false;
    }

    if (type.toLowerCase() != foundType.toLowerCase())
    {
        let lengthDefinition = type.substring(foundType.length).trim();
        let regex = new RegExp('[\(](0|[1-9][0-9]*)[\)]');

        if (!regex.test(lengthDefinition))
        {
            return false;
        }
    }

    return true;
}
