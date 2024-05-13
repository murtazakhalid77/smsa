import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IManifestData } from '../../model/manifest-data.model';
import { environment } from 'src/Environments/environment';
import { ManifestDataService } from 'src/app/pages/manifest-data/service/manifest-data.service';
import { ToastrService } from 'ngx-toastr';
import { FileService } from 'src/app/services/file-service.service';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { SessionStorageService } from 'src/app/services/session-storage.service';

@Component({
  selector: 'app-manifest-data',
  templateUrl: './manifest-data.component.html',
  styleUrls: ['./manifest-data.component.css']
})
export class ManifestDataComponent {
  itemsPerPage: number = 10;
  totalItems: number=0;
  pageIndex: number = 0;
  selectedSearchOption?: string;
  awb: boolean = false;
  manifestInput: boolean = false;
  manifestData?: IManifestData[]=[];
  allManifestData: IManifestData[]=[];
  awbs: any;
  manifestDatas?: any;

  manifestNo?: any;
  manifestNoInput?: any;
  awbsInput?: any;
  prefixInput?: any;
  weight?: any;
  actualWeight?: any;
  dimWeight?: any;
  prefix?: any;
  
  _url = environment.backend;

  constructor(private manifestDataService: ManifestDataService,
    private router: Router,
    private http: HttpClient,
    private toastr: ToastrService,
    private fileService: FileService,
    private route: ActivatedRoute,
    public storageService:SessionStorageService
  ){}

  ngOnInit(){
    this.route.paramMap.subscribe(params => {
      // Access the state object and retrieve the passed data
      this.manifestNoInput = history.state.manifestNoInput;
      this.prefixInput = history.state.prefixInput;
      this.awbsInput = history.state.awbsInput;
    });
  }

  onSearchOptionChange(selectedOption?: String){

    this.manifestData=[];
    if(selectedOption === 'awbs') {
      this.awb=true;
      this.manifestInput=false;
      this.awbs=this.awbsInput;
      
    }
    else if(selectedOption === 'manifestNo') {
      this.awb=false;
      this.manifestInput=true;
      this.manifestNo=this.manifestNoInput;
      this.prefix=this.prefixInput;
    }
  };

  getManifestData() {
    const searchManifestData = {
      manifestNo: this.selectedSearchOption === 'manifestNo'? this.manifestNo : null,
      prefix: this.selectedSearchOption === 'manifestNo'? this.prefixInput : null,
      awbs: this.selectedSearchOption === 'awbs'? this.awbs: null,
      mapper: 'MANIFEST_DATA'
    };

    if (
      // (this.selectedSearchOption === 'invoice' && (this.invoiceTo || this.invoiceFrom)) ||
      // (this.selectedSearchOption === 'date' && (this.startDate || this.endDate)) ||
      (this.selectedSearchOption === 'awbs' && this.awbs) ||
      (this.selectedSearchOption === 'manifestNo' && this.manifestNo)
    ) {
      this.manifestDataService.getManifestData(searchManifestData).subscribe(
        (res: any) => {
          if (res && res.body && res.body.length > 0) {
            this.allManifestData = res.body;
            this.totalItems=this.allManifestData.length;
            debugger // Save all sales reports
            this.paginateManifestData(); // Paginate the retrieved data initially
          } else {
            this.toastr.error('No Data Found');
          }
        },
        (error) => {
          this.toastr.error(error.error.body);
        }
      );
    } else {
      this.toastr.error('Please provide valid search criteria');
    }
  }

  changePage(event: any) {
    this.pageIndex = event.pageIndex;
    this.paginateManifestData(); // Update manifestData based on the pageIndex
  }

  paginateManifestData() {
    debugger
    const startIndex = this.pageIndex * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.manifestData = this.allManifestData.slice(startIndex, endIndex);
  }

  updateManifestData(id: any, prefixInput: any, manifestNoInput: any, awbsInput: any){
    this.router.navigate(['/manifestDataUpdate'], { queryParams: {id: id, prefixInput: prefixInput, manifestNoInput: manifestNoInput, awbsInput: awbsInput}});
  }

  downloadFile() {
    const manifestDataIds = this.allManifestData?.map(manifest => manifest.id); // Replace with your desired salesReportIds
    const params = new HttpParams().set('manifestDataIds', manifestDataIds!.join(','));
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http
      .get(`${this._url}/excel/export/manifest-data`, {
        responseType: 'blob', // Important: responseType should be 'blob'
        headers,
        params: params
      })
      .subscribe((response: any) => {
        const blob = new Blob([response], { type: 'application/octet-stream' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.download = 'manifest_data.xlsx'; // Set the desired filename here
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
      });
  }
}
