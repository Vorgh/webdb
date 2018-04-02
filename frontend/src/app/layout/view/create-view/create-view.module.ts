import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreateViewComponent } from './create-view.component';
import {ReactiveFormsModule} from "@angular/forms";
import {PageHeaderModule} from "../../../shared/modules";
import {CreateViewRoutingModule} from "./create-view-routing.module";
import {MonacoEditorModule} from "ngx-monaco-editor";

@NgModule({
    imports: [CommonModule, CreateViewRoutingModule, ReactiveFormsModule, PageHeaderModule, MonacoEditorModule],
    declarations: [CreateViewComponent]
})
export class CreateViewModule { }
