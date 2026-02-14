package IncreaseMinionsAge;

import GetVillainNames.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class App {
    static void main() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Scanner input = new Scanner(System.in);

        int[] ids = Arrays.stream(input.nextLine().split(" "))
                .mapToInt(Integer::parseInt).toArray();

        increaseAges(conn, ids);
        printMinions(conn);
    }

    private static void printMinions(Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("""
                select name, age from minions;
                """);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            System.out.printf("%s %d%n", name, age);
        }
    }

    private static void increaseAges(Connection conn, int[] ids) throws SQLException {
        PreparedStatement preparedStatement =  conn.prepareStatement("""
                select name, age from minions where id = ?;
        """);
        for (int id : ids) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                updateAge(conn, id);
                updateName(conn, id, name);
            }
        }
    }

    private static void updateName(Connection conn, int id, String name) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("""
                update minions set name = ? where id = ?;
                """);
        name = name.toLowerCase();
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
    }

    private static void updateAge(Connection conn, int id) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("""
                update minions set age = age + 1 where id = ?;
                """);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }
}
