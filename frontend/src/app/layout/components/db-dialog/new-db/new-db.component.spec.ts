import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewDbComponent } from './new-db.component';

describe('NewDbComponent', () => {
  let component: NewDbComponent;
  let fixture: ComponentFixture<NewDbComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewDbComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewDbComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
