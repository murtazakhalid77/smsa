import { Injectable, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { AuthGuard } from '../auth.guard';

@Injectable({
  providedIn: 'root'
})
export class SessionStorageService  {
  userPermissions: string[] = [];
  userRoles: string[] = [];

  constructor(private authGuardSerivce:AuthGuard) {

    const token = sessionStorage.getItem('jwtToken');
    const decodedToken =authGuardSerivce.getDecodedAccessToken(token!);
    if (decodedToken) {
    let  decodedTokenPermissions= decodedToken.PERMISSIONS;
    let decodedRoles=decodedToken.ROLES
    this.userPermissions=decodedTokenPermissions
    this.userRoles=decodedRoles;
    }
  }

  hasPermission(requiredPermission: string): boolean {
    return this.userPermissions.includes(requiredPermission);
  }

  hasRole(requiredRole: string): boolean {
    return this.userRoles.includes(requiredRole);
  }

  


  
  }

