package org.jetbrains.actionTracker;

import org.codehaus.jettison.json.JSONObject;

import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by kim on 2017-06-18.
 */
public class ActionDataSender {
    private static final String SEND_API_ADDRESS = "http://13.124.57.236";
    private static final String SEND_API_PATH = "/api";
    private static final int SEND_API_FAIL = -1;

    public static int send(Map<String, Integer> data) {
        if (data.isEmpty()) {
            return SEND_API_FAIL;
        }

        int responseCode = SEND_API_FAIL;
        PrintStream ps = null;
        HttpURLConnection con = null;

        try {
            URL url = new URL(SEND_API_ADDRESS + SEND_API_PATH);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(3000);
            con.setReadTimeout(3000);
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json;charset=utf-8");
            con.setDoOutput(true); //use post mode

            ps = new PrintStream(con.getOutputStream());
            ps.print(mapTojson(data));
            responseCode = con.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("API 호출 실패라능");
        } finally {
            ps.close();
            con.disconnect();
            //System.out.println("response:" + responseCode);
            return responseCode;
        }
    }

    private static String mapTojson(Map<String, Integer> map) throws Exception {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            json.put("action_type", entry.getKey());
            json.put("cnt", entry.getValue());
            json.put("current_dt", System.currentTimeMillis());
        }
        //System.out.println("send:" + json.toString());
        return json.toString();
    }
}
