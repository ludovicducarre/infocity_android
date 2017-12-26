package fr.univtln.tbezenger858.infocity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import fr.univtln.lducarre365.infoCity.business.Advert;
import fr.univtln.lducarre365.infoCity.business.Town;

public class MainActivity extends AppCompatActivity implements Observer{

    private Model model;
    private TextView cityName;
    private ListView eventsList;
    private ArrayList<String> events;
    private ArrayAdapter<String> eventsArrayAdapter;
    private SearchView citySearch;
    private LinearLayout linearScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = Model.getInstance();
        model.addObserver(this);

        linearScroll = (LinearLayout) findViewById(R.id.linearScroll);

        // Affichage de la ville de démarrage
        //TODO : faire ça en géoloc si possible
        final String startTownName = "Toulon";
        cityName = (TextView) findViewById(R.id.NomVille);
        cityName.setText(startTownName);

        // creation de la SearchView
        citySearch = (SearchView) findViewById(R.id.citySearch);
        citySearch.setQueryHint("Rechercher une ville");
        citySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (model.getAdvertsByTown(query)) {
                    cityName.setText(query);
                    citySearch.setQuery("", false);
                    citySearch.clearFocus();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        // creation de la ListView
        events = new ArrayList<>();
        eventsList = (ListView) findViewById(R.id.EventListe);
        eventsArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,events);
        eventsList.setAdapter(eventsArrayAdapter);
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getBaseContext(),EventDescriptionActivity.class)
                                        .putExtra("numAdvert",position));
            }
        });

        // remplissage de la page d'accueil
        model.getAdvertsByTown(startTownName);
    }


    @Override
    public void update(Observable observable, Object o) {
        // suppression de tous les boutons existants pour en créer de nouveaux
        linearScroll.removeAllViews();

        // Ajout d'un bouton "Tous" pour afficher tous les types d'advert
        Button button = new Button(this);
        button.setText("Tous");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                events.clear();
                for (Advert advert:model.getAdverts()) {
                    events.add(advert.getTitle());
                }
                eventsArrayAdapter.notifyDataSetChanged();
            }
        });
        linearScroll.addView(button);

        // remise à zero de la liste d'advert (events ...)
        // et creation d'un liste de types pour eviter les boutons dupliqués
        events.clear();
        ArrayList<String> types = new ArrayList<>();

        // creation des boutons de selection d'advert par type
        // et initialisation de la liste complète
        for (Advert advert:model.getAdverts()) {
            events.add(advert.getTitle());
            if (!types.contains(advert.getType()) && advert.getType() != null) {
                button = new Button(this);
                button.setTag(advert.getType());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        events.clear();
                        for (Advert advert : model.getAdverts()) {
                            if (advert.getType() == view.getTag())
                                events.add(advert.getTitle());
                        }
                        eventsArrayAdapter.notifyDataSetChanged();
                    }
                });
                button.setText(advert.getType());
                linearScroll.addView(button);
            }
        }

    }
}
