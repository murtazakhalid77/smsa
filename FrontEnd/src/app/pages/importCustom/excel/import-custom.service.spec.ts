import { TestBed } from '@angular/core/testing';

import { ImportCustomService } from './import-custom.service';

describe('ImportCustomService', () => {
  let service: ImportCustomService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImportCustomService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
