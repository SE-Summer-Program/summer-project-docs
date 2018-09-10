package com.sjtubus.utils;

import android.annotation.SuppressLint;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HgS_1217_ on 2017/2/20.
 */

public class GsonUtilDateTypeAdapter extends TypeAdapter<Date> {
    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.getTime());
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public Date read(JsonReader in) throws IOException {
        if (in.peek() == null) {
            return null;
        }
        String str = in. nextString();
        Date d = null;
        try {
            d = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }
}
