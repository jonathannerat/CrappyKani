package com.jteran.crappykani.wanikani;

import android.support.annotation.NonNull;

import com.jteran.crappykani.exceptions.PersonalAccessTokenNotFound;
import com.jteran.crappykani.helper.rx.RxHelper;
import com.jteran.crappykani.helper.utils.Constants;
import com.jteran.crappykani.manager.preferences.PrefManager;
import com.jteran.crappykani.models.credential.LoginCredentials;
import com.jteran.crappykani.wanikani.service.ScrapperService;
import com.jteran.crappykani.wanikani.service.SessionService;
import com.jteran.crappykani.wanikani.service.SettingsService;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

public abstract class Wanikani {

    /**
     * Logins to the Wanikani site. Fetches cookies and apiKeys / PATs from settings page.
     *
     * @param loginCredentials Credentials used to login
     * @param listener         callback to call once the process is completed
     * @return a Disposable to cancel the process
     */
    public static Disposable login(@NonNull LoginCredentials loginCredentials, @NonNull LoginListener listener) {
        final String RELATIVE_URL_PAT = "settings/personal_access_tokens";

        SessionService session = RestProvider.getSessionService();
        ScrapperService scrapper = RestProvider.getScrapperService();
        SettingsService settings = RestProvider.getSettingsService();

        if (PrefManager.isUserLoggedIn()) return Completable.complete().subscribe();

        Completable login = scrapper
                .navigateTo("login")  // Get login page
                .map(Scrapper::getLoginAuthenticityToken)
                .flatMapCompletable(
                        authToken -> session.login(
                                loginCredentials.getUsername(),
                                loginCredentials.getPassword(),
                                loginCredentials.getRememberMe(),
                                Constants.UTF8_TICK,
                                authToken)
                );

        Completable fetchPAT = Completable.defer(() ->
                scrapper.navigateTo(RELATIVE_URL_PAT)
                        .map(Scrapper::getPersonalAccessToken)
                        .flatMapCompletable(PAT -> {
                            PrefManager.setPAT(PAT);
                            return Completable.complete();
                        })
        );

        Completable createPAT = scrapper.navigateTo(RELATIVE_URL_PAT)
                .map(Scrapper::getAuthenticityTokenFrom)
                .flatMapCompletable(authToken ->
                        settings.createPersonalAccessToken(
                                Constants.CRAPPYKANI_PAT_NAME,
                                "1",
                                "1",
                                "1",
                                "1",
                                "1",
                                Constants.UTF8_TICK,
                                authToken
                        ));

        return RxHelper.getCompletable(
                login
                        .andThen(fetchPAT)
                        .onErrorResumeNext(
                                throwable -> {
                                    if (throwable instanceof PersonalAccessTokenNotFound)
                                        return createPAT.andThen(fetchPAT);
                                    else
                                        return Completable.error(throwable);
                                }
                        )
        ).subscribe(
                listener::onLoginSuccess,
                listener::onLoginError
        );
    }

    public static Disposable logout(LogoutListener listener) {
        ScrapperService scrapper = RestProvider.getScrapperService();

        Completable logout = scrapper.navigateTo("dashboard")
                .map(Scrapper::getAuthenticityTokenFrom)
                .flatMapCompletable(
                        authToken -> RestProvider.getSessionService().logout("delete", authToken)
                );

        return RxHelper.getCompletable(logout).subscribe(
                listener::onLogoutSuccess,
                listener::onLogoutError
        );
    }


    public interface LoginListener {
        void onLoginSuccess();

        void onLoginError(Throwable t);
    }

    public interface LogoutListener {
        void onLogoutSuccess();

        void onLogoutError(Throwable t);
    }
}
