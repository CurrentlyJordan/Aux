package nyc.c4q.ashiquechowdhury.auxx;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nyc.c4q.ashiquechowdhury.auxx.joinandcreate.CreateRoomFragment;
import nyc.c4q.ashiquechowdhury.auxx.model.Example;
import nyc.c4q.ashiquechowdhury.auxx.model.Item;
import nyc.c4q.ashiquechowdhury.auxx.model.SpotifyService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by shawnspeaks on 3/6/17.
 */

public class FloatingActionSearchFragment extends Fragment {

    private long lastChange = 0;
    private List<Item> itemList = new ArrayList<Item>();
    private RecyclerView recyclerView;
    private EditText editText;
    private ImageButton backSearchButton;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_song_search, container, false);
            return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.search_recycler_fragment);
        findItems();
        editText = (EditText) view.findViewById(R.id.search_edit_text);
        editText.addTextChangedListener(searchWatcher);
        backSearchButton = (ImageButton) view.findViewById(R.id.back_search_btn);

        backSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.searchandchoose_innerframe, new CreateRoomFragment()).commit();
            }
        });


    }

    void getSongData(String query) {
        query = query.replace(" ", "+");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpotifyService spotifyService = retrofit.create(SpotifyService.class);
        Call<Example> httpRequest = spotifyService.getOtherResults(query, "track");
        httpRequest.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                try {
                    if (response.isSuccessful()) {
                        itemList = response.body().getTracks().getItems();
                        findItems();
                    } else {
                        Log.d(TAG, "Error" + response.errorBody().string());
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d("failure", "no connection");

            }
        });
    }



    private final TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final String searchTerm = s.toString();
            new Handler().postDelayed(

                    new Runnable() {
                        @Override
                        public void run() {

                            if (noChangeInText()) {
                                getSongData(searchTerm);
                            }
                        }
                    },
                    300);

            lastChange = System.currentTimeMillis();

        }  @Override
        public void afterTextChanged(Editable s) {

        }

        private boolean noChangeInText() {
            return System.currentTimeMillis() - lastChange >= 300;
        }
    };


    void findItems() {
        SearchAdapter searchAdapter = new SearchAdapter(itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(searchAdapter);
    }
}
