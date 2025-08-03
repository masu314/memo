package com.example.memo;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    public static String formatUpdatedAt(Date updatedAt) {
        //更新日時が取得できなかった場合
        if (updatedAt == null) {
            // ログにエラーを出力
            Log.w("DateUtils", "updatedAt is null");
            return "";
        }

        long serverNowMillis = ServerTime.getCurrentServerTimeMillis();
        // 現在日時のカレンダーインスタンスを作成（自動的に現在日時が設定される）
        Calendar calNow = Calendar.getInstance();
        // Firebaseの現在日時をインスタンスに設定
        calNow.setTimeInMillis(serverNowMillis);

        // 更新日時のカレンダーインスタンスを作成
        Calendar calUpdated = Calendar.getInstance();
        // Firebaseの更新日時をインスタンスに設定
        calUpdated.setTime(updatedAt);

        // 今日中の場合
        if (isSameDay(calNow, calUpdated)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.JAPAN);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo")); // ← 日本時間に変換
            return sdf.format(updatedAt);
        // 同じ週の場合
        } else if (isSameWeek(calNow, calUpdated)) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm", Locale.JAPAN); // "水曜日 15:30"
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo")); // ← 日本時間に変換
            return sdf.format(updatedAt);
        // それ以外の場合
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo")); // ← 日本時間に変換
            return sdf.format(updatedAt);
        }
    }

    // 更新日が本日中か確認
    private static boolean isSameDay(Calendar calNow, Calendar calUpdated){
        return calNow.get(Calendar.YEAR) == calUpdated.get(Calendar.YEAR)
                && calNow.get(Calendar.DAY_OF_YEAR) == calUpdated.get(Calendar.DAY_OF_YEAR);
    }

    // 更新日が同じ週か確認
    private static boolean isSameWeek(Calendar c1, Calendar c2) {
        return c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    }
}
