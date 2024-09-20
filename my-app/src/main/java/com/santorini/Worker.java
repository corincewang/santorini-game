package com.santorini;

public class Worker {
    private Cell position;
    private boolean canMove;

    public Worker() {
        this.position = null;
        this.canMove = true;
    }

    public Cell getPosition() {
        return position;
    }

    public boolean canMove(Cell newCell) {
        // 这里可以添加更多规则，比如高度差的限制
        return canMove && !newCell.isOccupied() && newCell.getTower().getHeight() <= position.getTower().getHeight() + 1;
    }

    public void move(Cell newCell) {
        if (position != null) {
            position.setOccupiedWorker(null);  // 释放旧的 Cell
        }
        newCell.setOccupiedWorker(this);  // 占据新的 Cell
        this.position = newCell;  // 更新位置
    }
}
