import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TableRoutingModule } from './table-routing.module';
import { TableComponent } from './table.component';
import { PageHeaderModule } from './../../shared';
import {CustomPipeModule} from "../../shared/modules/pipe/custom-pipe.module";
import {NgbModule, NgbTabsetModule} from "@ng-bootstrap/ng-bootstrap";

@NgModule({
    imports: [CommonModule, TableRoutingModule, PageHeaderModule, CustomPipeModule, NgbTabsetModule.forRoot()],
    declarations: [TableComponent]
})
export class TableModule {}
