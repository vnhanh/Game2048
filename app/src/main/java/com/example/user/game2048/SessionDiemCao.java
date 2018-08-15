package com.example.user.game2048;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by USER on 02/02/2018.
 * Lưu trữ điểm cao nhất của người chơi bằng SharedPreferences
 * Số điểm cao nhất sẽ được lưu trữ vào điện thoại và truy xuất từ đó
 */

public class SessionDiemCao {
    private final String TAG = SessionDiemCao.class.getName();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SessionDiemCao";

    private static final String KEY_DIEMSO = "diemSo";


    public SessionDiemCao(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createSession(int diemSo) {

        editor.putInt(KEY_DIEMSO, diemSo);

        editor.commit();
    }

    public int getSessionDiemSo() {
        return pref.getInt(KEY_DIEMSO, 0);
    }

    public void clearData() {
        editor.clear();
        editor.commit();
    }
}
