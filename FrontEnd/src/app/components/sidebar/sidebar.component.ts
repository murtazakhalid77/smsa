import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionStorageService } from 'src/app/services/session-storage.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit{

  
  permissions1!:String[]

  constructor(private router: Router, public sessionStorageService:SessionStorageService) {
  
   }

  ngOnInit():void {
    
  }
  
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
    this.router.navigateByUrl('/currency/view');
  }
  region(){
    this.router.navigateByUrl('region/view')
  }
  salesReports(){
    this.router.navigateByUrl('reports')
  }
  permissions(){
    this.router.navigateByUrl('permissions')
  }
  sheetHistory(){
    this.router.navigateByUrl('sheetHistory')
  }
}