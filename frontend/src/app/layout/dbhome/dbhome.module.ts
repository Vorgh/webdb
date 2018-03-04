import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DBHomeRoutingModule } from './dbhome-routing.module';
import { DBHomeComponent } from './dbhome.component';
import { PageHeaderModule } from './../../shared';
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";

@NgModule({
    imports: [CommonModule, DBHomeRoutingModule, PageHeaderModule, NgbModule],
    declarations: [DBHomeComponent]
})
export class DBHomeModule {}
