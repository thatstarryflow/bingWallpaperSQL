public class Main {
    public static void main(String[] args) {
       Main.run();
    }

    public static void run(){
        for (byte i = 0 ; i < Apis.APIs.length; i++) Apis.ParseAPIData(Tools.getHttpData(Apis.APIs[i]), i);
        String path = "images/" + Apis.needData.get("date").getAsString()+".json";
        Tools.saveToFile(Apis.needData, path);
    }
}
