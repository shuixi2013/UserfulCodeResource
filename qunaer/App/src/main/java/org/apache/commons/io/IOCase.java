package org.apache.commons.io;

import java.io.Serializable;

public final class IOCase implements Serializable {
    public static final IOCase INSENSITIVE = new IOCase("Insensitive", false);
    public static final IOCase SENSITIVE = new IOCase("Sensitive", true);
    public static final IOCase SYSTEM;
    private static final long serialVersionUID = -6343169151696340687L;
    private final String name;
    private final transient boolean sensitive;

    static {
        boolean z = true;
        String str = "System";
        if (FilenameUtils.isSystemWindows()) {
            z = false;
        }
        SYSTEM = new IOCase(str, z);
    }

    public static IOCase forName(String str) {
        if (SENSITIVE.name.equals(str)) {
            return SENSITIVE;
        }
        if (INSENSITIVE.name.equals(str)) {
            return INSENSITIVE;
        }
        if (SYSTEM.name.equals(str)) {
            return SYSTEM;
        }
        throw new IllegalArgumentException("Invalid IOCase name: " + str);
    }

    private IOCase(String str, boolean z) {
        this.name = str;
        this.sensitive = z;
    }

    private Object readResolve() {
        return forName(this.name);
    }

    public String getName() {
        return this.name;
    }

    public boolean isCaseSensitive() {
        return this.sensitive;
    }

    public int checkCompareTo(String str, String str2) {
        if (str != null && str2 != null) {
            return this.sensitive ? str.compareTo(str2) : str.compareToIgnoreCase(str2);
        } else {
            throw new NullPointerException("The strings must not be null");
        }
    }

    public boolean checkEquals(String str, String str2) {
        if (str != null && str2 != null) {
            return this.sensitive ? str.equals(str2) : str.equalsIgnoreCase(str2);
        } else {
            throw new NullPointerException("The strings must not be null");
        }
    }

    public boolean checkStartsWith(String str, String str2) {
        return str.regionMatches(!this.sensitive, 0, str2, 0, str2.length());
    }

    public boolean checkEndsWith(String str, String str2) {
        int length = str2.length();
        return str.regionMatches(!this.sensitive, str.length() - length, str2, 0, length);
    }

    public int checkIndexOf(String str, int i, String str2) {
        int length = str.length() - str2.length();
        if (length >= i) {
            for (int i2 = i; i2 <= length; i2++) {
                if (checkRegionMatches(str, i2, str2)) {
                    return i2;
                }
            }
        }
        return -1;
    }

    public boolean checkRegionMatches(String str, int i, String str2) {
        return str.regionMatches(!this.sensitive, i, str2, 0, str2.length());
    }

    public String toString() {
        return this.name;
    }
}
