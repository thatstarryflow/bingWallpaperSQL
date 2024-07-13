import com.google.gson.JsonObject;

public class Main {
    public static void main(String[] args) {
       Main.run();
    }

    public static void run(){
        JsonObject data = Apis.mergeData();
        String path = "images/" + data.get("date").getAsString()+".json";
        Tools.saveToFile(data, path);
    }
}
