# Google Sheets integration challage

# About the project
This project demonstrates the integration of the Google Sheets API with a Java application using Gradle. 
It allows you to retrieve data from a specific Google Sheets [spreadsheet](https://docs.google.com/spreadsheets/d/1pkhzeS1TeRS7oreb56Blopeq_jUQVM1D0YBtTVsO288/edit#gid=0), process it, and then update the spreadsheet with the processed data.
This project was made in response to the Tunts.Rocks code challange
(What is the purpose of the project)

# Technologies used
- Java
- Google Sheets API
- Gson
- Graddle

# Skills
- API Integration
- OAuth 2.0
- Dependency Management

# Integrations
- Google Sheets API Client Library
- Google Authorization Code Flow
- Google Client Secrets

# Features
- Authentication: The application implemented OAuth 2.0 authentication flow to authorize access to the user's Google Sheets account. This involved obtaining consent from the user and exchanging authorization codes for access tokens.
- Spreadsheet Data Retrieval: The application was able to fetch data from a specified Google Sheets spreadsheet. It made API calls to retrieve the values from the specified range in the spreadsheet and stored the data locally.
- Data Processing: Once the data was retrieved, the application implemented custom logic to process and manipulate the data as per the specific requirements. This could include data transformation, calculations, or any other necessary operations.
- Data Update: After processing the data, the application was capable of updating the spreadsheet with the processed information. It made API calls to update the specified range in the spreadsheet with the modified data. 

# How to run the project

## Prerequisites
- Java Development Kit (JDK) 17
- Graddle 6.X or higher
- Google Sheets API credentials (Only if you want do run your credentials)
  - Use provided email and password instead
 
## Instructions 
1. Clone the repository
```bash
git clone https://github.com/LindermanBR/google-sheets-integration-challange.git
```
2. Open the project in your favorite Java IDE.
3. Authentication: Choose one of the following
  * Update the credentials.json file with your Google Sheets API credentials. Make sure to place the file in the src/main/resources directory.
  * OR
  * Use the email (requiemdev.tester@gmail.com) and password(@Az12345) when asked for a google account.
6. Open a terminal or command prompt and navigate to the project directory.
7. Build the project using Gradle:
```bash
gradle build
```
7. Run the application:

```bash
gradle run
```

# Author

## Linderman Moura

[![E-mail](https://img.shields.io/badge/-Email-000?style=for-the-badge&logo=microsoft-outlook&logoColor=E94D5F)](mailto:linderman.moura@outlook.com)
[![LinkedIn](https://img.shields.io/badge/-LinkedIn-000?style=for-the-badge&logo=linkedin&logoColor=30A3DC)](https://www.linkedin.com/in/linderman-moura/)
