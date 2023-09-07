import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/Environments/environment';
import { IRole, Role } from '../model/roles.model';


export type EntityRolesResponseType = HttpResponse<IRole[]>;
export type EntityRoleResponseType = HttpResponse<IRole>;

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  _url = environment.backend;
  constructor(private http: HttpClient) { }

  getRoles(): Observable<EntityRolesResponseType> {
    let url = `${this._url}/roles`;
    return this.http.get<Role[]>(`${url}`, { observe: 'response' });
}

}
