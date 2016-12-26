package com.maksimik.weather.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.maksimik.weather.R;
import com.maksimik.weather.activites.WeatherDetailsActivity;
import com.maksimik.weather.constants.Constants;
import com.maksimik.weather.model.DayWeather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PageFragment extends Fragment {

    static final String ARGUMENT_DAY_WEATHER = "arg_day_weather";

    private DayWeather dayWeather;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dayWeather = (DayWeather) getArguments().getSerializable(ARGUMENT_DAY_WEATHER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment, container, false);
        fillingViewPage(dayWeather);
        return view;
    }

    public static Fragment newInstance(DayWeather dayWeather) {
        Fragment fragment = new PageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGUMENT_DAY_WEATHER, dayWeather);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void fillingViewPage(final DayWeather dayWeather) {

        if (dayWeather != null) {

            final String ATTRIBUTE_TIME = "time";
            final String ATTRIBUTE_TEMP = "temp";
            final String ATTRIBUTE_ICON = "icon";
            final String ATTRIBUTE_DESCRIPTION = "description";
            Map<String, Object> m;
            ListView lvSimple;
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

            ArrayList<Map<String, Object>> data = new ArrayList<>();

            for (int i = 0; i < dayWeather.size(); i++) {
                m = new HashMap<>();

                if((i==dayWeather.size()-1 && i!=0) ||(dayWeather.size()==1)) {
                    m.put(ATTRIBUTE_TIME, "24:00");
                }else{
                    m.put(ATTRIBUTE_TIME, dateFormat.format(dayWeather.getWeatherHour(i).getDate()));
                }
                m.put(ATTRIBUTE_ICON, dayWeather.getWeatherHour(i).getWeather().getIcon());
                m.put(ATTRIBUTE_DESCRIPTION, getString(getResources().getIdentifier("forecast_" + dayWeather.getWeatherHour(i).getWeather().getIcon(),
                        "string", getActivity().getPackageName())));
                m.put(ATTRIBUTE_TEMP, (int) dayWeather.getWeatherHour(i).getMain().getTemp() + "°");
                data.add(m);
            }


            String[] from = {ATTRIBUTE_TIME, ATTRIBUTE_ICON, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_TEMP};
            int[] to = {R.id.textViewTime, R.id.imageView, R.id.textViewDescription, R.id.textViewTemp};
            SimpleAdapter sAdapter = new SimpleAdapter(getActivity().getBaseContext(), data, R.layout.list_item_weather, from, to);
            sAdapter.setViewBinder(new MyViewBinder());
            lvSimple = (ListView) view.findViewById(R.id.list);

            lvSimple.setAdapter(sAdapter);
            lvSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                    Intent intent = new Intent(getActivity().getBaseContext(), WeatherDetailsActivity.class);
                    intent.putExtra(Constants.WEATHER_DETAILS_KEY, dayWeather.getWeatherHour(pos));
                    startActivity(intent);
                }
            });

        }
    }

    class MyViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {

            if (view.getId() == R.id.imageView) {
                ImageView iv = (ImageView) view;
                iv.setImageResource(getResources().getIdentifier(Constants.IMAGE + data.toString(), "drawable", getActivity().getPackageName()));
                return true;
            }
            return false;
        }
    }

}