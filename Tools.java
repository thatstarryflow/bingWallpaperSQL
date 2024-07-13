import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonElement;

public class Tools {

    public static String getHttpData(String httpURL){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(httpURL)).build();
        HttpResponse<String> response;
        try {
            response = client
                .send(request,HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) { throw new RuntimeException(e); }
        return response != null ? response.body() : null;
    }

    public static void saveToFile(JsonElement jEle,String filePath){
        try {
            createFile(filePath);
            writeFile(filePath,jEle.toString());
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    public static void writeFile(String filePath,String content){
        FileOutputStream fos ;
        try {
            fos = new FileOutputStream(filePath, true);
            fos.write(content.getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    public static void createFile(String newFilePath){
        File file = new File(newFilePath);
        File parentDir = file.getParentFile();
        try {
            if (!parentDir.exists()) if(!parentDir.mkdirs()) throw new RuntimeException("Make new directory failed");
            if(!file.exists()) file.createNewFile(); else throw new RuntimeException("File is exist");
         }catch (Exception e){ throw new RuntimeException(e); }
    } 

}
