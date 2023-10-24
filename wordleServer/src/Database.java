import java.sql.*;

public class Database {
    Connection connection;
    Statement statement;
    ResultSet resultSet;


    public Database() throws SQLException { //connect to database
        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/wordle", "root", "pass");
        statement = connection.createStatement();
    }

    public String drawWord() throws SQLException {
        resultSet = statement.executeQuery("SELECT `Word` FROM `words` ORDER BY RAND() LIMIT 1");
        resultSet.next();

        return resultSet.getString(1);
    }

    public boolean checkWord(String word) throws SQLException {
        resultSet = statement.executeQuery("SELECT COUNT(*) from `allWords` WHERE `Word`='" + word.toUpperCase() + "'");
        resultSet.next();

        int count = Integer.parseInt(resultSet.getString(1));
        return count > 0;
    }

}
