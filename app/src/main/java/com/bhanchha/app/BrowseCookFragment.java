package com.bhanchha.app;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link com.bhanchha.app.BrowseCookFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class BrowseCookFragment extends Fragment {
    JSONArray successfulResponse = null;
    protected SwipeRefreshLayout swipeLayout;

    public static BrowseCookFragment newInstance() {
        BrowseCookFragment fragment = new BrowseCookFragment();
        return fragment;
    }
    public BrowseCookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse_cook, container, false);
        // Do something with the view. If needed.// Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        updateCookCards();
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container_cook);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start async http request
                // on success
                // update UI
                // setRefreshing(false)
                updateCookCards();  // does all above
            }
        });
        swipeLayout.setColorScheme(
                android.R.color.holo_blue_bright, android.R.color.holo_orange_dark,
                android.R.color.holo_green_light, android.R.color.holo_red_light);
    }

    private void updateCookCards() {
        // first request and fetch json data from server
        //http://localhost:81/samplejson
        // on success, update the UI
        final ArrayList<Card> cardArray = new ArrayList<Card>();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(BuildURL.browseCook(), new JsonHttpResponseHandler() {
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
                        customCard.cAddCardImage(BuildURL.cookImage(row.getString("image_id")));
                        cardArray.add(customCard);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(getActivity(), cardArray);
                final CardGridView gridView = (CardGridView) getView().findViewById(R.id.cookCardGrid);
                if (gridView!=null) {
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
        if (((CardGridView) getView().findViewById(R.id.cookCardGrid)).getChildCount() == 0) {
            getActivity().findViewById(R.id.browse_cook_no_connection_placeholder).setVisibility(View.VISIBLE);
        }
    }

    public void onCustomCardClick(Card card) {
        String cardID = ((CustomCard) card).getIdentifier();    // or one can use getTitle()
        //Toast.makeText(getActivity(), "Got " + cardID, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < successfulResponse.length(); i++) {
            JSONObject row = null;
            try {
                row = successfulResponse.getJSONObject(i);
                if (row.getString("id") == cardID){
                    // match found
                    // do works
                    // extract cook info
                    JSONArray foods = row.getJSONArray("foods");
                    // launch food page with those info
                    Intent intent = new Intent(getActivity(), CookPageActivity.class);
                    intent.putExtra("foods", foods.toString());
                    intent.putExtra("cook_id", cardID);
                    startActivity(intent);
                    // break out of for loop
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
