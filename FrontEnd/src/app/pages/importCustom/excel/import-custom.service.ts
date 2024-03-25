import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NumberData } from '../../import/importCustom/excel';

@Injectable({
  providedIn: 'root'
})
export class ImportCustomService {
  private apiUrl = 'http://localhost:3000'; 
  constructor(private http: HttpClient) { }
  
  getNumberData(): Observable<NumberData[]> {
    return this.http.get<NumberData[]>(`${this.apiUrl}/NumbertData`);
  }

  postAllData(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/AllData`, data);
  }
}
