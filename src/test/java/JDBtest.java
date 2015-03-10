import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by lewin on 3/9/15.
 */
public class JDBtest {
    public static void main(String [] args) throws Exception{
        Connection conn = null;
        String sql;
        String mysqlUrl = "jdbc:mysql://localhost:3306/llh?"
                + "user=root&password=lewin123&useUnicade=true&characterEncoding=UTF8";

        //加载Mysql驱动
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载MySQL驱动程序");
            conn = DriverManager.getConnection(mysqlUrl);
            java.sql.Statement stmt = conn.createStatement();
            sql = "create table student(NO char(20) , name varchar(20) , primary key(NO))";

            int result = stmt.executeUpdate(sql);
            if (result != -1)  {
                System.out.println("创建数据表成功");
                sql = "INSERT INTO student(NO,name) VALUES('2012001','李林翰')";
                result = stmt.executeUpdate(sql);
                sql = "INSERT INTO student(NO,name) VALUES('2012002','张三')";
                result = stmt.executeUpdate(sql);
                sql = "SELECT * FROM student";
                ResultSet rs = stmt.executeQuery(sql);
                System.out.println("学号\t姓名");
                while (rs.next())  {
                    System.out.println(rs.getString(1) + "\t" + rs.getString(2));
                }
            }
        }catch (SQLException e)  {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.close();
        }
    }
}
