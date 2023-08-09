import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from 'src/Environments/environment';

interface LoginResponse {
  token: string;
  // Add other properties if needed
}

@Injectable({
  providedIn: 'root'
})

export class LoginService {
  [x: string]: any;
  _url = environment.backend;
  loader = new BehaviorSubject<boolean>(false);


  constructor(private http: HttpClient) { }

  loginAuthenticated(loginData:any) {
   
    return this.http.post(`${this._url}/login`, loginData);
  }
}
