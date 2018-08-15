package com.example.user.game2048;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by USER on 01/02/2018.
 */

public class GameController {
    public static int SO_O = 4;
    private static GameController instance;
    private Random random;
    private int[][] mangDiemSo;
    private List<DataGame> dataGameList;
    private List<List<DataGame>> dataGameSaveList;
    private int[] mangMau;
    private IEventChangeDataGame iEventChangeDataGame;

    private int diemSo = 0;

    private Flag flag1;
    private Flag flag2;

    public static GameController getInstance() {
        if (instance == null)
            instance = new GameController();
        return instance;
    }

    public List<DataGame> getDataGameList() {
        return dataGameList;
    }

    public List<DataGame> getDataGameListSave() {
        if (dataGameSaveList == null)
            return null;
        return dataGameSaveList.get(dataGameSaveList.size() - 1);
    }

    public void initDataGame(Context context, IEventChangeDataGame iEventChangeDataGame) {
        this.iEventChangeDataGame = iEventChangeDataGame;
        dataGameList = new ArrayList<>();
        dataGameSaveList = new ArrayList<>();
        mangDiemSo = new int[SO_O][SO_O];
        mangMau = new int[SO_O * SO_O];
        random = new Random();
        flag1 = new Flag();
        flag2 = new Flag();

        @SuppressLint("Recycle") TypedArray ta = context.getResources().obtainTypedArray(R.array.mangMau);
        for (int i = 0; i < SO_O * SO_O; i++) {
            mangMau[i] = ta.getColor(i, 0);
        }

        for (int i = 0; i < SO_O; i++) {
            for (int j = 0; j < SO_O; j++) {
                mangDiemSo[i][j] = 0;
                dataGameList.add(new DataGame(mangDiemSo[i][j], getColor(mangDiemSo[i][j]), false));
            }
        }

    }

    public void thayDoiSoO(Context context) {
        mangDiemSo = new int[SO_O][SO_O];
        mangMau = new int[SO_O * SO_O];
        @SuppressLint("Recycle") TypedArray ta = context.getResources().obtainTypedArray(R.array.mangMau);
        for (int i = 0; i < SO_O * SO_O; i++) {
            mangMau[i] = ta.getColor(i, 0);
        }

        restartGame();
    }


    public void startGame() {

        taoSoNgauNhien();
        iEventChangeDataGame.onCancel();
    }

    // quay lại trạng thái trước của game
    public void quayLai() {
        if (dataGameSaveList.size() < 2)
            return;
        dataGameList.clear();

        List<DataGame> dataGames = new ArrayList<>();

        for (int n = 0; n < SO_O * SO_O; n++) {
            dataGames.add(new DataGame(dataGameSaveList.get(dataGameSaveList.size() - 2).get(n)));
        }

        dataGameList.addAll(dataGames);


        for (int i = 0; i < SO_O; i++) {
            for (int j = 0; j < SO_O; j++) {
                mangDiemSo[i][j] = dataGameList.get(i * SO_O + j).getDiemSo();

            }
        }
        dataGameSaveList.remove(dataGameSaveList.size() - 1);
        iEventChangeDataGame.onCancel();
        //dataGameSaveList.remove(dataGameSaveList.size() - 1);
    }


    public void restartGame() {

        diemSo = 0;
        dataGameSaveList.clear();
        dataGameList.clear();
        for (int i = 0; i < SO_O; i++) {
            for (int j = 0; j < SO_O; j++) {
                mangDiemSo[i][j] = 0;
                dataGameList.add(new DataGame(0, getColor(0), false));
            }
        }
        iEventChangeDataGame.onRefreshData();
        iEventChangeDataGame.changePoint(diemSo);
        iEventChangeDataGame.onCancel();
        taoSoNgauNhien();
    }

    // get màu sắc con số theo số điểm
    public int getColor(int soDiem) {
        if (soDiem == 0)
            return mangMau[0];
        else {
            // xác định n theo logarit cơ số 2
            int n = (int) (Math.log(soDiem) / Math.log(2));
            return mangMau[n];
        }
    }

