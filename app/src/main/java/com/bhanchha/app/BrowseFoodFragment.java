package com.bhanchha.app;



import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 *
 */
public class BrowseFoodFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrowseFoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrowseFoodFragment newInstance(String param1, String param2) {
        BrowseFoodFragment fragment = new BrowseFoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
    }

    private void updateFoodCards() {
        // first request and fetch json data from server
                //http://localhost:81/samplejson
        // on success, update the UI
        final ArrayList<Card> cardArray = new ArrayList<Card>();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.2.110:81/samplejson", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                CustomCard customCard = null;
                for (int i = 0; i < response.length(); i++) {
                    JSONObject row = null;
                    customCard = new CustomCard(getActivity(), R.layout.custom_card);
                    try {
                        row = response.getJSONObject(i);
                        customCard.cAddCardTitle(row.getString("id") + " " + row.getString("name"));
                        customCard.cAddCardImage(R.drawable.ic_launcher);
                        cardArray.add(customCard);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(getActivity(), cardArray);
                CardGridView gridView = (CardGridView) getView().findViewById(R.id.foodCardGrid);
                if (gridView!=null) {
                    gridView.setAdapter(mCardArrayAdapter);
                }
            }
        });
    }

    public void fetchJSON () {
        AsyncHttpClient client = new AsyncHttpClient();
        //https://api.github.com/users/mralexgray/repos
        client.get("http://ip.jsontest.com/", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                try {
                    Toast.makeText(getActivity(), "Your ip is: " + response.getString("ip"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Check your internet connection and try again.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
