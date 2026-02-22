package model;

public class GameEvents {
	private int game_events_id;
    private String events_name;
    private String description;
    private String event_type;    // MONSTER / BUFF
    private int effect_value;
    private int min_realm;
    private String event_image;

    public GameEvents() { super(); }

	public int getGame_events_id() {
		return game_events_id;
	}

	public void setGame_events_id(int game_events_id) {
		this.game_events_id = game_events_id;
	}

	public String getEvents_name() {
		return events_name;
	}

	public void setEvents_name(String events_name) {
		this.events_name = events_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEvent_type() {
		return event_type;
	}

	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}

	public int getEffect_value() {
		return effect_value;
	}

	public void setEffect_value(int effect_value) {
		this.effect_value = effect_value;
	}

	public int getMin_realm() {
		return min_realm;
	}

	public void setMin_realm(int min_realm) {
		this.min_realm = min_realm;
	}

	public String getEvent_image() {
		return event_image;
	}

	public void setEvent_image(String event_image) {
		this.event_image = event_image;
	}

	public int getFinalMonsterAtk() {
	    // 基礎值 (effect_value) * 境界加成 (1 + (境界-1) * 0.3)
	    double realmMultiplier = 1.0 + (this.min_realm - 1) * 0.3;
	    return (int) (this.effect_value * realmMultiplier);
	}
    
}
