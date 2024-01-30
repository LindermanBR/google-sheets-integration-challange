package google.sheets.integration.challange;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import google.sheets.integration.challange.entities.Student;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiIntegration {
    // Stores the name of the application
    private static final String APPLICATION_NAME = "Google Sheets API Java Integration";
    // Used to create JSON-related objects for the Google Sheets API
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    // The directory where tokens for Google Sheets API authentication will be
    // stored
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    // The scope required for accessing spreadsheets.
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    // The path to the credentials file.
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * The entry point of the program.
     *
     * @param args The command-line arguments passed to the program.
     * @throws IOException              If an error occurs while interacting with
     *                                  the Google Sheets API.
     * @throws GeneralSecurityException If there is a security-related error during
     *                                  the authentication process.
     */
    public static void main(String... args) throws IOException, GeneralSecurityException {
        final String spreadsheetId = "1pkhzeS1TeRS7oreb56Blopeq_jUQVM1D0YBtTVsO288";
        final String range = "engenharia_de_software!A4:H";

        System.out.println("Getting google api service");
        Sheets service = getService();

        System.out.println("Getting sheet data");

        System.out.println("Getting total semester classes");
        ValueRange totalSemesterCell = getValueRange(spreadsheetId, service, "A2:H2");

        int totalSemesterClasses = Integer
                .parseInt(totalSemesterCell.getValues().get(0).toString().split(": ")[1].replace("]", ""));

        System.out.println("Getting students data from sheet");
        ValueRange studentData = getValueRange(spreadsheetId, service, range);

        System.out.println("Creating students list");
        System.out.println("Processing Students grades");
        System.out.println("Processing Students situaltion");
        List<Student> students = getStudents(studentData.getValues(), totalSemesterClasses);

        System.out.println("Sending data to google sheet");
        sendToGoogleSheet(spreadsheetId, range, service, students);

    }

    /**
     * Retrieves the necessary credentials for accessing the Google Sheets API.
     *
     * @param HTTP_TRANSPORT The transport used for the HTTP requests.
     * @return The credentials required for API authentication.
     * @throws IOException If an error occurs while reading the credentials file.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load the credentials file
        InputStream in = ApiIntegration.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        // Load the client secrets from the credentials file
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build the authorization flow for the API
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        // Set up a local server receiver for authorization
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        // Authorize and return the installed application credential
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Retrieves the Google Sheets service for accessing the Google Sheets API.
     *
     * @return The Google Sheets service instance.
     * @throws GeneralSecurityException If there is a security-related error during
     *                                  the authentication process.
     * @throws IOException              If an error occurs while retrieving the
     *                                  credentials or setting up the service.
     */
    private static Sheets getService() throws GeneralSecurityException, IOException {
        // Create the HTTP transport
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        // Retrieve the credentials
        Credential credential = getCredentials(HTTP_TRANSPORT);
        // Build the Google Sheets service
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        return service;
    }

    /**
     * Retrieves the value range from a specific range in a Google Sheets
     * spreadsheet.
     *
     * @param spreadsheetId The ID of the spreadsheet.
     * @param service       The Google Sheets service instance.
     * @param range         The range of cells to retrieve values from.
     * @return The retrieved value range.
     * @throws IOException If an error occurs while retrieving the value range.
     */
    private static ValueRange getValueRange(final String spreadsheetId, Sheets service, String range)
            throws IOException {
        return service.spreadsheets().values().get(spreadsheetId, range).execute();
    }

    /**
     * Retrieves a list of Student objects from a list of student data.
     *
     * @param students             The list of student data, where each entry
     *                             represents a row of student information.
     * @param totalSemesterClasses The total number of classes in the semester.
     * @return A list of Student objects created from the student data.
     */
    private static List<Student> getStudents(List<List<Object>> students, int totalSemesterClasses) {
        List<Student> studentList = new ArrayList<>();
        if (students == null || students.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (List<Object> row : students) {
                Student student = new Student();
                student.setEnrollment(Integer.parseInt(row.get(0).toString()));
                student.setName(row.get(1).toString());
                student.setClassAbsence(Integer.parseInt(row.get(2).toString()));
                student.setGrade1(Double.parseDouble(row.get(3).toString()));
                student.setGrade2(Double.parseDouble(row.get(4).toString()));
                student.setGrade3(Double.parseDouble(row.get(5).toString()));
                student.setSituation(totalSemesterClasses);
                double finalApprovalGrade = 0;
                if (student.getSituation().equalsIgnoreCase("Exame Final")) {
                    finalApprovalGrade = 2 * 5 - student.getAvarageGrade();
                }
                student.setFinalGradeToApproval(finalApprovalGrade);
                studentList.add(student);
            }

        }
        return studentList;
    }

    /**
     * Creates a ValueRange object from a list of Student objects to be used for
     * updating a Google Sheets spreadsheet.
     *
     * @param students The list of Student objects containing the data to be
     *                 updated.
     * @return A ValueRange object representing the data to be updated in the
     *         spreadsheet.
     */
    private static ValueRange updateSheetBody(List<Student> students) {
        List<List<Object>> data = new ArrayList<>();
        for (Student student : students) {
            List<Object> row = new ArrayList<>();
            row.add(0, student.getEnrollment());
            row.add(1, student.getName());
            row.add(2, student.getClassAbsence());
            row.add(3, (student.getGrade1() * 10));
            row.add(4, (student.getGrade2() * 10));
            row.add(5, (student.getGrade3() * 10));
            row.add(6, student.getSituation());
            row.add(7, (student.getFinalGradeToApproval() * 10));
            data.add(row);
        }
        return new ValueRange().setValues(data);

    }

    /**
     * Sends the data of a list of Student objects to a specific range in a Google
     * Sheets spreadsheet.
     *
     * @param spreadsheetId The ID of the spreadsheet.
     * @param range         The range in the spreadsheet to update.
     * @param service       The Google Sheets service instance.
     * @param students      The list of Student objects containing the data to be
     *                      sent.
     * @throws IOException If an error occurs while updating the spreadsheet.
     */
    private static void sendToGoogleSheet(final String spreadsheetId, final String range, Sheets service,
            List<Student> students) throws IOException {
        service.spreadsheets().values()
                .update(spreadsheetId, range, updateSheetBody(students))
                .setValueInputOption("RAW")
                .execute();
    }
}