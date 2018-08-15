package com.example.user.game2048;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IEventChangeDataGame, View.OnClickListener {
    private RecyclerView recyclerView;
    private Adapter_RecyclerView adapter_recyclerView;
    private TextView txt_DiemSo, txt_DiemCao;
    private View.OnTouchListener onTouchListener;
    private float X, Y;

    private SessionDiemCao sessionDiemCao;
    private EditText edt_SoO;
    private Button btn_StartGame, btn_QuayLai, btn_XacNhan;

    // biến trạng thái cho biết đang xử lý sự kiện vuốt màn hình hay không
    private boolean isHandle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // tạo 1 SharedPreferences nhằm lưu trữ điểm cao nhất của người chơi
        // trong điện thoại
        sessionDiemCao = new SessionDiemCao(getBaseContext());

        initView();
        initRecyclerView();
        GameController.getInstance().startGame();

        txt_DiemCao.setText(String.valueOf(sessionDiemCao.getSessionDiemSo()));

    }

    // Setup hiển thị cho RecyclerView
    // nơi hiển thị bàn cờ ô điểm
    @SuppressLint("ClickableViewAccessibility")
    private void initRecyclerView() {
        // setup GridLayout cho RecyclerView (dạng lưới, kiểu như bảng)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(), GameController.SO_O);
        recyclerView.setLayoutManager(gridLayoutManager);

        GameController.getInstance().initDataGame(getBaseContext(), this);
        adapter_recyclerView = new Adapter_RecyclerView(getBaseContext(), GameController.getInstance().getDataGameList());
        recyclerView.setAdapter(adapter_recyclerView);

        // bắt sự kiện vuốt màn hình trên RecyclerView
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    // lúc nhấn tay xuống
                    case MotionEvent.ACTION_DOWN: {
                        // tọa độ X và Y của vị trí nhấn
                        X = motionEvent.getX();
                        Y = motionEvent.getY();
                        break;
                    }
                    // lúc nhả tay ra
                    case MotionEvent.ACTION_UP: {
                        // nếu đang xử lý tọa độ nhấn nhả tay
                        // thì thoát ra
                        // vì chưa xử lý xong lần vuốt trước
                        if (isHandle)
                            return true;
                        // bật cờ isHandle
                        // để xác định đang xử lý sự kiện vuốt màn hình
                        isHandle = true;

                        // tọa độ x và y của vị trí nhả
                        float x = motionEvent.getX();
                        float y = motionEvent.getY();

                        // tính độ dài từ vị trí nhấn và nhả theo X và Y
                        float xX = Math.abs(x - X);
                        float yY = Math.abs(y - Y);

                        // nếu vuốt quá ngắn hoặc không vuốt thì không xử lý
                        // đổi cờ isHandle sang false
                        // để app có thể xử lý lần vuốt tiếp theo
                        if (Math.abs(xX - yY) < 10) {
                            isHandle = false;
                            return true;
                        }

                        // xác định vuốt theo chiều ngang
                        // xử lý tác vụ
                        // sau khi xử lý xong
                        // GameController sẽ chạy hàm onCancel()
                        // để đổi lại trạng thái cờ isHandle
                        // và app có thể lại xử lý tác vụ vuốt màn hình
                        if (xX > yY) {
                            if (x > X) {
                                // Toast.makeText(getBaseContext(), "Right", Toast.LENGTH_SHORT).show();
                                GameController.getInstance().vuotPhai();
                                adapter_recyclerView.notifyDataSetChanged();
                            } else {
                                //  Toast.makeText(getBaseContext(), "Left", Toast.LENGTH_SHORT).show();
                                GameController.getInstance().vuotTrai();
                                adapter_recyclerView.notifyDataSetChanged();
                            }
                        }
                        // xác định vuốt theo chiều dọc
                        // cách thức xử lý tương tự như trên
                        else {
                            if (y > Y) {
                                // Toast.makeText(getBaseContext(), "Down", Toast.LENGTH_SHORT).show();
                                GameController.getInstance().vuotXuong();
                                adapter_recyclerView.notifyDataSetChanged();
                            } else {
                                // Toast.makeText(getBaseContext(), "Up", Toast.LENGTH_SHORT).show();
                                GameController.getInstance().vuotLen();
                                adapter_recyclerView.notifyDataSetChanged();
                            }
                        }

                        break;
                    }
                }
                return true;
            }
        });
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        btn_StartGame = findViewById(R.id.btn_StartGame);
        txt_DiemSo = findViewById(R.id.txt_DiemSo);
        txt_DiemCao = findViewById(R.id.txt_DiemCao);
        btn_QuayLai = findViewById(R.id.btn_QuayLai);
        btn_XacNhan = findViewById(R.id.btn_XacNhan);
        edt_SoO = findViewById(R.id.edt_SoO);

        btn_XacNhan.setOnClickListener(this);
        btn_QuayLai.setOnClickListener(this);
        btn_StartGame.setOnClickListener(this);
    }

    // GameController thông báo hiển thị điểm số ngẫu nhiên mới tạo cho các ô
    // nằm ở vị trí trong list position
    @Override
    public void onInsertData(List<Integer> position) {
        for (int i = 0; i < position.size(); i++) {
            adapter_recyclerView.notifyItemChanged(position.get(i));
        }

        isHandle = false;

    }

    // GameController thông báo cần cập nhật hiển thị lại điểm các ô
    // nằm ở vị trí trong list position
    @Override
    public void onChangeData(List<Integer> position) {
        for (int i = 0; i < position.size(); i++) {
            adapter_recyclerView.notifyItemChanged(position.get(i));
        }
    }

    // Cập nhật lại adapter của RecyclerView
    // -> cập nhật lại hiển thị điểm số
    @Override
    public void onRefreshData() {
        adapter_recyclerView.notifyDataSetChanged();
    }

    // Kết thúc game
    // khi kiểm tra theo cả chiều dọc và chiều ngang
    // đều không còn ô 0 điểm hoặc không còn 2 ô nào liền kề bằng điểm nhau
    @Override
    public void onEnd() {
        // show 1 dialog full màn hình
        // hiển thị chữ thất bại
        // và nền mờ xám
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                GameController.getInstance().restartGame();
            }
        });
        dialog.show();
    }

    // Cập nhật lại điểm số của người chơi
    // nếu nó cao hơn điểm tối đã từng đạt được sẽ được lưu trữ
    // và hiển thị trên TextView Điểm cao nhất
    @Override
    public void changePoint(int point) {
        txt_DiemSo.setText(String.valueOf(point));
        if (point > sessionDiemCao.getSessionDiemSo()) {
            sessionDiemCao.clearData();
            sessionDiemCao.createSession(point);
            txt_DiemCao.setText(String.valueOf(sessionDiemCao.getSessionDiemSo()));
        }

    }

    // Sau khi GameController hoàn thành tác vụ vuốt của mình
    @Override
    public void onCancel() {
        isHandle = false;
    }

    // Set OnClickListener cho các nút trong trò chơi
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_StartGame: {
                GameController.getInstance().restartGame();
                break;
            }
            case R.id.btn_QuayLai: {
                GameController.getInstance().quayLai();
                adapter_recyclerView.notifyDataSetChanged();


                break;
            }
            case R.id.btn_XacNhan: {
                if (edt_SoO.getText().toString().isEmpty())
                    return;

                int soO = Integer.parseInt(edt_SoO.getText().toString());
                if (soO < 4 || soO > 10)
                    return;

                GameController.SO_O = soO;
                initRecyclerView();

               /* GameController.getInstance().initDataGame(getBaseContext(),this);
                GameController.getInstance().startGame();*/

                GameController.getInstance().thayDoiSoO(getBaseContext());

                break;
            }
        }
    }
}
