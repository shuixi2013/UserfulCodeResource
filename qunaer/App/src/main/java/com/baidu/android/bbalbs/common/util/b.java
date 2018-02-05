package com.baidu.android.bbalbs.common.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.system.Os;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.baidu.android.bbalbs.common.a.d;
import com.mqunar.hy.res.model.HybridManifest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.crypto.Cipher;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public final class b {
    private static final String a;
    private static b e;
    private final Context b;
    private int c = 0;
    private PublicKey d;

    class a {
        public ApplicationInfo a;
        public int b;
        public boolean c;
        public boolean d;

        private a() {
            this.b = 0;
            this.c = false;
            this.d = false;
        }
    }

    class b {
        public String a;
        public String b;
        public int c;

        private b() {
            this.c = 2;
        }

        public static b a(String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            try {
                JSONObject jSONObject = new JSONObject(str);
                Object string = jSONObject.getString("deviceid");
                String string2 = jSONObject.getString("imei");
                int i = jSONObject.getInt("ver");
                if (TextUtils.isEmpty(string) || string2 == null) {
                    return null;
                }
                b bVar = new b();
                bVar.a = string;
                bVar.b = string2;
                bVar.c = i;
                return bVar;
            } catch (Throwable e) {
                b.b(e);
                return null;
            }
        }

        public String a() {
            try {
                return new JSONObject().put("deviceid", this.a).put("imei", this.b).put("ver", this.c).toString();
            } catch (Throwable e) {
                b.b(e);
                return null;
            }
        }

        public String b() {
            String str = this.b;
            if (TextUtils.isEmpty(str)) {
                str = "0";
            }
            return this.a + "|" + new StringBuffer(str).reverse().toString();
        }
    }

    class c {
        static boolean a(String str, int i) {
            try {
                Os.chmod(str, i);
                return true;
            } catch (Throwable e) {
                b.b(e);
                return false;
            }
        }
    }

    static {
        String str = new String(com.baidu.android.bbalbs.common.a.b.a(new byte[]{(byte) 77, (byte) 122, (byte) 65, (byte) 121, (byte) 77, (byte) 84, (byte) 73, (byte) 120, (byte) 77, (byte) 68, (byte) 73, (byte) 61}));
        a = str + new String(com.baidu.android.bbalbs.common.a.b.a(new byte[]{(byte) 90, (byte) 71, (byte) 108, (byte) 106, (byte) 100, (byte) 87, (byte) 82, (byte) 112, (byte) 89, (byte) 87, (byte) 73, (byte) 61}));
    }

    private b(Context context) {
        this.b = context.getApplicationContext();
        a();
    }

    public static String a(Context context) {
        return c(context).b();
    }

    private static String a(File file) {
        FileReader fileReader;
        Throwable e;
        Throwable th;
        String str = null;
        try {
            fileReader = new FileReader(file);
            try {
                char[] cArr = new char[8192];
                CharArrayWriter charArrayWriter = new CharArrayWriter();
                while (true) {
                    int read = fileReader.read(cArr);
                    if (read <= 0) {
                        break;
                    }
                    charArrayWriter.write(cArr, 0, read);
                }
                str = charArrayWriter.toString();
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (Throwable e2) {
                        b(e2);
                    }
                }
            } catch (Exception e3) {
                e2 = e3;
                try {
                    b(e2);
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (Throwable e22) {
                            b(e22);
                        }
                    }
                    return str;
                } catch (Throwable th2) {
                    th = th2;
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (Throwable e222) {
                            b(e222);
                        }
                    }
                    throw th;
                }
            }
        } catch (Exception e4) {
            e222 = e4;
            Object obj = str;
            b(e222);
            if (fileReader != null) {
                fileReader.close();
            }
            return str;
        } catch (Throwable e2222) {
            fileReader = str;
            th = e2222;
            if (fileReader != null) {
                fileReader.close();
            }
            throw th;
        }
        return str;
    }

    private static String a(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("Argument b ( byte array ) is null! ");
        }
        String str = "";
        str = "";
        for (byte b : bArr) {
            String toHexString = Integer.toHexString(b & 255);
            str = toHexString.length() == 1 ? str + "0" + toHexString : str + toHexString;
        }
        return str.toLowerCase();
    }

    private List<a> a(Intent intent, boolean z) {
        List<a> arrayList = new ArrayList();
        PackageManager packageManager = this.b.getPackageManager();
        List<ResolveInfo> queryBroadcastReceivers = packageManager.queryBroadcastReceivers(intent, 0);
        if (queryBroadcastReceivers != null) {
            for (ResolveInfo resolveInfo : queryBroadcastReceivers) {
                if (!(resolveInfo.activityInfo == null || resolveInfo.activityInfo.applicationInfo == null)) {
                    try {
                        Bundle bundle = packageManager.getReceiverInfo(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name), 128).metaData;
                        if (bundle != null) {
                            Object string = bundle.getString("galaxy_data");
                            if (!TextUtils.isEmpty(string)) {
                                byte[] a = com.baidu.android.bbalbs.common.a.b.a(string.getBytes("utf-8"));
                                JSONObject jSONObject = new JSONObject(new String(a));
                                a aVar = new a();
                                aVar.b = jSONObject.getInt(LogFactory.PRIORITY_KEY);
                                aVar.a = resolveInfo.activityInfo.applicationInfo;
                                if (this.b.getPackageName().equals(resolveInfo.activityInfo.applicationInfo.packageName)) {
                                    aVar.d = true;
                                }
                                if (z) {
                                    Object string2 = bundle.getString("galaxy_sf");
                                    if (!TextUtils.isEmpty(string2)) {
                                        int i;
                                        PackageInfo packageInfo = packageManager.getPackageInfo(resolveInfo.activityInfo.applicationInfo.packageName, 64);
                                        JSONArray jSONArray = jSONObject.getJSONArray("sigs");
                                        String[] strArr = new String[jSONArray.length()];
                                        for (i = 0; i < strArr.length; i++) {
                                            strArr[i] = jSONArray.getString(i);
                                        }
                                        if (a(strArr, a(packageInfo.signatures))) {
                                            byte[] a2 = a(com.baidu.android.bbalbs.common.a.b.a(string2.getBytes()), this.d);
                                            i = (a2 == null || !Arrays.equals(a2, d.a(a))) ? 0 : 1;
                                            if (i != 0) {
                                                aVar.c = true;
                                            }
                                        }
                                    }
                                }
                                arrayList.add(aVar);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        Collections.sort(arrayList, new c(this));
        return arrayList;
    }

    private void a() {
        ByteArrayInputStream byteArrayInputStream;
        Throwable e;
        ByteArrayInputStream byteArrayInputStream2 = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(a.a());
            try {
                this.d = CertificateFactory.getInstance("X.509").generateCertificate(byteArrayInputStream).getPublicKey();
                if (byteArrayInputStream != null) {
                    try {
                        byteArrayInputStream.close();
                    } catch (Throwable e2) {
                        b(e2);
                    }
                }
            } catch (Exception e3) {
                if (byteArrayInputStream != null) {
                    try {
                        byteArrayInputStream.close();
                    } catch (Throwable e22) {
                        b(e22);
                    }
                }
            } catch (Throwable th) {
                Throwable th2 = th;
                byteArrayInputStream2 = byteArrayInputStream;
                e22 = th2;
                if (byteArrayInputStream2 != null) {
                    try {
                        byteArrayInputStream2.close();
                    } catch (Throwable th3) {
                        b(th3);
                    }
                }
                throw e22;
            }
        } catch (Exception e4) {
            byteArrayInputStream = null;
            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
            }
        } catch (Throwable th4) {
            e22 = th4;
            if (byteArrayInputStream2 != null) {
                byteArrayInputStream2.close();
            }
            throw e22;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.annotation.SuppressLint({"NewApi"})
    private boolean a(java.lang.String r7) {
        /*
        r6 = this;
        r2 = 1;
        r1 = 0;
        r3 = 0;
        r0 = android.os.Build.VERSION.SDK_INT;
        r4 = 24;
        if (r0 < r4) goto L_0x003b;
    L_0x0009:
        r0 = r1;
    L_0x000a:
        r4 = r6.b;	 Catch:{ Exception -> 0x0042, all -> 0x0053 }
        r5 = "libcuid.so";
        r3 = r4.openFileOutput(r5, r0);	 Catch:{ Exception -> 0x0042, all -> 0x0053 }
        r4 = r7.getBytes();	 Catch:{ Exception -> 0x0062, all -> 0x0053 }
        r3.write(r4);	 Catch:{ Exception -> 0x0062, all -> 0x0053 }
        r3.flush();	 Catch:{ Exception -> 0x0062, all -> 0x0053 }
        if (r3 == 0) goto L_0x0021;
    L_0x001e:
        r3.close();	 Catch:{ Exception -> 0x003d }
    L_0x0021:
        if (r0 != 0) goto L_0x003a;
    L_0x0023:
        r0 = 436; // 0x1b4 float:6.11E-43 double:2.154E-321;
        r1 = new java.io.File;
        r2 = r6.b;
        r2 = r2.getFilesDir();
        r3 = "libcuid.so";
        r1.<init>(r2, r3);
        r1 = r1.getAbsolutePath();
        r2 = com.baidu.android.bbalbs.common.util.b.c.a(r1, r0);
    L_0x003a:
        return r2;
    L_0x003b:
        r0 = r2;
        goto L_0x000a;
    L_0x003d:
        r1 = move-exception;
        b(r1);
        goto L_0x0021;
    L_0x0042:
        r0 = move-exception;
        r2 = r3;
    L_0x0044:
        b(r0);	 Catch:{ all -> 0x005f }
        if (r2 == 0) goto L_0x004c;
    L_0x0049:
        r2.close();	 Catch:{ Exception -> 0x004e }
    L_0x004c:
        r2 = r1;
        goto L_0x003a;
    L_0x004e:
        r0 = move-exception;
        b(r0);
        goto L_0x004c;
    L_0x0053:
        r0 = move-exception;
    L_0x0054:
        if (r3 == 0) goto L_0x0059;
    L_0x0056:
        r3.close();	 Catch:{ Exception -> 0x005a }
    L_0x0059:
        throw r0;
    L_0x005a:
        r1 = move-exception;
        b(r1);
        goto L_0x0059;
    L_0x005f:
        r0 = move-exception;
        r3 = r2;
        goto L_0x0054;
    L_0x0062:
        r0 = move-exception;
        r2 = r3;
        goto L_0x0044;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.bbalbs.common.util.b.a(java.lang.String):boolean");
    }

    private boolean a(String str, String str2) {
        try {
            return System.putString(this.b.getContentResolver(), str, str2);
        } catch (Throwable e) {
            b(e);
            return false;
        }
    }

    private boolean a(String[] strArr, String[] strArr2) {
        int i = 0;
        if (strArr == null || strArr2 == null || strArr.length != strArr2.length) {
            return false;
        }
        HashSet hashSet = new HashSet();
        for (Object add : strArr) {
            hashSet.add(add);
        }
        HashSet hashSet2 = new HashSet();
        int length = strArr2.length;
        while (i < length) {
            hashSet2.add(strArr2[i]);
            i++;
        }
        return hashSet.equals(hashSet2);
    }

    private static byte[] a(byte[] bArr, PublicKey publicKey) {
        Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        instance.init(2, publicKey);
        return instance.doFinal(bArr);
    }

    private String[] a(Signature[] signatureArr) {
        String[] strArr = new String[signatureArr.length];
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = a(d.a(signatureArr[i].toByteArray()));
        }
        return strArr;
    }

    private b b() {
        boolean z;
        a aVar;
        boolean z2;
        String str;
        String str2;
        b a;
        b e;
        String str3 = null;
        int i = 0;
        List a2 = a(new Intent("com.baidu.intent.action.GALAXY").setPackage(this.b.getPackageName()), true);
        int i2;
        if (a2 == null || a2.size() == 0) {
            for (i2 = 0; i2 < 3; i2++) {
                Log.w("DeviceId", "galaxy lib host missing meta-data,make sure you know the right way to integrate galaxy");
            }
            z = false;
        } else {
            aVar = (a) a2.get(0);
            z2 = aVar.c;
            if (!aVar.c) {
                for (i2 = 0; i2 < 3; i2++) {
                    Log.w("DeviceId", "galaxy config err, In the release version of the signature should be matched");
                }
            }
            z = z2;
        }
        File file = new File(this.b.getFilesDir(), "libcuid.so");
        b a3 = file.exists() ? b.a(f(a(file))) : null;
        if (a3 == null) {
            this.c |= 16;
            List<a> a4 = a(new Intent("com.baidu.intent.action.GALAXY"), z);
            if (a4 != null) {
                str = HybridManifest.FILES_KEY;
                file = this.b.getFilesDir();
                if (str.equals(file.getName())) {
                    str2 = str;
                } else {
                    Log.e("DeviceId", "fetal error:: app files dir name is unexpectedly :: " + file.getAbsolutePath());
                    str2 = file.getName();
                }
                for (a aVar2 : a4) {
                    if (!aVar2.d) {
                        File file2 = new File(new File(aVar2.a.dataDir, str2), "libcuid.so");
                        if (file2.exists()) {
                            a = b.a(f(a(file2)));
                            if (a != null) {
                                break;
                            }
                        }
                        a = a3;
                        a3 = a;
                    }
                }
            }
        }
        a = a3;
        if (a == null) {
            a = b.a(f(b("com.baidu.deviceid.v2")));
        }
        boolean c = c("android.permission.READ_EXTERNAL_STORAGE");
        if (a == null && c) {
            this.c |= 2;
            e = e();
        } else {
            e = a;
        }
        if (e == null) {
            this.c |= 8;
            e = d();
        }
        if (e == null && c) {
            this.c |= 1;
            str = h("");
            e = d(str);
            i = 1;
        } else {
            str = null;
        }
        if (e == null) {
            this.c |= 4;
            if (i == 0) {
                str = h("");
            }
            b bVar = new b();
            str2 = b(this.b);
            bVar.a = com.baidu.android.bbalbs.common.a.c.a((VERSION.SDK_INT < 23 ? str + str2 + UUID.randomUUID().toString() : "com.baidu" + str2).getBytes(), true);
            bVar.b = str;
            a = bVar;
        } else {
            a = e;
        }
        file = new File(this.b.getFilesDir(), "libcuid.so");
        if (!((this.c & 16) == 0 && file.exists())) {
            str2 = TextUtils.isEmpty(null) ? e(a.a()) : null;
            a(str2);
            str3 = str2;
        }
        z2 = c();
        if (z2 && ((this.c & 2) != 0 || TextUtils.isEmpty(b("com.baidu.deviceid.v2")))) {
            if (TextUtils.isEmpty(str3)) {
                str3 = e(a.a());
            }
            a("com.baidu.deviceid.v2", str3);
        }
        if (c("android.permission.WRITE_EXTERNAL_STORAGE")) {
            File file3 = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig/.cuid2");
            if (!((this.c & 8) == 0 && file3.exists())) {
                if (TextUtils.isEmpty(str3)) {
                    str3 = e(a.a());
                }
                g(str3);
            }
        }
        if (z2 && ((this.c & 1) != 0 || TextUtils.isEmpty(b("com.baidu.deviceid")))) {
            a("com.baidu.deviceid", a.a);
            a("bd_setting_i", a.b);
        }
        if (z2 && !TextUtils.isEmpty(a.b)) {
            file = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig/.cuid");
            if (!((this.c & 2) == 0 && file.exists())) {
                b(a.b, a.a);
            }
        }
        return a;
    }

    public static String b(Context context) {
        String str = "";
        Object string = Secure.getString(context.getContentResolver(), "android_id");
        return TextUtils.isEmpty(string) ? "" : string;
    }

    private String b(String str) {
        try {
            return System.getString(this.b.getContentResolver(), str);
        } catch (Throwable e) {
            b(e);
            return null;
        }
    }

    private static void b(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append("=");
            stringBuilder.append(str2);
            File file = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig");
            File file2 = new File(file, ".cuid");
            try {
                if (file.exists() && !file.isDirectory()) {
                    File file3;
                    Random random = new Random();
                    File parentFile = file.getParentFile();
                    String name = file.getName();
                    do {
                        file3 = new File(parentFile, name + random.nextInt() + ".tmp");
                    } while (file3.exists());
                    file.renameTo(file3);
                    file3.delete();
                }
                file.mkdirs();
                FileWriter fileWriter = new FileWriter(file2, false);
                fileWriter.write(com.baidu.android.bbalbs.common.a.b.a(com.baidu.android.bbalbs.common.a.a.a(a, a, stringBuilder.toString().getBytes()), "utf-8"));
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
            } catch (Exception e2) {
            }
        }
    }

    private static void b(Throwable th) {
    }

    private static b c(Context context) {
        if (e == null) {
            synchronized (b.class) {
                if (e == null) {
                    SystemClock.uptimeMillis();
                    e = new b(context).b();
                    SystemClock.uptimeMillis();
                }
            }
        }
        return e;
    }

    private boolean c() {
        return c("android.permission.WRITE_SETTINGS");
    }

    private boolean c(String str) {
        return this.b.checkPermission(str, Process.myPid(), Process.myUid()) == 0;
    }

    private b d() {
        Object b = b("com.baidu.deviceid");
        String b2 = b("bd_setting_i");
        if (TextUtils.isEmpty(b2)) {
            b2 = h("");
            if (!TextUtils.isEmpty(b2)) {
                a("bd_setting_i", b2);
            }
        }
        if (TextUtils.isEmpty(b)) {
            b = b(com.baidu.android.bbalbs.common.a.c.a(("com.baidu" + b2 + b(this.b)).getBytes(), true));
        }
        if (TextUtils.isEmpty(b)) {
            return null;
        }
        b bVar = new b();
        bVar.a = b;
        bVar.b = b2;
        return bVar;
    }

    private b d(String str) {
        Object obj = null;
        Object obj2 = VERSION.SDK_INT < 23 ? 1 : null;
        if (obj2 != null && TextUtils.isEmpty(str)) {
            return null;
        }
        String str2;
        b bVar;
        Object obj3 = "";
        File file = new File(Environment.getExternalStorageDirectory(), "baidu/.cuid");
        if (!file.exists()) {
            file = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig/.cuid");
            int i = 1;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuilder.append(readLine);
                stringBuilder.append(IOUtils.LINE_SEPARATOR_WINDOWS);
            }
            bufferedReader.close();
            String[] split = new String(com.baidu.android.bbalbs.common.a.a.b(a, a, com.baidu.android.bbalbs.common.a.b.a(stringBuilder.toString().getBytes()))).split("=");
            if (split != null && split.length == 2) {
                if (obj2 != null && str.equals(split[0])) {
                    obj3 = split[1];
                    str2 = str;
                    if (obj == null) {
                        b(str2, obj3);
                    }
                    if (!TextUtils.isEmpty(obj3)) {
                        return null;
                    }
                    bVar = new b();
                    bVar.a = obj3;
                    bVar.b = str2;
                    return bVar;
                } else if (obj2 == null) {
                    if (TextUtils.isEmpty(str)) {
                        str = split[1];
                    }
                    obj3 = split[1];
                    str2 = str;
                    if (obj == null) {
                        try {
                            b(str2, obj3);
                        } catch (FileNotFoundException e) {
                        } catch (IOException e2) {
                        } catch (Exception e3) {
                        }
                    }
                    if (!TextUtils.isEmpty(obj3)) {
                        return null;
                    }
                    bVar = new b();
                    bVar.a = obj3;
                    bVar.b = str2;
                    return bVar;
                }
            }
            str2 = str;
            if (obj == null) {
                b(str2, obj3);
            }
        } catch (FileNotFoundException e4) {
            str2 = str;
        } catch (IOException e5) {
            str2 = str;
        } catch (Exception e6) {
            str2 = str;
        }
        if (!TextUtils.isEmpty(obj3)) {
            return null;
        }
        bVar = new b();
        bVar.a = obj3;
        bVar.b = str2;
        return bVar;
    }

    private b e() {
        File file = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig/.cuid2");
        if (file.exists()) {
            Object a = a(file);
            if (!TextUtils.isEmpty(a)) {
                try {
                    return b.a(new String(com.baidu.android.bbalbs.common.a.a.b(a, a, com.baidu.android.bbalbs.common.a.b.a(a.getBytes()))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String e(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return com.baidu.android.bbalbs.common.a.b.a(com.baidu.android.bbalbs.common.a.a.a(a, a, str.getBytes()), "utf-8");
        } catch (Throwable e) {
            b(e);
            return "";
        } catch (Throwable e2) {
            b(e2);
            return "";
        }
    }

    private static String f(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return new String(com.baidu.android.bbalbs.common.a.a.b(a, a, com.baidu.android.bbalbs.common.a.b.a(str.getBytes())));
        } catch (Throwable e) {
            b(e);
            return "";
        }
    }

    private static void g(String str) {
        File file = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig");
        File file2 = new File(file, ".cuid2");
        try {
            if (file.exists() && !file.isDirectory()) {
                File file3;
                Random random = new Random();
                File parentFile = file.getParentFile();
                String name = file.getName();
                do {
                    file3 = new File(parentFile, name + random.nextInt() + ".tmp");
                } while (file3.exists());
                file.renameTo(file3);
                file3.delete();
            }
            file.mkdirs();
            FileWriter fileWriter = new FileWriter(file2, false);
            fileWriter.write(str);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
        } catch (Exception e2) {
        }
    }

    private String h(String str) {
        String deviceId;
        CharSequence i;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) this.b.getSystemService("phone");
            if (telephonyManager != null) {
                deviceId = telephonyManager.getDeviceId();
                i = i(deviceId);
                return TextUtils.isEmpty(i) ? str : i;
            }
        } catch (Throwable e) {
            Log.e("DeviceId", "Read IMEI failed", e);
        }
        deviceId = null;
        i = i(deviceId);
        if (TextUtils.isEmpty(i)) {
        }
    }

    private static String i(String str) {
        return (str == null || !str.contains(":")) ? str : "";
    }
}
