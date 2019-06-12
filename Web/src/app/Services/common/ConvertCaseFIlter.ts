import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'convertCaseFilter' })
export class convertCaseFilter implements PipeTransform {
    transform(value: string): string {
        let newStr: string = "";
        newStr = value.replace(/([a-z0-9])([A-Z])/g, "$1 $2");
        newStr = value.charAt(0).toUpperCase() + newStr.slice(1);

        return newStr;

    }
}