import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/Environments/environment';
import { IPermission, Permission } from '../model/permissionModel';


export type EntityPermissionsResponseType = HttpResponse<IPermission[]>;
export type EntityPermissionResponseType = HttpResponse<IPermission>;

@Injectable({
  providedIn: 'root'
})
export class PermissionService {

  _url = environment.backend;

  private userPermissions: string[] = [];

  constructor(private http: HttpClient) { }

  getPermissions(): Observable<EntityPermissionsResponseType> {
    let url = `${this._url}/permission`;
    return this.http.get<Permission[]>(`${url}`, { observe: 'response' });
  }



updatePermissionOfRoles(role: any): Observable<any> {
    let url = `${this._url}/role`
    return this.http
      .post(url, role, { observe: 'response' })
  }

  getPermissionOfRoles(id: any): any {
    const url = `${this._url}/role/${id}`;
    return this.http.get(url);
  }


  getPermissionsByUsername(username: string): Observable<string[]> {
    const url = `${this._url}/permissions?username=${username}`;
    return this.http.get<string[]>(url);
  }
}
