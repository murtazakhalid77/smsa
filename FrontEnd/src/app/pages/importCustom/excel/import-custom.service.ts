import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NumberData } from '../../import/importCustom/excel';
import { environment } from 'src/Environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ImportCustomService {
  _url = environment.backend;
  
  constructor(private http: HttpClient) { }
  
  getNumberData(data:any): Observable<NumberData[]> {
    return this.http.post<NumberData[]>(`${this._url}excel/NumbertData`,data);
  }

  postAllData(data: any): Observable<any> {
    return this.http.post(`${this._url}/excAllData`, data);
  }
}
