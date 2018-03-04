import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import {PageHeaderModule} from "../../../shared/modules";
import {CreateTableComponent} from "./create-table.component";
import {CreateTableRoutingModule} from "./create-table-routing.module";
import {ReactiveFormsModule} from "@angular/forms";

@NgModule({
    imports: [CommonModule, ReactiveFormsModule, CreateTableRoutingModule, PageHeaderModule, NgbModule],
    declarations: [CreateTableComponent]
})
export class CreateTableModule {}
