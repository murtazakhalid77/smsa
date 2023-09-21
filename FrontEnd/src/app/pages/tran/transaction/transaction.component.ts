import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/Environments/environment';
import { Itransaction } from '../../model/transaction.model';
import { TransactionService } from './transaction.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class TransactionComponent {

  id?:any
  transaction?: Itransaction[];
  currentPage:number  = 0;
  itemsPerPage: number = 10;
  totalItems?: string;
  _url = environment.backend;



  constructor(private transactionServer: TransactionService,
              private router: Router,   private route: ActivatedRoute
              , private http: HttpClient){}

  ngOnInit(){
    this.route.queryParams.subscribe( params => {
      this.id = params['id'];
      if(this.id!=null){
        this.getAllTransaction(this.currentPage,this.itemsPerPage,this.id);
      }
    });
 
  }

  
  getAllTransaction(page?: any, size?: any,id?:any){
    this.transactionServer.getTransaction(page, size,id).subscribe(res =>{
      if(res && res.body){
        this.transaction = res.body;
        this.totalItems = res.headers.get('X-Total-Count') ?? '';
      }
    })
  }

  changePage(value: any){
    this.getAllTransaction(value.pageIndex, this.itemsPerPage);
  }

  downloadExcel(obj:any){
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http
      .get(`${this._url}/download/${obj.excelDownload}`, {
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
  downloadPdf(obj:any){
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http
      .get(`${this._url}/download/${obj.pdfDownload}`, {
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
  
