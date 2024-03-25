import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'currencyWithCommasPipe'
})
export class CurrencyWithCommasPipePipe implements PipeTransform {

  transform(value: number): string {
    if (value >= 10000000) {
      // Format as crore with commas
      return (value / 10000000).toFixed(2).replace(/\d(?=(\d{2})+\.)/g, '$&,') + ' Cr';
    } else {
      // Use default currency pipe for values less than 1 crore
      return value.toLocaleString('en-IN', { style: 'currency', currency: 'USD' });
    }
  }


}
