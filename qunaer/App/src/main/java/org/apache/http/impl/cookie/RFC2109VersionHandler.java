package org.apache.http.impl.cookie;

import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.SetCookie;

@Deprecated
public class RFC2109VersionHandler extends AbstractCookieAttributeHandler {
    public RFC2109VersionHandler() {
        throw new RuntimeException("Stub!");
    }

    public void parse(SetCookie setCookie, String str) {
        throw new RuntimeException("Stub!");
    }

    public void validate(Cookie cookie, CookieOrigin cookieOrigin) {
        throw new RuntimeException("Stub!");
    }
}
