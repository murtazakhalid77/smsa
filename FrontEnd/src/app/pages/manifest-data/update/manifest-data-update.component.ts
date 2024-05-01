import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IManifestData, ManifestData } from '../../model/manifest-data.model';
import { ManifestDataService, EntityManifestDataResponseType } from 'src/app/pages/manifest-data/service/manifest-data.service';
import { ActivatedRoute, Router, NavigationExtras } from '@angular/router';
import { Observable, map } from 'rxjs';

@Component({
  selector: 'app-manifest-data-update',
  templateUrl: './manifest-data-update.component.html',
  styleUrls: ['./manifest-data-update.component.css']
})
export class ManifestDataUpdateComponent implements OnInit {
  customForm!: FormGroup;
  manifestData?: IManifestData;
  id: any;
  prefixInput: any;
  manifestNoInput: any;
  awbsInput: any;
  dateObject?: any;


  constructor(
    private formbuilder: FormBuilder,
    private manifestDataService: ManifestDataService,
    private router: Router,
    private route: ActivatedRoute
  ){}

  ngOnInit(): void {
      this.customForm = this.formbuilder.group({
        id: ['', [Validators.required]],
        prefix: ['', [Validators.required]],
        manifestNumber: ['', [Validators.required]],
        awb: ['', [Validators.required]],
        weight: ['', [Validators.required]],
        actualWeight: ['', [Validators.required]],
        dimWeight:  ['', [Validators.required]],
        companyName: ['', [Validators.required]],
        mode: ['', [Validators.required]],
        shipmentMode: ['', [Validators.required]],
        encodeDesc: ['', [Validators.required]],
        loadingPortCode: ['', [Validators.required]],
        encodeDescSec: ['', [Validators.required]],
        destinationPort: ['', [Validators.required]],
        carrierCode: ['', [Validators.required]],
        flightNumber: ['', [Validators.required]],
        departureDate: ['', [Validators.required]],
        arrivalDate: ['', [Validators.required]],
        blDate: ['', [Validators.required]],
        orderNumber: ['', [Validators.required]],
        customShipDate: ['', [Validators.required]],
        accountNumber: ['', [Validators.required]],
        amount: ['', [Validators.required]],
        shipmentCountry: ['', [Validators.required]],
        consigneeName: ['', [Validators.required]],
        consigneeCity: ['', [Validators.required]]
      })

      this.route.queryParams.subscribe( params => {
        this.id = params['id'];
        this.prefixInput = params['prefixInput'];
        this.manifestNoInput = params['manifestNoInput'];
        this.awbsInput = params['awbsInput'];
        if(this.id!=null){
          this.updateForm(this.id);
        }
      });
      console.log('Received Prefix Input:', this.prefixInput);
      console.log('Received Manifest No Input:', this.manifestNoInput);
      console.log('Received AWBS Input:', this.awbsInput);
  }

  updateForm(id: any){
    this.getCustomById(id).subscribe(res => {
      if (res) {
        this.manifestData = res;
        this.customForm.patchValue({
          id: this.manifestData.id,
          prefix: this.manifestData.prefix,
          manifestNumber: this.manifestData.manifestNumber,
          awb: this.manifestData.awb,
          weight: this.manifestData.weight,
          actualWeight: this.manifestData.actualWeight,
          dimWeight: this.manifestData.dimWeight,
          companyName: this.manifestData.companyName,
          mode: this.manifestData.mode,
          shipmentMode: this.manifestData.shipmentMode,
          encodeDesc: this.manifestData.encodeDesc,
          loadingPortCode: this.manifestData.loadingPortCode,
          encodeDescSec: this.manifestData.encodeDescSec,
          destinationPort: this.manifestData.destinationPort,
          carrierCode: this.manifestData.carrierCode,
          flightNumber: this.manifestData.flightNumber,
          departureDate: this.formatStringToDate(this.manifestData.departureDate ?? ''),
          arrivalDate: this.formatStringToDate(this.manifestData.arrivalDate ?? ''),
          blDate: this.formatStringToDate(this.manifestData.blDate ?? ''),
          orderNumber: this.manifestData.orderNumber,
          customShipDate: this.formatStringToDateTime(this.manifestData.customShipDate ?? ''),
          accountNumber: this.manifestData.accountNumber,
          amount: this.manifestData.amount,
          shipmentCountry: this.manifestData.shipmentCountry,
          consigneeName: this.manifestData.consigneeName,
          consigneeCity: this.manifestData.consigneeCity
        });
      }
    });
  }

  getCustomById(id?: any): Observable<ManifestData | null> {
    return this.manifestDataService.getManifestDataById(id).pipe(
      map((res: EntityManifestDataResponseType) => {
        if (res && res.body) {
          return res.body;
        }
        return null;
      })
    );
  }

