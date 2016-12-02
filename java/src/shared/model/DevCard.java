package shared.model;

import java.io.Serializable;

import shared.definitions.DevCardType;

public class DevCard implements Serializable {
	private DevCardType type;
	private Player holder;
}
