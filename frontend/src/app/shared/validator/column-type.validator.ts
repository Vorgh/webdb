import {AbstractControl, ValidatorFn} from "@angular/forms";
import {isColumnTypeValid} from "./shared/utils";

export function columnTypeValidator(): ValidatorFn
{
    return (control: AbstractControl): {[key: string]: any} =>
    {
        if (!isColumnTypeValid(control.value))
        {
            return {columnType: {valid: false}};
        }

        return null;
    }
}
