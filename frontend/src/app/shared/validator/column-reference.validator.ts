import {AbstractControl, ValidatorFn} from "@angular/forms";

export function columnReferenceValidator(): ValidatorFn
{
    return (control: AbstractControl): {[key: string]: any} =>
    {
        let reference = control.value;

        let regex = new RegExp('^[A-z]+[.][A-z]+[(][A-z]+[)]');
        if (!regex.test(reference))
        {
            return {columnReference: {valid: false}};
        }

        return null;
    }
}