  submit(customForm: FormGroup){
    console.log('This is Custom Ship Date When going to db: ', this.formatDateTimeToString(customForm.value.customShipDate))
    let manifestData = {
      id: this.id,
        prefix: customForm.value.prefix,
        manifestNumber: customForm.value.manifestNumber,
        awb: customForm.value.awb,
        weight: customForm.value.weight,
        actualWeight: customForm.value.actualWeight,
        dimWeight:  customForm.value.dimWeight,
        companyName: customForm.value.companyName,
        mode: customForm.value.mode,
        shipmentMode: customForm.value.shipmentMode,
        encodeDesc: customForm.value.encodeDesc,
        loadingPortCode: customForm.value.loadingPortCode,
        encodeDescSec: customForm.value.encodeDescSec,
        destinationPort: customForm.value.destinationPort,
        carrierCode: customForm.value.carrierCode,
        flightNumber: customForm.value.flightNumber,
        departureDate: this.formatDateToString(customForm.value.departureDate),
        arrivalDate: this.formatDateToString(customForm.value.arrivalDate),
        blDate: this.formatDateToString(customForm.value.blDate),
        orderNumber: customForm.value.orderNumber,
        customShipDate: this.formatDateTimeToString(customForm.value.customShipDate),
        accountNumber: customForm.value.accountNumber,
        amount: customForm.value.amount,
        shipmentCountry: customForm.value.shipmentCountry,
        consigneeName: customForm.value.consigneeName,
        consigneeCity: customForm.value.consigneeCity
    }

    console.log(manifestData)

    if(this.id!=null ){
      this.updateManifestData(manifestData);
    }
  }

  updateManifestData(manifestData: ManifestData) {
    this.manifestDataService.updateManifestData(manifestData).subscribe((res: any)=>{
      if(res){
        const navigationExtras: NavigationExtras = {
          state: {
              manifestNoInput: this.manifestNoInput,
              prefixInput: this.prefixInput,
              awbsInput: this.awbsInput // Pass any updated data back to the main component
          }
      };
        this.router.navigateByUrl('/manifestData', navigationExtras)
      }
    })
  }

  formatDateToString(originalDate: string): string {
    // Split the original date string into year, month, and day
    const [year, month, day] = originalDate.split('-');

    // Create a new Date object with the provided year, month, and day
    const formattedDate = new Date(Number(year), Number(month) - 1, Number(day));

    // Extract day, month, and year from the formatted date
    const formattedDay = formattedDate.getDate();
    const formattedMonth = formattedDate.getMonth() + 1;
    const formattedYear = formattedDate.getFullYear();

    // Return the formatted date string in the desired format
    return `${formattedMonth}/${formattedDay}/${formattedYear}`;
  }

  formatStringToDate(dateString: string): string {
    const parts = dateString.split('/');
    const day = parseInt(parts[1], 10); // Day (as integer)
    const month = parseInt(parts[0], 10) - 1; // Month (as integer), subtract 1 because months are zero-based in JavaScript Date
    const year = parseInt(parts[2], 10); // Year (as integer)

    // Create a new Date object with the parsed parts
    this.dateObject = new Date(year, month, day);

    // Format the date object into ISO format (YYYY-MM-DD)
    const timezoneOffset = this.dateObject.getTimezoneOffset() * 60000; // Convert minutes to milliseconds
    const localTime = this.dateObject.getTime() - timezoneOffset; // Get local time
    const formattedDate = new Date(localTime).toISOString().split('T')[0];

    return formattedDate;
  }

  formatStringToDateTime(dateString: string): string {
    // Parse the date string
    const dateObject = new Date(dateString);
    let hoursInString: String;
    // Extract date and time components
    const year = dateObject.getFullYear();
    const month = String(dateObject.getMonth() + 1).padStart(2, '0'); // Month is zero-based, so add 1 and pad with leading zero if necessary
    const day = String(dateObject.getDate()).padStart(2, '0'); // Pad with leading zero if necessary
    let hours = dateObject.getHours();
    const minutes = String(dateObject.getMinutes()).padStart(2, '0'); // Pad with leading zero if necessary

    hours = (hours % 12) || 12;
    if (hours === 0) {
      hours = 12;
    }
    else {
      hours = hours + 12;
    }

    hoursInString = String(hours).padStart(2, '0');
    // Construct the datetime-local string
    const formattedDate = `${year}-${month}-${day}T${hoursInString}:${minutes}`;

    return formattedDate;
  }

  formatDateTimeToString(originalDateTime: string): string {
    // Create a new Date object from the original date-time string
    const dateTime = new Date(originalDateTime);

    // Extract day, month, year, hour, and minute from the formatted date
    const formattedDay = dateTime.getDate();
    const formattedMonth = dateTime.getMonth() + 1;
    const formattedYear = dateTime.getFullYear() % 100; // Get only last two digits of the year
    let formattedHour = dateTime.getHours();
    const formattedMinute = dateTime.getMinutes();
    let ampm = 'AM';

    // Convert hour to 12-hour format and determine AM/PM
    if (formattedHour >= 12) {
      ampm = 'PM';
      if (formattedHour > 12) {
        formattedHour -= 12;
      }
    }

    // Handle midnight edge case (0 AM becomes 12 AM)
    if (formattedHour === 0) {
      formattedHour = 12;
    }

    // Format the date and time string
    const formattedDateTime = `${formattedMonth}/${formattedDay}/${formattedYear} ${formattedHour}:${formattedMinute.toString().padStart(2, '0')} ${ampm}`;

    return formattedDateTime;
  }

}
