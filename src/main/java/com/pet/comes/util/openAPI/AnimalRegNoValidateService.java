package com.pet.comes.util.openAPI;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class AnimalRegNoValidateService {

        public String isValidAnimalRegNo(String animalRegNo, String ownerBirth, String ownerName) throws IOException {
            StringBuilder urlBuilder = new StringBuilder("http://openapi.animal.go.kr/openapi/service/rest/animalInfoSrvc/animalInfo"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=서비스키"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("dog_reg_no","UTF-8") + "=" + URLEncoder.encode(animalRegNo, "UTF-8")); /*동물등록번호*/
            urlBuilder.append("&" + URLEncoder.encode("rfid_cd","UTF-8") + "=" + URLEncoder.encode(animalRegNo, "UTF-8")); /*RFID번호*/
            urlBuilder.append("&" + URLEncoder.encode("owner_birth","UTF-8") + "=" + URLEncoder.encode(ownerBirth, "UTF-8")); /*소유자 생년월일*/
            urlBuilder.append("&" + URLEncoder.encode("owner_nm","UTF-8") + "=" + URLEncoder.encode(ownerName, "UTF-8")); /*소유자 성명*/

            URL url = new URL(urlBuilder.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;

            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
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
            // System.out.println(sb.toString());
        }
}

