import { CurrencyWithCommasPipePipe } from './currency-with-commas-pipe.pipe';

describe('CurrencyWithCommasPipePipe', () => {
  it('create an instance', () => {
    const pipe = new CurrencyWithCommasPipePipe();
    expect(pipe).toBeTruthy();
  });
});
