import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TableComponent } from "./table.component";

const routes: Routes = [
    {path: '', redirectTo: '/not-found', pathMatch: 'full'},
    {path: ':table', component: TableComponent}
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TableRoutingModule {
}
