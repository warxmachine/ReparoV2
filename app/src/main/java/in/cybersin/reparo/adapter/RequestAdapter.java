package in.cybersin.reparo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.cybersin.reparo.R;
import in.cybersin.reparo.model.Reqeust;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder>{
    ArrayList<Reqeust> list;
    View view;

    public RequestAdapter(ArrayList<Reqeust> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request_adapter, parent, false);

        return new RequestAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
