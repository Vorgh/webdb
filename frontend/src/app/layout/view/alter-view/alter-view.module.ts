import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlterViewComponent } from './alter-view.component';
import {PageHeaderModule} from "../../../shared/modules";
import {ReactiveFormsModule} from "@angular/forms";
import {AlterViewRoutingModule} from "./alter-view-routing.module";
import {MonacoEditorModule} from "ngx-monaco-editor";

@NgModule({
    imports: [CommonModule, AlterViewRoutingModule, ReactiveFormsModule, PageHeaderModule, MonacoEditorModule],
    declarations: [AlterViewComponent]
})
export class AlterViewModule { }
