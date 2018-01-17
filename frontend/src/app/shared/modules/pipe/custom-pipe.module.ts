import {NgModule} from '@angular/core';
import {CommonModule} from "@angular/common";
import {KeysPipe} from "./keys.pipe";
import {ValuesPipe} from "./values.pipe";

@NgModule({
    declarations: [KeysPipe, ValuesPipe],
    imports: [CommonModule],
    exports: [KeysPipe, ValuesPipe]
})

export class CustomPipeModule
{
}
