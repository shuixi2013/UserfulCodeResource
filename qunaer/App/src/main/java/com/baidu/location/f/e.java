package com.baidu.location.f;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.http.Headers;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.baidu.location.Jni;
import com.baidu.location.a.h;
import com.baidu.location.a.j;
import com.baidu.location.f;
import com.baidu.location.h.i;
import com.mqunar.tools.DateTimeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import org.apache.http.HttpStatus;

public class e extends g {
    private static e c = null;
    private static int k = 0;
    private static String r = null;
    private final long a = 1000;
    private final long b = 9000;
    private Context d;
    private LocationManager e = null;
    private Location f;
    private b g = null;
    private c h = null;
    private GpsStatus i;
    private a j = null;
    private long l = 0;
    private boolean m = false;
    private boolean n = false;
    private String o = null;
    private boolean p = false;
    private long q = 0;
    private Handler s = null;
    private final int t = 1;
    private final int u = 2;
    private final int v = 3;
    private final int w = 4;
    private int x;
    private int y;
    private HashMap<Integer, List<GpsSatellite>> z;

    class a implements Listener, NmeaListener {
        long a;
        final /* synthetic */ e b;
        private long c;
        private final int d;
        private boolean e;
        private List<String> f;
        private String g;
        private String h;
        private String i;
        private long j;

        private a(e eVar) {
            this.b = eVar;
            this.a = 0;
            this.c = 0;
            this.d = HttpStatus.SC_BAD_REQUEST;
            this.e = false;
            this.f = new ArrayList();
            this.g = null;
            this.h = null;
            this.i = null;
            this.j = 0;
        }

        public void a(String str) {
            if (System.currentTimeMillis() - this.c > 400 && this.e && this.f.size() > 0) {
                try {
                    h hVar = new h(this.f, this.g, this.h, this.i);
                    if (hVar.a()) {
                        i.d = this.b.a(hVar, this.b.y);
                        if (i.d > 0) {
                            e.r = String.format(Locale.CHINA, "&nmea=%.1f|%.1f&g_tp=%d", new Object[]{Double.valueOf(hVar.c()), Double.valueOf(hVar.b()), Integer.valueOf(i.d)});
                        }
                    } else {
                        i.d = 0;
                    }
                } catch (Exception e) {
                    i.d = 0;
                }
                this.f.clear();
                this.i = null;
                this.h = null;
                this.g = null;
                this.e = false;
            }
            if (str.startsWith("$GPGGA")) {
                this.e = true;
                this.g = str.trim();
            } else if (str.startsWith("$GPGSV")) {
                this.f.add(str.trim());
            } else if (str.startsWith("$GPGSA")) {
                this.i = str.trim();
            }
            this.c = System.currentTimeMillis();
        }

        public void onGpsStatusChanged(int i) {
            if (this.b.e != null) {
                switch (i) {
                    case 2:
                        this.b.d(null);
                        this.b.b(false);
                        e.k = 0;
                        return;
                    case 4:
                        if (this.b.n) {
                            try {
                                if (this.b.i == null) {
                                    this.b.i = this.b.e.getGpsStatus(null);
                                } else {
                                    this.b.e.getGpsStatus(this.b.i);
                                }
                                this.b.x = 0;
                                this.b.y = 0;
                                this.b.z = new HashMap();
                                int i2 = 0;
                                for (GpsSatellite gpsSatellite : this.b.i.getSatellites()) {
                                    if (gpsSatellite.usedInFix()) {
                                        i2++;
                                        if (gpsSatellite.getSnr() >= ((float) i.H)) {
                                            this.b.y = this.b.y + 1;
                                        }
                                        this.b.a(gpsSatellite, this.b.z);
                                    }
                                }
                                if (i2 > 0) {
                                    this.j = System.currentTimeMillis();
                                    e.k = i2;
                                    return;
                                } else if (System.currentTimeMillis() - this.j > 100) {
                                    this.j = System.currentTimeMillis();
                                    e.k = i2;
                                    return;
                                } else {
                                    return;
                                }
                            } catch (Exception e) {
                                return;
                            }
                        }
                        return;
                    default:
                        return;
                }
            }
        }

