import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/Environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UploadExcelService {
  _url = environment.backend;


  constructor(private http: HttpClient) {

  }

  public uploadFile(file:File, excelImportDto: any){
      const formData: FormData = new FormData();
      debugger
      formData.append('file', file);
      formData.append('excelImport', JSON.stringify(excelImportDto));
      return this.http.post<any>(`${this._url}/excel/upload`, formData);
  }

   }

