import { Injectable } from '@angular/core';
import { environment } from 'src/Environments/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Custom } from '../../custom-port/custom.model';
import { ICurrencyDto, Currency } from '../../model/Currency.model';

export type EntityAllCurrencyResponseType = HttpResponse<ICurrencyDto[]>;
export type EntityCurrencyResponseType = HttpResponse<ICurrencyDto>;

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {
  _url = environment.backend;

  constructor(private http: HttpClient) { }

  updateCurrency(currency: Currency) {
    let url = `${this._url}/currency/${currency.id}`
    // return this.http.post(url, customer)
    return this.http
      .patch<ICurrencyDto>(url, currency, { observe: 'response' })
  }
  getCurrencyById(id: any): Observable<EntityCurrencyResponseType> {
    let url = `${this._url}/currency/${id}`;
    return this.http.get<Custom>(`${url}`, { observe: 'response' });
    // .pipe(map((res: EntityUserResponse) => res));
  }

  addCurrency(currency: Currency): Observable<EntityCurrencyResponseType> {
    let url = `${this._url}/currency`
    // return this.http.post(url, customer)
    return this.http
      .post<Currency>(url, currency, { observe: 'response' })
  }

  
  getCurrency(): Observable<EntityAllCurrencyResponseType> {
    let url = `${this._url}/currency`;
    return this.http.get<Custom[]>(`${url}`, { observe: 'response' });
    // .pipe(map((res: EntityUserResponse) => res));
  }

  getCustomsPresent(): Observable<EntityAllCurrencyResponseType> {
    let url = `${this._url}/currency`;
    return this.http.get<Currency[]>(`${url}`, { observe: 'response' });
    // .pipe(map((res: EntityUserResponse) => res));
  }

  getDistinctCurrencies(): Observable<any> {
    let url = `${this._url}/currency-distinct`;
    return this.http.get<any>(`${url}`, { observe: 'response' });
  }

}
