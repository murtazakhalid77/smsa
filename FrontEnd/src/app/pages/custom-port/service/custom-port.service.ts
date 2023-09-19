import { Injectable } from "@angular/core";
import { environment } from "src/Environments/environment";
import { Custom, ICustom } from "../custom.model";
import { HttpClient, HttpParams, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";

export type EntityAllCustomsResponseType = HttpResponse<ICustom[]>;
export type EntityCustomResponseType = HttpResponse<ICustom>;
// export type EntityCustomerResponseType = HttpResponse<ICustomer>;


@Injectable({
  providedIn: 'root'
})
export class CustomService {

  _url = environment.backend;

  constructor(private http: HttpClient) { }

  addCustom(custom: Custom): Observable<EntityCustomResponseType> {
    let url = `${this._url}/custom`
    // return this.http.post(url, customer)
    return this.http
      .post<ICustom>(url, custom, { observe: 'response' })
  }

  updateCustom(custom: Custom) {
    let url = `${this._url}/custom/${custom.id}`
    // return this.http.post(url, customer)
    return this.http
      .patch<ICustom>(url, custom, { observe: 'response' })
  }

  getCustoms(params?: any): Observable<EntityAllCustomsResponseType> {
    let url = `${this._url}/custom`;
    return this.http.get<Custom[]>(`${url}`, { params, observe: 'response' });
    // .pipe(map((res: EntityUserResponse) => res));
  }

  getCustomsPresent(): Observable<EntityAllCustomsResponseType> {
    let url = `${this._url}/custom-import`;
    return this.http.get<Custom[]>(`${url}`, { observe: 'response' });
    // .pipe(map((res: EntityUserResponse) => res));
  }

  getCustomById(id: any): Observable<EntityCustomResponseType> {
    let url = `${this._url}/custom/${id}`;
    return this.http.get<Custom>(`${url}`, { observe: 'response' });
    // .pipe(map((res: EntityUserResponse) => res));
  }

}