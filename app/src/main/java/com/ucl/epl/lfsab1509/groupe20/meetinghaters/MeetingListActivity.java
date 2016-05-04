package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.Adapter.MeetingItem;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.Adapter.RecyclerAdapter;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.DB.JsonArrayRequestHelper;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.DB.JsonRequestHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MeetingListActivity extends AppCompatActivity {
    private static final String TAG = "MeetingListActivity";
    MeetingApplication appInstance = MeetingApplication.getAppInstance();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<MeetingItem> meetings = new ArrayList<>();

    private RecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);

        recyclerView = (RecyclerView) findViewById(R.id.meeting_recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        FloatingActionButton btn_add = (FloatingActionButton) findViewById(R.id.fab_add_meeting);
        btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateMeetingActivity.class);
                startActivity(i);
            }
        });

        recyclerAdapter = new RecyclerAdapter(/*getApplicationContext(),*/ meetings);
        generateMeetingList();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.meeting_swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateMeetings();
                recyclerAdapter.swap(meetings);
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        recyclerView.setAdapter(recyclerAdapter);
        updateMeetings();

    }

    @Override
    // Disable Back to SignUp or SignIn
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void generateMeetingList(){
        //We generate the list of meeting in this place
        //meetings.clear();
        recyclerAdapter.clear();
        JsonArrayRequestHelper request = new JsonArrayRequestHelper(
                Request.Method.GET,
                appInstance.remoteDBHandler.apiMeetingURL(),
                null, //GET REQUEST so no JSONObject to pass
                appInstance.myDBHandler.getToken(),
                appInstance.myDBHandler.getUser(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "success length response :: " + response.length());
                        for (int i=0; i<response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Log.e(TAG, "my json : " + jsonObject.toString());
                                MeetingItem meetingItem = new MeetingItem(
                                        jsonObject.getString("_id"),
                                        jsonObject.getString("name"),
                                        //jsonObject.getString("description"),
                                        "new desc",
                                        jsonObject.getString("time_start"),
                                        jsonObject.getString("time_end"));
                                //meetings.add(meetingItem);
                                recyclerAdapter.addItem(meetingItem);
                                Log.e(TAG, "size equal " + meetings.size());
                            } catch (JSONException jsonex) {
                                jsonex.printStackTrace();
                            }
                        }
                        Log.e(TAG, "success :: " + meetings.toString());
                        //recyclerAdapter.swap(meetings);
                        //recyclerAdapter.dataSetChanged();

                        recyclerView.setAdapter(recyclerAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error volley " + error.networkResponse.statusCode);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                        } else {
                            switch (error.networkResponse.statusCode) {
                                case 500:
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    }
                }
        );
        request.setPriority(Request.Priority.HIGH);
        appInstance.remoteDBHandler.add(request);
    }

    private void updateMeetings() {
        generateMeetingList();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meeting_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signout:
                appInstance.myDBHandler.deleteUser();
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
                return true;

            case R.id.action_delete_account:
                final ProgressDialog progressDialog = new ProgressDialog(MeetingListActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getString(R.string.menu_delete_account_message_dialog));
                progressDialog.show();

                final String user = appInstance.myDBHandler.getUser();

                JsonRequestHelper deleteAccountRequest = new JsonRequestHelper(
                        Request.Method.DELETE,
                        appInstance.remoteDBHandler.apiUserURL(user),
                        null,
                        appInstance.myDBHandler.getToken(),
                        user,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                appInstance.myDBHandler.deleteUser();
                                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                                startActivity(i);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, new String(error.networkResponse.data));
                                progressDialog.hide();
                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                                } else if (error.networkResponse.statusCode == 500) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                );

                deleteAccountRequest.setPriority(Request.Priority.HIGH);
                appInstance.remoteDBHandler.add(deleteAccountRequest);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
