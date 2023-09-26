import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from 'src/Environments/environment';
import { Customer, ICustomer } from '../pages/customer/customer.model';

export type EntityAllCustomersResponseType = HttpResponse<ICustomer[]>;
export type EntityCustomerResponseType = HttpResponse<ICustomer>;


@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  _url = environment.backend;

  constructor(private http: HttpClient) { }

  addCustomer(customer: Customer): Observable<EntityCustomerResponseType> {
    let url = `${this._url}/customer`
    // return this.http.post(url, customer)
    return this.http
      .post<ICustomer>(url, customer, { observe: 'response' })
  }

  // getOrders() {
  //   let url = `${this._url}/customer`;
  //   return this.http.get(url);
  // }

  getCustomers(params?: any): Observable<EntityAllCustomersResponseType> {
    let url = `${this._url}/customer`;
    return this.http.get<Customer[]>(`${url}`, {params, observe: 'response' });
    // .pipe(map((res: EntityUserResponse) => res));
  }

  getCustomerByAccountNumber(accountNumber?: any): Observable<EntityCustomerResponseType> {
    let url = `${this._url}/customer/${accountNumber}`
    return this.http.get<Customer>(`${url}`, { observe: 'response' });
  }

  deleteCustomer(accountNumber?: any): Observable<EntityCustomerResponseType> {
    let url = `${this._url}/customer/${accountNumber}`
    return this.http.delete<Customer>(`${url}`, { observe: 'response' });
  }
  updateCustomer(accountNumber: any, customer: Customer): Observable<EntityCustomerResponseType> {
    let url = `${this._url}/customer/${accountNumber}`;
    return this.http.patch<any>(url,customer, { observe: 'response' });
  }

  getOrderById(id: any) {
    let url = `${this._url}/customer/${id}`
    return this.http.get(url)
  }

  updateOrder(id: any, editOrder: any) {
    let url = `${this._url}/customer/${id}`
    return this.http.put(url, editOrder)
  }

  private update = new BehaviorSubject('')
  update$ = this.update.asObservable()
  getNewOrders(order: any) {
    this.update.next(order)
  }

  statusSorting(find: any) {
    let url = `${this._url}/customer?status_like=${find}`
    return this.http.get(url)
  }

  searchById(id: any) {
    let url = `${this._url}/customer?account_like=${id}`
    return this.http.get(url)
  }
}