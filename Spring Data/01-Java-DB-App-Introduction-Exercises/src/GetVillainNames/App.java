package GetVillainNames;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
    static void main() throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(""" 
                    select v.name, count(mv.minion_id) as minion_count from villains v
                    join minions_villains mv on v.id = mv.villain_id
                    group by v.id
                    having minion_count > 15
                    order by minion_count desc;
                 """
        );
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()){
            System.out.printf("%s %d%n", rs.getString("name"), rs.getInt("minion_count"));
        }
    }
}
