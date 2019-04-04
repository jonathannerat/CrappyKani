package com.jteran.crappykani.wanikani;

import com.jteran.crappykani.exceptions.ElementNotFound;
import com.jteran.crappykani.exceptions.PersonalAccessTokenNotFound;
import com.jteran.crappykani.helper.utils.Constants;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Wanikani web scrapper.
 * <p>
 * It's purpose is to abstract the information retrieval process and network operations, e.g.:
 * login, logout, navigation, apikey, etc. Since most of this operations require a network
 * transaction, it uses a set of tasks to perform different actions.
 */
public final class Scrapper {

    /**
     * Searches for the authenticity_token in the specified document
     *
     * @param document Document to search for token
     * @return authenticity_token
     */
    public static String getAuthenticityTokenFrom(Document document) throws ElementNotFound {
        Elements authenticityTokenElem = document
                .getElementsByAttributeValue("name", Constants.META__CSRF_TOKEN);

        if (!authenticityTokenElem.isEmpty()) {
            return authenticityTokenElem.attr("content");
        } else {
            throw new ElementNotFound("AuthenticityToken was not found in " + document.title());
        }
    }

    public static String getLoginAuthenticityToken(Document loginPage) throws ElementNotFound {
        Elements authenticityTokenElem = loginPage
                .getElementsByAttributeValue("name", Constants.LOGIN_FN__AUTHENTICITY_TOKEN);

        if (!authenticityTokenElem.isEmpty()) {
            return authenticityTokenElem.val();
        } else {
            throw new ElementNotFound("authenticity_token was not found in " + loginPage.title());
        }
    }

    public static String getPersonalAccessToken(Document document) throws PersonalAccessTokenNotFound {
        Elements descriptions = document.getElementsByClass("personal-access-token-description");
        Elements tokens = document.getElementsByClass("personal-access-token-token");

        for (int i = 0; i < descriptions.size(); i++) {
            String description = descriptions
                    .get(i)     // get row
                    .ownText()  // get text
                    .trim();    // remove whitespace

            if (description.equals(Constants.CRAPPYKANI_PAT_NAME)) {
                return tokens
                        .get(i)     // get same row
                        .child(0)   // get <code/> child
                        .ownText()  // get <code/>'s own text
                        .trim();    // remove whitespace
            }
        }

        throw new PersonalAccessTokenNotFound();
    }
}

