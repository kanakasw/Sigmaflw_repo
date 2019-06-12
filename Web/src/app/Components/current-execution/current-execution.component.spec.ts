import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentExecutionComponent } from './current-execution.component';

describe('CurrentExecutionComponent', () => {
  let component: CurrentExecutionComponent;
  let fixture: ComponentFixture<CurrentExecutionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CurrentExecutionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrentExecutionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
