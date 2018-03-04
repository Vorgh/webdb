import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AlterTableComponent } from './alter-table.component';

describe('CreateTableComponent', () => {
  let component: AlterTableComponent;
  let fixture: ComponentFixture<AlterTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlterTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlterTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
