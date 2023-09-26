// import { CanActivateFn } from '@angular/router';

// export const authGuard: CanActivateFn = (route, state) => {
//   return true;
// };


import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthguardService } from './services/authguard.service';
import jwt_decode from 'jwt-decode';
import { UserService } from './pages/user/service/user.service';



@Injectable({
  providedIn: 'root'
})
  export class    AuthGuard implements CanActivate {
    
  

    constructor(private authService: AuthguardService, private router: Router,userService:UserService) { }

    canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      const jwtToken = sessionStorage.getItem('jwtToken');
      if (jwtToken) {
        const decodedToken = this.getDecodedAccessToken(jwtToken);
        const userPermissions = decodedToken.PERMISSIONS;
        const userRoles = decodedToken.ROLES;
        
        const url = state.url;
        let permission: any = {};
        const permissionsObj = this.getPermissionsObj();
        const matchingPermission = permissionsObj.find((p:any) => p.url.some((u:any) => this.urlMatches(u, url)));
        
        if (matchingPermission) {
          permission = matchingPermission;
        }
        
        if (userPermissions.includes(permission.permissions)) {
          return true;
        } else {
          this.router.navigate(['/unauthorized']); // Redirect to an unauthorized page or handle it as needed
          return false;
        }
      } else {
        this.router.navigate(['/login']);
        return false;
      }
    }
    
    getDecodedAccessToken(token: string): any {
      try {
        return jwt_decode(token);
      } catch(Error) {
        console.error('Error decoding JWT token:'+Error);
      }
}
getPermissionsObj():any{

  const customerObj={
    url:['/customer/view','/customer',],
    permissions:'CUSTOMER_MANAGEMENT'
   }
   const userObj={
    url:['/user/view','/user'],
    permissions:'USER_MANAGEMENT'
   }
   const currencyObj={
    url:['/currency/view','/currency','/currency/history'],
    permissions:'CURRENCY_MANAGEMENT'
   }
   const customObj={  
    url:['/custom/view','/custom'],
    permissions:'CUSTOM_PORT_MANAGEMENT'
   }
   const regionObj={
    url:['/region/view','/region'],
    permissions:'REGIONS_MANAGEMENT'
   }
   const importObj={
    url:['/import'],
    permissions:'IMPORT_EXCEL_MANAGEMENT'
   }
   const salesReportObj={
    url:['/reports'],
    permissions:'SALES_REPORT_MANAGEMENT'
   }
   const permissionsObj={
    url:['/permissions'],
    permissions:'PERMISSIONS_MANAGEMENT'
   }
   const dashboardObj={
    url:['/dashboard'],
    permissions:'DASHBOARD_MANAGEMENT'
   }
   const sheetHistoryObj={
    url:['/sheetHistory '],
    permissions:'SHEET_HISTORY_REPOSITORY'
   }
   
   
   return  [customerObj,userObj,currencyObj,customObj,regionObj,importObj,salesReportObj,permissionsObj,dashboardObj,sheetHistoryObj]
}
private urlMatches(pattern: string, url: string): boolean {
  const patternSegments = pattern.split('?')[0].split('/'); // Get URL segments without query parameters
  const urlSegments = url.split('?')[0].split('/'); // Get URL segments without query parameters
  
  if (patternSegments.length !== urlSegments.length) {
    return false; // URLs have different segment counts
  }
  
  for (let i = 0; i < patternSegments.length; i++) {
    if (patternSegments[i] !== urlSegments[i]) {
      return false; // Segments don't match
    }
  }
  
  return true; // All segments match
}
  }
