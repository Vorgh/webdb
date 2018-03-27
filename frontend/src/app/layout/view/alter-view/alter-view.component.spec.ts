import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AlterViewComponent } from './alter-view.component';

describe('AlterViewComponent', () => {
  let component: AlterViewComponent;
  let fixture: ComponentFixture<AlterViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlterViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlterViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
