import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import {AlterTableComponent} from "./alter-table.component";
import {PageHeaderModule} from "../../../shared/modules";
import {AlterTableRoutingModule} from "./alter-table-routing.module";
import {ReactiveFormsModule} from "@angular/forms";

@NgModule({
    imports: [CommonModule, ReactiveFormsModule, AlterTableRoutingModule, PageHeaderModule, NgbModule],
    declarations: [AlterTableComponent]
})
export class AlterTableModule {}
