package fr.univtln.tbezenger858.infocity;

import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

import fr.univtln.lducarre365.infoCity.business.Advert;
import fr.univtln.lducarre365.infoCity.business.Town;
import fr.univtln.tbezenger858.infocity.Utils.Config;
import fr.univtln.tbezenger858.infocity.Utils.JsonDecoder;
import fr.univtln.tbezenger858.infocity.Utils.Requete;

/**
 * Created by tomtom on 26/12/17.
 */

public class Model extends Observable{
    private static final Model ourInstance = new Model();

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
    }

    private List<Advert> adverts;

    public List<Advert> getAdverts() {
        return adverts;
    }


    // TODO : mettre une vraie URL quand le serveur sera prêt
    public boolean getAdvertsByTown(String townName){
        Log.d("NTM", "getAdvertsByTown: ");
        adverts = new ArrayList<>();
        try {
            String url = Config.URL + "/advert/tous";
            String JSON = new Requete().execute(url,"GET").get();
            JsonDecoder<Advert> decoder = new JsonDecoder<>();
            adverts = decoder.DecoderList(JSON,Advert.class);
            setChanged();
            notifyObservers();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return !(adverts.isEmpty());
    }
}
