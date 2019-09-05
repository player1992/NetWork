package com.leo.retrofit.bean;

import java.io.Serializable;
import java.util.UUID;

/**
 * <p>Date:2019-09-03.09:57</p>
 * <p>Author:niu bao</p>
 * <p>Desc:</p>
 */
public class Leo implements Serializable{

    private int code;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {


        private String name;
        private String gender;
        private int age;

        @Override
        public String toString() {
            return "DataBean{" +
                    "name='" + name + '\'' +
                    ", gender='" + gender + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Leo{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
