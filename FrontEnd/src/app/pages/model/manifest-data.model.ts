export interface IManifestData {
    id?: number;
    prefix?: string;
    manifestNumber?: string;
    awb?: string;
    weight?: string;
    actualWeight?: string;
    dimWeight?: string; 
    companyName?: string;
    mode?: string;
    shipmentMode?: string;
    encodeDesc?: string;
    loadingPortCode?: string;
    encodeDescSec?: string;
    destinationPort?: string;
    carrierCode?: string;
    flightNumber?: string;
    departureDate?: string;
    arrivalDate?: string;
    blDate?: string;
    orderNumber?: string;
    customShipDate?: string;
    accountNumber?: string;
    amount?: string;
    shipmentCountry?: string;
  }

  export class ManifestData implements IManifestData {
    constructor(
      public id?: number,
      public prefix?: string,
      public manifestNumber?: string,
      public awb?: string,
      public weight?: string,
      public actualWeight?: string,
      public dimWeight?: string,
      public companyName?: string,
      public mode?: string,
      public shipmentMode?: string,
      public encodeDesc?: string,
      public loadingPortCode?: string,
      public encodeDescSec?: string,
      public destinationPort?: string,
      public carrierCode?: string,
      public flightNumber?: string,
      public departureDate?: string,
      public arrivalDate?: string,
      public blDate?: string,
      public orderNumber?: string,
      public customShipDate?: string,
      public accountNumber?: string,
      public amount?: string,
      public shipmentCountry?: string,
    ) {}
  }