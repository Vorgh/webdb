import {Pipe, PipeTransform} from '@angular/core';

@Pipe({name: 'values'})
export class ValuesPipe implements PipeTransform
{
    transform(value): any
    {
        if (!value) return null;
        return Object.values(value);
    }
}
