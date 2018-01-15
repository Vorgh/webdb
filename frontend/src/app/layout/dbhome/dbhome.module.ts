import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DBHomeRoutingModule } from './dbhome-routing.module';
import { DBHomeComponent } from './dbhome.component';
import { PageHeaderModule } from './../../shared';

@NgModule({
    imports: [CommonModule, DBHomeRoutingModule, PageHeaderModule],
    declarations: [DBHomeComponent]
})
export class DBHomeModule {}
