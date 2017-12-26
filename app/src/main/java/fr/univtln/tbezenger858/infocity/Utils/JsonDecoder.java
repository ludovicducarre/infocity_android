package fr.univtln.tbezenger858.infocity.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomtom on 24/12/17.
 */

public class JsonDecoder<T> {

    public T Decoder(String pJson, Class pType) throws IOException
    {
        return (T) new ObjectMapper().readValue(pJson, pType);
    }

    public List<T> DecoderList (String pJson, Class pType) throws IOException, JSONException {
        return new ObjectMapper().readValue(pJson, new ObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, pType));
    }
}
