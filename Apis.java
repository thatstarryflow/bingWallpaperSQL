import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Apis {

    private static final String[] APIs = { 
        "https://global.bing.com/HPImageArchive.aspx?format=js&idx=0&n=9&pid=hp&FORM=BEHPTB&uhd=1&uhdwidth=3840&uhdheight=2160&setmkt=zh-CN&setlang=en" , 
        "https://www.bing.com/hp/api/model?mkt=zh-CN" 
    };
    private static final byte API1 = 0 , API2 = 1;
    private static JsonObject needData = new JsonObject();

    public static void ParseAPIData(String APIData,byte api) {
        if (APIData == null) throw new RuntimeException("response is null");
        JsonObject jsonBody = JsonParser.parseString(APIData).getAsJsonObject();
        JsonObject ImageData;
        switch (api) {
            case Apis.API1 ->{
                ImageData = jsonBody.get("images").getAsJsonArray().get(0).getAsJsonObject();
                String fullUrl = ImageData.get("url").getAsString();
                int index = fullUrl.indexOf("&");
                boolean hasParmeters = (index > 0);
                Apis.needData.add("date",ImageData.get("enddate"));
                Apis.needData.addProperty("url",hasParmeters ? fullUrl.substring(0, index) : fullUrl);
                Apis.needData.addProperty("parameters", hasParmeters ? fullUrl.substring(index) : "");
            }
            case Apis.API2 ->{
                ImageData = jsonBody.get("MediaContents").getAsJsonArray().get(0).getAsJsonObject();
                JsonObject ImageContent = ImageData.get("ImageContent").getAsJsonObject();
                Apis.needData.add("description",ImageContent.get("Description"));
                Apis.needData.add("head",ImageContent.get("Headline"));
                Apis.needData.add("title",ImageContent.get("Title"));
                Apis.needData.add("copyright", ImageContent.get("Copyright"));
                Apis.needData.add("quickfact",ImageContent.get("QuickFact").getAsJsonObject().get("MainText"));
                Apis.needData.add("datestr", ImageData.get("FullDateString"));
            }
            default ->{ throw new RuntimeException("Not find apiCode " + api); }
        }
    }

    public static void mergeData() throws Exception{
        for (byte i = 0 ; i < Apis.APIs.length; i++) {
            Apis.ParseAPIData(Tools.getHttpData(Apis.APIs[i]), i);
        }
        Tools.insert(needData.get("date").getAsString(),needData.toString());
    }
}
