import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { CustomService } from '../../custom-port/service/custom-port.service';
import { CurrencyService, EntityAllCurrencyResponseType } from '../service/currency.service';
import { ICurrencyDto } from '../../model/Currency.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/Environments/environment';

@Component({
  selector: 'app-currency-list',
  templateUrl: './currency-list.component.html',
  styleUrls: ['./currency-list.component.css']
})
export class CurrencyListComponent {

  currency?: ICurrencyDto[];
  currentPage:number  = 0;
  itemsPerPage: number = 10;
  totalItems?: string;
  searchText?: string
  _url = environment.backend;

  constructor(private router: Router, private loginService: LoginService, private currencyService: CurrencyService,  private http: HttpClient){}

  ngOnInit(){
    this.getCurrency(this.currentPage, this.itemsPerPage);
  }

  getCurrency(page?: any, size?: any){

    let search = {};
  
    const pageable = {
      page: page,
      size: size,
    };
  
    if (this.searchText !== undefined && this.searchText !== '') {
      search = {
        mapper: 'CURRENCY',
        searchText: this.searchText,
      };
    }

        // Construct the query parameter for pageable
        const queryParams = {
          page: page,
          size: size,
          search: JSON.stringify(search),
        };

    this.currencyService.getCurrency(queryParams).subscribe(
      (res: EntityAllCurrencyResponseType) => {
        if(res && res.body){
          debugger;
          this.currency = res.body;
          this.totalItems = res.headers.get('X-Total-Count') ?? '';
        } 
      },
      (error) => {
        // Handle the error if needed
        console.error('Error fetching customers:', error);
      }
    );
  }

  addCurrency(){
    this.router.navigateByUrl('/currency')
  }

  
  updateCurrency(id: any){
    this.router.navigate(['/currency'], { queryParams: { id: id } });
  }

  showHistory(id: any){
    this.router.navigate(['/currency/history'], { queryParams: { id: id } });
  }

  changePage(value: any){
    debugger;    
    this.getCurrency(value.pageIndex, this.itemsPerPage);
  }


  downloadFile() {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http
      .get(`${this._url}/excel/currency`, {
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
