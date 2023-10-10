import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { CustomService, EntityAllCustomsResponseType } from '../service/custom-port.service';
import { ICustom } from '../custom.model';
import {DashboardHeadComponent} from 'src/app/components/dashboard-head/dashboard-head.component'
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/Environments/environment';
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
  searchText?: string;
  _url = environment.backend;


  constructor(private router: Router, private loginService: LoginService, private customService: CustomService, private http: HttpClient) { }

  ngOnInit(): void {
    this.getCustoms(this.currentPage, this.itemsPerPage);
  }

  getCustoms(page?: any, size?: any) {


    let search = {};
  
    const pageable = {
      page: page,
      size: size,
    };
  
    if (this.searchText !== undefined && this.searchText !== '') {
      search = {
        mapper: 'CUSTOM_PORT',
        searchText: this.searchText,
      };
    }

        // Construct the query parameter for pageable
        const queryParams = {
          page: page,
          size: size,
          search: JSON.stringify(search),
        };


    this.customService.getCustoms(queryParams).subscribe(
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

  downloadFile() {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http
      .get(`${this._url}/excel/custom`, {
        responseType: 'blob', // Important: responseType should be 'blob'
        headers,
      })
      .subscribe((response: any) => {
        const blob = new Blob([response], { type: 'application/octet-stream' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.download = 'sales_report.xlsx'; // Set the desired filename here
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
      });
  }

}
