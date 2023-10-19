
import { IsheetHistory, sheetHistory } from '../../model/sheetHistory.model';
import { Router } from '@angular/router';
import { SheetHistoryService } from '../sheet-history.service';
import { Component } from '@angular/core';
import { FileService } from 'src/app/services/file-service.service';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/Environments/environment';

@Component({
  selector: 'app-sheet-history-list',
  templateUrl: './sheet-history-list.component.html',
  styleUrls: ['./sheet-history-list.component.css']
})
export class SheetHistoryListComponent {

  
  sheetHistory?: IsheetHistory[]=[];
  currentPage:number  = 0;
  itemsPerPage: number = 10;
  totalItems?: string;
  _url = environment.backend;

  constructor(private sheetHistoryService: SheetHistoryService,
              private router: Router,
              private fileService: FileService,private http: HttpClient){}

  ngOnInit(){
    this.getAllSSheetHistory(this.currentPage, this.itemsPerPage);
  }

  
  getAllSSheetHistory(page?: any, size?: any){
    this.sheetHistoryService.getSheetHistory(page, size).subscribe(res =>{
      if(res && res.body){
        this.sheetHistory = res.body;
        this.totalItems = res.headers.get('X-Total-Count') ?? '';
      }
    })
  }

  changePage(value: any){
    this.getAllSSheetHistory(value.pageIndex, this.itemsPerPage);
  }
  
  gotoTransaction(id: any){
    this.router.navigate(['/transactions'], { queryParams: { id: id } });

  }
  

  downloadExcel(saleReport: any){
    this.fileService.downloadExcel(saleReport, 'sheet.xlsx');
  }

  deleteData(sheetId?: any) {
    let url = `${this._url}/delete/invoiceDetails/${sheetId}`;
    debugger
    return this.http.delete(url).subscribe(
      (success) => {  
          this.getAllSSheetHistory(this.currentPage,this.itemsPerPage);
      },
      (error) => {
        // Handle errors here, if needed
        console.error("An error occurred:", error);
     
      }
    );

  }
  
}
