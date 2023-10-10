import { environment } from "src/Environments/environment";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
@Injectable({
    providedIn: 'root'
  })
  export class FileService {
    _url = environment.backend;


    constructor(private http: HttpClient) {

    }

    download(): Observable<Blob> {
        return this.http.get(`${this._url}/sample-file`, {
          responseType: 'blob'
        });
      }

      downloadExcel(obj:any, name: any){
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
            a.download = obj.accountNumber ? obj.accountNumber + name : name; // Set the desired filename here
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
            a.download = obj.accountNumber ? obj.accountNumber + '_invoice.pdf' : '_invoice.pdf';// Set the desired filename here
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
          });
      }

}
