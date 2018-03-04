import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateTriggerComponent } from './create-trigger.component';

describe('CreateTriggerComponent', () => {
  let component: CreateTriggerComponent;
  let fixture: ComponentFixture<CreateTriggerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateTriggerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateTriggerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
