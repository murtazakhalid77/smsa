import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthguardService {

  constructor() { }
  isAuthenticated(): boolean {
    // Example implementation, you can modify this based on your authentication mechanism
    const token = localStorage.getItem('token');
    return !!token; // Return true if token exists, false otherwise
  }
}
