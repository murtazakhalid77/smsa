import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/Environments/environment';
import { ISalesReport, SalesReport } from '../pages/model/sales-report.model.';

export type EntitySalesReportResponseType = HttpResponse<ISalesReport[]>;

@Injectable({
  providedIn: 'root'
})
export class SaleReportService {
    _url = environment.backend;

  constructor(private http: HttpClient) { }

  getSalesReport(searchSalesReport: any): Observable<EntitySalesReportResponseType> {
    let url = `${this._url}/sales-report`
    // return this.http.post(url, customer)
    return this.http
      .post<SalesReport[]>(url, searchSalesReport, { observe: 'response' })
  }


}
