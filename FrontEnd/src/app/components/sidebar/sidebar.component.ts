import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {

  constructor(private router: Router) { }
  dashboard() {
    this.router.navigateByUrl('/dashboard');
  }
  products() {
    this.router.navigateByUrl('/products');
  }
  customer() {
    this.router.navigateByUrl('/customer/view');
  }

  import() {
    this.router.navigateByUrl('/import');
  }
  customPort(){
    this.router.navigateByUrl('/custom/view');
  }
  user(){
    this.router.navigateByUrl('/user/view');
  }
  currency(){
    this.router.navigateByUrl('/currency');
  }
}