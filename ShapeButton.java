import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ShapeButton extends JButton implements ActionListener {
//Composite
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public String selected_shape;
	private ButtonStateContext tempstateContext;

	public ShapeButton(String name, ButtonStateContext df) {
		this.name = name;
		setPreferredSize(new Dimension(80, 20));
		setText(name);
		addActionListener(this);
		this.tempstateContext = df;
	}

	public void actionPerformed(ActionEvent event) {
		System.out.println("name is "+name+tempstateContext.getStateName());
		if (name == "Line") {
			tempstateContext.setState(new Line());
		} else if (name == "Box") {
			tempstateContext.setState(new Box());
		} else
			tempstateContext.setState(new Circle());

		tempstateContext.stateAlert();

		System.out.println("After name is "+name+tempstateContext.getStateName());
	}
}

// Observer
interface ButtonState {

	public void alert(ButtonStateContext ctx);
}

class Line implements ButtonState {
	public void alert(ButtonStateContext ctx) {
		ctx.setStateName("Line");
		System.out.println("Line Implementation...");
	}

}

class Circle implements ButtonState {
	public void alert(ButtonStateContext ctx) {
		ctx.setStateName("Circle");
		System.out.println("Circle Implementation...");
	}

}

class Box implements ButtonState {
	public void alert(ButtonStateContext ctx) {
		ctx.setStateName("Box");
		System.out.println("Box Implementation...");
	}

}
