import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LayoutComponent } from './layout.component';

const routes: Routes = [
    {
        path: '',
        component: LayoutComponent,
        children: [
            { path: '', redirectTo: 'home' },
            { path: 'home', loadChildren: './home/home.module#HomeModule' },
            { path: 'db', loadChildren: './dbhome/dbhome.module#DBHomeModule'},
            { path: 'table', loadChildren: './table/table.module#TableModule'},
            { path: 'view', loadChildren: './view/view.module#ViewModule'},
            { path: 'trigger', loadChildren: './trigger/trigger.module#TriggerModule'},
            { path: 'procedure', loadChildren: './procedure/procedure.module#ProcedureModule'},
            { path: 'custom', loadChildren: './custom/custom.module#CustomModule'}
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class LayoutRoutingModule {}
