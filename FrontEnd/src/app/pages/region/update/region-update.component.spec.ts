import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegionUpdateComponent } from './region-update.component';

describe('RegionUpdateComponent', () => {
  let component: RegionUpdateComponent;
  let fixture: ComponentFixture<RegionUpdateComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegionUpdateComponent]
    });
    fixture = TestBed.createComponent(RegionUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
