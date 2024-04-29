import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManifestDataComponent } from './manifest-data.component';

describe('ManifestDataComponent', () => {
  let component: ManifestDataComponent;
  let fixture: ComponentFixture<ManifestDataComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManifestDataComponent]
    });
    fixture = TestBed.createComponent(ManifestDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
