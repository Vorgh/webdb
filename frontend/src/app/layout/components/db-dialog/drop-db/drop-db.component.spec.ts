import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DropDbComponent } from './drop-db.component';

describe('DropDbComponent', () => {
  let component: DropDbComponent;
  let fixture: ComponentFixture<DropDbComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DropDbComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DropDbComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
