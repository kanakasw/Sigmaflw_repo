import { Injectable } from '@angular/core';

@Injectable({
	providedIn: 'root'
})
export class UtilityService {

	constructor() { }
	/*  return {
		 replaceTagWithValue : replaceTagWithValue
	 };
		 */
	replaceTagWithValue(url, tagName, tagvalue) {
		tagName = "{" + tagName + "}";
		var replacedUrl = url.replace(tagName, tagvalue);
		return replacedUrl;
	}
}
