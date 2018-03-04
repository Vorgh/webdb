import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {CreateTriggerComponent} from "./create-trigger.component";

const routes: Routes = [
    {path: '', component: CreateTriggerComponent}
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class CreateTriggerRoutingModule {
}