    // hàm tạo số ngẫu nhiên (2 hoặc 4) cho các ô còn trống
    // hàm luôn chạy sau khi có ô được dồn hoặc lúc khởi đầu game
    // nên luôn bảo đảm còn ô trống có thể tạo
    private void taoSoNgauNhien() {
        //  chỉ số xác định còn bao nhiêu ô trống
        int soOTrong = 0;
        for (int i = 0; i < SO_O; i++) {
            for (int j = 0; j < SO_O; j++) {
                if (mangDiemSo[i][j] == 0)
                    soOTrong++;

                if (soOTrong == 2)
                    break;
            }
        }
        int i = 0;
        int j = 0;
        List<Integer> integerList = new ArrayList<>();
        // còn đúng 1 ô trống
        if (soOTrong == 1) {

            while (true) {
                i = random.nextInt(SO_O);
                j = random.nextInt(SO_O);
                if (mangDiemSo[i][j] == 0)
                    break;
            }
            int soDiem = random.nextInt(2);
            if (soDiem == 0)
                mangDiemSo[i][j] = 2;
            else
                mangDiemSo[i][j] = 4;

            dataGameList.get(i * SO_O + j).setDiemSo(mangDiemSo[i][j]);
            dataGameList.get(i * SO_O + j).setColor(getColor(mangDiemSo[i][j]));
            dataGameList.get(i * SO_O + j).setNew(true);
            integerList.add(i * SO_O + j);

        }
        // còn từ 2 ô trống trở lên
        else {
            for (int k = 0; k < random.nextInt(2) + 1; k++) {
                while (true) {
                    i = random.nextInt(SO_O);
                    j = random.nextInt(SO_O);
                    if (mangDiemSo[i][j] == 0)
                        break;
                }
                int soDiem = random.nextInt(2);
                if (soDiem == 0)
                    mangDiemSo[i][j] = 2;
                else
                    mangDiemSo[i][j] = 4;

                dataGameList.get(i * SO_O + j).setDiemSo(mangDiemSo[i][j]);
                dataGameList.get(i * SO_O + j).setColor(getColor(mangDiemSo[i][j]));
                dataGameList.get(i * SO_O + j).setNew(true);
                integerList.add(i * SO_O + j);

            }

            List<DataGame> dataGames = new ArrayList<>();
            for (int n = 0; n < SO_O * SO_O; n++) {
                dataGames.add(new DataGame(dataGameList.get(n)));
            }

            dataGameSaveList.add(dataGames);


        }
        // thông báo phải cập nhật lại điểm các ô có vị trí nằm trong integerList
        iEventChangeDataGame.onInsertData(integerList);
        iEventChangeDataGame.onCancel();

    }

    // kiểm tra theo hàng ngang
    // trả về FALSE nếu có bất kỳ hàng ngang nào có ô 0 điểm hoặc 2 ô liền kề bằng điểm nhau
    // trả về TRUE nếu tất cả các ô đều có điểm số lớn hơn 0 và không có 2 ô liền kề nào theo hàng ngang bằng điểm nhau
    public boolean kiemTraNgang() {
        int diemSo_Temp = 0;
        for (int i = 0; i < SO_O; i++) {
            diemSo_Temp = 0;
            for (int j = 0; j < SO_O; j++) {
                if (mangDiemSo[i][j] == 0)
                    return false;
                else {
                    if (diemSo_Temp == mangDiemSo[i][j])
                        return false;
                    diemSo_Temp = mangDiemSo[i][j];
                }
            }
        }
        return true;
    }

    // tương tự như kiểm tra hàng ngang
    public boolean kiemTraDoc() {
        int diemSo_Temp = 0;
        for (int i = 0; i < SO_O; i++) {
            diemSo_Temp = 0;
            for (int j = 0; j < SO_O; j++) {
                if (mangDiemSo[j][i] == 0)
                    return false;
                else {
                    if (diemSo_Temp == mangDiemSo[j][i])
                        return false;
                    diemSo_Temp = mangDiemSo[j][i];
                }
            }
        }
        return true;
    }


