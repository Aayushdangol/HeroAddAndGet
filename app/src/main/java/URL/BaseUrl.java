package URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseUrl {

    public static final String Base_Url ="http://10.0.2.2:3000/";


    public static Retrofit getInstance() {

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BaseUrl.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
    }
}
