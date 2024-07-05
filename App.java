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
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class App {

    static final int TODAY = 0;
    static final int YESTERDAY = 1;

    public static void main(String[] args){
        run();
    }
    
    public static void run(){
        final String todayURL = "https://www.bing.com/hp/api/model?mkt=zh-CN";
        getDaysData(getData(todayURL),TODAY);
        final String yesterdayURL = "https://www.bing.com/hp/api/v1/imagegallery?format=json&mkt=zh-CN";
        getDaysData(getData(yesterdayURL),YESTERDAY);
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

    public static void getDaysData(String data , int day){
        if (data == null) throw new RuntimeException("response is null");
        JsonObject jsonBody = JsonParser.parseString(data).getAsJsonObject();
        String filePath = null;
        JsonElement needSaveData = null;
        String parentDir = "./";
        switch (day) {
            case TODAY ->{
                needSaveData = jsonBody
                    .get("MediaContents").getAsJsonArray().get(0);
                filePath = parentDir + "images/today/" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".json";
            }
            case YESTERDAY ->{
                needSaveData = jsonBody
                    .get("data").getAsJsonObject()
                    .get("images").getAsJsonArray().get(0);
                filePath = parentDir + "images/" + needSaveData.getAsJsonObject().get("isoDate").getAsString() + ".json";
            }
            default -> { throw new RuntimeException("There is no corresponding \"day\" type"); }
        }
        saveToFile(needSaveData, filePath);
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
