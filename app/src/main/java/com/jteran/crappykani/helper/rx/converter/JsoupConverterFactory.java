package com.jteran.crappykani.helper.rx.converter;

import android.support.annotation.NonNull;

import org.jsoup.nodes.Document;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class JsoupConverterFactory extends Converter.Factory {

    public static JsoupConverterFactory create(@NonNull String baseUri) {
        return new JsoupConverterFactory(baseUri);
    }

    private String baseUri;

    private JsoupConverterFactory(String baseUri) {
        this.baseUri = baseUri;
    }

    @Override
    public Converter<ResponseBody, Document> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new JsoupResponseBodyConverter(baseUri);
    }

}
