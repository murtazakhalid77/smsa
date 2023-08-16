import { environment } from "src/Environments/environment";
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
@Injectable({
    providedIn: 'root'
  })
  export class FileService {
    _url = environment.backend;


    constructor(private http: HttpClient) {

    }

    download(): Observable<Blob> {
        return this.http.get(`${this._url}/sample-file`, {
          responseType: 'blob'
        });
      }
}
