package io.github.codeutilities.mod.features.social.tab;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.sys.util.chat.TextUtil;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class CodeUtilitiesServer extends WebSocketClient {
    private static List<User> users = Collections.emptyList();

    public CodeUtilitiesServer(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {}

    @Override
    public void onMessage(String message) {
        JsonObject jsonObject = CodeUtilities.JSON_PARSER.parse(message).getAsJsonObject();
        if (jsonObject.get("type").getAsString().equals("users")) {
            List<User> users2 = new ArrayList<>();
            for (JsonElement element :
                    jsonObject.get("content").getAsJsonArray()) {
                users2.add(new User(element.getAsJsonObject()));
            }

            users = users2;
        } else if (jsonObject.get("type").getAsString().equals("chat")) {
            if(MinecraftClient.getInstance().player != null){
                MinecraftClient.getInstance().player.sendMessage(TextUtil.colorCodesToTextComponent(jsonObject.get("content").getAsString()), false);
            }
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        users = Collections.emptyList();
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public static User getUser(String query){
        query = query.replaceAll("-", "");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if(query.length() <= 16 && user.getUsername().equals(query)) return user;
            else if (user.getUuid().equals(query)) return user;
        }
        return null;
    }

    public static List<User> getUsers() {
        return users;
    }

    public static int getUserAmount(){
        return users.size();
    }

}

