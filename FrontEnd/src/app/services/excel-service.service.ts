import { Injectable } from '@angular/core';
import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';
import { ISalesReport } from '../pages/model/sales-report.model.';
import { environment } from 'src/Environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ExcelService {
  _url = environment.backend;


  constructor(private http: HttpClient) {

  }

  // download(): Observable<Blob> {
   
  // }
  
  
}
