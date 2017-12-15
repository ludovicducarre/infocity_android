package fr.univtln.tbezenger858.infocity;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView cityName;
    private ListView eventsList;
    private ArrayList<String> events;
    private ArrayAdapter<String> eventsArrayAdapter;
    private SearchView citySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        citySearch = (SearchView) findViewById(R.id.citySearch);
        citySearch.setQueryHint("Rechercher une ville");

        citySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cityName.setText(query);
                citySearch.setQuery("",false);
                citySearch.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        cityName = (TextView) findViewById(R.id.NomVille);
        eventsList = (ListView) findViewById(R.id.EventListe);

        events = new ArrayList<>();
        events.add("Event 1");
        events.add("Event 2");

        eventsArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,events);
        eventsList.setAdapter(eventsArrayAdapter);
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ouvrir une page de description détaillée
//                openDescriptionActivity();
                startActivity(new Intent(getBaseContext(),EventDescriptionActivity.class));
            }
        });
    }

    public void openDescriptionActivity(){

    }

}