    public void vuotPhai() {
        // kiểm tra theo hàng ngang
        // tác vụ kết thúc ngay
        // nếu không có ô 0 điểm nào cũng như không có 2 ô liền kề theo hàng ngang bằng điểm nhau
        // tức là không thể dồn được bất kỳ ô nào theo chiều ngang
        if (kiemTraNgang()) {
            iEventChangeDataGame.onCancel();
            return;
        }

        // có thể dồn được ít nhất là 2 ô theo chiều ngang
        for (int i = 0; i < SO_O; i++) {
            // refresh các cờ nhớ
            // sau khi sang dòng mới
            flag1.refresh();
            flag2.refresh();

            // vì là vuốt phải nên sẽ bắt đầu duyệt từ bên phải sang
            for (int j = SO_O - 1; j >= 0; j--) {
                // sẽ chỉ duyệt khi ô đang duyệt có số điểm dương
                // hoặc là ô cuối cùng trong hàng được duyệt (ô bên trái cùng ~ j == 0)
                if (mangDiemSo[i][j] != 0) {
                    // sau khi reset flag
                    // chúng sẽ có giá trị điểm == -1
                    if (flag1.getSoDiem() == -1) {
                        flag1.setSoDiem(mangDiemSo[i][j]);
                        flag1.setViTri_i(i);
                        flag1.setViTri_j(j);
                    } else {
                        if (flag2.getSoDiem() == -1) {
                            flag2.setSoDiem(mangDiemSo[i][j]);
                            flag2.setViTri_i(i);
                            flag2.setViTri_j(j);

                            // nếu 2 ô liền kề bằng điểm nhau
                            if (flag1.getSoDiem() == flag2.getSoDiem()) {
                                // biến viTri nhớ vị trí mới sau khi dồn điểm 2 ô liền kề
                                // bằng điểm nhau
                                // biến viTri chính là index cột của ô mới sau khi cộng dồn 2 ô liền kề giống nhau
                                int viTri = flag1.getViTri_j();
                                // tăng điểm số của người chơi theo số điểm cộng dồn
                                // của 2 ô liền kề giống nhau
                                diemSo += flag1.getSoDiem() * 2;

                                // duyệt các cột bên phải của vị trí đặt flag1
                                // cho đến khi gặp ô có điểm số dương hoặc đến biên
                                // biến viTri sẽ ghi nhớ cột mới của 2 ô bằng điểm nhau được cộng dồn
                                for (int m = flag1.getViTri_j() + 1; m < SO_O; m++) {
                                    if (mangDiemSo[i][m] == 0) {
                                        viTri = m;
                                    } else
                                        break;
                                }
                                // lưu state của vị trí mới
                                // index dòng chính là flag1.getViTri_i()
                                // index cột chính là viTri
                                mangDiemSo[flag1.getViTri_i()][viTri] = flag1.getSoDiem() * 2;
                                dataGameList.get(flag1.getViTri_i() * SO_O + viTri).setDiemSo(flag1.getSoDiem() * 2);
                                dataGameList.get(flag1.getViTri_i() * SO_O + viTri).setColor(getColor(flag1.getSoDiem() * 2));

                                // nếu ô mới có vị trí khác với ô đang set flag1
                                // set lại 0 điểm cho ô đang set flag1
                                if (viTri != flag1.getViTri_j()) {
                                    mangDiemSo[flag1.getViTri_i()][flag1.getViTri_j()] = 0;
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setDiemSo(0);
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setColor(getColor(0));
                                }

                                // ô đang set flag2 dĩ nhiên sẽ được set 0 điểm
                                mangDiemSo[flag2.getViTri_i()][flag2.getViTri_j()] = 0;
                                dataGameList.get(flag2.getViTri_i() * SO_O + flag2.getViTri_j()).setDiemSo(0);
                                dataGameList.get(flag2.getViTri_i() * SO_O + flag2.getViTri_j()).setColor(getColor(0));

                                // refresh 2 cờ
                                // sau khi đã dồn 2 ô giống nhau vào 1 ô
                                flag1.refresh();
                                flag2.refresh();
                            }
                            // nếu 2 ô liền kề không bằng điểm nhau
                            else {
                                // biến viTri ghi nhớ index cột của ô mới sau khi dồn
                                int viTri = flag1.getViTri_j();

                                // duyệt cho đến khi gặp ô 0 điểm hoặc ô biên
                                for (int m = flag1.getViTri_j() + 1; m < SO_O; m++) {
                                    if (mangDiemSo[i][m] == 0) {
                                        viTri = m;
                                    } else
                                        break;
                                }

                                // nếu index của ô mới không trùng với ô đang set flag1
                                // dồn ô đang set flag1 sang ô mới
                                if (viTri != flag1.getViTri_j()) {
                                    // set điểm của ô đang set flag1 cho ô mới
                                    mangDiemSo[flag1.getViTri_i()][viTri] = flag1.getSoDiem();
                                    dataGameList.get(flag1.getViTri_i() * SO_O + viTri).setDiemSo(flag1.getSoDiem());
                                    dataGameList.get(flag1.getViTri_i() * SO_O + viTri).setColor(getColor(flag1.getSoDiem()));

                                    // reset 0 điểm cho ô đang set flag1
                                    mangDiemSo[flag1.getViTri_i()][flag1.getViTri_j()] = 0;
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setDiemSo(0);
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setColor(getColor(0));
                                }

                                // set lại state cho 2 cờ
                                flag1.setSoDiem(flag2.getSoDiem());
                                flag1.setViTri_i(flag2.getViTri_i());
                                flag1.setViTri_j(flag2.getViTri_j());
                                flag2.refresh();
                            }
                        }
                    }

                }
                // nếu duyệt đến ô bên trái cùng
                // tìm vị trí của ô mới được dồn
                // cách thức tương tự như trên
                if (j == 0) {
                    if (flag1.getSoDiem() != -1) {
                        int viTri = flag1.getViTri_j();

                        for (int m = flag1.getViTri_j() + 1; m < SO_O; m++) {
                            if (mangDiemSo[i][m] == 0) {
                                viTri = m;
                            } else
                                break;
                        }

                        // nếu vị trí ô mới không trùng ô đang set flag1
                        // set điểm cho ô mới bằng ô flag1
                        // reset điểm cho ô flag1 bằng 0
                        if (viTri != flag1.getViTri_j()) {
                            mangDiemSo[flag1.getViTri_i()][viTri] = flag1.getSoDiem();
                            dataGameList.get(flag1.getViTri_i() * SO_O + viTri).setDiemSo(flag1.getSoDiem());
                            dataGameList.get(flag1.getViTri_i() * SO_O + viTri).setColor(getColor(flag1.getSoDiem()));

                            mangDiemSo[flag1.getViTri_i()][flag1.getViTri_j()] = 0;
                            dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setDiemSo(0);
                            dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setColor(getColor(0));
                        }
                    }
                }


            }
        }

        // tạo số ngẫu nhiên (2 hoặc 4 cho các ô còn trống (nếu có))
        taoSoNgauNhien();
        // set điểm số của người chơi lên view UI
        iEventChangeDataGame.changePoint(diemSo);
        // kiểm tra xem game đã kết thúc chưa
        if (kiemTraNgang() && kiemTraDoc())
            iEventChangeDataGame.onEnd();
    }

