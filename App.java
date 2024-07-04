/**
 * 
 * @author github@thatstarryflow
 * @date 2024/07/03 20:14
 */

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class App {

    public static void main(String[] args){
        run();
    }
    
    public static void run(){
        final String URL = "https://www.bing.com/hp/api/v1/imagegallery?format=json&mkt=zh-CN";
        String data = getData(URL);
        saveToFile(data);
    }

    public static String getData(String httpURL){
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

    public static void saveToFile(String data){
        if (data == null) throw new RuntimeException("response is null");
        JsonObject jsonBody = JsonParser.parseString(data).getAsJsonObject();
        JsonArray jsonBody_data_image = jsonBody
            .get("data").getAsJsonObject()
            .get("images").getAsJsonArray();
        JsonObject todayImageData = jsonBody_data_image.get(0).getAsJsonObject();
        String date = todayImageData.get("isoDate").getAsString();
        String filePath = "./images/" + date + ".json";
        try {
            createFile(filePath);
            writeFile(filePath,todayImageData.toString());
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
