import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomComponent } from './custom.component';
import {CustomRoutingModule} from "./custom-routing.module";
import {MonacoEditorModule} from "ngx-monaco-editor";
import {FormsModule} from "@angular/forms";
import {CustomPipeModule} from "../../shared/modules/pipe/custom-pipe.module";
import {PageHeaderModule} from "../../shared/modules";

@NgModule({
    imports: [CommonModule, CustomRoutingModule, MonacoEditorModule, FormsModule, CustomPipeModule, PageHeaderModule],
  declarations: [CustomComponent]
})
export class CustomModule { }
