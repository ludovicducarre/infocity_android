package fr.univtln.tbezenger858.infocity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import fr.univtln.lducarre365.infoCity.business.Advert;
import fr.univtln.tbezenger858.infocity.Utils.Config;


/**
 * Created by tomy- on 14/12/2017.
 */
public class EventDescriptionActivity extends AppCompatActivity{

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_description_activity);

        Model model = Model.getInstance();

        text = (TextView) findViewById(R.id.textdescriptionpage);

        String url = Config.URL + "/advert/tous";

        String advertTitle = getIntent().getStringExtra("advertTitle");
        if (!advertTitle.equals("")){
            for(Advert advert:model.getAdverts()){
                if (advert.getTitle().equals(advertTitle)){
                    text.setText(advert.toString());
                    break;
                }
            }
        }
    }
}
