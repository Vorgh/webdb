import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DBHomeComponent } from './dbhome.component';

describe('DBHomeComponent', () => {
  let component: DBHomeComponent;
  let fixture: ComponentFixture<DBHomeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DBHomeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DBHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
