package PrintAllMinions;

import GetVillainNames.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class App {
    static void main() throws SQLException {
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                """
                select name from minions;
                """);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<String> minionNames = new ArrayList<>();

        while (resultSet.next()){
            minionNames.add(resultSet.getString("name"));
        }

        int start = 0;
        int end = minionNames.size() - 1;

        while (start <= end){
            if (start == end){
                System.out.println(minionNames.get(start));
            }else {
                System.out.println(minionNames.get(start));
                System.out.println(minionNames.get(end));
            }

            start++;
            end--;
        }

        connection.close();
    }
}
