import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { CustomService, EntityAllCustomsResponseType } from '../service/custom-port.service';
import { ICustom } from '../custom.model';
import {DashboardHeadComponent} from 'src/app/components/dashboard-head/dashboard-head.component'
@Component({
  selector: 'app-custom-port',
  templateUrl: './custom-port.component.html',
  styleUrls: ['./custom-port.component.css']
})
export class CustomPortComponent implements OnInit {

  customs?: ICustom[];
  currentPage:number  = 0;
  itemsPerPage: number = 10;
  totalItems?: string;



  constructor(private router: Router, private loginService: LoginService, private customService: CustomService) { }

  ngOnInit(): void {
    debugger
    this.getCustoms(this.currentPage, this.itemsPerPage);
  }

  getCustoms(page?: any, size?: any) {
    this.customService.getCustoms(page, size).subscribe(
      (res: EntityAllCustomsResponseType) => {
        if(res && res.body){
          this.customs = res.body;
          this.totalItems = res.headers.get('X-Total-Count') ?? '';
        } 
      },
      (error) => {
        // Handle the error if needed
        console.error('Error fetching customers:', error);
      }
    );
  }

  updateCustom(id: any){
    this.router.navigate(['/custom'], { queryParams: { id: id } });

  }

  addCustom(){
    this.router.navigateByUrl('/custom')
  }

  changePage(value: any){
    this.getCustoms(value.pageIndex, this.itemsPerPage);
  }

}
