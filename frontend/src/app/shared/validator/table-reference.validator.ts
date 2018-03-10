import {AbstractControl, ValidatorFn} from "@angular/forms";
import {Utils} from "../util/utils";

export function tableReferenceValidator(): ValidatorFn
{
    return (control: AbstractControl): {[key: string]: any} =>
    {
        let reference = control.value;

        let regex = new RegExp('^[A-z]+[.][A-z]+');
        if (!regex.test(reference))
        {
            return {tableReference: {valid: false}};
        }

        return null;
    }
}
