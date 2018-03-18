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

    let type_upper = type.toUpperCase();
    let found_upper = foundType.toUpperCase();

    //Check types with no length
    if (Utils.NO_LENGTH_TYPES.indexOf(found_upper) > -1)
    {
        if (type_upper != found_upper)
        {
            return false;
        }
    }

    //Check types with optional length
    if (Utils.OPTIONAL_LENGTH_TYPES.indexOf(found_upper) > -1)
    {
        if (type_upper == found_upper)
        {
            return true;
        }

        let lengthDefinition = type.substring(foundType.length).trim();
        let regex = new RegExp('^([\(](0|[1-9][0-9]*)[\)])|([\(](0|[1-9][0-9]*),(0|[1-9][0-9]*)[\)])$');

        if (!regex.test(lengthDefinition))
        {
            return false;
        }
    }

    //Check types with required length
    if (Utils.REQUIRED_LENGTH_TYPES.indexOf(found_upper) > -1)
    {
        let lengthDefinition = type.substring(foundType.length).trim();

        let regex = new RegExp('^[\(](0|[1-9][0-9]*)[\)]$');
        if (!regex.test(lengthDefinition))
        {
            return false;
        }
    }

    //Check types with any number of arguments
    if (Utils.ANY_NUMBER_ARGS_TYPES.indexOf(found_upper) > -1)
    {
        let argsDefinition = type.substring(foundType.length).trim();
        let regex = new RegExp('^[\(].*[\)]$');

        if (!regex.test(argsDefinition))
        {
            return false;
        }
    }

    return true;
}
