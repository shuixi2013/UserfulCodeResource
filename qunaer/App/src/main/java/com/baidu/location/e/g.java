package com.baidu.location.e;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.WeightedLatLng;
import com.mqunar.tools.DateTimeUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class g {
    private static final double[] b = new double[]{45.0d, 135.0d, 225.0d, 315.0d};
    private final d a;
    private final int c;
    private final SQLiteDatabase d;
    private int e = -1;
    private int f = -1;

    final class a {
        private double a;
        private double b;

        private a(double d, double d2) {
            this.a = d;
            this.b = d2;
        }
    }

    enum b {
        AREA("RGCAREA", "area", "addrv", 0, 1000) {
            List<String> a(JSONObject jSONObject, String str, int i) {
                Iterator keys = jSONObject.keys();
                StringBuffer stringBuffer = new StringBuffer();
                StringBuffer stringBuffer2 = new StringBuffer();
                List<String> arrayList = new ArrayList();
                int i2 = 0;
                while (keys.hasNext()) {
                    String str2 = null;
                    String str3 = null;
                    String str4 = null;
                    String str5 = null;
                    String str6 = null;
                    String str7 = null;
                    String str8 = (String) keys.next();
                    try {
                        JSONObject jSONObject2 = jSONObject.getJSONObject(str8);
                        if (jSONObject2.has("cy")) {
                            str2 = jSONObject2.getString("cy");
                        }
                        if (jSONObject2.has("cyc")) {
                            str3 = jSONObject2.getString("cyc");
                        }
                        if (jSONObject2.has("prov")) {
                            str4 = jSONObject2.getString("prov");
                        }
                        if (jSONObject2.has("ctc")) {
                            str5 = jSONObject2.getString("ctc");
                        }
                        if (jSONObject2.has("ct")) {
                            str6 = jSONObject2.getString("ct");
                        }
                        if (jSONObject2.has("dist")) {
                            str7 = jSONObject2.getString("dist");
                        }
                        if (stringBuffer.length() > 0) {
                            stringBuffer.append(",");
                        }
                        stringBuffer.append("(\"").append(str8).append("\",\"").append(str2).append("\",\"").append(str3).append("\",\"").append(str4).append("\",\"").append(str6).append("\",\"").append(str5).append("\",\"").append(str7).append("\",").append(System.currentTimeMillis() / 1000).append(",\"\")");
                        b.b(stringBuffer2, str8, str, 0);
                    } catch (JSONException e) {
                    }
                    if (i2 % 50 == 49 && stringBuffer.length() > 0) {
                        arrayList.add(String.format(Locale.US, "INSERT OR REPLACE INTO %s VALUES %s", new Object[]{"RGCAREA", stringBuffer}));
                        arrayList.add(String.format(Locale.US, "INSERT OR REPLACE INTO %s VALUES %s", new Object[]{"RGCUPDATE", stringBuffer2}));
                        stringBuffer.setLength(0);
                    }
                    i2++;
                }
                if (stringBuffer.length() > 0) {
                    arrayList.add(String.format(Locale.US, "INSERT OR REPLACE INTO %s VALUES %s", new Object[]{"RGCAREA", stringBuffer}));
                    arrayList.add(String.format(Locale.US, "INSERT OR REPLACE INTO %s VALUES %s", new Object[]{"RGCUPDATE", stringBuffer2}));
                    stringBuffer.setLength(0);
                }
                arrayList.add(String.format(Locale.US, "DELETE FROM RGCAREA WHERE gridkey NOT IN (SELECT gridkey FROM RGCAREA LIMIT %d);", new Object[]{Integer.valueOf(i)}));
                return arrayList;
            }
        },
        ROAD("RGCROAD", "road", "addrv", 1000, LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL) {
            List<String> a(JSONObject jSONObject, String str, int i) {
                Iterator keys = jSONObject.keys();
                List<String> arrayList = new ArrayList();
                StringBuffer stringBuffer = new StringBuffer();
                while (keys.hasNext()) {
                    JSONArray jSONArray;
                    JSONArray jSONArray2 = null;
                    StringBuffer stringBuffer2 = new StringBuffer();
                    String str2 = (String) keys.next();
                    b.b(stringBuffer, str2, str, 0);
                    try {
                        jSONArray = jSONObject.getJSONArray(str2);
                    } catch (JSONException e) {
                        jSONArray = jSONArray2;
                    }
                    if (jSONArray != null) {
                        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                            Object obj = null;
                            Object obj2 = null;
                            Object obj3 = null;
                            Object obj4 = null;
                            String str3 = null;
                            try {
                                JSONObject jSONObject2 = jSONArray.getJSONObject(i2);
                                if (jSONObject2.has("st")) {
                                    str3 = jSONObject2.getString("st");
                                }
                                if (jSONObject2.has("x1")) {
                                    obj = Double.valueOf(jSONObject2.getDouble("x1"));
                                }
                                if (jSONObject2.has("y1")) {
                                    obj2 = Double.valueOf(jSONObject2.getDouble("y1"));
                                }
                                if (jSONObject2.has("x2")) {
                                    obj3 = Double.valueOf(jSONObject2.getDouble("x2"));
                                }
                                if (jSONObject2.has("y2")) {
                                    obj4 = Double.valueOf(jSONObject2.getDouble("y2"));
                                }
                                if (!(str3 == null || obj == null || obj2 == null || obj3 == null || obj4 == null)) {
                                    if (stringBuffer2.length() > 0) {
                                        stringBuffer2.append(",");
                                    }
                                    stringBuffer2.append("(NULL,\"").append(str2).append("\",\"").append(str3).append("\",").append(obj).append(",").append(obj2).append(",").append(obj3).append(",").append(obj4).append(")");
                                }
                            } catch (JSONException e2) {
                            }
                            if (i2 % 50 == 49 && stringBuffer2.length() > 0) {
                                arrayList.add(String.format(Locale.US, "INSERT OR REPLACE INTO %s VALUES %s", new Object[]{"RGCROAD", stringBuffer2.toString()}));
                                stringBuffer2.setLength(0);
                            }
                        }
                        if (stringBuffer2.length() > 0) {
                            arrayList.add(String.format(Locale.US, "INSERT OR REPLACE INTO %s VALUES %s", new Object[]{"RGCROAD", stringBuffer2.toString()}));
                        }
                    }
                }
                if (stringBuffer.length() > 0) {
                    arrayList.add(String.format(Locale.US, "INSERT OR REPLACE INTO %s VALUES %s", new Object[]{"RGCUPDATE", stringBuffer}));
                }
                arrayList.add(String.format(Locale.US, "DELETE FROM RGCROAD WHERE _id NOT IN (SELECT _id FROM RGCROAD LIMIT %d);", new Object[]{Integer.valueOf(i)}));
                return arrayList;
            }
        },
        SITE("RGCSITE", "site", "addrv", 100, 50000) {
            List<String> a(JSONObject jSONObject, String str, int i) {
                Iterator keys = jSONObject.keys();
                List<String> arrayList = new ArrayList();
                StringBuffer stringBuffer = new StringBuffer();
                while (keys.hasNext()) {
                    JSONArray jSONArray;
                    JSONArray jSONArray2 = null;
                    StringBuffer stringBuffer2 = new StringBuffer();
                    String str2 = (String) keys.next();
                    b.b(stringBuffer, str2, str, 0);
                    try {
                        jSONArray = jSONObject.getJSONArray(str2);
                    } catch (JSONException e) {
                        jSONArray = jSONArray2;
                    }
                    if (jSONArray != null) {
                        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                            Object obj = null;
                            Object obj2 = null;
                            String str3 = null;
                            String str4 = null;
                            try {
                                JSONObject jSONObject2 = jSONArray.getJSONObject(i2);
                                if (jSONObject2.has("st")) {
                                    str3 = jSONObject2.getString("st");
                                }
                                if (jSONObject2.has("stn")) {
                                    str4 = jSONObject2.getString("stn");
                                }
                                if (jSONObject2.has(MapViewConstants.ATTR_X)) {
                                    obj = Double.valueOf(jSONObject2.getDouble(MapViewConstants.ATTR_X));
                                }
                                if (jSONObject2.has(MapViewConstants.ATTR_Y)) {
                                    obj2 = Double.valueOf(jSONObject2.getDouble(MapViewConstants.ATTR_Y));
                                }
                                if (stringBuffer2.length() > 0) {
                                    stringBuffer2.append(",");
                                }
                                stringBuffer2.append("(NULL,\"").append(str2).append("\",\"").append(str3).append("\",\"").append(str4).append("\",").append(obj).append(",").append(obj2).append(")");
                            } catch (JSONException e2) {
                            }
                            if (i2 % 50 == 49 && stringBuffer2.length() > 0) {
                                arrayList.add(String.format(Locale.US, "INSERT OR REPLACE INTO %s VALUES %s", new Object[]{"RGCSITE", stringBuffer2.toString()}));
                                stringBuffer2.setLength(0);
                            }
                        }
                        if (stringBuffer2.length() > 0) {
                            arrayList.add(String.format(Locale.US, "INSERT OR REPLACE INTO %s VALUES %s", new Object[]{"RGCSITE", stringBuffer2.toString()}));
                        }
                    }
                }
                if (stringBuffer.length() > 0) {
                    arrayList.add(String.format(Locale.US, "INSERT OR REPLACE INTO %s VALUES %s", new Object[]{"RGCUPDATE", stringBuffer}));
                }
                arrayList.add(String.format(Locale.US, "DELETE FROM RGCSITE WHERE _id NOT IN (SELECT _id FROM RGCSITE LIMIT %d);", new Object[]{Integer.valueOf(i)}));
                return arrayList;
            }
        },
        POI("RGCPOI", "poi", "poiv", 1000, 5000) {
            List<String> a(JSONObject jSONObject, String str, int i) {
                Iterator keys = jSONObject.keys();
                List<String> arrayList = new ArrayList();
                StringBuffer stringBuffer = new StringBuffer();
                while (keys.hasNext()) {
                    JSONArray jSONArray;
                    JSONArray jSONArray2 = null;
                    StringBuffer stringBuffer2 = new StringBuffer();
                    String str2 = (String) keys.next();
                    b.b(stringBuffer, str2, str, 1);
                    try {
                        jSONArray = jSONObject.getJSONArray(str2);
                    } catch (JSONException e) {
                        jSONArray = jSONArray2;
                    }
                    if (jSONArray != null) {
                        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                            Object obj = null;
                            Object obj2 = null;
                            String str3 = null;
                            String str4 = null;
                            String str5 = null;
                            Object obj3 = null;
                            try {
                                JSONObject jSONObject2 = jSONArray.getJSONObject(i2);
                                if (jSONObject2.has("pid")) {
                                    str3 = jSONObject2.getString("pid");
                                }
                                if (jSONObject2.has("ne")) {
                                    str4 = jSONObject2.getString("ne");
                                }
                                if (jSONObject2.has("tp")) {
                                    str5 = jSONObject2.getString("tp");
                                }
                                if (jSONObject2.has("rk")) {
                                    obj3 = Integer.valueOf(jSONObject2.getInt("rk"));
                                }
                                if (jSONObject2.has(MapViewConstants.ATTR_X)) {
                                    obj = Double.valueOf(jSONObject2.getDouble(MapViewConstants.ATTR_X));
                                }
                                if (jSONObject2.has(MapViewConstants.ATTR_Y)) {
                                    obj2 = Double.valueOf(jSONObject2.getDouble(MapViewConstants.ATTR_Y));
                                }
                                if (stringBuffer2.length() > 0) {
                                    stringBuffer2.append(",");
                                }
                                stringBuffer2.append("(\"").append(str3).append("\",\"").append(str2).append("\",\"").append(str4).append("\",\"").append(str5).append("\",").append(obj).append(",").append(obj2).append(",").append(obj3).append(")");
                            } catch (JSONException e2) {
                            }
                            if (i2 % 50 == 49) {
                                arrayList.add(String.format(Locale.US, "INSERT OR REPLACE INTO %s VALUES %s", new Object[]{"RGCPOI", stringBuffer2.toString()}));
                                stringBuffer2.setLength(0);
                            }
                        }
                        if (stringBuffer2.length() > 0) {
                            arrayList.add(String.format(Locale.US, "INSERT OR REPLACE INTO %s VALUES %s", new Object[]{"RGCPOI", stringBuffer2.toString()}));
                        }
                    }
                }
                if (stringBuffer.length() > 0) {
                    arrayList.add(String.format(Locale.US, "INSERT OR REPLACE INTO %s VALUES %s", new Object[]{"RGCUPDATE", stringBuffer}));
                }
                arrayList.add(String.format(Locale.US, "DELETE FROM RGCPOI WHERE pid NOT IN (SELECT pid FROM RGCPOI LIMIT %d);", new Object[]{Integer.valueOf(i)}));
                return arrayList;
            }
        };
        
        private final int e;
        private final String f;
        private final String g;
        private final String h;
        private final int i;

        private b(String str, String str2, String str3, int i, int i2) {
            this.f = str;
            this.g = str2;
            this.h = str3;
            this.e = i;
            this.i = i2;
        }

        private String a(int i, double d, double d2) {
            HashSet hashSet = new HashSet();
            hashSet.add(g.b(i, d, d2));
            double d3 = ((double) this.e) * 1.414d;
            if (this.e > 0) {
                for (double a : g.b) {
                    double[] a2 = g.b(d2, d, d3, a);
                    hashSet.add(g.b(i, a2[1], a2[0]));
                }
            }
            StringBuffer stringBuffer = new StringBuffer();
            Iterator it = hashSet.iterator();
            Object obj = 1;
            while (it.hasNext()) {
                String str = (String) it.next();
                if (obj != null) {
                    obj = null;
                } else {
                    stringBuffer.append(',');
                }
                stringBuffer.append("\"").append(str).append("\"");
            }
            return String.format("SELECT * FROM %s WHERE gridkey IN (%s);", new Object[]{this.f, stringBuffer.toString()});
        }

        private String a(JSONObject jSONObject) {
            Iterator keys = jSONObject.keys();
            StringBuffer stringBuffer = new StringBuffer();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                if (stringBuffer.length() != 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append("\"").append(str).append("\"");
            }
            return String.format(Locale.US, "DELETE FROM %s WHERE gridkey IN (%s)", new Object[]{this.f, stringBuffer});
        }

        private static void b(StringBuffer stringBuffer, String str, String str2, int i) {
            if (stringBuffer.length() > 0) {
                stringBuffer.append(",");
            }
            stringBuffer.append("(\"").append(str).append("\",\"").append(str2).append("\",").append(i).append(",").append(System.currentTimeMillis() / DateTimeUtils.ONE_DAY).append(")");
        }

        abstract List<String> a(JSONObject jSONObject, String str, int i);
    }

    g(d dVar, SQLiteDatabase sQLiteDatabase, int i) {
        this.a = dVar;
        this.d = sQLiteDatabase;
        this.c = i;
        if (this.d != null && this.d.isOpen()) {
            try {
                this.d.execSQL("CREATE TABLE IF NOT EXISTS RGCAREA(gridkey VARCHAR(10) PRIMARY KEY, country VARCHAR(100),countrycode VARCHAR(100), province VARCHAR(100), city VARCHAR(100), citycode VARCHAR(100), district VARCHAR(100), timestamp INTEGER, version VARCHAR(50))");
                this.d.execSQL("CREATE TABLE IF NOT EXISTS RGCROAD(_id INTEGER PRIMARY KEY AUTOINCREMENT, gridkey VARCHAR(10), street VARCHAR(100), x1 DOUBLE, y1 DOUBLE, x2 DOUBLE, y2 DOUBLE)");
                this.d.execSQL("CREATE TABLE IF NOT EXISTS RGCSITE(_id INTEGER PRIMARY KEY AUTOINCREMENT, gridkey VARCHAR(10), street VARCHAR(100), streetnumber VARCHAR(100), x DOUBLE, y DOUBLE)");
                this.d.execSQL("CREATE TABLE IF NOT EXISTS RGCPOI(pid VARCHAR(50) PRIMARY KEY , gridkey VARCHAR(10), name VARCHAR(100), type VARCHAR(50), x DOUBLE, y DOUBLE, rank INTEGER)");
                this.d.execSQL("CREATE TABLE IF NOT EXISTS RGCUPDATE(gridkey VARCHAR(10), version VARCHAR(50), type INTEGER, timestamp INTEGER, PRIMARY KEY(gridkey, type))");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private double a(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = ((d5 - d3) * (d - d3)) + ((d6 - d4) * (d2 - d4));
        if (d7 <= 0.0d) {
            return Math.sqrt(((d - d3) * (d - d3)) + ((d2 - d4) * (d2 - d4)));
        }
        double d8 = ((d5 - d3) * (d5 - d3)) + ((d6 - d4) * (d6 - d4));
        if (d7 >= d8) {
            return Math.sqrt(((d - d5) * (d - d5)) + ((d2 - d6) * (d2 - d6)));
        }
        d7 /= d8;
        d8 = ((d5 - d3) * d7) + d3;
        d7 = (d7 * (d6 - d4)) + d4;
        return Math.sqrt(((d7 - d2) * (d7 - d2)) + ((d - d8) * (d - d8)));
    }

    private static int a(int i, int i2) {
        double d;
        int i3;
        if (100 > i2) {
            d = -0.1d;
            i3 = 60000;
        } else if (500 > i2) {
            d = -0.75d;
            i3 = 55500;
        } else {
            d = -0.5d;
            i3 = 0;
        }
        return ((int) ((d * ((double) i2)) + ((double) i3))) + i;
    }

    private static String b(int i, double d, double d2) {
        Object obj = 1;
        int i2 = i * 5;
        char[] cArr = new char[(i + 1)];
        a aVar = new a(90.0d, -90.0d);
        a aVar2 = new a(180.0d, -180.0d);
        int i3 = 1;
        int i4 = 0;
        while (i3 <= i2) {
            double d3;
            a aVar3;
            int i5;
            int i6;
            if (obj != null) {
                d3 = d;
                aVar3 = aVar2;
            } else {
                d3 = d2;
                aVar3 = aVar;
            }
            double a = (aVar3.b + aVar3.a) / 2.0d;
            i4 <<= 1;
            if (((int) (d3 * 1000000.0d)) > ((int) (1000000.0d * a))) {
                aVar3.b = a;
                i5 = i4 | 1;
            } else {
                aVar3.a = a;
                i5 = i4;
            }
            if (i3 % 5 == 0) {
                cArr[(i3 / 5) - 1] = "0123456789bcdefghjkmnpqrstuvwxyz".charAt(i5);
                i6 = 0;
            } else {
                i6 = i5;
            }
            i3++;
            obj = obj == null ? 1 : null;
            i4 = i6;
        }
        cArr[i] = '\u0000';
        StringBuffer stringBuffer = new StringBuffer();
        for (int i7 = 0; i7 < i; i7++) {
            stringBuffer.append(cArr[i7]);
        }
        return stringBuffer.toString();
    }

    private static double[] b(double d, double d2, double d3, double d4) {
        double[] dArr = new double[2];
        double toRadians = Math.toRadians(d);
        double toRadians2 = Math.toRadians(d2);
        double toRadians3 = Math.toRadians(d4);
        double asin = Math.asin((Math.sin(toRadians) * Math.cos(d3 / 6378137.0d)) + ((Math.cos(toRadians) * Math.sin(d3 / 6378137.0d)) * Math.cos(toRadians3)));
        toRadians = Math.atan2((Math.sin(toRadians3) * Math.sin(d3 / 6378137.0d)) * Math.cos(toRadians), Math.cos(d3 / 6378137.0d) - (Math.sin(toRadians) * Math.sin(asin))) + toRadians2;
        dArr[0] = Math.toDegrees(asin);
        dArr[1] = Math.toDegrees(toRadians);
        return dArr;
    }

    private double c(double d, double d2, double d3, double d4) {
        double d5 = d4 - d2;
        double d6 = d3 - d;
        double toRadians = Math.toRadians(d);
        Math.toRadians(d2);
        double toRadians2 = Math.toRadians(d3);
        Math.toRadians(d4);
        d5 = Math.toRadians(d5);
        d6 = Math.toRadians(d6);
        d5 = (Math.sin(d5 / 2.0d) * ((Math.cos(toRadians) * Math.cos(toRadians2)) * Math.sin(d5 / 2.0d))) + (Math.sin(d6 / 2.0d) * Math.sin(d6 / 2.0d));
        return (Math.atan2(Math.sqrt(d5), Math.sqrt(WeightedLatLng.DEFAULT_INTENSITY - d5)) * 2.0d) * 6378137.0d;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    com.baidu.location.Address a(double r32, double r34) {
        /*
        r31 = this;
        r24 = 0;
        r23 = 0;
        r22 = 0;
        r21 = 0;
        r20 = 0;
        r25 = 0;
        r12 = 0;
        r11 = 0;
        r10 = 0;
        r4 = com.baidu.location.e.g.b.SITE;	 Catch:{ Exception -> 0x011b, all -> 0x012f }
        r0 = r31;
        r5 = r0.c;	 Catch:{ Exception -> 0x011b, all -> 0x012f }
        r6 = r32;
        r8 = r34;
        r4 = r4.a(r5, r6, r8);	 Catch:{ Exception -> 0x011b, all -> 0x012f }
        r0 = r31;
        r5 = r0.d;	 Catch:{ Exception -> 0x011b, all -> 0x012f }
        r6 = 0;
        r13 = r5.rawQuery(r4, r6);	 Catch:{ Exception -> 0x011b, all -> 0x012f }
        r4 = r13.moveToFirst();	 Catch:{ Exception -> 0x02de, all -> 0x02db }
        if (r4 == 0) goto L_0x0322;
    L_0x002c:
        r4 = 9218868437227405311; // 0x7fefffffffffffff float:NaN double:1.7976931348623157E308;
        r16 = r4;
        r14 = r11;
        r15 = r12;
    L_0x0035:
        r4 = r13.isAfterLast();	 Catch:{ Exception -> 0x02ea, all -> 0x02db }
        if (r4 != 0) goto L_0x0074;
    L_0x003b:
        r4 = 2;
        r19 = r13.getString(r4);	 Catch:{ Exception -> 0x02ea, all -> 0x02db }
        r4 = 3;
        r18 = r13.getString(r4);	 Catch:{ Exception -> 0x02ea, all -> 0x02db }
        r4 = 5;
        r9 = r13.getDouble(r4);	 Catch:{ Exception -> 0x02ea, all -> 0x02db }
        r4 = 4;
        r11 = r13.getDouble(r4);	 Catch:{ Exception -> 0x02ea, all -> 0x02db }
        r4 = r31;
        r5 = r34;
        r7 = r32;
        r6 = r4.c(r5, r7, r9, r11);	 Catch:{ Exception -> 0x02ea, all -> 0x02db }
        r4 = (r6 > r16 ? 1 : (r6 == r16 ? 0 : -1));
        if (r4 >= 0) goto L_0x031c;
    L_0x005d:
        r4 = com.baidu.location.e.g.b.SITE;	 Catch:{ Exception -> 0x02ea, all -> 0x02db }
        r4 = r4.e;	 Catch:{ Exception -> 0x02ea, all -> 0x02db }
        r4 = (double) r4;
        r4 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r4 > 0) goto L_0x031c;
    L_0x0068:
        r4 = r18;
        r5 = r19;
    L_0x006c:
        r13.moveToNext();	 Catch:{ Exception -> 0x02e4, all -> 0x02db }
        r16 = r6;
        r14 = r4;
        r15 = r5;
        goto L_0x0035;
    L_0x0074:
        r5 = r14;
        r6 = r15;
    L_0x0076:
        if (r13 == 0) goto L_0x0317;
    L_0x0078:
        r13.close();	 Catch:{ Exception -> 0x0115 }
        r17 = r5;
        r10 = r6;
    L_0x007e:
        if (r17 != 0) goto L_0x013f;
    L_0x0080:
        r11 = 0;
        r4 = com.baidu.location.e.g.b.ROAD;	 Catch:{ Exception -> 0x0258, all -> 0x0267 }
        r0 = r31;
        r5 = r0.c;	 Catch:{ Exception -> 0x0258, all -> 0x0267 }
        r6 = r32;
        r8 = r34;
        r4 = r4.a(r5, r6, r8);	 Catch:{ Exception -> 0x0258, all -> 0x0267 }
        r0 = r31;
        r5 = r0.d;	 Catch:{ Exception -> 0x0258, all -> 0x0267 }
        r6 = 0;
        r18 = r5.rawQuery(r4, r6);	 Catch:{ Exception -> 0x0258, all -> 0x0267 }
        r4 = r18.moveToFirst();	 Catch:{ Exception -> 0x02d0, all -> 0x02ce }
        if (r4 == 0) goto L_0x0314;
    L_0x009e:
        r26 = 9218868437227405311; // 0x7fefffffffffffff float:NaN double:1.7976931348623157E308;
        r4 = "wgs842mc";
        r0 = r32;
        r2 = r34;
        r29 = com.baidu.location.Jni.coorEncrypt(r0, r2, r4);	 Catch:{ Exception -> 0x02d0, all -> 0x02ce }
        r19 = r10;
    L_0x00af:
        r4 = r18.isAfterLast();	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        if (r4 != 0) goto L_0x0137;
    L_0x00b5:
        r4 = 2;
        r0 = r18;
        r28 = r0.getString(r4);	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r4 = 3;
        r0 = r18;
        r4 = r0.getDouble(r4);	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r6 = 4;
        r0 = r18;
        r6 = r0.getDouble(r6);	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r8 = "wgs842mc";
        r4 = com.baidu.location.Jni.coorEncrypt(r4, r6, r8);	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r5 = 5;
        r0 = r18;
        r5 = r0.getDouble(r5);	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r7 = 6;
        r0 = r18;
        r7 = r0.getDouble(r7);	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r9 = "wgs842mc";
        r15 = com.baidu.location.Jni.coorEncrypt(r5, r7, r9);	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r5 = 0;
        r5 = r29[r5];	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r7 = 1;
        r7 = r29[r7];	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r9 = 0;
        r9 = r4[r9];	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r11 = 1;
        r11 = r4[r11];	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r4 = 0;
        r13 = r15[r4];	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r4 = 1;
        r15 = r15[r4];	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r4 = r31;
        r4 = r4.a(r5, r7, r9, r11, r13, r15);	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r6 = (r4 > r26 ? 1 : (r4 == r26 ? 0 : -1));
        if (r6 >= 0) goto L_0x030e;
    L_0x0100:
        r6 = com.baidu.location.e.g.b.ROAD;	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r6 = r6.e;	 Catch:{ Exception -> 0x02d5, all -> 0x02ce }
        r6 = (double) r6;
        r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r6 > 0) goto L_0x030e;
    L_0x010b:
        r10 = r28;
    L_0x010d:
        r18.moveToNext();	 Catch:{ Exception -> 0x02d0, all -> 0x02ce }
        r26 = r4;
        r19 = r10;
        goto L_0x00af;
    L_0x0115:
        r4 = move-exception;
        r17 = r5;
        r10 = r6;
        goto L_0x007e;
    L_0x011b:
        r4 = move-exception;
        r4 = r10;
        r5 = r11;
        r6 = r12;
    L_0x011f:
        if (r4 == 0) goto L_0x0317;
    L_0x0121:
        r4.close();	 Catch:{ Exception -> 0x0129 }
        r17 = r5;
        r10 = r6;
        goto L_0x007e;
    L_0x0129:
        r4 = move-exception;
        r17 = r5;
        r10 = r6;
        goto L_0x007e;
    L_0x012f:
        r4 = move-exception;
        r13 = r10;
    L_0x0131:
        if (r13 == 0) goto L_0x0136;
    L_0x0133:
        r13.close();	 Catch:{ Exception -> 0x029f }
    L_0x0136:
        throw r4;
    L_0x0137:
        r4 = r19;
    L_0x0139:
        if (r18 == 0) goto L_0x030b;
    L_0x013b:
        r18.close();	 Catch:{ Exception -> 0x0254 }
        r10 = r4;
    L_0x013f:
        r4 = com.baidu.location.e.g.b.AREA;
        r0 = r31;
        r5 = r0.c;
        r6 = r32;
        r8 = r34;
        r5 = r4.a(r5, r6, r8);
        r4 = 0;
        r0 = r31;
        r6 = r0.d;	 Catch:{ Exception -> 0x0270, all -> 0x0293 }
        r7 = 0;
        r4 = r6.rawQuery(r5, r7);	 Catch:{ Exception -> 0x0270, all -> 0x0293 }
        r5 = r4.moveToFirst();	 Catch:{ Exception -> 0x0270, all -> 0x02a9 }
        if (r5 == 0) goto L_0x02fd;
    L_0x015d:
        r5 = r4.isAfterLast();	 Catch:{ Exception -> 0x0270, all -> 0x02a9 }
        if (r5 != 0) goto L_0x02fd;
    L_0x0163:
        r5 = "country";
        r5 = r4.getColumnIndex(r5);	 Catch:{ Exception -> 0x0270, all -> 0x02a9 }
        r9 = r4.getString(r5);	 Catch:{ Exception -> 0x0270, all -> 0x02a9 }
        r5 = "countrycode";
        r5 = r4.getColumnIndex(r5);	 Catch:{ Exception -> 0x02b0, all -> 0x02a9 }
        r8 = r4.getString(r5);	 Catch:{ Exception -> 0x02b0, all -> 0x02a9 }
        r5 = "province";
        r5 = r4.getColumnIndex(r5);	 Catch:{ Exception -> 0x02ba, all -> 0x02a9 }
        r7 = r4.getString(r5);	 Catch:{ Exception -> 0x02ba, all -> 0x02a9 }
        r5 = "city";
        r5 = r4.getColumnIndex(r5);	 Catch:{ Exception -> 0x02c2, all -> 0x02a9 }
        r6 = r4.getString(r5);	 Catch:{ Exception -> 0x02c2, all -> 0x02a9 }
        r5 = "citycode";
        r5 = r4.getColumnIndex(r5);	 Catch:{ Exception -> 0x02c8, all -> 0x02a9 }
        r5 = r4.getString(r5);	 Catch:{ Exception -> 0x02c8, all -> 0x02a9 }
        r11 = "district";
        r11 = r4.getColumnIndex(r11);	 Catch:{ Exception -> 0x02cc, all -> 0x02a9 }
        r25 = r4.getString(r11);	 Catch:{ Exception -> 0x02cc, all -> 0x02a9 }
        r11 = r9;
        r9 = r8;
        r8 = r7;
        r7 = r6;
        r6 = r5;
        r5 = r25;
    L_0x01a6:
        if (r4 == 0) goto L_0x01ab;
    L_0x01a8:
        r4.close();	 Catch:{ Exception -> 0x02a4 }
    L_0x01ab:
        if (r11 == 0) goto L_0x01bb;
    L_0x01ad:
        r4 = new java.lang.String;
        r11 = r11.getBytes();
        r11 = com.baidu.location.b.a.b.a(r11);
        r4.<init>(r11);
        r11 = r4;
    L_0x01bb:
        if (r9 == 0) goto L_0x01cb;
    L_0x01bd:
        r4 = new java.lang.String;
        r9 = r9.getBytes();
        r9 = com.baidu.location.b.a.b.a(r9);
        r4.<init>(r9);
        r9 = r4;
    L_0x01cb:
        if (r8 == 0) goto L_0x01db;
    L_0x01cd:
        r4 = new java.lang.String;
        r8 = r8.getBytes();
        r8 = com.baidu.location.b.a.b.a(r8);
        r4.<init>(r8);
        r8 = r4;
    L_0x01db:
        if (r7 == 0) goto L_0x01eb;
    L_0x01dd:
        r4 = new java.lang.String;
        r7 = r7.getBytes();
        r7 = com.baidu.location.b.a.b.a(r7);
        r4.<init>(r7);
        r7 = r4;
    L_0x01eb:
        if (r6 == 0) goto L_0x01fb;
    L_0x01ed:
        r4 = new java.lang.String;
        r6 = r6.getBytes();
        r6 = com.baidu.location.b.a.b.a(r6);
        r4.<init>(r6);
        r6 = r4;
    L_0x01fb:
        if (r5 == 0) goto L_0x020b;
    L_0x01fd:
        r4 = new java.lang.String;
        r5 = r5.getBytes();
        r5 = com.baidu.location.b.a.b.a(r5);
        r4.<init>(r5);
        r5 = r4;
    L_0x020b:
        if (r10 == 0) goto L_0x021b;
    L_0x020d:
        r4 = new java.lang.String;
        r10 = r10.getBytes();
        r10 = com.baidu.location.b.a.b.a(r10);
        r4.<init>(r10);
        r10 = r4;
    L_0x021b:
        if (r17 == 0) goto L_0x02f0;
    L_0x021d:
        r4 = new java.lang.String;
        r12 = r17.getBytes();
        r12 = com.baidu.location.b.a.b.a(r12);
        r4.<init>(r12);
    L_0x022a:
        r12 = new com.baidu.location.Address$Builder;
        r12.<init>();
        r11 = r12.country(r11);
        r9 = r11.countryCode(r9);
        r8 = r9.province(r8);
        r7 = r8.city(r7);
        r6 = r7.cityCode(r6);
        r5 = r6.district(r5);
        r5 = r5.street(r10);
        r4 = r5.streetNumber(r4);
        r4 = r4.build();
        return r4;
    L_0x0254:
        r5 = move-exception;
        r10 = r4;
        goto L_0x013f;
    L_0x0258:
        r4 = move-exception;
        r5 = r11;
        r4 = r10;
    L_0x025b:
        if (r5 == 0) goto L_0x030b;
    L_0x025d:
        r5.close();	 Catch:{ Exception -> 0x0263 }
        r10 = r4;
        goto L_0x013f;
    L_0x0263:
        r5 = move-exception;
        r10 = r4;
        goto L_0x013f;
    L_0x0267:
        r4 = move-exception;
        r18 = r11;
    L_0x026a:
        if (r18 == 0) goto L_0x026f;
    L_0x026c:
        r18.close();	 Catch:{ Exception -> 0x02a2 }
    L_0x026f:
        throw r4;
    L_0x0270:
        r5 = move-exception;
        r5 = r20;
        r6 = r21;
        r7 = r22;
        r8 = r23;
        r9 = r24;
    L_0x027b:
        if (r4 == 0) goto L_0x02f4;
    L_0x027d:
        r4.close();	 Catch:{ Exception -> 0x0289 }
        r11 = r9;
        r9 = r8;
        r8 = r7;
        r7 = r6;
        r6 = r5;
        r5 = r25;
        goto L_0x01ab;
    L_0x0289:
        r4 = move-exception;
        r11 = r9;
        r9 = r8;
        r8 = r7;
        r7 = r6;
        r6 = r5;
        r5 = r25;
        goto L_0x01ab;
    L_0x0293:
        r5 = move-exception;
        r30 = r5;
        r5 = r4;
        r4 = r30;
    L_0x0299:
        if (r5 == 0) goto L_0x029e;
    L_0x029b:
        r5.close();	 Catch:{ Exception -> 0x02a7 }
    L_0x029e:
        throw r4;
    L_0x029f:
        r5 = move-exception;
        goto L_0x0136;
    L_0x02a2:
        r5 = move-exception;
        goto L_0x026f;
    L_0x02a4:
        r4 = move-exception;
        goto L_0x01ab;
    L_0x02a7:
        r5 = move-exception;
        goto L_0x029e;
    L_0x02a9:
        r5 = move-exception;
        r30 = r5;
        r5 = r4;
        r4 = r30;
        goto L_0x0299;
    L_0x02b0:
        r5 = move-exception;
        r5 = r20;
        r6 = r21;
        r7 = r22;
        r8 = r23;
        goto L_0x027b;
    L_0x02ba:
        r5 = move-exception;
        r5 = r20;
        r6 = r21;
        r7 = r22;
        goto L_0x027b;
    L_0x02c2:
        r5 = move-exception;
        r5 = r20;
        r6 = r21;
        goto L_0x027b;
    L_0x02c8:
        r5 = move-exception;
        r5 = r20;
        goto L_0x027b;
    L_0x02cc:
        r11 = move-exception;
        goto L_0x027b;
    L_0x02ce:
        r4 = move-exception;
        goto L_0x026a;
    L_0x02d0:
        r4 = move-exception;
        r5 = r18;
        r4 = r10;
        goto L_0x025b;
    L_0x02d5:
        r4 = move-exception;
        r5 = r18;
        r4 = r19;
        goto L_0x025b;
    L_0x02db:
        r4 = move-exception;
        goto L_0x0131;
    L_0x02de:
        r4 = move-exception;
        r4 = r13;
        r5 = r11;
        r6 = r12;
        goto L_0x011f;
    L_0x02e4:
        r6 = move-exception;
        r6 = r5;
        r5 = r4;
        r4 = r13;
        goto L_0x011f;
    L_0x02ea:
        r4 = move-exception;
        r4 = r13;
        r5 = r14;
        r6 = r15;
        goto L_0x011f;
    L_0x02f0:
        r4 = r17;
        goto L_0x022a;
    L_0x02f4:
        r11 = r9;
        r9 = r8;
        r8 = r7;
        r7 = r6;
        r6 = r5;
        r5 = r25;
        goto L_0x01ab;
    L_0x02fd:
        r5 = r25;
        r6 = r20;
        r7 = r21;
        r8 = r22;
        r9 = r23;
        r11 = r24;
        goto L_0x01a6;
    L_0x030b:
        r10 = r4;
        goto L_0x013f;
    L_0x030e:
        r4 = r26;
        r10 = r19;
        goto L_0x010d;
    L_0x0314:
        r4 = r10;
        goto L_0x0139;
    L_0x0317:
        r17 = r5;
        r10 = r6;
        goto L_0x007e;
    L_0x031c:
        r6 = r16;
        r4 = r14;
        r5 = r15;
        goto L_0x006c;
    L_0x0322:
        r5 = r11;
        r6 = r12;
        goto L_0x0076;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.e.g.a(double, double):com.baidu.location.Address");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void a(org.json.JSONObject r9) {
        /*
        r8 = this;
        r0 = r8.d;
        if (r0 == 0) goto L_0x0075;
    L_0x0004:
        r0 = r8.d;
        r0 = r0.isOpen();
        if (r0 == 0) goto L_0x0075;
    L_0x000c:
        r0 = r8.d;	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r0.beginTransaction();	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r2 = com.baidu.location.e.g.b.values();	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r3 = r2.length;	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r0 = 0;
        r1 = r0;
    L_0x0018:
        if (r1 >= r3) goto L_0x007a;
    L_0x001a:
        r4 = r2[r1];	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r0 = r4.g;	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r0 = r9.has(r0);	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        if (r0 == 0) goto L_0x0076;
    L_0x0026:
        r0 = "";
        r5 = r4.h;	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r5 = r9.has(r5);	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        if (r5 == 0) goto L_0x003a;
    L_0x0032:
        r0 = r4.h;	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r0 = r9.getString(r0);	 Catch:{ Exception -> 0x006f, all -> 0x008d }
    L_0x003a:
        r5 = new java.util.ArrayList;	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r5.<init>();	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r6 = r4.g;	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r6 = r9.getJSONObject(r6);	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r7 = r4.a(r6);	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r5.add(r7);	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r7 = r4.i;	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r0 = r4.a(r6, r0, r7);	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r5.addAll(r0);	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r4 = r5.iterator();	 Catch:{ Exception -> 0x006f, all -> 0x008d }
    L_0x005d:
        r0 = r4.hasNext();	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        if (r0 == 0) goto L_0x0076;
    L_0x0063:
        r0 = r4.next();	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r0 = (java.lang.String) r0;	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r5 = r8.d;	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r5.execSQL(r0);	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        goto L_0x005d;
    L_0x006f:
        r0 = move-exception;
        r0 = r8.d;	 Catch:{ Exception -> 0x0096 }
        r0.endTransaction();	 Catch:{ Exception -> 0x0096 }
    L_0x0075:
        return;
    L_0x0076:
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x0018;
    L_0x007a:
        r0 = r8.d;	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r0.setTransactionSuccessful();	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r0 = -1;
        r8.e = r0;	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r0 = -1;
        r8.f = r0;	 Catch:{ Exception -> 0x006f, all -> 0x008d }
        r0 = r8.d;	 Catch:{ Exception -> 0x008b }
        r0.endTransaction();	 Catch:{ Exception -> 0x008b }
        goto L_0x0075;
    L_0x008b:
        r0 = move-exception;
        goto L_0x0075;
    L_0x008d:
        r0 = move-exception;
        r1 = r8.d;	 Catch:{ Exception -> 0x0094 }
        r1.endTransaction();	 Catch:{ Exception -> 0x0094 }
    L_0x0093:
        throw r0;
    L_0x0094:
        r1 = move-exception;
        goto L_0x0093;
    L_0x0096:
        r0 = move-exception;
        goto L_0x0075;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.e.g.a(org.json.JSONObject):void");
    }

    boolean a() {
        Cursor rawQuery;
        Throwable th;
        Cursor cursor = null;
        if (this.a.l().l() && this.f == -1 && this.e == -1 && this.d != null && this.d.isOpen()) {
            try {
                rawQuery = this.d.rawQuery("SELECT COUNT(*) FROM RGCSITE;", null);
                try {
                    rawQuery.moveToFirst();
                    this.f = rawQuery.getInt(0);
                    cursor = this.d.rawQuery("SELECT COUNT(*) FROM RGCAREA;", null);
                    cursor.moveToFirst();
                    this.e = cursor.getInt(0);
                    if (rawQuery != null) {
                        try {
                            rawQuery.close();
                        } catch (Exception e) {
                        }
                    }
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Exception e2) {
                        }
                    }
                } catch (Exception e3) {
                    if (rawQuery != null) {
                        try {
                            rawQuery.close();
                        } catch (Exception e4) {
                        }
                    }
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Exception e5) {
                        }
                    }
                    return this.f == 0 ? false : false;
                } catch (Throwable th2) {
                    th = th2;
                    if (rawQuery != null) {
                        try {
                            rawQuery.close();
                        } catch (Exception e6) {
                        }
                    }
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Exception e7) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e8) {
                rawQuery = null;
                if (rawQuery != null) {
                    rawQuery.close();
                }
                if (cursor != null) {
                    cursor.close();
                }
                if (this.f == 0) {
                }
            } catch (Throwable th3) {
                th = th3;
                rawQuery = null;
                if (rawQuery != null) {
                    rawQuery.close();
                }
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }
        if (this.f == 0 && this.e == 0) {
            return true;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    java.util.List<com.baidu.location.Poi> b(double r18, double r20) {
        /*
        r17 = this;
        r7 = 0;
        r13 = new java.util.ArrayList;
        r13.<init>();
        r1 = com.baidu.location.e.g.b.POI;
        r0 = r17;
        r2 = r0.c;
        r3 = r18;
        r5 = r20;
        r3 = r1.a(r2, r3, r5);
        r1 = 0;
        r2 = 0;
        r0 = r17;
        r4 = r0.d;	 Catch:{ Exception -> 0x009b, all -> 0x00a5 }
        r5 = 0;
        r11 = r4.rawQuery(r3, r5);	 Catch:{ Exception -> 0x009b, all -> 0x00a5 }
        r3 = r11.moveToFirst();	 Catch:{ Exception -> 0x00b3, all -> 0x00b1 }
        if (r3 == 0) goto L_0x0090;
    L_0x0025:
        r12 = r2;
        r10 = r1;
    L_0x0027:
        r1 = r11.isAfterLast();	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        if (r1 != 0) goto L_0x008f;
    L_0x002d:
        r1 = 0;
        r14 = r11.getString(r1);	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r1 = 2;
        r15 = r11.getString(r1);	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r1 = 4;
        r8 = r11.getDouble(r1);	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r1 = 5;
        r6 = r11.getDouble(r1);	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r1 = 6;
        r16 = r11.getInt(r1);	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r1 = r17;
        r2 = r20;
        r4 = r18;
        r2 = r1.c(r2, r4, r6, r8);	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r1 = com.baidu.location.e.g.b.POI;	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r1 = r1.e;	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r4 = (double) r1;	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r1 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r1 >= 0) goto L_0x00ba;
    L_0x005b:
        r1 = new com.baidu.location.Poi;	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r4 = new java.lang.String;	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r5 = r14.getBytes();	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r5 = com.baidu.location.b.a.b.a(r5);	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r4.<init>(r5);	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r5 = new java.lang.String;	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r6 = r15.getBytes();	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r6 = com.baidu.location.b.a.b.a(r6);	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r5.<init>(r6);	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r6 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r1.<init>(r4, r5, r6);	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r2 = (float) r2;	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r2 = java.lang.Math.round(r2);	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        r0 = r16;
        r2 = a(r0, r2);	 Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
        if (r2 <= r12) goto L_0x00ba;
    L_0x0089:
        r11.moveToNext();	 Catch:{ Exception -> 0x00b3, all -> 0x00b1 }
        r12 = r2;
        r10 = r1;
        goto L_0x0027;
    L_0x008f:
        r1 = r10;
    L_0x0090:
        if (r11 == 0) goto L_0x0095;
    L_0x0092:
        r11.close();	 Catch:{ Exception -> 0x00ad }
    L_0x0095:
        if (r1 == 0) goto L_0x009a;
    L_0x0097:
        r13.add(r1);
    L_0x009a:
        return r13;
    L_0x009b:
        r2 = move-exception;
        r2 = r7;
    L_0x009d:
        if (r2 == 0) goto L_0x0095;
    L_0x009f:
        r2.close();	 Catch:{ Exception -> 0x00a3 }
        goto L_0x0095;
    L_0x00a3:
        r2 = move-exception;
        goto L_0x0095;
    L_0x00a5:
        r1 = move-exception;
        r11 = r7;
    L_0x00a7:
        if (r11 == 0) goto L_0x00ac;
    L_0x00a9:
        r11.close();	 Catch:{ Exception -> 0x00af }
    L_0x00ac:
        throw r1;
    L_0x00ad:
        r2 = move-exception;
        goto L_0x0095;
    L_0x00af:
        r2 = move-exception;
        goto L_0x00ac;
    L_0x00b1:
        r1 = move-exception;
        goto L_0x00a7;
    L_0x00b3:
        r2 = move-exception;
        r2 = r11;
        goto L_0x009d;
    L_0x00b6:
        r1 = move-exception;
        r1 = r10;
        r2 = r11;
        goto L_0x009d;
    L_0x00ba:
        r2 = r12;
        r1 = r10;
        goto L_0x0089;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.e.g.b(double, double):java.util.List<com.baidu.location.Poi>");
    }

    JSONObject b() {
        Cursor cursor = null;
        Cursor cursor2 = null;
        JSONObject jSONObject = new JSONObject();
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        int currentTimeMillis = (int) (System.currentTimeMillis() / DateTimeUtils.ONE_DAY);
        String str = "SELECT * FROM RGCUPDATE WHERE type=%d AND %d > timestamp+%d ORDER BY gridkey";
        String str2 = "UPDATE RGCUPDATE SET timestamp=timestamp+1 WHERE type = %d AND gridkey IN (%s)";
        try {
            if (this.d != null && this.d.isOpen()) {
                HashSet hashSet;
                String string;
                String[] strArr;
                JSONObject jSONObject2;
                JSONArray jSONArray = new JSONArray();
                JSONArray jSONArray2 = new JSONArray();
                JSONArray jSONArray3 = new JSONArray();
                JSONArray jSONArray4 = new JSONArray();
                cursor2 = this.d.rawQuery(String.format(str, new Object[]{Integer.valueOf(0), Integer.valueOf(currentTimeMillis), Integer.valueOf(this.a.l().p())}), null);
                cursor = this.d.rawQuery(String.format(str, new Object[]{Integer.valueOf(1), Integer.valueOf(currentTimeMillis), Integer.valueOf(this.a.l().q())}), null);
                if (cursor2.moveToFirst()) {
                    hashSet = new HashSet();
                    while (!cursor2.isAfterLast()) {
                        str = cursor2.getString(0);
                        string = cursor2.getString(1);
                        jSONArray3.put(str);
                        hashSet.add(string);
                        if (stringBuffer2.length() > 0) {
                            stringBuffer2.append(",");
                        }
                        stringBuffer2.append("\"").append(str).append("\"");
                        cursor2.moveToNext();
                    }
                    strArr = new String[hashSet.size()];
                    hashSet.toArray(strArr);
                    for (Object put : strArr) {
                        jSONArray4.put(put);
                    }
                }
                if (cursor.moveToFirst()) {
                    hashSet = new HashSet();
                    while (!cursor.isAfterLast()) {
                        str = cursor.getString(0);
                        string = cursor.getString(1);
                        jSONArray.put(str);
                        hashSet.add(string);
                        if (stringBuffer.length() > 0) {
                            stringBuffer.append(",");
                        }
                        stringBuffer.append("\"").append(str).append("\"");
                        cursor.moveToNext();
                    }
                    strArr = new String[hashSet.size()];
                    hashSet.toArray(strArr);
                    for (Object put2 : strArr) {
                        jSONArray2.put(put2);
                    }
                }
                if (jSONArray3.length() != 0) {
                    jSONObject2 = new JSONObject();
                    jSONObject2.put("gk", jSONArray3);
                    jSONObject2.put("ver", jSONArray4);
                    jSONObject.put("addr", jSONObject2);
                }
                if (jSONArray.length() != 0) {
                    jSONObject2 = new JSONObject();
                    jSONObject2.put("gk", jSONArray);
                    jSONObject2.put("ver", jSONArray2);
                    jSONObject.put("poi", jSONObject2);
                }
            }
            if (stringBuffer2.length() > 0) {
                this.d.execSQL(String.format(Locale.US, str2, new Object[]{Integer.valueOf(0), stringBuffer2.toString()}));
            }
            if (stringBuffer.length() > 0) {
                this.d.execSQL(String.format(Locale.US, str2, new Object[]{Integer.valueOf(1), stringBuffer.toString()}));
            }
            if (cursor2 != null) {
                try {
                    cursor2.close();
                } catch (Exception e) {
                }
            }
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                }
            }
        } catch (Exception e3) {
            if (cursor2 != null) {
                try {
                    cursor2.close();
                } catch (Exception e4) {
                }
            }
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e5) {
                }
            }
        } catch (Throwable th) {
            if (cursor2 != null) {
                try {
                    cursor2.close();
                } catch (Exception e6) {
                }
            }
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e7) {
                }
            }
        }
        return (jSONObject.has("poi") || jSONObject.has("addr")) ? jSONObject : null;
    }
}
