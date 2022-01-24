package com.pet.comes.util.openAPI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class AnimalRegNoValidateService {

    @Value("${openAPI.serviceKey}")
    private String serviceKey;

    public String isValidAnimalRegNo(String animalRegNo, String ownerBirth, String ownerName) throws IOException {

        StringBuilder urlBuilder = new StringBuilder("http://openapi.animal.go.kr/openapi/service/rest/animalInfoSrvc/animalInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("dog_reg_no", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(animalRegNo, StandardCharsets.UTF_8)); /*동물등록번호*/
        urlBuilder.append("&" + URLEncoder.encode("rfid_cd","UTF-8") + "=" + URLEncoder.encode("410000000227825", "UTF-8")); /*RFID번호*/
        urlBuilder.append("&" + URLEncoder.encode("owner_birth", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(ownerBirth, StandardCharsets.UTF_8)); /*소유자 생년월일*/
        urlBuilder.append("&" + URLEncoder.encode("owner_nm", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(ownerName, StandardCharsets.UTF_8)); /*소유자 성명*/

        URL url = new URL(urlBuilder.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        return sb.toString();
    }
}

