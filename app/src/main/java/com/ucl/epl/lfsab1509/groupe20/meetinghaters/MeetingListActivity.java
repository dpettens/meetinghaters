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
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.DB.JsonRequestHelper;

import org.json.JSONObject;

import java.util.ArrayList;

public class MeetingListActivity extends AppCompatActivity {
    private static final String TAG = "MeetingListActivity";
    MeetingApplication appInstance = MeetingApplication.getAppInstance();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.meeting_recyclerView);
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

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.meeting_swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateMeetings();
            }
        });

        recyclerView.setAdapter(new RecyclerAdapter(generateMeetingList()));
    }

    @Override
    // Disable Back to SignUp or SignIn
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private ArrayList<MeetingItem> generateMeetingList(){

        ArrayList<MeetingItem> meetings = new ArrayList<>();
        //We generate the list of meeting in this place
        /*
        JsonRequestHelper request = new JsonRequestHelper(
                Request.Method.GET,
                appInstance.remoteDBHandler.apiMeetingURL(),
                null, //GET REQUEST so no JSONObject to pass
                appInstance.myDBHandler.getToken(),
                appInstance.myDBHandler.getUser(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //TODO
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO
                    }
                }
        );//*/
        /*meetings.add(new MeetingItem("P4", "A short meeting", "Start at 12h20", "End at 12h30", "Reaumur"));
        meetings.add(new MeetingItem("P4 Assistant", "Another short meeting", "Start at 14h20", "End at 14h35", "Paul Otlet"));*/
        return meetings;
    }

    private void updateMeetings() {
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
