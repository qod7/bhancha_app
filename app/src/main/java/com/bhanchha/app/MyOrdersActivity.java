package com.bhanchha.app;

import android.app.ActionBar;
 import android.app.Activity;
 import android.graphics.Color;
 import android.os.Bundle;
 import android.support.v4.widget.SwipeRefreshLayout;
 import android.view.Menu;
 import android.view.MenuItem;
 import android.view.View;
 import android.widget.TableLayout;
 import android.widget.TableRow;
 import android.widget.TextView;

 import com.loopj.android.http.AsyncHttpClient;
 import com.loopj.android.http.JsonHttpResponseHandler;

 import org.apache.http.Header;
 import org.json.JSONArray;
 import org.json.JSONException;
 import org.json.JSONObject;


public class MyOrdersActivity extends Activity {
      protected SwipeRefreshLayout swipeLayout;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_my_orders);
         ActionBar actionBar = getActionBar();
         if (actionBar != null) {
             actionBar.setDisplayHomeAsUpEnabled(true);
             // TODO: Put a cook icon and uncomment the line below
             //actionBar.setIcon(R.drawable.cook_icon);
         }
         if (!updateMyOrdersTable()) {
             // apply placeholder
         }
         swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container_my_orders);
         swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 // start async http request
                 // on success
                 // update UI
                 // setRefreshing(false)
                 updateMyOrdersTable();  // does all above
             }
         });
         swipeLayout.setColorScheme(
                 android.R.color.holo_blue_bright, android.R.color.holo_orange_dark,
                 android.R.color.holo_green_light, android.R.color.holo_red_light);
     }

     private void setTableData(JSONArray response) {
         TableLayout tableLayout = (TableLayout) findViewById(R.id.my_orders_table_layout);
         tableLayout.setVisibility(View.VISIBLE);
         tableLayout.removeViewsInLayout(1, tableLayout.getChildCount()-1);
         int l = (int) getResources().getDimension(R.dimen.myOrderTextPaddingLeft),
                 t = (int) getResources().getDimension(R.dimen.myOrderTextPaddingTop),
                 r = (int) getResources().getDimension(R.dimen.myOrderTextPaddingRight),
                 b = (int) getResources().getDimension(R.dimen.myOrderTextPaddingBottom);
         for (int i = 0; i < response.length(); i++) {
             JSONObject row = null;
             TableRow tr = new TableRow(this);
             try {
                 row = response.getJSONObject(i);
                 TextView tdCook = new TextView(this);
                 TextView tdFood = new TextView(this);
                 TextView tdStatus = new TextView(this);
                 TextView tdOrdered = new TextView(this);

                 tdCook.setText(row.getString("cook"));
                 tdFood.setText(row.getString("food"));
                 tdStatus.setText(row.getString("status"));
                 tdOrdered.setText(row.getString("ordered"));

                 tdCook.setPadding(l, t, r, b);
                 tdFood.setPadding(l, t, r, b);
                 tdStatus.setPadding(l * 2, t, r, b);
                 tdOrdered.setPadding(l, t, r, b);

                 tr.addView(tdCook);
                 tr.addView(tdFood);
                 tr.addView(tdStatus);
                 tr.addView(tdOrdered);
                 tableLayout.addView(tr);
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
     }

     private boolean updateMyOrdersTable() {
         // new http request to retrieve orders
         AsyncHttpClient client = new AsyncHttpClient();
         client.get(BuildURL.myOrders(), new JsonHttpResponseHandler() {
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                 setTableData(response);
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                 super.onFailure(statusCode, headers, responseString, throwable);
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                 super.onFailure(statusCode, headers, throwable, errorResponse);
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                 super.onFailure(statusCode, headers, throwable, errorResponse);
             }

             @Override
             public void onFinish() {
                 super.onFinish();
                 swipeLayout.setRefreshing(false);
             }
         });
         // on success populate tableLayout

         //         // demo below
         //         // reference the table layout
         //         TableLayout tableLayout = (TableLayout) findViewById(R.id.my_orders_table_layout);
         //         tableLayout.setVisibility(View.VISIBLE);
         //         // declare a new row
         //         String[][] tableData = new String[][]{{"111", "aaa", "***"}, {"222", "bbb", "---"}, {"333", "ccc", "///"}};
         //         for (String[] tableRow : tableData) {
         //             TableRow tr = new TableRow(this);
         //             // add views to the row
         //             for (String tableCell : tableRow) {
         //                 TextView td = new TextView(this);
         //                 //td.setTextColor(Color.WHITE);
         //                 td.setPadding(10, 10, 10, 10);
         //                 TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
         //                         0, TableRow.LayoutParams.WRAP_CONTENT, 1);
         //                 layoutParams.setMargins(4, 4, 4, 4);
         //                 td.setLayoutParams(layoutParams);
         //
         //                 td.setText(tableCell);
         //                 tr.addView(td);
         //             }
         //             // add the row to the table layout
         //             tableLayout.addView(tr);
         //         }

         return true;
     }


     @Override
     public boolean onNavigateUp() {
         finish();
         return true;
     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.my_orders, menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action bar item clicks here. The action bar will
         // automatically handle clicks on the Home/Up button, so long
         // as you specify a parent activity in AndroidManifest.xml.
//         int id = item.getItemId();
//         if (id == R.id.action_settings) {
//             return true;
//         }
         return super.onOptionsItemSelected(item);
     }
 }
