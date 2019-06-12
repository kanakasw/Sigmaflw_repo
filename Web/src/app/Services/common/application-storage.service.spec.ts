import { TestBed } from '@angular/core/testing';

import { ApplicationStorageService } from './application-storage.service';

describe('ApplicationStorageService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ApplicationStorageService = TestBed.get(ApplicationStorageService);
    expect(service).toBeTruthy();
  });
});
