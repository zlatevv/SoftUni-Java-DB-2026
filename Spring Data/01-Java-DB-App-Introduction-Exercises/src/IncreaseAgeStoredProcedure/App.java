package IncreaseAgeStoredProcedure;

import GetVillainNames.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    static void main() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Scanner input = new Scanner(System.in);
        int id = Integer.parseInt(input.nextLine());

        PreparedStatement preparedStatement = conn.prepareStatement("""
                call usp_get_older(?);
        """);
        preparedStatement.setInt(1, id);
        preparedStatement.executeQuery();
        printMinion(conn, id);
    }

    private static void printMinion(Connection conn, int id) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("""
                select name, age from minions where id = ?;
                """);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            System.out.printf("%s %d%n", name, age);
        }
    }
}
