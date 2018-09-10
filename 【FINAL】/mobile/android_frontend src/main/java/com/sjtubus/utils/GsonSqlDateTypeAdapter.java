package com.sjtubus.utils;

import android.annotation.SuppressLint;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class GsonSqlDateTypeAdapter extends TypeAdapter<Date> {
    @Override
    public void write(JsonWriter out,Date value) throws IOException {
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
        String str = in.nextString();
        Date d = null;
        try {
            d = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(str).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }
}
