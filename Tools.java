import java.io.PrintStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Tools {
    private static final PrintStream out = System.out;
    public static String getHttpData(String httpURL) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(httpURL)).build();
        HttpResponse<String> response = client
                .send(request,HttpResponse.BodyHandlers.ofString());

        return response != null ? response.body() : null;
    }

    public static void insert(String date , String json) throws Exception{
        out.println(json);
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/database","user","password");

        PreparedStatement ment = con.prepareStatement("SELECT EXISTS (SELECT date FROM dataMap WHERE date = ?) AS dateExists;");
        ment.setString(1,date);
        ResultSet ret = ment .executeQuery();

        if (ret.next() && ret.getInt("dateExists") >= 1) out.println("date exit , update err\n");
        else {
            PreparedStatement pment = con.prepareStatement("INSERT INTO picdata (date , json ) VALUES (? , ?)");
            pment.setString(1, date);
            pment.setString(2, json);
            out.println(pment.executeUpdate() != 1 ? "update err\n" : "update ok\n");
        }
    }



}

