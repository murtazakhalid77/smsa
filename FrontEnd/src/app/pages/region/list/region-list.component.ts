import { Component } from '@angular/core';
import { IRegion } from '../../model/region.model';
import { RegionService } from '../service/region.service';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/Environments/environment';

@Component({
  selector: 'app-region-list',
  templateUrl: './region-list.component.html',
  styleUrls: ['./region-list.component.css']
})
export class RegionListComponent {

  regions?: IRegion[];
  currentPage:number  = 0;
  itemsPerPage: number = 10;
  totalItems?: string;
  searchText?: string;
  _url = environment.backend;

  constructor(private regionService: RegionService,
              private router: Router,
              private http: HttpClient){}

  ngOnInit(){
    this.getAllRegions(this.currentPage, this.itemsPerPage);
  }



  
  getAllRegions(page?: any, size?: any){

    let search = {};
  
    const pageable = {
      page: page,
      size: size,
    };
  
    if (this.searchText !== undefined && this.searchText !== '') {
      search = {
        mapper: 'REGION',
        searchText: this.searchText,
      };
    }

        // Construct the query parameter for pageable
        const queryParams = {
          page: page,
          size: size,
          search: JSON.stringify(search),
        };


    this.regionService.getRegionsByPageination(queryParams).subscribe(res =>{
      if(res && res.body){
        this.regions = res.body;
        this.totalItems = res.headers.get('X-Total-Count') ?? '';
      }
    })
  }

  addRegion(){
    this.router.navigateByUrl('/region')
  }

  updateRegion(id: any){
    this.router.navigate(['/region'], { queryParams: { id: id } });

  }
  
  searchRegion(search: any){}

  changePage(value: any){
    this.getAllRegions(value.pageIndex, this.itemsPerPage);
  }

  downloadFile() {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http
      .get(`${this._url}/region/currency`, {
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
