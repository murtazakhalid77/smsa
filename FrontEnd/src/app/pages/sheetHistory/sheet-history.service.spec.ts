import { TestBed } from '@angular/core/testing';

import { SheetHistoryService } from './sheet-history.service';

describe('SheetHistoryService', () => {
  let service: SheetHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SheetHistoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
