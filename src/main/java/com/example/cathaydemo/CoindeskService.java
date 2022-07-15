package com.example.cathaydemo;

import com.example.cathaydemo.pojo.Coindesk;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CoindeskService {

    public Coindesk getCoindesk() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("https://api.coindesk.com/v1/bpi/currentprice.json");
        CloseableHttpResponse response = null;
        String result = null;
        Coindesk coindesk = null;

        try {
            response = httpclient.execute(httpget);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
                Gson gson = new Gson();
                coindesk = gson.fromJson(result, Coindesk.class);
            } else {
                System.out.println("取得資料失敗");
            }

        } catch (ParseException | IOException e) {
            e.printStackTrace();

        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return coindesk;
    }
}
