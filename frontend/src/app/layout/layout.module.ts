import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {NgbDropdownModule, NgbModalModule, NgbTooltipModule} from '@ng-bootstrap/ng-bootstrap';

import { LayoutRoutingModule } from './layout-routing.module';
import { LayoutComponent } from './layout.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { HeaderComponent } from './components/header/header.component';
import { CreateTableComponent } from './components/create-table/create-table.component';
import { ReactiveFormsModule } from "@angular/forms";
import {AlterTableComponent} from "./components/alter-table/alter-table.component";

@NgModule({
    imports: [
        CommonModule,
        LayoutRoutingModule,
        ReactiveFormsModule,
        NgbDropdownModule.forRoot(),
        NgbModalModule.forRoot(),
        NgbTooltipModule.forRoot()
    ],
    declarations: [LayoutComponent, SidebarComponent, HeaderComponent, CreateTableComponent, AlterTableComponent],
    entryComponents: [CreateTableComponent, AlterTableComponent]
})
export class LayoutModule {}
