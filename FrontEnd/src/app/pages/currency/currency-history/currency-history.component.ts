import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { CurrencyService } from '../service/currency.service';
import { ICurrencyHistory } from '../../model/currency-history.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/Environments/environment';

@Component({
  selector: 'app-currency-history',
  templateUrl: './currency-history.component.html',
  styleUrls: ['./currency-history.component.css']
})
export class CurrencyHistoryComponent {

  id?: any;
  currencyHistory?: ICurrencyHistory[];
  currentPage:number  = 0;
  itemsPerPage: number = 10;
  totalItems?: string;
  _url = environment.backend;
  
  constructor(private router: Router, private loginService: LoginService, private route: ActivatedRoute, private currencyService: CurrencyService, private http: HttpClient){}

  ngOnInit(){
    this.route.queryParams.subscribe( params => {
      this.id = params['id'];
      if(this.id!=null){
        this.getCurrencyHistory(this.currentPage, this.itemsPerPage);
      }
    });
  }

  getCurrencyHistory(page?: any, size?: any){
    if(this.id!=null){
      this.currencyService.getCurrencyHistory(this.id, page, size).subscribe(res =>{
        if(res && res.body){
          this.currencyHistory = res.body;
          this.totalItems = res.headers.get('X-Total-Count') ?? '';
        }
       });
    }
  }

  changePage(value: any){
    this.getCurrencyHistory(value.pageIndex, this.itemsPerPage);
  }

  downloadFile() {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http
      .get(`${this._url}/excel/currency-audit/${this.id}`, {
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
