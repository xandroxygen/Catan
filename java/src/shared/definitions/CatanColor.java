package shared.definitions;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public enum CatanColor
{
	RED, ORANGE, YELLOW, BLUE, GREEN, PURPLE, PUCE, WHITE, BROWN;
	
	private Color color;
	
	static
	{
		RED.color = new Color(227, 66, 52);
		ORANGE.color = new Color(255, 165, 0);
		YELLOW.color = new Color(253, 224, 105);
		BLUE.color = new Color(111, 183, 246);
		GREEN.color = new Color(109, 192, 102);
		PURPLE.color = new Color(157, 140, 212);
		PUCE.color = new Color(204, 136, 153);
		WHITE.color = new Color(223, 223, 223);
		BROWN.color = new Color(161, 143, 112);
	}
	
	public Color getJavaColor()
	{
		return color;
	}
	
	public static List<CatanColor> getColors() {
		ArrayList<CatanColor> colors = new ArrayList<>();
		colors.add(RED);
		colors.add(ORANGE);
		colors.add(YELLOW);
		colors.add(BLUE);
		colors.add(GREEN);
		colors.add(PURPLE);
		colors.add(PUCE);
		colors.add(WHITE);
		colors.add(BROWN);
		
		return colors;
	}
}

