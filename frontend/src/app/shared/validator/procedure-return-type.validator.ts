import {AbstractControl, ValidatorFn} from "@angular/forms";
import {isColumnTypeValid} from "./shared/utils";

export function procedureReturnTypeValidator(): ValidatorFn
{
    return (control: AbstractControl): {[key: string]: any} =>
    {
        if (control.value == '') return null;

        if (!isColumnTypeValid(control.value))
        {
            return {procedureReturnType: {valid: false, message: "Invalid return type."}};
        }

        return null;
    }
}
