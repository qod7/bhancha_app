package com.bhanchha.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;


public class FoodPageActivity extends Activity {

    ActionMode mActionMode;
    private CustomCard currentSelectedCard = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_page);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // TODO: Put a cook icon and uncomment the line below
            //actionBar.setIcon(R.drawable.cook_icon);
        }
        populateFoodPageGrid();
        //registerForContextMenu(findViewById(R.id.foodPageGrid));
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        if (v.getId() == R.id.foodCardGrid) {
//            MenuInflater inflater = getMenuInflater();
//            inflater.inflate(R.menu.food_page_grid, menu);
//        }
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
//                item.getMenuInfo();
//        if (item.getItemId() == R.id.add)
//            Log.d("Qod", "something added");
//        else if (item.getItemId() == R.id.edit)
//            Log.d("Qod", "something edited");
//        else if (item.getItemId() == R.id.delete)
//            Log.d("Qod", "something deleted");
//        else
//            return super.onContextItemSelected(item);
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.food_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateFoodPageGrid() {
        // get cook details
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("cooks")) {
                JSONArray cooks = null;
                try {
                    cooks = new JSONArray(extras.getString("cooks"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
                ArrayList<Card> cardArray = new ArrayList<Card>();
                // populate foodPageGrid
                CustomCard customCard = null;
                for (int i = 0; i < cooks.length(); i++) {
                    JSONObject row = null;
                    customCard = new CustomCard(this, R.layout.custom_card);
                    customCard.setOnClickListener(new Card.OnCardClickListener() {
                        @Override
                        public void onClick(Card card, View view) {
                            onCustomCardClick((CustomCard)card);
                        }
                    });

                    customCard.setOnLongClickListener(new Card.OnLongCardClickListener() {
                        @Override
                        public boolean onLongClick(Card card, View view) {
                            if (mActionMode != null) {
                                currentSelectedCard = null;
                                mActionMode.finish();
                                return false;
                            }
                            // Start the CAB using the ActionMode.Callback defined below
                            captureSelectedCard(card);
                            mActionMode = startActionMode(mActionModeCallback);
                            view.setActivated(true);
                            return true;
                        }
                    });

                    try {
                        row = cooks.getJSONObject(i);
                        customCard.setIdentifier(row.getString("id"));
                        customCard.cAddCardTitle(row.getString("name"));
                        customCard.cAddCardImage(BuildURL.cookImage(row.getString("image_id")));
                        cardArray.add(customCard);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(this, cardArray);
                CardGridView gridView = (CardGridView) findViewById(R.id.foodPageGrid);
                if (gridView != null) {
                    gridView.setAdapter(mCardArrayAdapter);
                }
            }
        }
    }

    public void onCustomCardClick(CustomCard card) {
        // prepare order
        // get info
        Intent intent = new Intent(this, OrderDialogActivity.class);
                                        // cook_id + food_id
        intent.putExtra("order_info", card.getIdentifier() + "-" + getIntent().getExtras().getString("food_id"));
        // show OrderDialog
        startActivity(intent);
        //Toast.makeText(this, "Some cook clicked", Toast.LENGTH_LONG).show();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.food_page_grid, menu);

            // check current favorite status; change icon to star_on if favorite'd
            menu.findItem(R.id.food_page_menu_favorite).setIcon(android.R.drawable.star_on);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.food_page_menu_favorite:
                    Toast.makeText(getApplicationContext(), "Change  " + ((CustomCard)currentSelectedCard).getIdentifier(),
                            Toast.LENGTH_LONG).show();
                    break;
                case R.id.food_page_menu_details:
                    break;
                default:
                    mode.finish(); // Action picked, so close the CAB
                    return false;
            }
            mode.finish(); // Action picked, so close the CAB
            return true;
        }

        // Called when the user exits the action mode
        // also on mode.finish()
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            releaseSelectedCard();
            mActionMode = null;
        }
    };

    private void captureSelectedCard(Card card) {
        // capture
        currentSelectedCard = (CustomCard) card;
        // UI change
        currentSelectedCard.cSetOverlayVisibility(View.VISIBLE);
        currentSelectedCard.getCardView().refreshCard(currentSelectedCard);
        //Toast.makeText(this, "Card Captured", Toast.LENGTH_LONG).show();
    }

    private void releaseSelectedCard() {
        // UI change
        currentSelectedCard.cSetOverlayVisibility(View.GONE);
        currentSelectedCard.getCardView().refreshCard(currentSelectedCard);
        // release
        currentSelectedCard = null;
        //Toast.makeText(this, "Card Released", Toast.LENGTH_LONG).show();
    }
}
