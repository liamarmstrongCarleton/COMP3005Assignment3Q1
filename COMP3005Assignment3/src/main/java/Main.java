import java.sql.*;

    /*  -----===-----   *functionNameHere*   -----===-----
        Purpose     :
        Input(s)    :
        Output(s)   :
     */

public class Main {
    //   -----===-----   IMPORTANT INPUTS HERE   -----===-----
    static String ip = "AddIpHere";
    static String port = "AddPortHere";
    static String database = "AddDatabaseNameHere";
    static String user = "AddUserHere";
    static String password = "AddPasswordHere";

    static String url = "jdbc:postgresql://" + ip + ":" + port + "/" + database;
    static Connection connection;

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(url, user, password);
            if(connection != null){
                System.out.println("Successfully connected to the database");

            }
            else {
                System.out.println("Connection to the database has failed");

            }
        }
        catch(ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        //addRequiredTable();

        //getAllStudents();

//        addStudent("Liam", "Armstrong", "liam.armstrong879@gmail.com", "2021-09-06");
//        getAllStudents();

//        updateStudentEmail(4, "lemon300@icloud.com");
//        getAllStudents();

//        deleteStudent(4);
//        getAllStudents();
    }



//      ==========   THIS IS A DEPRECIATED FUNCTION, USED FOR TESTING ONLY   ==========
    /*  -----===-----   testDatabaseConnection   -----===-----
        Purpose     :   The purpose of this function is to test if you are successfully able to connect to the database
        this function is slightly redundant, but nice to use if you want to test your connection before trying any methods
        Inputs      :   N/A
        Outputs     :   N/A
     */
//    static void testDatabaseConnection() {
//        try {
//            Class.forName("org.postgresql.Driver");
//            connection = DriverManager.getConnection(url, user, password);
//            if(connection != null)
//                System.out.println("Successful connection to the database");
//            else
//                System.out.println("Unsuccessful connection to the database");
//        }
//        catch(ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
//    }


    /*  -----===-----   addRequiredTable   -----===-----
        Purpose     :   create and populate your database with the standard schema and initial data required by Assignment 3 Q1
                        DO NOT RUN THIS MORE THAN ONCE you will get errors
        Input(s)    :   N/A
        Output(s)   :   N/A
     */
    static void addRequiredTable() {
        String tableQuery = "CREATE TABLE IF NOT EXISTS students (" +
                "student_id SERIAL PRIMARY KEY," +
                "first_name VARCHAR(255) NOT NULL," +
                "last_name VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) NOT NULL UNIQUE," +
                "enrollment_date DATE" +
                ");";

        String dataQuery = "INSERT INTO Students (first_name, last_name, email, enrollment_date) VALUES" +
                "('John', 'Doe', 'john.doe@example.com', '2023-09-01')," +
                "('Jane', 'Smith', 'jane.smith@example.com', '2023-09-01')," +
                "('Jim', 'Beam', 'jim.beam@example.com', '2023-09-02');";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(tableQuery);
            statement.executeUpdate(dataQuery);
            System.out.println("Table schema and data successfully added to database");
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


    /*  -----===-----   getAllStudents   -----===-----
        Purpose     :   Queries all columns from the 'students' table and outputs them on the standard output stream
        Input(s)    :   N/A
        Output(s)   :   N/A
     */
    static void getAllStudents() {
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM students");
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                System.out.print(resultSet.getInt("student_id") + " \t");
                System.out.print(resultSet.getString("first_name") + " \t");
                System.out.print(resultSet.getString("last_name") + " \t");
                System.out.print(resultSet.getString("email") + " \t");
                System.out.println(resultSet.getDate("enrollment_date"));
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


    /*  -----===-----   addStudent   -----===-----
        Purpose     :   takes 4 inputs and inserts into the 'students' table based on the data in the parameters
                        Also converts enrollment_date into a Date value for the sql database to read
        Input(s)    :   String first_name, String last_ name, String email, String enrollment_date
                        first_name      -   first name of the student being inserted
                        last_name       -   last name of the student being inserted
                        email           -   email of the student being inserted
                        enrollment_date -   date of enrollment of student being inserted
        Output(s)   :   N/A
     */
    static void addStudent(String first_name, String last_name, String email, String enrollment_date) {
        String insert = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
        java.sql.Date convertedDate = java.sql.Date.valueOf(enrollment_date);
        try (PreparedStatement addPS = connection.prepareStatement (insert)) {
            addPS.setString(1, first_name);
            addPS.setString(2, last_name);
            addPS.setString(3, email);
            addPS.setDate(4, convertedDate);
            addPS.executeUpdate();
            System.out.println(first_name + " " + last_name + " has been added to the database");
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


    /*  -----===-----   updateStudentEmail   -----===-----
        Purpose     :   takes 2 inputs and updates a single email column based on the student_id
        Input(s)    :   int student_id, String new_email
                        student_id  -   numerical value corresponding to the id of the student in the 'students' table
                        new_email   -   new email value for the student being updated
        Output(s)   :   N/A
     */
    static void updateStudentEmail(int student_id, String new_email) {
        String update = "UPDATE students SET email = ? WHERE student_id = ?";

        try (PreparedStatement updateStatement = connection.prepareStatement(update)) {
            updateStatement.setString(1, new_email);
            updateStatement.setInt(2, student_id);

            int changedRows = updateStatement.executeUpdate();
            System.out.println(changedRows + " row(s) has been updated"); //need to change. changedRows only gives the number of changed rows
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

    }


    /*  -----===-----   deleteStudent   -----===-----
        Purpose     :   deletes a single row from the database corresponding to the student_id input
        Input(s)    :   int student_id
                        student_id  -   numerical value corresponding to the data in the 'students' table
        Output(s)   :   N/A
     */
    static void deleteStudent(int student_id) {
        String delete = "DELETE FROM students WHERE student_id = ?";

        try (PreparedStatement deleteStatement = connection.prepareStatement(delete)) {
            deleteStatement.setInt(1, student_id);

            int changedRows = deleteStatement.executeUpdate();
            System.out.println(changedRows + " row has been deleted");
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
