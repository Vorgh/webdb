import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TableComponent } from "./table.component";

const routes: Routes = [
    {path: '', component: TableComponent},
    {path: 'create', loadChildren: './create-table/create-table.module#CreateTableModule'},
    {path: 'alter', loadChildren: './alter-table/alter-table.module#AlterTableModule'}
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TableRoutingModule {
}
