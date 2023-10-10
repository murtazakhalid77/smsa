
import { IsheetHistory } from '../../model/sheetHistory.model';
import { Router } from '@angular/router';
import { SheetHistoryService } from '../sheet-history.service';
import { Component } from '@angular/core';
import { FileService } from 'src/app/services/file-service.service';

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


  constructor(private sheetHistoryService: SheetHistoryService,
              private router: Router,
              private fileService: FileService){}

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
}
