package fr.univtln.tbezenger858.infocity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
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

        int numAdvert = getIntent().getIntExtra("numAdvert",-1);
        if (numAdvert >=0 ){
            text.setText(model.getAdverts().get(numAdvert).toString());
        }
    }
}
