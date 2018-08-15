package com.example.user.game2048;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 02/02/2018.
 */

/*
* Class này không được dùng nên tên màu xám
* Có thể bỏ qua
* */
public class SessionGamePlay {
    private final String TAG = SessionGamePlay.class.getName();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SessionGamePlay";

    private static final String KEY_GAMEPLAY = "GamePlay";


    public SessionGamePlay(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public void createSessionGamePlay(List<DataGame> dataGames) {
        if (dataGames == null) {
            //Log.e(TAG + " createLoginSession cuahang = null");
            return;
        }
        Gson gson = new Gson();
        for (int i = 0; i < dataGames.size(); i++) {
            DataGame dataGame = dataGames.get(i);
            String json = gson.toJson(dataGame);
            editor.putString(KEY_GAMEPLAY + i, json);
        }

        editor.commit();
    }
/*
    public List<DataGame> getSessionGamePlay() {
        List<DataGame> dataGames = new ArrayList<>();
        Gson gson = new Gson();

        for (int i = 0; i < Common.JsonKey.NUMBER_HISTORY; i++) {
            String json = pref.getString(KEY_LICHSU + i, "");
            if (json.isEmpty())
                continue;
            LichSuTimKiemTD lichSuTimKiemTD1 = gson.fromJson(json, LichSuTimKiemTD.class);
            lichSuTimKiemTDList.add(lichSuTimKiemTD1);
        }

        return lichSuTimKiemTDList;
    }*/

    public void clearData() {
        editor.clear();
        editor.commit();
    }
}
