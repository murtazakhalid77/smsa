import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardHeadComponent } from './dashboard-head.component';

describe('DashboardComponent', () => {
  let component: DashboardHeadComponent;
  let fixture: ComponentFixture<DashboardHeadComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DashboardHeadComponent]
    });
    fixture = TestBed.createComponent(DashboardHeadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
