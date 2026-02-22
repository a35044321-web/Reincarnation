package model;

public class Player_items {
    private int items_id;
    private int player_id;
    private int item_id;
    private int quantity;

    public Player_items() { super(); }

    public int getItems_id() { return items_id; }
    public void setItems_id(int items_id) { this.items_id = items_id; }

    public int getPlayer_id() { return player_id; }
    public void setPlayer_id(int player_id) { this.player_id = player_id; }

    public int getItem_id() { return item_id; }
    public void setItem_id(int item_id) { this.item_id = item_id; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
