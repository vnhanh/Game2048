package com.example.user.game2048;

import java.util.List;

/**
 * Created by USER on 01/02/2018.
 * Interface được GameController sử dụng
 * để thông báo cập nhật UI và state mỗi khi nó hoàn thành việc tính toán số điểm của người chơi
 * cũng như trạng thái các ô game
 */

public interface IEventChangeDataGame {
    void onInsertData(List<Integer> position);

    void onChangeData(List<Integer> position);

    void onRefreshData();

    void onEnd();

    void changePoint(int point);

    void onCancel();
}