        public void onNmeaReceived(long j, String str) {
            if (!this.b.n) {
                return;
            }
            if (!com.baidu.location.c.c.a().g) {
                i.d = 0;
            } else if (str != null && !str.equals("") && str.length() >= 9 && str.length() <= 150 && this.b.i()) {
                this.b.s.sendMessage(this.b.s.obtainMessage(2, str));
            }
        }
    }

    class b implements LocationListener {
        final /* synthetic */ e a;

        private b(e eVar) {
            this.a = eVar;
        }

        public void onLocationChanged(Location location) {
            this.a.q = System.currentTimeMillis();
            this.a.b(true);
            this.a.d(location);
            this.a.m = false;
        }

        public void onProviderDisabled(String str) {
            this.a.d(null);
            this.a.b(false);
        }

        public void onProviderEnabled(String str) {
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
            switch (i) {
                case 0:
                    this.a.d(null);
                    this.a.b(false);
                    return;
                case 1:
                    this.a.l = System.currentTimeMillis();
                    this.a.m = true;
                    this.a.b(false);
                    return;
                case 2:
                    this.a.m = false;
                    return;
                default:
                    return;
            }
        }
    }

    class c implements LocationListener {
        final /* synthetic */ e a;
        private long b;

        private c(e eVar) {
            this.a = eVar;
            this.b = 0;
        }

        public void onLocationChanged(Location location) {
            if (!this.a.n && location != null && location.getProvider() == "gps" && System.currentTimeMillis() - this.b >= 10000 && j.a(location, false)) {
                this.b = System.currentTimeMillis();
                this.a.s.sendMessage(this.a.s.obtainMessage(4, location));
            }
        }

        public void onProviderDisabled(String str) {
        }

