import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomPortComponent } from './custom-port.component';

describe('CustomPortComponent', () => {
  let component: CustomPortComponent;
  let fixture: ComponentFixture<CustomPortComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CustomPortComponent]
    });
    fixture = TestBed.createComponent(CustomPortComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
