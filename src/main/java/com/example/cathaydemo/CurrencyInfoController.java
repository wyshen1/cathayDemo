package com.example.cathaydemo;

import com.example.cathaydemo.db.CurrencyInfoEntity;
import com.example.cathaydemo.db.CurrencyInfoRepository;
import com.example.cathaydemo.pojo.Coindesk;
import com.example.cathaydemo.pojo.EUR;
import com.example.cathaydemo.pojo.GBP;
import com.example.cathaydemo.pojo.USD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServlet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/currency")
public class CurrencyInfoController extends HttpServlet {

    @Autowired
    CurrencyInfoRepository currencyInfoRepository;
    @Autowired
    CoindeskService coindeskService;

    /**
     * 查詢所有幣別資訊
     */
    @RequestMapping(value="/", method = RequestMethod.GET)
    public List<CurrencyInfoEntity> read() {
        return currencyInfoRepository.findAll();
    }

    /**
     * 新增幣別資訊
     */
    @RequestMapping(value="/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody CurrencyInfoEntity currencyInfo) {
        currencyInfoRepository.save(currencyInfo);
    }

    /**
     * 更新幣別資訊
     */
    @RequestMapping(value="/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String update(@RequestBody CurrencyInfoEntity currencyInfo) {
        Optional<CurrencyInfoEntity> currency = currencyInfoRepository.findByCode(currencyInfo.getCode());
        if (!currency.isPresent()) {
            return "No such currency";
        }
        currencyInfoRepository.save(currencyInfo);
        return "success";
    }

    /**
     * 刪除指定幣別
     */
    @RequestMapping(value="/{code}", method = RequestMethod.DELETE)
    public boolean delete(@PathVariable String code) {
        boolean isSuccess = currencyInfoRepository.deleteByCode(code) > 0;
        return isSuccess;
    }

    /**
     * 呼叫coindesk API
     */
    @RequestMapping(value="/coindesk", method = RequestMethod.PUT)
    public Coindesk coindeskUpdate() {
        Coindesk coindesk = coindeskService.getCoindesk();
        return coindesk;
    }

    /**
     * 資料轉換API
     */
    @PostConstruct
    @RequestMapping(value="/coindesktransfer", method = RequestMethod.GET)
    public CoindeskTransfer coindeskTransfer() {
        Coindesk coindesk = coindeskService.getCoindesk();

        List<CurrencyInfoEntity> currencyInfoList = new ArrayList<>();
        CurrencyInfoEntity currencyInfo;

        currencyInfo = new CurrencyInfoEntity();
        EUR eur = coindesk.getBpi().getEUR();
        currencyInfo.setCode(eur.getCode());
        currencyInfo.setName("歐元");
        currencyInfo.setRate(eur.getRate_float());
        currencyInfoList.add(currencyInfo);

        currencyInfo = new CurrencyInfoEntity();
        GBP gbp = coindesk.getBpi().getGBP();
        currencyInfo.setCode(gbp.getCode());
        currencyInfo.setName("英鎊");
        currencyInfo.setRate(gbp.getRate_float());
        currencyInfoList.add(currencyInfo);

        currencyInfo = new CurrencyInfoEntity();
        USD usd = coindesk.getBpi().getUSD();
        currencyInfo.setCode(usd.getCode());
        currencyInfo.setName("美金");
        currencyInfo.setRate(usd.getRate_float());
        currencyInfoList.add(currencyInfo);

        currencyInfoRepository.saveAll(currencyInfoList);

        CoindeskTransfer coindeskTransfer = new CoindeskTransfer();
        String updateTime = LocalDateTime.parse(coindesk.getTime().getUpdatedISO(), DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        coindeskTransfer.setUpdate_time(updateTime);
        coindeskTransfer.setCurrency_list(currencyInfoList);

        return coindeskTransfer;
    }

}
