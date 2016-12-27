package com.maksimik.weather.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.maksimik.weather.R;
import com.maksimik.weather.loader.ImageLoader;
import com.maksimik.weather.model.City;
import com.maksimik.weather.model.CityWithWeatherHour;
import com.maksimik.weather.utils.ContextHolder;

import java.util.List;

public class CitiesRecyclerViewAdapter extends RecyclerView.Adapter<CitiesRecyclerViewAdapter.ViewHolder> {

    private final OnItemClickListener listener;

    private final List<CityWithWeatherHour> cityWithWeatherHours;

    private final ImageLoader imageLoader;
    private final Context context;
    private final Context c;
    private final OnMenuItemClickListener lis;

    public interface OnItemClickListener {

        void onItemClick(CityWithWeatherHour cityWithWeatherHour);

    }

    public interface OnMenuItemClickListener {

        boolean onMenuItemClick(City city, int pos, MenuItem item);
    }

    public CitiesRecyclerViewAdapter(final List<CityWithWeatherHour> cityWithWeatherHours, final Context context, final ImageLoader imageLoader, final OnItemClickListener listener, final OnMenuItemClickListener lis) {
        this.listener = listener;
        this.cityWithWeatherHours = cityWithWeatherHours;
        this.imageLoader = imageLoader;
        this.context = ContextHolder.getInstance().getContext();
        //TODO context and listener
        this.lis = lis;
        c = context;
    }

    @Override
    public CitiesRecyclerViewAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View cityView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(cityView);
    }

    @Override
    public void onBindViewHolder(final CitiesRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.bind(cityWithWeatherHours.get(position));

        holder.iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {

                showPopup(holder.iv, position);
            }
        });

    }

    private void showPopup(final View v, final int position) {

        final PopupMenu popup = new PopupMenu(c, v);
        final MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                lis.onMenuItemClick(cityWithWeatherHours.get(position).getCity(), position, item);

                return true;
            }
        });
        inflater.inflate(R.menu.popupmenu, popup.getMenu());
        popup.show();
    }

    @Override
    public int getItemCount() {
        return cityWithWeatherHours.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView cityName;
        private final ImageView cityImage;
        private final TextView cityTemp;
        private final TextView cityWind;
        private final TextView cityHumidity;
        private final ImageButton iv;

        ViewHolder(final View itemView) {
            super(itemView);

            cityName = (TextView) itemView.findViewById(R.id.cityNameRecycler);
            cityImage = (ImageView) itemView.findViewById(R.id.cityImageRecycler);
            cityTemp = (TextView) itemView.findViewById(R.id.cityTempRecycler);
            cityWind = (TextView) itemView.findViewById(R.id.cityWindRecycler);
            cityHumidity = (TextView) itemView.findViewById(R.id.cityHumidityRecycler);
            iv = (ImageButton) itemView.findViewById(R.id.btMenu);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(final View view) {
            final int position = getAdapterPosition();
            listener.onItemClick(cityWithWeatherHours.get(position));
        }

        void bind(final CityWithWeatherHour cityWithWeatherHour) {
            final Resources res = context.getResources();
            cityName.setText(cityWithWeatherHour.getCity().getName());
            if (cityWithWeatherHour.getWeatherHour() != null) {

                cityTemp.setText(String.format(res.getString(R.string.temp_recicler), (int) cityWithWeatherHour.getWeatherHour().getMain().getTemp()));

                cityWind.setText(String.format(res.getString(R.string.wind_recicler), cityWithWeatherHour.getWeatherHour().getWind().getSpeed()));
                cityHumidity.setText(String.format(res.getString(R.string.humidity_recicler), cityWithWeatherHour.getWeatherHour().getMain().getHumidity()));

                imageLoader.displayImage(cityWithWeatherHour.getWeatherHour().getWeather().getIcon(), cityImage);

            } else {

                cityTemp.setText("Нет актуальной погоды");
                cityWind.setText(" ");
                cityHumidity.setText(" ");
                imageLoader.displayImage("background", cityImage);
            }
        }
    }
}