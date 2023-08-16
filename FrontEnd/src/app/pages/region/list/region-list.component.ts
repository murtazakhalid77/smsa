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

  constructor(private regionService: RegionService,
              private router: Router){}

  ngOnInit(){
    this.getAllRegions()
  }

  
  getAllRegions(){
    this.regionService.getRegions().subscribe(res =>{
      if(res && res.body){
        this.regions = res.body;
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
}
