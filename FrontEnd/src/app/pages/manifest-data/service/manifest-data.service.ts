import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { IManifestData, ManifestData } from "../../model/manifest-data.model";
import { environment } from 'src/Environments/environment';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export type EntityAllManifestDataResponseType = HttpResponse<IManifestData[]>;
export type EntityManifestDataResponseType = HttpResponse<IManifestData>;


@Injectable({
    providedIn: 'root'
})
export class ManifestDataService {
    _url = environment.backend;

    constructor(private http: HttpClient){}

    getManifestData(searchManifestData: any): Observable<EntityAllManifestDataResponseType> {
        let url = `${this._url}/manifest-data`
        return this.http
        .post<ManifestData[]>(url, searchManifestData, { observe: 'response'})
    }
    getManifestDataById(id: any): Observable<EntityManifestDataResponseType> {
        let url = `${this._url}/manifest-data/${id}`;
        return this.http.get<ManifestData>(`${url}`, { observe: 'response' });
        // .pipe(map((res: EntityUserResponse) => res));
    }
    updateManifestData(manifestData: ManifestData) {
        let url = `${this._url}/manifest-data/${manifestData.id}`
        // return this.http.post(url, customer)
        return this.http
          .patch<IManifestData>(url, manifestData, { observe: 'response' })
      }
}