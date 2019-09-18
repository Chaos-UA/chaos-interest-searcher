import {Component, Input, OnInit, Output} from "@angular/core";

@Component({
  selector: 'chaos-select',
  templateUrl: './chaos-select.component.html',
  styleUrls: ['./chaos-select.component.scss']
})
export class ChaosSelectComponent<T> implements OnInit {

  @Input()
  placeholder : string;

  @Input()
  options : T[];

  @Input()
  values? : T[];

  @Input()
  optionToString : any;

  filterText: string = '';

  ngOnInit(): void {
  }

  getFilteredOptions() : T[] {
    return this.options.filter(value => {
      if (this.values.indexOf(value) !== -1) {
        return false;
      }
      const textToFilter = this.filterText;
        if (!textToFilter) {
          return true;
        }
        const str = this.optionToString(value).toLowerCase();
        return str && str.indexOf(textToFilter.toLowerCase()) !== -1
      })
  }

  remove(option : T) {
    const index = this.values.indexOf(option);
    this.values.splice(index, 1);
  }

  selectOption(option : T) {
    this.filterText = '';
    this.values.push(option);
  }
}
