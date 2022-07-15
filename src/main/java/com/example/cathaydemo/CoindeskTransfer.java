package com.example.cathaydemo;

import com.example.cathaydemo.db.CurrencyInfoEntity;

import java.util.List;

public class CoindeskTransfer {
    String update_time;
    List<CurrencyInfoEntity> currency_list;

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public List<CurrencyInfoEntity> getCurrency_list() {
        return currency_list;
    }

    public void setCurrency_list(List<CurrencyInfoEntity> currency_list) {
        this.currency_list = currency_list;
    }
}
