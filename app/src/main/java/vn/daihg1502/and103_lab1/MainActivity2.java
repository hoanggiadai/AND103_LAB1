package vn.daihg1502.and103_lab1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity {

    FirebaseFirestore database;
    Button btnInsert, btnUpdate, btnDelete;
    TextView tvResult, tvResult1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        database = FirebaseFirestore.getInstance(); // Khởi tạo database
        tvResult = findViewById(R.id.tvResult);
        tvResult1 = findViewById(R.id.tvResult1);
        btnInsert = findViewById(R.id.btnInsert);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        btnInsert.setOnClickListener(v->{
            insertFirebase(tvResult);
        });

        btnUpdate.setOnClickListener(v->{
            updateFirebase(tvResult);
        });

        btnDelete.setOnClickListener(v->{
            deleteFirebase(tvResult);
        });

        SelectDataFromFirebase(tvResult1);
    }

    String id = "";
    ToDo toDo = null;

    public void insertFirebase(TextView tvResult){
        id = UUID.randomUUID().toString(); // Lấy 1 id bất kỳ

        // Tạo đối tượng để insert
        toDo = new ToDo(id, "title 1", "content 1");

        // Chuyển đổi sang đối tượng có thể thao tác với firebase
        HashMap<String, Object> mapToDo = toDo.convertHashMap();

        // Insert vào database
        database.collection("TODO").document(id)
                .set(mapToDo) // Đối tượng cần insert
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("Thêm thành công!!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
    }

    public void updateFirebase(TextView tvResult){
        id="4707b64c-bd10-42f0-b9a9-dbd88ce53884";
        toDo = new ToDo(id, "title Update", "content Update");
        database.collection("TODO").document(toDo.getId())
                .update(toDo.convertHashMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("Update thành công!!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
    }

    public void deleteFirebase(TextView tvResult){
        id="4707b64c-bd10-42f0-b9a9-dbd88ce53884";
        database.collection("TODO").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("Delete thành công!!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
    }

    String strResult = "";
    public ArrayList<ToDo> SelectDataFromFirebase(TextView tvResult){
        ArrayList<ToDo> list = new ArrayList<>();
        database.collection("TODO")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){ // sau khi lấy dữ liệu thành công
                            strResult="";
                            // đọc theo đối tượng dữ liệu
                            for (QueryDocumentSnapshot document: task.getResult()){
                                // Chuyen dòng đọc được sang đối tượng
                                ToDo toDo1 = document.toObject(ToDo.class);
                                //Chuyển đối tượng thành chuỗi
                                strResult+="Id: "+toDo1.getId()+"\n";
                                list.add(toDo1); // Thêm vào list
                                // Hiển thị kết quả
                                tvResult.setText(strResult);
                            }
                        } else {
                            tvResult.setText("Đọc dữ liệu thất bại");
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
        return list;
    }
}