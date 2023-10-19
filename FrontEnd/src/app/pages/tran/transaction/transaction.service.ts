import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/Environments/environment';
import { Observable } from "rxjs";
import { Itransaction, Transaction } from '../../model/transaction.model';

export type EntityTransactionsResponseType = HttpResponse<Itransaction[]>;
export type EntityTransactionResponseType = HttpResponse<Itransaction>;
@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  constructor(private http:HttpClient) { }
  _url = environment.backend;
  pageIndex: any;


  getTransaction(page?: any, size?: any, id?: any): Observable<EntityTransactionsResponseType> {
    let url = `${this._url}/transaction`;
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    // Check if the 'id' parameter is provided and add it to the URL if it exists
    if (id !== undefined) {
      url += `/${id}`;
    }

    return this.http.get<Transaction[]>(url, { params, observe: 'response' });
  }

  downloadFiles( params:any){
    let url = `${this._url}/download/${ params}`;
    return this.http.get(`${url}`, { params, observe: 'response' });
  }

  deleteData(page?: any, size?: any, accountNumber?: any,sheetId?:any) {
    let url = `${this._url}/transaction/delete/invoiceDetails/${accountNumber}/${sheetId}`;
     this.http.delete<Transaction[]>(`${url}`);
  }
}