    public void vuotTrai() {
        if (kiemTraNgang()) {
            iEventChangeDataGame.onCancel();
            return;
        }
        for (int i = 0; i < SO_O; i++) {

            flag1.refresh();
            flag2.refresh();

            for (int j = 0; j < SO_O; j++) {

                if (mangDiemSo[i][j] != 0) {
                    if (flag1.getSoDiem() == -1) {
                        flag1.setSoDiem(mangDiemSo[i][j]);
                        flag1.setViTri_i(i);
                        flag1.setViTri_j(j);
                    } else {
                        if (flag2.getSoDiem() == -1) {
                            flag2.setSoDiem(mangDiemSo[i][j]);
                            flag2.setViTri_i(i);
                            flag2.setViTri_j(j);

                            if (flag1.getSoDiem() == flag2.getSoDiem()) {
                                int viTri = flag1.getViTri_j();
                                diemSo += flag1.getSoDiem() * 2;

                                for (int m = flag1.getViTri_j() - 1; m >= 0; m--) {
                                    if (mangDiemSo[i][m] == 0) {
                                        viTri = m;
                                    } else
                                        break;
                                }
                                mangDiemSo[flag1.getViTri_i()][viTri] = flag1.getSoDiem() * 2;
                                dataGameList.get(flag1.getViTri_i() * SO_O + viTri).setDiemSo(flag1.getSoDiem() * 2);
                                dataGameList.get(flag1.getViTri_i() * SO_O + viTri).setColor(getColor(flag1.getSoDiem() * 2));

                                if (viTri != flag1.getViTri_j()) {
                                    mangDiemSo[flag1.getViTri_i()][flag1.getViTri_j()] = 0;
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setDiemSo(0);
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setColor(getColor(0));
                                }

                                mangDiemSo[flag2.getViTri_i()][flag2.getViTri_j()] = 0;
                                dataGameList.get(flag2.getViTri_i() * SO_O + flag2.getViTri_j()).setDiemSo(0);
                                dataGameList.get(flag2.getViTri_i() * SO_O + flag2.getViTri_j()).setColor(getColor(0));

                                flag1.refresh();
                                flag2.refresh();
                            } else {
                                int viTri = flag1.getViTri_j();

                                for (int m = flag1.getViTri_j() - 1; m >= 0; m--) {
                                    if (mangDiemSo[i][m] == 0) {
                                        viTri = m;
                                    } else
                                        break;
                                }


                                if (viTri != flag1.getViTri_j()) {
                                    mangDiemSo[flag1.getViTri_i()][viTri] = flag1.getSoDiem();
                                    dataGameList.get(flag1.getViTri_i() * SO_O + viTri).setDiemSo(flag1.getSoDiem());
                                    dataGameList.get(flag1.getViTri_i() * SO_O + viTri).setColor(getColor(flag1.getSoDiem()));

                                    mangDiemSo[flag1.getViTri_i()][flag1.getViTri_j()] = 0;
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setDiemSo(0);
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setColor(getColor(0));
                                }

                                flag1.setSoDiem(flag2.getSoDiem());
                                flag1.setViTri_i(flag2.getViTri_i());
                                flag1.setViTri_j(flag2.getViTri_j());
                                flag2.refresh();
                            }
                        }
                    }

                }
                if (j == SO_O - 1) {
                    if (flag1.getSoDiem() != -1) {
                        int viTri = flag1.getViTri_j();

                        for (int m = flag1.getViTri_j() - 1; m >= 0; m--) {
                            if (mangDiemSo[i][m] == 0) {
                                viTri = m;
                            } else
                                break;
                        }
                        if (viTri != flag1.getViTri_j()) {
                            mangDiemSo[flag1.getViTri_i()][viTri] = flag1.getSoDiem();
                            dataGameList.get(flag1.getViTri_i() * SO_O + viTri).setDiemSo(flag1.getSoDiem());
                            dataGameList.get(flag1.getViTri_i() * SO_O + viTri).setColor(getColor(flag1.getSoDiem()));

                            mangDiemSo[flag1.getViTri_i()][flag1.getViTri_j()] = 0;
                            dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setDiemSo(0);
                            dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setColor(getColor(0));
                        }
                    }
                }


            }
        }

        taoSoNgauNhien();
        iEventChangeDataGame.changePoint(diemSo);
        if (kiemTraNgang() && kiemTraDoc())
            iEventChangeDataGame.onEnd();
    }


