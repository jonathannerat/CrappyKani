package com.jteran.crappykani.wanikani;

import com.jteran.crappykani.exceptions.ElementNotFound;
import com.jteran.crappykani.exceptions.PersonalAccessTokenNotFound;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ScrapperTest {

    @Test
    public void fetchAuthenticityToken_isSuccessful() throws ElementNotFound {
        Document document = Jsoup.parse("<meta name=\"csrf-token\" content=\"1\"/>");

        assertEquals("1", Scrapper.getAuthenticityTokenFrom(document));
    }

    @Test(expected = ElementNotFound.class)
    public void fetchAuthenticityToken_notFound() throws ElementNotFound {
        Document document = Jsoup.parse("<meta name=\"_\" content=\"1\"/>");

        assertNotEquals("1", Scrapper.getAuthenticityTokenFrom(document));
    }

    @Test
    public void fetchLoginAuthenticityToken_isSuccessful() throws ElementNotFound {
        Document document = Jsoup.parse("<input name=\"authenticity_token\" value=\"1\"/>");

        assertEquals("1", Scrapper.getLoginAuthenticityToken(document));
    }

    @Test(expected = ElementNotFound.class)
    public void fetchLoginAuthenticityToken_notFound() throws ElementNotFound {
        Document document = Jsoup.parse("<input name=\"_\" value=\"1\"/>");

        assertNotEquals("1", Scrapper.getLoginAuthenticityToken(document));
    }

    @Test
    public void fetchPersonalAccessToken_isSuccessful() throws PersonalAccessTokenNotFound {
        String html =
                "<ul>" +
                        "  <li class=\"personal-access-token-description\"> Default Access Token </li>" +
                        "  <li class=\"personal-access-token-description\"> _crappykani_token </li>" +
                        "</ul>" +
                        "<ul>" +
                        "  <li class=\"personal-access-token-token\"><code> default_token </code></li>" +
                        "  <li class=\"personal-access-token-token\"><code> 1 </code></li>" +
                        "</ul>";

        Document document = Jsoup.parse(html);

        assertEquals("1", Scrapper.getPersonalAccessToken(document));
    }


    @Test(expected = PersonalAccessTokenNotFound.class)
    public void fetchPersonalAccessToken_notFound() throws PersonalAccessTokenNotFound {
        String html =
                "<ul>" +
                        "  <li class=\"personal-access-token-description\"> Default Access Token </li>" +
                        "  <li class=\"personal-access-token-description\"> _ </li>" +
                        "</ul>" +
                        "<ul>" +
                        "  <li class=\"personal-access-token-token\"> default_token </li>" +
                        "  <li class=\"personal-access-token-token\"><code> 1 </code></li>" +
                        "</ul>";

        Document document = Jsoup.parse(html);

        assertNotEquals("1", Scrapper.getPersonalAccessToken(document));
    }

}
