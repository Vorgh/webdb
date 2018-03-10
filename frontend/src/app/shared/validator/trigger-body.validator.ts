import {AbstractControl, ValidatorFn} from "@angular/forms";

export function triggerBodyValidator(): ValidatorFn
{
    return (control: AbstractControl): {[key: string]: any} =>
    {
        let body = control.value.toUpperCase();

        if (body.startsWith("BEGIN") && (body.endsWith("END") || body.endsWith("END;")))
        {
            return null
        }

        return {triggerBody: {valid: false, message: "Trigger body must start with 'BEGIN' and end with 'END'"}};
    }
}
