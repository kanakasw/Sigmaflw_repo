package com.data.integration.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockApiController {

	@RequestMapping("/Customer-Profile/{customerUniqueReferennce}")
	public String mockSingleUserUpdateApi(String customerUniqueReferennce) {
		return "{\"People\":{\"ColumnNames\":[\"FirstName\",\"LastName\",\"FullName\",\"State\",\"Dob\",\"Relationship\",\"CustomerUniqueReference\"],\"Values\":[[\"Elmer\",\"Fudd\",\"Elmer Fudd\",\"NY\",\"1960-03-09\",\"primary\",\"582C8CD0-2127-48E5-A5AF-47BA2B8F3A9D\"],[\"Wilma\",\"Fudd\",\"Wilma Fudd\",\"NY\",\"1961-04-12\",\"spouse\",\"634-21-5299\"]]},\"Accounts\":{\"ColumnNames\":[\"AccountId\",\"Name\",\"Is_active\",\"Type\",\"Balance\",\"CustomerUniqueReference\"],\"Values\":[[\"005703713536\",\"Checking 1\",true,\"Checking\",1234,\"582C8CD0-2127-48E5-A5AF-47BA2B8F3A9D\"],[\"005703713537\",\"Saving 1\",true,\"Saving\",1234,\"582C8CD0-2127-48E5-A5AF-47BA2B8F3A9D\"]]},\"Loans\":{\"ColumnNames\":[\"AccountId\",\"Name\",\"Is_active\",\"Type\",\"OutstandingDueAmount\",\"CustomerUniqueReference\",\"Term\",\"InterestRate\",\"MonthlyPayment\",\"OriginationDate\"],\"Values\":[[\"005703713845\",\"Mortgage 1\",true,\"Mortgage\",350000,\"582C8CD0-2127-48E5-A5AF-47BA2B8F3A9D\",30,\"3.45\",\"2000\",\"2005-07-21\"],[\"005703713956\",\"AutoLoan 1\",true,\"Autoloan\",25000,\"582C8CD0-2127-48E5-A5AF-47BA2B8F3A9D\",5,\"6.5\",\"1200\",\"2008-04-15\"]]},\"CreditCards\":{\"ColumnNames\":[\"CardNumber\",\"Name\",\"Expiry\",\"Issuer\",\"limit\",\"OutstandingDueAmount\",\"CustomerUniqueReference\"],\"Values\":[[\"5312234500022345\",\"Credit card 1\",\"042020\",\"Visa\",25000.00,8000.00,\"582C8CD0-2127-48E5-A5AF-47BA2B8F3A9D\"],[\"5612234530022378\",\"Credit card 1\",\"042020\",\"Master Card\",12000.00,1000.00,\"582C8CD0-2127-48E5-A5AF-47BA2B8F3A9D\"]]}}";
	}
}
