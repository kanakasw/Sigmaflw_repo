import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExecutionStepOutcomePopupDialogComponent } from './execution-step-outcome-popup-dialog.component';

describe('ExecutionStepOutcomePopupDialogComponent', () => {
  let component: ExecutionStepOutcomePopupDialogComponent;
  let fixture: ComponentFixture<ExecutionStepOutcomePopupDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExecutionStepOutcomePopupDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExecutionStepOutcomePopupDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
