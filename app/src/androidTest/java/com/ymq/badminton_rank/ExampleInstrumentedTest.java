package com.ymq.badminton_rank;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ymq.badminton_rank.db.GameRecordDBHelper;
import com.ymq.badminton_rank.db.RecordDAO;
import com.ymq.badminton_rank.utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.ymq.badminton_rank", appContext.getPackageName());
    }
    @Test
    public void testCreateDB(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        GameRecordDBHelper helper =new GameRecordDBHelper(appContext);
        helper.getReadableDatabase();
    }

    @Test
    public void testQueryRecord(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        RecordDAO recordDAO =new RecordDAO(appContext);
        ArrayList arrayList = recordDAO.queryRecord();
        Log.i("Test",arrayList.toString());
    }

    @Test
    public void testAdd(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        RecordDAO recordDAO =new RecordDAO(appContext);
//        GameRecord gameRecord =new GameRecord(1,"haha,heihie,guf,da","2020202");
//        recordDAO.add(gameRecord);
    }
    @Test
    public void testGetHttpClient(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username","zhangsan");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Callback callback =new Callback() {
            public static final String TAG = "TEST";

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.i(TAG, "onResponse: "+response.body().toString());
                Context appContext = InstrumentationRegistry.getTargetContext();
                Toast.makeText(appContext, "appContext:" + appContext, Toast.LENGTH_SHORT).show();
            }
        };
        HttpUtil.postJson("https://www.baidu.com/",jsonObject,callback);
    }
}
