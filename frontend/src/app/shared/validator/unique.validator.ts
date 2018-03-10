import {AbstractControl, FormArray, ValidatorFn} from "@angular/forms";

export function uniqueFields(group: FormArray): ValidatorFn
{
    return (control: AbstractControl): {[key: string]: any} =>
    {
        for (let c1 of Object.keys(group.controls))
        {
            let control1 = group.get(c1);
            for (let c2 of Object.keys(group.controls))
            {
                if (c2 == c1) continue;

                let control2 = group.get(c2);

                if (control1.value == control2.value)
                {
                    return { nonUnique: true };
                    //control1.setErrors({nonUnique: true});
                    //return control2.setErrors({nonUnique: true});
                }
            }
        }
    }
}
