import { Component } from '@angular/core';
import { IRegion } from '../../model/region.model';
import { RegionService } from '../service/region.service';
import { Router } from '@angular/router';

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


  constructor(private regionService: RegionService,
              private router: Router){}

  ngOnInit(){
    this.getAllRegions(this.currentPage, this.itemsPerPage);
  }

  
  getAllRegions(page?: any, size?: any){
    this.regionService.getRegions(page, size).subscribe(res =>{
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

}
