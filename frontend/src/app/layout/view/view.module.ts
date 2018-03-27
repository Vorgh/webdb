import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ViewComponent } from './view.component';
import {PageHeaderModule} from "../../shared";
import {ReactiveFormsModule} from "@angular/forms";
import {CustomPipeModule} from "../../shared/modules/pipe/custom-pipe.module";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {ViewRoutingModule} from "./view-routing.module";

@NgModule({
    imports: [CommonModule, ViewRoutingModule, PageHeaderModule, CustomPipeModule, NgbModule, ReactiveFormsModule],
    declarations: [ViewComponent]
})
export class ViewModule { }
