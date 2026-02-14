package RemoveVillain;

import GetVillainNames.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    static void main() throws SQLException {
        Connection connection = DBConnection.getConnection();
        Scanner input = new Scanner(System.in);

        int id = Integer.parseInt(input.nextLine());

        removeVillain(connection, id);
    }

    private static void removeVillain(Connection connection, int id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("""
            select name from villains where id = ?;
            """);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            System.out.println("No such villain found");
            return;
        }

        String villainName = rs.getString("name");

        try {
            connection.setAutoCommit(false);

            int released = releaseMinions(connection, id);
            deleteVillain(connection, id);

            connection.commit();

            System.out.printf("%s was deleted%n", villainName);
            System.out.printf("%d minions released%n", released);

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    private static int releaseMinions(Connection connection, int id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("""
            delete from minions_villains where villain_id = ?;
            """);
        ps.setInt(1, id);
        return ps.executeUpdate();
    }

    private static void deleteVillain(Connection connection, int id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("""
            delete from villains where id = ?;
            """);
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}
