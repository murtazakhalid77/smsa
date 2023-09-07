import { HttpClient, HttpParams, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/Environments/environment";
import { IRegion, Region } from "../../model/region.model";
import { Observable } from "rxjs";

export type EntityRegionsResponseType = HttpResponse<IRegion[]>;
export type EntityRegionResponseType = HttpResponse<IRegion>;

@Injectable({
    providedIn: 'root'
  })
  export class RegionService {
  
    _url = environment.backend;
  
    constructor(private http: HttpClient) { }

    getRegions(page?: any, size?: any): Observable<EntityRegionsResponseType> {
        let url = `${this._url}/region`;
        const params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString());
        return this.http.get<Region[]>(`${url}`, { params, observe: 'response' });
    }

    getRegionById(id?: any): Observable<EntityRegionResponseType> {
        let url = `${this._url}/region/${id}`;
        return this.http.get<Region>(`${url}`, { observe: 'response' });
    }

    
    createRegion(region: Region): Observable<EntityRegionResponseType> {
    let url = `${this._url}/region`
    return this.http
      .post<IRegion>(url, region, { observe: 'response' })
    }

    updateRegion(id:any, region: Region): Observable<EntityRegionResponseType> {
        let url = `${this._url}/region/${id}`
        return this.http
          .put<IRegion>(url, region, { observe: 'response' })
    }
  
}