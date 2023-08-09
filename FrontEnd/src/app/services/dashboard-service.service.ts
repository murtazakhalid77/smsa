import { Injectable } from '@angular/core';
import { environment } from '../../Environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DashboardServiceService {

  _url = environment.jsonUrl;

  constructor(private http: HttpClient) { }

  getcards() {
    let url =`${this._url}/dashboard-cards`
    return this.http.get(url);
  }
}
