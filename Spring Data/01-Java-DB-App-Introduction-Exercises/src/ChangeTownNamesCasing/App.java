package ChangeTownNamesCasing;

import GetVillainNames.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    static void main() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DBConnection.getConnection();
        String country = scanner.nextLine();

        changeToUpperCase(connection, country);
        int townCount = getTownCount(connection, country);
        printOutput(connection, townCount, country);
    }

    private static void printOutput(Connection connection, int townCount, String country) throws SQLException {
        if (townCount > 0) {
            System.out.printf("%d town names were affected.%n", townCount);
            System.out.println(getTownArray(connection, country));
        }else {
            System.out.println("No town names were affected.");
        }
    }

    private static ArrayList<String> getTownArray(Connection connection, String country) throws SQLException {
        ArrayList<String> towns = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("""
                select name from towns where country = ?;
                """);

        preparedStatement.setString(1, country);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            towns.add(resultSet.getString("name"));
        }
        return towns;
    }

    private static int getTownCount(Connection connection, String country) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("""
                select count(*) as town_count from towns where country = ?;
                """);

        preparedStatement.setString(1, country);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("town_count");
        }
        return 0;
    }

    private static void changeToUpperCase(Connection connection, String country) throws SQLException {
        PreparedStatement selectStmt = connection.prepareStatement("""
            select name from towns where country = ?;
            """);
        selectStmt.setString(1, country);
        ResultSet resultSet = selectStmt.executeQuery();

        PreparedStatement updateStmt = connection.prepareStatement("""
            update towns set name = ? where name = ?;
            """);

        while (resultSet.next()) {
            String oldName = resultSet.getString("name");
            String newName = oldName.toUpperCase();

            updateStmt.setString(1, newName);
            updateStmt.setString(2, oldName);
            updateStmt.executeUpdate();
        }
    }
}
