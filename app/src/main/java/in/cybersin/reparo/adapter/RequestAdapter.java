package in.cybersin.reparo.adapter;


import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.tntkhang.gmailsenderlibrary.GMailSender;
import com.github.tntkhang.gmailsenderlibrary.GmailListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.cybersin.reparo.R;
import in.cybersin.reparo.model.Request;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    ArrayList<Request> list;
    android.app.AlertDialog.Builder dialog;
    View view;
    String positions;

    public RequestAdapter(ArrayList<Request> list) {

        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Problem.setText("Reason: " + list.get(position).getProblem());
        holder.Date.setText("Date&Time: " + list.get(position).getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positions = list.get(position).getId();
                Log.e("POSITION",  list.get(position).getId() );
                showdialog();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Problem, Date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Problem = itemView.findViewById(R.id.prob);
            Date = itemView.findViewById(R.id.date);
        }
    }

    private void showdialog() {
        Button Request, Remove;
        LayoutInflater factory = LayoutInflater.from(view.getContext());
        final View deleteDialogView = factory.inflate(R.layout.request_dialog, null);
        Remove = deleteDialogView.findViewById(R.id.remove);
        Request = deleteDialogView.findViewById(R.id.notresolved);
        EditText phone = deleteDialogView.findViewById(R.id.phone);
        dialog = new android.app.AlertDialog.Builder(view.getContext()); // Context, this, etc.
        dialog.setView(deleteDialogView);
        Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 DatabaseReference reference =FirebaseDatabase.getInstance().getReference("Requests").child(FirebaseAuth.getInstance().getUid()).child(positions);
                 reference.removeValue();
                Toast.makeText(view.getContext(), "Successfully Removed your Request", Toast.LENGTH_SHORT).show();

            }
        });
        Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("CustomerInformation").child(FirebaseAuth.getInstance().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                email(snapshot.child("phone").getValue().toString());
                                Toast.makeText(view.getContext(), "Successfully sent your request again!!", Toast.LENGTH_SHORT).show();

                                final AlertDialog show = dialog.show();
                                show.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
        });


        dialog.show();

    }

    private void email(String phone) {
        GMailSender.withAccount("reparo.noreply@gmail.com", "zqbmbzrthgepmywz")
                .withTitle("Request Remainder")
                .withBody("Request Remainder " + "\n " + phone)
                .withSender(String.valueOf(R.string.app_name))
                .toEmailAddress("shubhsingh20998@gmail.com,cybersinindustries@gmail.com,warxmachine3@gmail.com") // one or multiple addresses separated by a comma
                .withListenner(new GmailListener() {
                    @Override
                    public void sendSuccess() {
                        Toast.makeText(view.getContext(), "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void sendFail(String err) {
                        Toast.makeText(view.getContext(), "Fail: " + err, Toast.LENGTH_SHORT).show();
                    }
                })
                .send();

    }

}
