package fr.univtln.tbezenger858.infocity;


import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import fr.univtln.lducarre365.infoCity.business.Advert;
import fr.univtln.lducarre365.infoCity.business.Town;

public class MainActivity extends AppCompatActivity implements Observer {

    private Model model;
    private TextView cityName;
    private ListView eventsList;
    private ArrayList<String> events;
    private ArrayAdapter<String> eventsArrayAdapter;
    private SearchView citySearch;
    private LinearLayout linearScroll;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        model = Model.getInstance();
        model.addObserver(this);

        linearScroll = (LinearLayout) findViewById(R.id.linearScroll);

        // creation de la SearchView
        citySearch = (SearchView) findViewById(R.id.citySearch);
        citySearch.setQueryHint("Rechercher une ville");
        citySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (model.updateAdverts(query)) {
                    Log.d("NTM", "onQueryTextSubmit: ");
                    cityName.setText(query);
                    citySearch.setQuery("", false);
                    citySearch.clearFocus();
                    return true;
                }
                else {
                    Toast.makeText(getBaseContext(),"ville introuvable ou ne comportant aucune annonce",Toast.LENGTH_SHORT)
                            .show();
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
        events.add("Aucune annonce pour cette ville");
        eventsList = (ListView) findViewById(R.id.EventListe);
        eventsArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,events);
        eventsList.setAdapter(eventsArrayAdapter);
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getBaseContext(),EventDescriptionActivity.class)
                                        .putExtra("advertTitle",parent.getItemAtPosition(position).toString()));
            }
        });


        // Affichage de la ville de démarrage par geolocalisation
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        while (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},0);
        }
        cityName = (TextView) findViewById(R.id.NomVille);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(getBaseContext());
                            try {
                                cityName.setText(geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1).get(0).getLocality());
                                model.updateAdverts(cityName.getText().toString());
                                eventsList.setVisibility(View.VISIBLE);
                                citySearch.setVisibility(View.VISIBLE);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

    }


    @Override
    public void update(Observable observable, Object o) {
        // suppression de tous les boutons existants pour en créer de nouveaux
        linearScroll.removeAllViews();
        if (model.getAdverts().isEmpty()){
            linearScroll.removeAllViews();
            events.clear();
            events.add("Aucune annonce pour cette ville");
            eventsArrayAdapter.notifyDataSetChanged();
            return;
        }

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