    public void vuotLen() {
        if (kiemTraDoc()) {
            iEventChangeDataGame.onCancel();
            return;
        }
        for (int i = 0; i < SO_O; i++) {

            flag1.refresh();
            flag2.refresh();

            for (int j = 0; j < SO_O; j++) {

                if (mangDiemSo[j][i] != 0) {
                    if (flag1.getSoDiem() == -1) {
                        flag1.setSoDiem(mangDiemSo[j][i]);
                        flag1.setViTri_i(j);
                        flag1.setViTri_j(i);
                    } else {
                        if (flag2.getSoDiem() == -1) {
                            flag2.setSoDiem(mangDiemSo[j][i]);
                            flag2.setViTri_i(j);
                            flag2.setViTri_j(i);

                            if (flag1.getSoDiem() == flag2.getSoDiem()) {
                                int viTri = flag1.getViTri_i();
                                diemSo += flag1.getSoDiem() * 2;

                                for (int m = flag1.getViTri_i() - 1; m >= 0; m--) {
                                    if (mangDiemSo[m][i] == 0) {
                                        viTri = m;
                                    } else
                                        break;
                                }
                                mangDiemSo[viTri][flag1.getViTri_j()] = flag1.getSoDiem() * 2;
                                dataGameList.get(viTri * SO_O + flag1.getViTri_j()).setDiemSo(flag1.getSoDiem() * 2);
                                dataGameList.get(viTri * SO_O + flag1.getViTri_j()).setColor(getColor(flag1.getSoDiem() * 2));

                                if (viTri != flag1.getViTri_i()) {
                                    mangDiemSo[flag1.getViTri_i()][flag1.getViTri_j()] = 0;
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setDiemSo(0);
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setColor(getColor(0));
                                }

                                mangDiemSo[flag2.getViTri_i()][flag2.getViTri_j()] = 0;
                                dataGameList.get(flag2.getViTri_i() * SO_O + flag2.getViTri_j()).setDiemSo(0);
                                dataGameList.get(flag2.getViTri_i() * SO_O + flag2.getViTri_j()).setColor(getColor(0));

                                flag1.refresh();
                                flag2.refresh();
                            } else {
                                int viTri = flag1.getViTri_i();

                                for (int m = flag1.getViTri_i() - 1; m >= 0; m--) {
                                    if (mangDiemSo[m][i] == 0) {
                                        viTri = m;
                                    } else
                                        break;
                                }


                                if (viTri != flag1.getViTri_i()) {
                                    mangDiemSo[viTri][flag1.getViTri_j()] = flag1.getSoDiem();
                                    dataGameList.get(viTri * SO_O + flag1.getViTri_j()).setDiemSo(flag1.getSoDiem());
                                    dataGameList.get(viTri * SO_O + flag1.getViTri_j()).setColor(getColor(flag1.getSoDiem()));

                                    mangDiemSo[flag1.getViTri_i()][flag1.getViTri_j()] = 0;
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setDiemSo(0);
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setColor(getColor(0));
                                }

                                flag1.setSoDiem(flag2.getSoDiem());
                                flag1.setViTri_i(flag2.getViTri_i());
                                flag1.setViTri_j(flag2.getViTri_j());
                                flag2.refresh();
                            }
                        }
                    }

                }
                if (j == SO_O - 1) {
                    if (flag1.getSoDiem() != -1) {
                        int viTri = flag1.getViTri_i();

                        for (int m = flag1.getViTri_i() - 1; m >= 0; m--) {
                            if (mangDiemSo[m][i] == 0) {
                                viTri = m;
                            } else
                                break;
                        }


                        if (viTri != flag1.getViTri_i()) {
                            mangDiemSo[viTri][flag1.getViTri_j()] = flag1.getSoDiem();
                            dataGameList.get(viTri * SO_O + flag1.getViTri_j()).setDiemSo(flag1.getSoDiem());
                            dataGameList.get(viTri * SO_O + flag1.getViTri_j()).setColor(getColor(flag1.getSoDiem()));

                            mangDiemSo[flag1.getViTri_i()][flag1.getViTri_j()] = 0;
                            dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setDiemSo(0);
                            dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setColor(getColor(0));
                        }
                    }
                }


            }
        }

        taoSoNgauNhien();
        iEventChangeDataGame.changePoint(diemSo);
        if (kiemTraNgang() && kiemTraDoc())
            iEventChangeDataGame.onEnd();
    }


