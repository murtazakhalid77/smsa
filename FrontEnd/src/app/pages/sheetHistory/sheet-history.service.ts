import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { IsheetHistory, sheetHistory } from '../model/sheetHistory.model';
import { environment } from 'src/Environments/environment';
import { Observable } from "rxjs";


export type EntitySheetHistoriesResponseType = HttpResponse<IsheetHistory[]>;
export type EntitySheetHistoryResponseType = HttpResponse<IsheetHistory>;

@Injectable({
  providedIn: 'root'
})
export class SheetHistoryService {


  constructor(private http:HttpClient) { }
  _url = environment.backend;

  
    getSheetHistory(page?: any, size?: any): Observable<EntitySheetHistoriesResponseType> {
        let url = `${this._url}/sheetHistory`;
        const params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString())
        return this.http.get<sheetHistory[]>(`${url}`, { params, observe: 'response' });
    }
    

    deleteData(page?: any, size?: any, accountNumber?: any,sheetId?:any) {
      let url = `${this._url}/sheetHistory/delete/invoiceDetails/${sheetId}`;
       this.http.delete<void>(`${url}`);  
    }
}
