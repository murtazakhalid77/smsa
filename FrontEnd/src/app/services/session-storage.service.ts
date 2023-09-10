import { Injectable, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { AuthGuard } from '../auth.guard';

@Injectable({
  providedIn: 'root'
})
export class SessionStorageService  {
  userPermissions: string[] = [];

  constructor(private authGuardSerivce:AuthGuard) {

    const token = sessionStorage.getItem('jwtToken');
    const decodedToken =authGuardSerivce.getDecodedAccessToken(token!);
    if (decodedToken) {
    let  decodedTokenPermissions= decodedToken.PERMISSIONS;
    this.userPermissions=decodedTokenPermissions
    }
  }

  hasPermission(requiredPermission: string): boolean {
    return this.userPermissions.includes(requiredPermission);
  }

  


  
  }