        public void onProviderEnabled(String str) {
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    private e() {
    }

    private int a(h hVar, int i) {
        if (k >= i.E) {
            return 1;
        }
        if (k <= i.D) {
            return 4;
        }
        double c = hVar.c();
        if (c <= ((double) i.z)) {
            return 1;
        }
        if (c >= ((double) i.A)) {
            return 4;
        }
        c = hVar.b();
        return c > ((double) i.B) ? c >= ((double) i.C) ? 4 : i < i.G ? i <= i.F ? 4 : this.z != null ? a(this.z) : 3 : 1 : 1;
    }

    private int a(HashMap<Integer, List<GpsSatellite>> hashMap) {
        if (this.x > 4) {
            List arrayList = new ArrayList();
            List arrayList2 = new ArrayList();
            int i = 0;
            for (Entry value : hashMap.entrySet()) {
                int i2;
                List list = (List) value.getValue();
                if (list != null) {
                    Object a = a(list);
                    if (a != null) {
                        arrayList.add(a);
                        i2 = i + 1;
                        arrayList2.add(Integer.valueOf(i));
                        i = i2;
                    }
                }
                i2 = i;
                i = i2;
            }
            if (!arrayList.isEmpty()) {
                double[] dArr;
                double[] dArr2 = new double[2];
                int size = arrayList.size();
                for (int i3 = 0; i3 < size; i3++) {
                    dArr = (double[]) arrayList.get(i3);
                    i = ((Integer) arrayList2.get(i3)).intValue();
                    dArr[0] = dArr[0] * ((double) i);
                    dArr[1] = dArr[1] * ((double) i);
                    dArr2[0] = dArr2[0] + dArr[0];
                    dArr2[1] = dArr[1] + dArr2[1];
                }
                dArr2[0] = dArr2[0] / ((double) size);
                dArr2[1] = dArr2[1] / ((double) size);
                dArr = b(dArr2[0], dArr2[1]);
                if (dArr[0] <= ((double) i.I)) {
                    return 1;
                }
                if (dArr[0] >= ((double) i.J)) {
                    return 4;
                }
            }
        }
        return 3;
    }

    public static synchronized e a() {
        e eVar;
        synchronized (e.class) {
            if (c == null) {
                c = new e();
            }
            eVar = c;
        }
        return eVar;
    }

    private String a(GpsSatellite gpsSatellite, HashMap<Integer, List<GpsSatellite>> hashMap) {
        int floor = (int) Math.floor((double) (gpsSatellite.getAzimuth() / 6.0f));
        float elevation = gpsSatellite.getElevation();
        int floor2 = (int) Math.floor(((double) elevation) / 1.5d);
        float snr = gpsSatellite.getSnr();
        int round = Math.round(snr / 5.0f);
        int prn = gpsSatellite.getPrn();
        int i = prn >= 65 ? prn - 32 : prn;
        if (snr >= 10.0f && elevation >= 1.0f) {
            List list = (List) hashMap.get(Integer.valueOf(round));
            if (list == null) {
                list = new ArrayList();
            }
            list.add(gpsSatellite);
            hashMap.put(Integer.valueOf(round), list);
            this.x++;
        }
        if (floor >= 64) {
            if (floor2 < 64) {
                if (i >= 65) {
                }
            }
            return null;
        }
        if (floor2 < 64) {
            if (i >= 65) {
            }
        }
        return null;
        if (i >= 65) {
        }
        return null;
    }

    public static String a(Location location) {
        float f = -1.0f;
        if (location == null) {
            return null;
        }
        float speed = (float) (((double) location.getSpeed()) * 3.6d);
        if (!location.hasSpeed()) {
            speed = -1.0f;
        }
        int accuracy = (int) (location.hasAccuracy() ? location.getAccuracy() : -1.0f);
        double altitude = location.hasAltitude() ? location.getAltitude() : 555.0d;
        if (location.hasBearing()) {
            f = location.getBearing();
        }
        return String.format(Locale.CHINA, "&ll=%.5f|%.5f&s=%.1f&d=%.1f&ll_r=%d&ll_n=%d&ll_h=%.2f&ll_t=%d", new Object[]{Double.valueOf(location.getLongitude()), Double.valueOf(location.getLatitude()), Float.valueOf(speed), Float.valueOf(f), Integer.valueOf(accuracy), Integer.valueOf(k), Double.valueOf(altitude), Long.valueOf(location.getTime() / 1000)});
    }

    private void a(double d, double d2, float f) {
        int i = 0;
        if (com.baidu.location.c.c.a().f) {
            if (d >= 73.146973d && d <= 135.252686d && d2 <= 54.258807d && d2 >= 14.604847d && f <= 18.0f) {
                int i2 = (int) ((d - i.t) * 1000.0d);
                int i3 = (int) ((i.u - d2) * 1000.0d);
                if (i2 <= 0 || i2 >= 50 || i3 <= 0 || i3 >= 50) {
                    String str = String.format(Locale.CHINA, "&ll=%.5f|%.5f", new Object[]{Double.valueOf(d), Double.valueOf(d2)}) + "&im=" + com.baidu.location.h.c.a().b();
                    i.r = d;
                    i.s = d2;
                    com.baidu.location.c.c.a().a(str);
                } else {
                    i2 += i3 * 50;
                    i3 = i2 >> 2;
                    i2 &= 3;
                    if (i.x) {
                        i = (i.w[i3] >> (i2 * 2)) & 3;
                    }
                }
            }
            if (i.v != i) {
                i.v = i;
            }
        }
    }

    private void a(String str, Location location) {
        if (location != null) {
            String str2 = str + com.baidu.location.a.a.a().c();
            boolean d = j.a().d();
            h.a(new a(b.a().f()));
            h.a(System.currentTimeMillis());
            h.a(new Location(location));
            h.a(str2);
            if (!d) {
                j.a(h.c(), null, h.d(), str2);
            }
        }
    }

    public static boolean a(Location location, Location location2, boolean z) {
        if (location == location2) {
            return false;
        }
        if (location == null || location2 == null) {
            return true;
        }
        float speed = location2.getSpeed();
        if (z && ((i.v == 3 || !com.baidu.location.h.e.a().a(location2.getLongitude(), location2.getLatitude())) && speed < 5.0f)) {
            return true;
        }
        float distanceTo = location2.distanceTo(location);
        return speed > i.L ? distanceTo > i.N : speed > i.K ? distanceTo > i.M : distanceTo > 5.0f;
    }

    private double[] a(double d, double d2) {
        return new double[]{Math.sin(Math.toRadians(d2)) * d, Math.cos(Math.toRadians(d2)) * d};
    }

    private double[] a(List<GpsSatellite> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        double[] dArr = new double[2];
        for (GpsSatellite gpsSatellite : list) {
            if (gpsSatellite != null) {
                double[] a = a((double) (90.0f - gpsSatellite.getElevation()), (double) gpsSatellite.getAzimuth());
                dArr[0] = dArr[0] + a[0];
                dArr[1] = dArr[1] + a[1];
            }
        }
        int size = list.size();
        dArr[0] = dArr[0] / ((double) size);
        dArr[1] = dArr[1] / ((double) size);
        return dArr;
    }

    public static String b(Location location) {
        String a = a(location);
        return a != null ? a + "&g_tp=0" : a;
    }

    private void b(boolean z) {
        this.p = z;
        if (!z || !i()) {
        }
    }

    private double[] b(double d, double d2) {
        double d3 = 0.0d;
        if (d2 != 0.0d) {
            d3 = Math.toDegrees(Math.atan(d / d2));
        } else if (d > 0.0d) {
            d3 = 90.0d;
        } else if (d < 0.0d) {
            d3 = 270.0d;
        }
        return new double[]{Math.sqrt((d * d) + (d2 * d2)), d3};
    }

    public static String c(Location location) {
        String a = a(location);
        return a != null ? a + r : a;
    }

    private void d(Location location) {
        this.s.sendMessage(this.s.obtainMessage(1, location));
    }

    private void e(Location location) {
        if (location != null) {
            int i = k;
            if (i == 0) {
                try {
                    i = location.getExtras().getInt("satellites");
                } catch (Exception e) {
                }
            }
            if (i != 0 || i.n) {
                this.f = location;
                if (this.f == null) {
                    this.o = null;
                } else {
                    this.f.setTime(System.currentTimeMillis());
                    float speed = (float) (((double) this.f.getSpeed()) * 3.6d);
                    if (!this.f.hasSpeed()) {
                        speed = -1.0f;
                    }
                    i = k;
                    if (i == 0) {
                        try {
                            i = this.f.getExtras().getInt("satellites");
                        } catch (Exception e2) {
                        }
                    }
                    this.o = String.format(Locale.CHINA, "&ll=%.5f|%.5f&s=%.1f&d=%.1f&ll_n=%d&ll_t=%d", new Object[]{Double.valueOf(this.f.getLongitude()), Double.valueOf(this.f.getLatitude()), Float.valueOf(speed), Float.valueOf(this.f.getBearing()), Integer.valueOf(i), Long.valueOf(r1)});
                    a(this.f.getLongitude(), this.f.getLatitude(), speed);
                }
                try {
                    com.baidu.location.a.c.a().a(this.f);
                } catch (Exception e3) {
                }
                if (this.f != null) {
                    com.baidu.location.c.e.a().a(this.f);
                }
                if (i() && this.f != null) {
                    com.baidu.location.a.a.a().a(f());
                    if (k > 2 && j.a(this.f, true)) {
                        boolean d = j.a().d();
                        h.a(new a(b.a().f()));
                        h.a(System.currentTimeMillis());
                        h.a(new Location(this.f));
                        h.a(com.baidu.location.a.a.a().c());
                        if (!d) {
                            j.a(h.c(), null, h.d(), com.baidu.location.a.a.a().c());
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        this.f = null;
    }

    public void a(boolean z) {
        if (z) {
            c();
        } else {
            d();
        }
    }

    public synchronized void b() {
        if (f.isServing) {
            this.d = f.getServiceContext();
            try {
                this.e = (LocationManager) this.d.getSystemService(Headers.LOCATION);
                this.j = new a();
                this.e.addGpsStatusListener(this.j);
                this.h = new c();
                this.e.requestLocationUpdates("passive", 9000, 0.0f, this.h);
            } catch (Exception e) {
            }
            this.s = new Handler(this) {
                final /* synthetic */ e a;

                {
                    this.a = r1;
                }

                public void handleMessage(Message message) {
                    if (f.isServing) {
                        switch (message.what) {
                            case 1:
                                this.a.e((Location) message.obj);
                                return;
                            case 2:
                                if (this.a.j != null) {
                                    this.a.j.a((String) message.obj);
                                    return;
                                }
                                return;
                            case 3:
                                this.a.a("&og=1", (Location) message.obj);
                                return;
                            case 4:
                                this.a.a("&og=2", (Location) message.obj);
                                return;
                            default:
                                return;
                        }
                    }
                }
            };
        }
    }

    public void c() {
        b();
        if (!this.n) {
            try {
                this.g = new b();
                try {
                    this.e.sendExtraCommand("gps", "force_xtra_injection", new Bundle());
                } catch (Exception e) {
                }
                this.e.requestLocationUpdates("gps", 1000, 0.0f, this.g);
                this.e.addNmeaListener(this.j);
                this.n = true;
            } catch (Exception e2) {
            }
        }
    }

    public void d() {
        if (this.n) {
            if (this.e != null) {
                try {
                    if (this.g != null) {
                        this.e.removeUpdates(this.g);
                    }
                    if (this.j != null) {
                        this.e.removeNmeaListener(this.j);
                    }
                } catch (Exception e) {
                }
            }
            i.d = 0;
            i.v = 0;
            this.g = null;
            this.n = false;
            b(false);
        }
    }

    public synchronized void e() {
        d();
        if (this.e != null) {
            try {
                if (this.j != null) {
                    this.e.removeGpsStatusListener(this.j);
                }
                this.e.removeUpdates(this.h);
            } catch (Exception e) {
            }
            this.j = null;
            this.e = null;
        }
    }

    public String f() {
        if (this.f == null) {
            return null;
        }
        double[] dArr;
        int i;
        String str = "{\"result\":{\"time\":\"" + i.a() + "\",\"error\":\"61\"},\"content\":{\"point\":{\"x\":" + "\"%f\",\"y\":\"%f\"},\"radius\":\"%d\",\"d\":\"%f\"," + "\"s\":\"%f\",\"n\":\"%d\"";
        int accuracy = (int) (this.f.hasAccuracy() ? this.f.getAccuracy() : 10.0f);
        float speed = (float) (((double) this.f.getSpeed()) * 3.6d);
        if (!this.f.hasSpeed()) {
            speed = -1.0f;
        }
        double[] dArr2 = new double[2];
        if (com.baidu.location.h.e.a().a(this.f.getLongitude(), this.f.getLatitude())) {
            dArr2 = Jni.coorEncrypt(this.f.getLongitude(), this.f.getLatitude(), "gps2gcj");
            if (dArr2[0] > 0.0d || dArr2[1] > 0.0d) {
                dArr = dArr2;
                i = 1;
            } else {
                dArr2[0] = this.f.getLongitude();
                dArr2[1] = this.f.getLatitude();
                dArr = dArr2;
                i = 1;
            }
        } else {
            dArr2[0] = this.f.getLongitude();
            dArr2[1] = this.f.getLatitude();
            dArr = dArr2;
            i = 0;
        }
        String format = String.format(Locale.CHINA, str, new Object[]{Double.valueOf(dArr[0]), Double.valueOf(dArr[1]), Integer.valueOf(accuracy), Float.valueOf(this.f.getBearing()), Float.valueOf(speed), Integer.valueOf(k)});
        if (i == 0) {
            format = format + ",\"in_cn\":\"0\"";
        }
        if (!this.f.hasAltitude()) {
            return format + "}}";
        }
        return format + String.format(Locale.CHINA, ",\"h\":%.2f}}", new Object[]{Double.valueOf(this.f.getAltitude())});
    }

    public Location g() {
        return (this.f != null && Math.abs(System.currentTimeMillis() - this.f.getTime()) <= DateTimeUtils.ONE_MINUTE) ? this.f : null;
    }

    public boolean h() {
        try {
            return (this.f == null || this.f.getLatitude() == 0.0d || this.f.getLongitude() == 0.0d || (k <= 2 && this.f.getExtras().getInt("satellites", 3) <= 2)) ? false : true;
        } catch (Exception e) {
            return (this.f == null || this.f.getLatitude() == 0.0d || this.f.getLongitude() == 0.0d) ? false : true;
        }
    }

    public boolean i() {
        if (!h() || System.currentTimeMillis() - this.q > 10000) {
            return false;
        }
        return (!this.m || System.currentTimeMillis() - this.l >= 3000) ? this.p : true;
    }
}
