import { Directive, ElementRef, HostListener, Renderer2 } from '@angular/core';

@Directive({
  selector: '[autoGrow]'
})
export class AutoGrowDirective {

  constructor(private el: ElementRef, private renderer: Renderer2) {}

  @HostListener('input', ['$event.target'])
  onInput(textArea: HTMLTextAreaElement): void {
    this.adjustTextAreaHeight(textArea);
  }

  ngOnInit(): void {
    // Adjust the initial height if needed
    this.adjustTextAreaHeight(this.el.nativeElement);
  }

  private adjustTextAreaHeight(textArea: HTMLTextAreaElement): void {
    textArea.style.overflowY = 'hidden';
    textArea.style.height = 'auto';
  
    // Set a maximum height of 50px
    const maxHeight = 50;
    if (textArea.scrollHeight > maxHeight) {
      textArea.style.overflowY = 'auto';
      textArea.style.height = maxHeight + 'px';
    } else {
      textArea.style.height = textArea.scrollHeight + 'px';
    }
  }
  
}
