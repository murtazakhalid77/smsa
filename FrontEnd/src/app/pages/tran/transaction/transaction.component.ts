import { HttpClient, HttpParams } from '@angular/common/http';
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


  constructor(private transactionServer: TransactionService,
              private router: Router,   private route: ActivatedRoute,){}

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
  
  }
  
