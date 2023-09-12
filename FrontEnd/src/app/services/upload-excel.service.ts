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

  async uploadFile(file: File, excelImportDto: any): Promise<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('excelImport', JSON.stringify(excelImportDto));

    try {
      const response = await this.http
        .post<any>(`${this._url}/excel/upload`, formData)
        .toPromise();

      // Process the response data as needed
      return response;
    } catch (error) {
      // Handle HTTP or other errors here
      console.error('Error:', error);
      throw error;
    }
  }

   }

