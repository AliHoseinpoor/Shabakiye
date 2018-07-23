package com.example.ali.shabakiye.Holders;

/**
 * Created by ali on 7/14/18.
 */

public class User {

    private String pppoe_username;
    private String name;
    private String lastname;
    private String phone_code;
    private String phone;
    private String mobile;
    private String melicode;
    private String service_pahnaye_band;
    private String service_price;
    private String service_day;
    private String service_remain_day;
    private String date_service_start_fa;
    private String date_service_expire_fa;
    private String Traffic;
    private String gender;
    private String service_title;
    private String birthday_year;
    private String birthday_month;
    private String birthday_day;

    public User(String pppoe_username, String name, String lastname, String phone_code, String phone,
                String mobile, String melicode, String service_pahnaye_band, String service_price,
                String service_day, String service_remain_day, String date_service_start_fa,
                String date_service_expire_fa, String traffic, String gender, String service_title,
                String birthday_year, String birthday_month, String birthday_day) {
        this.pppoe_username = pppoe_username;
        this.name = name;
        this.lastname = lastname;
        this.phone_code = phone_code;
        this.phone = phone;
        this.mobile = mobile;
        this.melicode = melicode;
        this.service_pahnaye_band = service_pahnaye_band;
        this.service_price = service_price;
        this.service_day = service_day;
        this.service_remain_day = service_remain_day;
        this.date_service_start_fa = date_service_start_fa;
        this.date_service_expire_fa = date_service_expire_fa;
        Traffic = traffic;
        this.gender = gender;
        this.service_title = service_title;
        this.birthday_year = birthday_year;
        this.birthday_month = birthday_month;
        this.birthday_day = birthday_day;
    }

    public String getPppoe_username() {
        return pppoe_username;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getMobile() {
        return mobile;
    }

    public String getMelicode() {
        return melicode;
    }

    public String getService_pahnaye_band() {
        return service_pahnaye_band;
    }

    public String getService_price() {
        return service_price;
    }

    public String getService_day() {
        return service_day;
    }

    public String getService_remain_day() {
        return service_remain_day;
    }

    public String getDate_service_start_fa() {
        return date_service_start_fa;
    }

    public String getDate_service_expire_fa() {
        return date_service_expire_fa;
    }

    public String getTraffic() {
        return Traffic;
    }

    public String getGender() {
        return gender;
    }

    public String getService_title() {
        return service_title;
    }

    public String getBirthday_year() {
        return birthday_year;
    }

    public String getBirthday_month() {
        return birthday_month;
    }

    public String getBirthday_day() {
        return birthday_day;
    }
}
