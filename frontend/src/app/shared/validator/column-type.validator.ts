import {AbstractControl, ValidatorFn} from "@angular/forms";
import {Utils} from "../util/utils";

export function columnTypeValidator(): ValidatorFn
{
    return (control: AbstractControl): {[key: string]: any} =>
    {
        const type = control.value;
        let foundType: string;
        let wrongType: boolean = true;

        for (let t of Utils.dataTypes)
        {
            if (control.value.toUpperCase().startsWith(t))
            {
                foundType = t;
                wrongType = false;
                break;
            }
        }

        if (wrongType)
        {
            return {columnType: {valid: false}};
        }

        if (type.toLowerCase() != foundType.toLowerCase())
        {
            let lengthDefinition = type.substring(foundType.length).trim();
            let regex = new RegExp('[\(](0|[1-9][0-9]*)[\)]');

            if (!regex.test(lengthDefinition))
            {
                return {columnType: {valid: false}};
            }
        }

        return null;
    }
}
