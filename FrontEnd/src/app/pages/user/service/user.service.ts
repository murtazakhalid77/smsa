export type EntityUsersResponseType = HttpResponse<IUser[]>;

import { HttpClient, HttpParams, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/Environments/environment";
import { IUser, User } from "../../model/user.model";
import { Observable } from "rxjs";

export type EntityAllUserResponseType = HttpResponse<IUser[]>;
export type EntityUserResponseType = HttpResponse<IUser>;

@Injectable({
    providedIn: 'root'
  })
  export class UserService {
  
    _url = environment.backend;
  
    constructor(private http: HttpClient) { }

    getUsers(params?: any): Observable<EntityUsersResponseType> {
        let url = `${this._url}/user`;
        return this.http.get<User[]>(`${url}`, { params, observe: 'response' });
    }

    addUser(user: User): Observable<EntityUserResponseType> {
      let url = `${this._url}/user`
      // return this.http.post(url, customer)
      return this.http
        .post<IUser>(url, user, { observe: 'response' })
    }

    getUserById(id: any): Observable<EntityUserResponseType> {
      let url = `${this._url}/user/${id}`;
      return this.http.get<any>(`${url}`, { observe: 'response' });
      // .pipe(map((res: EntityUserResponse) => res));
    }

    updateUser(user: User) {
      let url = `${this._url}/user/${user.id}`
      // return this.http.post(url, customer)
      return this.http
        .patch<IUser>(url, user, { observe: 'response' })
    }

    
}