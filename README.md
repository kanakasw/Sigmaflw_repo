# Process Integration

Process Integration is an open source application for data transformation.  
It uses Pentaho Data Integration (PDI) tool for data transformation.  


## Getting Started / Installation


### Built With
 This application is generated with [Angular CLI](https://github.com/angular/angular-cli) version 7.3.5.    
 It uses Java development enviroment and Pentaho API at server side.

### Prerequisites
 - Pentaho Data Integration Tool need to be installed on local machine.  
To know more about Pentaho Data Integration goto [Pentaho Data Integration](https://help.pentaho.com/Documentation/7.1/0D0/Pentaho_Data_Integration)
- Node.js need to be installed on local machine to run npm commands required for the configuration of the application.  
   install [node.js](https://nodejs.org/en/) here.
- Kettle Transformation(KTR) Management([ what is ktr? ]()):
   - ktr files are generated as output of PDI Tranformations which provides data mining and extract, transform, load (ETL) capabilities. 
   - Despite the ktr and kjb extensions, PDI transformations and jobs are just XML files.
   - ktr(s) will be created offline on the server where Pentaho is installed.  
     
- KTR Structure
  - Ktr file name has to be the process name.
  - The format for input file to the KTR is ProcessName_input.{extension}.  
  - Output of the KTR will be ProcessName_output.{extension}.


### Setting up

Here's a brief introduction  about what a user must do in order to start using the application further:

```bash
git clone https://github.com/your/your-project.git
cd your-project/
npm install -g --save
Run ng serve  for dev server
Navigate to http://localhost:4200/. 
``` 

## Usage

### Features
Process Integration supports two use cases  
- File transformation (ex: csv to json)
   - In file tranformation data will be Retrieving from a source file and transformed into the target file or database of user choice.  
   - It supports sorting, filtering and various other operaions on data. 
     
- Database transformation (ex: MySQL to Postgres)
  - In database transformation source database is transformed into target database of use choice.
  - Data can be extracted from a table of source database and spreaded over multiple tables of a target database.


### Screens
  Sample ktr file for database to database tranformation.  
    
![ScreenShot](Web/src/assets/Screens/sampleKtr.png "Sample ktr")

## Contributing

Please read [CONTRIBUTING.md]() for details on our code of conduct, and the process for submitting pull requests to us.

## License

This project is licensed under the MIT License - see the [LICENSE.md](License.md) file for details

