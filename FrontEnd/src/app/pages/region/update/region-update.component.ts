import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { IRegion, Region } from '../../model/region.model';
import { EntityRegionResponseType, RegionService } from '../service/region.service';
import { ToastrService } from 'ngx-toastr';
import { Observable, map } from 'rxjs';

@Component({
  selector: 'app-region-update',
  templateUrl: './region-update.component.html',
  styleUrls: ['./region-update.component.css']
})
export class RegionUpdateComponent {

  id?:any;
  region?: IRegion;

  regionForm = this.fb.group({
    id: [''],
    customerRegion: ['', [Validators.required]],
    headerName: ['', [Validators.required]],
    vat: ['', [Validators.required]],
    vatNumber: ['', [Validators.required]],
    description: ['', [Validators.required]],
    status: [this.region?.status,[Validators.required]]
  });

  constructor(protected fb: FormBuilder,
              private router: Router,
              private route: ActivatedRoute,
              private regionService: RegionService,
              private toastr: ToastrService){}

  ngOnInit(){
    this.route.queryParams.subscribe( params => {
      this.id = params['id'];
      if(this.id!=null){
        this.updateForm(this.id);
      }
    });
  }

  updateForm(id?: number){

    this.getRegionById(id).subscribe((region) => {
      if(region){
        this.region = region;
        const formData = {
          customerRegion: region.customerRegion || null,
          headerName: region.headerName || null,
          vat: region.vat || null,
          vatNumber: region.vatNumber || null,
          description: region.description || null,
          status: region.status || null,
        };
        this.regionForm.patchValue(formData);
      }
    });
  } 

  getRegionById(id?: any): Observable<Region | null> {
    return this.regionService.getRegionById(id).pipe(
      map((res: EntityRegionResponseType) => {
        if (res && res.body) {
          return res.body;
        }
        return null;
      })
    );
  }

  submit(regionForm: FormGroup) {
    let region = {
      customerRegion: regionForm.value.customerRegion,
      headerName: regionForm.value.headerName,
      vat: regionForm.value.vat,
      vatNumber: regionForm.value.vatNumber,
      description: regionForm.value.description,
      status: regionForm.value.status,
    }

    if(this.id!=null && this.id.length!=0){
      this.updateRegion(this.id, region);
    }else{
      this.createRegion(region);
    }

  }

  updateRegion(id:any, region: Region){
    this.regionService.updateRegion(id, region).subscribe((res: any)=>{
      if(res){
        this.router.navigateByUrl('/region/view')
      }
    },
    error => {
      this.toastr.error(error.error.body)
    })
  }

  createRegion(region: Region){
    console.log(region)
    this.regionService.createRegion(region).subscribe((res: any)=>{
      if(res){
        this.router.navigateByUrl('/region/view')
      }
    },
    error => {
      this.toastr.error(error.error.body)
    })
  }

}