    public void vuotXuong() {
        if (kiemTraDoc()) {
            iEventChangeDataGame.onCancel();
            return;
        }
        for (int i = 0; i < SO_O; i++) {

            flag1.refresh();
            flag2.refresh();

            for (int j = SO_O - 1; j >= 0; j--) {

                if (mangDiemSo[j][i] != 0) {
                    if (flag1.getSoDiem() == -1) {
                        flag1.setSoDiem(mangDiemSo[j][i]);
                        flag1.setViTri_i(j);
                        flag1.setViTri_j(i);
                    } else {
                        if (flag2.getSoDiem() == -1) {
                            flag2.setSoDiem(mangDiemSo[j][i]);
                            flag2.setViTri_i(j);
                            flag2.setViTri_j(i);

                            if (flag1.getSoDiem() == flag2.getSoDiem()) {
                                int viTri = flag1.getViTri_i();
                                diemSo += flag1.getSoDiem() * 2;

                                for (int m = flag1.getViTri_i() + 1; m < SO_O; m++) {
                                    if (mangDiemSo[m][i] == 0) {
                                        viTri = m;
                                    } else
                                        break;
                                }
                                mangDiemSo[viTri][flag1.getViTri_j()] = flag1.getSoDiem() * 2;
                                dataGameList.get(viTri * SO_O + flag1.getViTri_j()).setDiemSo(flag1.getSoDiem() * 2);
                                dataGameList.get(viTri * SO_O + flag1.getViTri_j()).setColor(getColor(flag1.getSoDiem() * 2));

                                if (viTri != flag1.getViTri_i()) {
                                    mangDiemSo[flag1.getViTri_i()][flag1.getViTri_j()] = 0;
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setDiemSo(0);
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setColor(getColor(0));
                                }

                                mangDiemSo[flag2.getViTri_i()][flag2.getViTri_j()] = 0;
                                dataGameList.get(flag2.getViTri_i() * SO_O + flag2.getViTri_j()).setDiemSo(0);
                                dataGameList.get(flag2.getViTri_i() * SO_O + flag2.getViTri_j()).setColor(getColor(0));

                                flag1.refresh();
                                flag2.refresh();
                            } else {
                                int viTri = flag1.getViTri_i();

                                for (int m = flag1.getViTri_i() + 1; m < SO_O; m++) {
                                    if (mangDiemSo[m][i] == 0) {
                                        viTri = m;
                                    } else
                                        break;
                                }


                                if (viTri != flag1.getViTri_i()) {
                                    mangDiemSo[viTri][flag1.getViTri_j()] = flag1.getSoDiem();
                                    dataGameList.get(viTri * SO_O + flag1.getViTri_j()).setDiemSo(flag1.getSoDiem());
                                    dataGameList.get(viTri * SO_O + flag1.getViTri_j()).setColor(getColor(flag1.getSoDiem()));

                                    mangDiemSo[flag1.getViTri_i()][flag1.getViTri_j()] = 0;
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setDiemSo(0);
                                    dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setColor(getColor(0));
                                }

                                flag1.setSoDiem(flag2.getSoDiem());
                                flag1.setViTri_i(flag2.getViTri_i());
                                flag1.setViTri_j(flag2.getViTri_j());
                                flag2.refresh();
                            }
                        }
                    }

                }
                if (j == 0) {
                    if (flag1.getSoDiem() != -1) {
                        int viTri = flag1.getViTri_i();

                        for (int m = flag1.getViTri_i() + 1; m < SO_O; m++) {
                            if (mangDiemSo[m][i] == 0) {
                                viTri = m;
                            } else
                                break;
                        }


                        if (viTri != flag1.getViTri_i()) {
                            mangDiemSo[viTri][flag1.getViTri_j()] = flag1.getSoDiem();
                            dataGameList.get(viTri * SO_O + flag1.getViTri_j()).setDiemSo(flag1.getSoDiem());
                            dataGameList.get(viTri * SO_O + flag1.getViTri_j()).setColor(getColor(flag1.getSoDiem()));

                            mangDiemSo[flag1.getViTri_i()][flag1.getViTri_j()] = 0;
                            dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setDiemSo(0);
                            dataGameList.get(flag1.getViTri_i() * SO_O + flag1.getViTri_j()).setColor(getColor(0));
                        }
                    }
                }


            }
        }

        taoSoNgauNhien();
        iEventChangeDataGame.changePoint(diemSo);
        if (kiemTraNgang() && kiemTraDoc())
            iEventChangeDataGame.onEnd();
    }


    public class Flag {
        private int soDiem;
        private int viTri_i;
        private int viTri_j;

        public Flag(int soDiem, int viTri_i, int viTri_j) {
            this.soDiem = soDiem;
            this.viTri_i = viTri_i;
            this.viTri_j = viTri_j;
        }

        public Flag() {
        }

        public int getSoDiem() {
            return soDiem;
        }

        public void setSoDiem(int soDiem) {
            this.soDiem = soDiem;
        }

        public int getViTri_i() {
            return viTri_i;
        }

        public void setViTri_i(int viTri_i) {
            this.viTri_i = viTri_i;
        }

        public int getViTri_j() {
            return viTri_j;
        }

        public void setViTri_j(int viTri_j) {
            this.viTri_j = viTri_j;
        }

        public void refresh() {
            soDiem = -1;
            viTri_i = -1;
            viTri_j = -1;
        }
    }

}
