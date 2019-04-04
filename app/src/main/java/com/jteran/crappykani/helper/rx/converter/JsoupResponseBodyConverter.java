package com.jteran.crappykani.helper.rx.converter;

import android.support.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class JsoupResponseBodyConverter implements Converter<ResponseBody, Document> {

    private String baseUri;

    JsoupResponseBodyConverter(String baseUri) {
        this.baseUri = baseUri;
    }

    @Override
    public Document convert(@NonNull ResponseBody value) throws IOException {
        try {
            return Jsoup.parse(value.byteStream(), null, baseUri);
        } finally {
            value.close();
        }
    }
}
