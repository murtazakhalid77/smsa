export interface IRegion {
    id?: number,
    customerRegion?: string,
    vat?: string,
    headerName?: string,
    vatNumber?: string,
    description?: string,
    status?: boolean
  }

  export class Region implements IRegion {
    constructor(
        public id?: number,
        public customerRegion?: string,
        public vat?: string,
        public headerName?: string,
        public vatNumber?: string,
        public description?: string,
        public status?: boolean
    ) {}
  }