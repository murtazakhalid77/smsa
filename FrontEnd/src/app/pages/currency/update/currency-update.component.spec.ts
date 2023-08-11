import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrencyUpdateComponent } from './currency-update.component';

describe('CurrencyUpdateComponent', () => {
  let component: CurrencyUpdateComponent;
  let fixture: ComponentFixture<CurrencyUpdateComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CurrencyUpdateComponent]
    });
    fixture = TestBed.createComponent(CurrencyUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
