package com.bhanchha.app;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowseFoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseFoodFragment extends Fragment {
    JSONArray successfulResponse = null;
    protected SwipeRefreshLayout swipeLayout;

//    // TO-DO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TO-DO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment BrowseFoodFragment.
//     */
//    // TO-DO: Rename and change types and number of parameters
//    public static BrowseFoodFragment newInstance(String param1, String param2) {
//        BrowseFoodFragment fragment = new BrowseFoodFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static BrowseFoodFragment newInstance() {
        BrowseFoodFragment fragment = new BrowseFoodFragment();
        return fragment;
    }

    public BrowseFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse_food, container, false);
        // Do something with the view. If needed.
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        updateFoodCards();
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container_food);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start async http request
                // on success
                // update UI
                // setRefreshing(false)
                updateFoodCards();  // does all above
            }
        });
        swipeLayout.setColorScheme(
                android.R.color.holo_blue_bright, android.R.color.holo_orange_dark,
                android.R.color.holo_green_light, android.R.color.holo_red_light);
    }

    private void updateFoodCards() {
        // first request and fetch json data from server
        // on success, update the UI
        final ArrayList<Card> cardArray = new ArrayList<Card>();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(BuildURL.browseFood(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (getActivity() == null) return;
                successfulResponse = response;
                CustomCard customCard = null;
                for (int i = 0; i < response.length(); i++) {
                    JSONObject row = null;
                    customCard = new CustomCard(getActivity(), R.layout.custom_card);
                    customCard.setOnClickListener(new Card.OnCardClickListener() {
                        @Override
                        public void onClick(Card card, View view) {
                            onCustomCardClick(card);
                        }
                    });
                    try {
                        row = response.getJSONObject(i);
                        customCard.setIdentifier(row.getString("id"));
                        customCard.cAddCardTitle(row.getString("name"));
                        customCard.cAddCardImage(BuildURL.foodImage(row.getString("image_id")));
                        cardArray.add(customCard);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(getActivity(), cardArray);
                final CardGridView gridView = (CardGridView) getView().findViewById(R.id.foodCardGrid);
                if (gridView != null) {
                    gridView.setAdapter(mCardArrayAdapter);
                    // when scrolling up on the list view, the on-refresh is fired. The following fixes that
                    gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            int topRowVerticalPosition =
                                    (gridView == null || gridView.getChildCount() == 0) ?
                                            0 : gridView.getChildAt(0).getTop();
                            swipeLayout.setEnabled(topRowVerticalPosition >= 0);
                        }
                    });
                }

                // hide no-connection placeholder in case it's visible
                //show placeholder here
                getActivity().findViewById(R.id.browse_food_no_connection_placeholder).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                showPlaceholder();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                showPlaceholder();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                showPlaceholder();
            }

            @Override
            public void onFinish() {
                if (getActivity() == null) return;
                super.onFinish();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    private void showPlaceholder() {
        if (getActivity() == null) return;
        //show no-connection placeholder here if view is empty
        if (((CardGridView) getView().findViewById(R.id.foodCardGrid)).getChildCount() == 0) {
            getActivity().findViewById(R.id.browse_food_no_connection_placeholder).setVisibility(View.VISIBLE);
        }
    }

    public void onCustomCardClick(Card card) {
        String cardID = ((CustomCard) card).getIdentifier();    // or one can use getTitle()
        //Toast.makeText(getActivity(), "Got " + cardID, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < successfulResponse.length(); i++) {
            JSONObject row = null;
            try {
                row = successfulResponse.getJSONObject(i);
                if (row.getString("id") == cardID) {
                    // match found
                    // do works
                    // extract cook info
                    JSONArray cooks = row.getJSONArray("cooks");
                    // launch food page with those info
                    Intent intent = new Intent(getActivity(), FoodPageActivity.class);
                    intent.putExtra("cooks", cooks.toString());
                    intent.putExtra("food_id", cardID);
                    startActivity(intent);
                    // break out of for loop
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

//    public void fetchJSON () {
//        AsyncHttpClient client = new AsyncHttpClient();
//        //https://api.github.com/users/mralexgray/repos
//        client.get("http://ip.jsontest.com/", new JsonHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                // called when response HTTP status is "200 OK"
//                try {
//                    Toast.makeText(getActivity(), "Your ip is: " + response.getString("ip"), Toast.LENGTH_LONG).show();
//                } catch (JSONException e) {
//                    Toast.makeText(getActivity(), "Check your internet connection and try again.", Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }

}
