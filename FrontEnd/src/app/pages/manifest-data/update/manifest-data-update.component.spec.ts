import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManifestDataUpdateComponent } from './manifest-data-update.component';

describe('ManifestDataUpdateComponent', () => {
  let component: ManifestDataUpdateComponent;
  let fixture: ComponentFixture<ManifestDataUpdateComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManifestDataUpdateComponent]
    });
    fixture = TestBed.createComponent(ManifestDataUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
