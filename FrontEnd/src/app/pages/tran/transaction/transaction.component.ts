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
  transaction: Itransaction[]=[];
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

  
  getAllTransaction(page?: any, size?: any, id?: any) {
    this.transactionServer.getTransaction(page, size, id).subscribe(
      (res) => {
        if (res && res.body) {
          this.transaction = res.body;
          this.totalItems = res.headers.get('X-Total-Count') ?? '';
  
          // Process the transactions
          this.transaction = this.transaction.map((item) => {
            if (item.mailSent === false) {
              console.error('Error loading transactions:');
            }
            return item;
          });
        }
      },
      (error) => {
        // Handle the error here, e.g., display an error message in the UI
      
        this.transaction = []; // Clear the transactions or handle it as needed
      }
    );
  }
  
 
  changePage(value: any){
    this.currentPage = value.currentPage;
    this.getAllTransaction(value.pageIndex, this.itemsPerPage,this.id);
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
        a.download = obj.accountNumber +'_sales_report.xlsx'; // Set the desired filename here
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
        a.download = obj.accountNumber +'_invoice.pdf'; // Set the desired filename here
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
      });
  }

  deleteData(accountNumber?: any, sheetId?: any) {
    debugger
    let url = `${this._url}/transaction/delete/invoiceDetails/${accountNumber}/${sheetId}`;
    return this.http.delete<void>(url).subscribe(
      () => {
        
          this.getAllTransaction(this.currentPage,this.itemsPerPage,this.id);
      },
      (error) => {
        // Handle errors here, if needed
        console.error("An error occurred:", error);
     
      }
    );

  }
  
  }
  
