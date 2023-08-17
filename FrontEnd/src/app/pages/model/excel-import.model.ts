export interface IExcelImportDto {
    id?: number;
    customPort?: string,
    custom?: string,
    smsaFeeVat?: number,
    present?: boolean,
    date1?: Date,
    date2?: Date,
    date3?: Date
  }

  export class Custom implements IExcelImportDto {
    constructor(
        public id?: number,
        public customPort?: string,
        public custom?: string,
        public smsaFeeVat?: number,
        public present?: boolean,
        public date1?: Date | undefined,
        public date2?: Date | undefined,
        public date3?: Date | undefined
    ) {}
  }