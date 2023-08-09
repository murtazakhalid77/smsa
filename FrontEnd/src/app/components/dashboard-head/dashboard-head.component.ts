import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard-head',
  templateUrl: './dashboard-head.component.html',
  styleUrls: ['./dashboard-head.component.css']
})
export class DashboardHeadComponent {

  constructor(private router:Router){}

  logout(){
    sessionStorage.removeItem('jwtToken');
    this.router.navigateByUrl('/login')
  }
}
