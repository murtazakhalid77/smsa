import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SheetHistoryListComponent } from './sheet-history-list.component';

describe('SheetHistoryListComponent', () => {
  let component: SheetHistoryListComponent;
  let fixture: ComponentFixture<SheetHistoryListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SheetHistoryListComponent]
    });
    fixture = TestBed.createComponent(SheetHistoryListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
