import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CreateTableComponent } from "./create-table.component";

const routes: Routes = [
    {path: '', component: CreateTableComponent}
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class CreateTableRoutingModule {
}
