package com.example.cathaydemo.test;

import com.example.cathaydemo.CathaydemoApplication;
import com.example.cathaydemo.CoindeskService;
import com.example.cathaydemo.CoindeskTransfer;
import com.example.cathaydemo.CurrencyInfoController;
import com.example.cathaydemo.db.CurrencyInfoEntity;
import com.example.cathaydemo.db.CurrencyInfoRepository;
import com.example.cathaydemo.pojo.Coindesk;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CathaydemoApplication.class)
public class currencyApiTest {

    Gson gson = new Gson();
    CurrencyInfoEntity currencyInfo;

    @Autowired
    CurrencyInfoController currencyInfoController;
    @Autowired
    CurrencyInfoRepository currencyInfoRepository;
    @Autowired
    CoindeskService coindeskService;

    /**
     * 測試呼叫查詢幣別對應表資料 API，並顯示其內容。
     */
    @Test
    public void readCurrencyInfo(){
        List<CurrencyInfoEntity> currencyInfoList = currencyInfoController.read();
        boolean actual = (currencyInfoList!=null);
        assertEquals(true, actual);
        System.out.println(gson.toJson(currencyInfoList));
    }

    /**
     * 測試呼叫新增幣別對應表資料 API。
     */
    @Test
    public void createCurrencyInfo(){
        currencyInfo = new CurrencyInfoEntity();
        currencyInfo.setCode("TWD");
        currencyInfo.setName("台幣");
        currencyInfo.setRate(1);
        currencyInfoController.create(currencyInfo);

        boolean actual = currencyInfoRepository.findByCode("TWD").isPresent();
        assertEquals(true, actual);

        System.out.println(gson.toJson(currencyInfoController.read()));
    }

    /**
     * 測試呼叫更新幣別對應表資料 API，並顯示其內容。
     */
    @Test
    public void updateCurrencyInfo(){

        currencyInfo = new CurrencyInfoEntity();
        currencyInfo.setCode("USD");
        currencyInfo.setName("美元");
        currencyInfo.setRate(1);

        String actual = currencyInfoController.update(currencyInfo);
        assertEquals("success", actual);

        System.out.println(gson.toJson(currencyInfoController.read()));
    }

    /**
     * 測試呼叫刪除幣別對應表資料 API。
     */
    @Test
    public void deleteCurrencyInfo(){

        boolean actual = currencyInfoController.delete("USD");
        assertEquals(true, actual);

        System.out.println(gson.toJson(currencyInfoController.read()));
    }

    /**
     * 測試呼叫 coindesk API，並顯示其內容。
     */
    @Test
    public void requestCoindeskApi(){
        Coindesk coindesk = coindeskService.getCoindesk();
        assertEquals(true, coindesk != null);

        System.out.println(gson.toJson(coindesk));
    }

    /**
     * 測試呼叫資料轉換的 API，並顯示其內容
     */
    @Test
    public void requestCurrencyTransfer(){
        CoindeskTransfer coindeskTransfer = currencyInfoController.coindeskTransfer();
        assertEquals(true, coindeskTransfer != null);

        System.out.println(gson.toJson(coindeskTransfer));
    }

}
