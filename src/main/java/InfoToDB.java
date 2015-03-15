import java.io.IOException;
import java.sql.*;

/**
 * Created by lewin on 15-3-12.
 */
public class InfoToDB {
    public void InfoToDB (String name , String content)  throws Exception{
        Connection connection = null;
        String sql;
        String mysqlURL = "jdbc:mysql://localhost:3306/MovieInfo?"
                + "user=root&password=lewin123&useUnicode=true&characterEncoding=UTF8";
        //加载MySQL驱动程序
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载驱动程序!");
            connection = DriverManager.getConnection(mysqlURL);
            Statement statement = connection.createStatement();
        }catch (SQLClientInfoException e)  {
            System.out.println("MySQL操作错误!");
            e.printStackTrace();
        }catch (Exception e)  {
            e.printStackTrace();
        }finally {
            connection.close();
        }
    }
}
