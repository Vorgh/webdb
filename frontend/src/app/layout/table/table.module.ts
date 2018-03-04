import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TableRoutingModule } from './table-routing.module';
import { TableComponent } from './table.component';
import { PageHeaderModule } from './../../shared';
import { CustomPipeModule } from "../../shared/modules/pipe/custom-pipe.module";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import {ReactiveFormsModule} from "@angular/forms";

@NgModule({
    imports: [CommonModule, TableRoutingModule, PageHeaderModule, CustomPipeModule, NgbModule, ReactiveFormsModule],
    declarations: [TableComponent]
})
export class TableModule {}
