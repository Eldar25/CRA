import com.sun.net.httpserver.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;


import java.net.HttpURLConnection;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;

import java.sql.*;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;

public class Main {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray json = new JSONArray(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static JSONObject readJsonObjectFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static void insertToBDAirKaz(Connection connection)throws IOException,JSONException{
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        //DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now  = LocalDateTime.now();
        //LocalDateTime yesterday = now.minusDays(1);
        //System.out.println(ymd.format(yesterday));
        //System.out.println(dtf.format(now));
        String[] manual_stationid ={"102","103","104","105","106"};
        //System.out.println("start");

        try{
            //System.out.println("first try");
            JSONArray json = readJsonFromUrl("http://93.185.75.19:4003/simple/averages/last?key=0e576b82bcf14a574b9611061243ddba0c17cd908ad13607b6890f01de3826eb");
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO station_indicators(datee, unit, code, ownid, valuee, stationid, city) values (?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < json.length(); i++) {
                try {
                    //System.out.println("second try");
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT exists(SELECT 1 FROM stations_automatic WHERE stationid = ? )");
                    preparedStatement.setInt(1, (int) json.getJSONObject(i).get("stationId"));
                    ResultSet resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    if (resultSet.getBoolean(1)) {
                        //System.out.println("first if");
                        String date = (String) json.getJSONObject(i).get("date");
                        String unit = (String) json.getJSONObject(i).get("unit");
                        String code = (String) json.getJSONObject(i).get("code");
                        String ownid = (String) json.getJSONObject(i).get("id");
                        String value = json.getJSONObject(i).get("value").toString();
                        Integer stationId = (Integer) json.getJSONObject(i).get("stationId");
                        String district = "Almaty";

                        pstmt.setString(1, date);
                        pstmt.setString(2, unit);
                        pstmt.setString(3, code);
                        pstmt.setString(4, ownid);
                        pstmt.setString(5, value);
                        pstmt.setInt(6, stationId);
                        pstmt.setString(7, district);

                        pstmt.executeUpdate();
//                        System.out.println(json.getJSONObject(i));
//                        System.out.println(i);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for(int j = 0; j < manual_stationid.length; j++) {
                try {
                    //System.out.println("third try");
                    JSONArray jsonArrayManualStations = readJsonFromUrl("http://93.185.75.19:4003/simple/averages?stationNumber=" + manual_stationid[j] + "&after=" + dtf.format(now) + "&key=388e3a08486ccafc50edfd8dc760018276dc711effebf571304769da99e10392");
                    for (int i = 0; i < jsonArrayManualStations.length(); i++) {
                        String date = (String) jsonArrayManualStations.getJSONObject(i).get("date");
                        String unit = (String) jsonArrayManualStations.getJSONObject(i).get("unit");
                        String code = (String) jsonArrayManualStations.getJSONObject(i).get("code");
                        String ownid = (String) jsonArrayManualStations.getJSONObject(i).get("id");
                        String value = jsonArrayManualStations.getJSONObject(i).get("value").toString();
                        Integer stationId = (Integer) jsonArrayManualStations.getJSONObject(i).get("stationId");
                        String district = "Almaty";

                        pstmt.setString(1, date);
                        pstmt.setString(2, unit);
                        pstmt.setString(3, code);
                        pstmt.setString(4, ownid);
                        pstmt.setString(5, value);
                        pstmt.setInt(6, stationId);
                        pstmt.setString(7, district);

                        pstmt.executeUpdate();
//                        System.out.println(jsonArrayManualStations.getJSONObject(i));
//                        System.out.println(i);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
//                    System.out.println("error");
                    System.out.println(e);
                }
            }



        }catch (SQLException e){
            //System.out.println("error");
            e.printStackTrace();
        }

        //System.out.println("End");
    }

    public static void insertToDBOpenweather(Connection connection)throws IOException,JSONException {
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            //System.out.println(dtf.format(now));
            String[] latitude = {"43.291370", "43.252326", "43.227425", "43.200036", "43.298358", "43.238771", "43.209064", "43.341258"};
            String[] longitude = {"76.837422", "76.909587", "76.848968", "76.905190", "76.910984", "76.971149", "76.814046", "76.981018"};
            String[] districts = {"Алатауский", "Алмалинский", "Ауезовсский", "Бостандыгский", "Жетисуский", "Медеуский", "Наурызбайский", "Турксибский"};
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO openweather(district, datetime, temp, feels_like, wind_speed, wind_degree, pressure, humidity) values (?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < districts.length; i++) {
                URL url = new URL("https://api.openweathermap.org/data/2.5/onecall?lat=\"+latitude[i]+\"&lon=\"+longitude[i]+\"&appid=bb08b852f842ee716dd28eddf162016c&units=metric");
                JSONObject jsonObject = readJsonObjectFromUrl("https://api.openweathermap.org/data/2.5/onecall?lat=" + latitude[i] + "&lon=" + longitude[i] + "&appid=bb08b852f842ee716dd28eddf162016c&units=metric");
                //System.out.println(jsonObject.getJSONObject("current").toString());
                //System.out.println(jsonObject.getJSONObject("current").get("weather"));
                //Object weather = jsonObject.getJSONObject("current").get("weather");

                String temp = (String) jsonObject.getJSONObject("current").get("temp").toString();
                String feels_like = (String) jsonObject.getJSONObject("current").get("feels_like").toString();
                String wind_speed = (String) jsonObject.getJSONObject("current").get("wind_speed").toString();
                Integer wind_degree = (Integer) jsonObject.getJSONObject("current").get("wind_deg");
                String pressure = (String) jsonObject.getJSONObject("current").get("pressure").toString();
                String humidity = (String) jsonObject.getJSONObject("current").get("humidity").toString();
                String district = districts[i];
                String datetime = dtf.format(now);

                pstmt.setString(1, district);
                pstmt.setString(2, datetime);
                pstmt.setString(3, temp);
                pstmt.setString(4, feels_like);
                pstmt.setString(5, wind_speed);
                pstmt.setInt(6, wind_degree);
                pstmt.setString(7, pressure);
                pstmt.setString(8, humidity);

                pstmt.executeUpdate();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws IOException, JSONException{
        try{
            String urlDB = "jdbc:postgresql://localhost:5432/pollutantsdb";
            String username = "postgres";
            String password = "ekod25";
            Connection connection = DriverManager.getConnection(urlDB,username,password);
            insertToBDAirKaz(connection);
            insertToDBOpenweather(connection);
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
