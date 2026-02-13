package AddMinion;

import GetVillainNames.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    static void main() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DBConnection.getConnection();
        connection.setAutoCommit(false);

        String[] minionInput = scanner.nextLine().split(" ");
        String villainName = scanner.nextLine();
        String minionName = minionInput[0];
        int age = Integer.parseInt(minionInput[1]);
        String town = minionInput[2];
        try {

            insertMinion(connection, minionName, age, town);

            PreparedStatement preparedStatement = connection.prepareStatement("""
                    select * from villains where name = ?
                    """);
            preparedStatement.setString(1, villainName);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                insertMinionVillains(connection, villainName, minionName);
            } else {
                insertVillain(villainName, connection);
            }
            connection.commit();

            System.out.printf("Successfully added %s to be minion of %s%n", minionName, villainName);
        } catch (SQLException e){
            System.out.println("error happened");
            e.printStackTrace();
            connection.rollback();
        }
    }

    private static void insertMinionVillains(Connection connection, String villainName, String minionName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("""
                select id from villains where name = ?;
                """);
        preparedStatement.setString(1, villainName);
        ResultSet resultSet = preparedStatement.executeQuery();
        int villainId = resultSet.getInt("id");

        preparedStatement = connection.prepareStatement("""
                select id from minions where name = ?;
                """);
        preparedStatement.setString(1, minionName);
        resultSet = preparedStatement.executeQuery();
        int minionId = resultSet.getInt("id");

        preparedStatement = connection.prepareStatement("""
                   insert into minions_villains(minion_id, villain_id) values (?, ?);
                """);
        preparedStatement.setInt(1, minionId);
        preparedStatement.setInt(2, villainId);
        preparedStatement.executeUpdate();
    }

    private static void insertVillain(String villainName, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("""
                insert into villains(name, evilness_factor) values (?, "evil");
                """);
        preparedStatement.setString(1, villainName);
        preparedStatement.executeUpdate();

        System.out.printf("Villain %s was added to the database.%n", villainName);
    }

    private static void insertMinion(Connection connection, String minionName, int age, String town) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("""
                select id from towns where name = ?;
                """);
        preparedStatement.setString(1, town);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            int town_id = resultSet.getInt("id");
            preparedStatement = connection.prepareStatement("""
                    insert into minions(name, age, town_id) values (?, ?, ?)
                    """);
            preparedStatement.setString(1, minionName);
            preparedStatement.setInt(2, age);
            preparedStatement.setInt(3, town_id);
            preparedStatement.executeUpdate();
        }else {
            insertTown(connection, town);
            insertMinion(connection, minionName, age, town);
        }
    }

    private static void insertTown(Connection connection, String town) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("""
                insert into towns(name) values (?);
                """);
        preparedStatement.setString(1, town);
        preparedStatement.executeUpdate();

        System.out.printf("Town %s was added to the database.%n", town);
    }
}

