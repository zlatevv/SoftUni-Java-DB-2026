package GetMinionNames;

import GetVillainNames.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    static void main() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Scanner scanner = new Scanner(System.in);

        int villainId = Integer.parseInt(scanner.nextLine());
        PreparedStatement preparedStatement = conn.prepareStatement("select name from villains where id = ?");
        preparedStatement.setInt(1, villainId);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            System.out.printf("Villain: %s%n", resultSet.getString("name"));

            preparedStatement = conn.prepareStatement("""
                    select m.name, m.age from minions m
                    join minions_villains mv on m.id = mv.minion_id
                    where villain_id = ?;
                    """);
            preparedStatement.setInt(1, villainId);
            resultSet = preparedStatement.executeQuery();

            for (int i = 0; resultSet.next(); i++) {
                System.out.printf("%d. %s %d%n",
                        i,
                        resultSet.getString("name"),
                        resultSet.getInt("age"));
            }
        }else {
            System.out.printf("No villain with ID %d exists in the database.", villainId);
        }
    }
}
