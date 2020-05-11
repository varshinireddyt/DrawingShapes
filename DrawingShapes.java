import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

//State Pattern
class ButtonStateContext {
	private ButtonState currentState;
	private String currentStateName;
	private DrawingPanel drawingPanel;
	private ActionControl actionctrl;

	public ButtonStateContext() {
		currentState = new Line();
	}

	public void setState(ButtonState state) {
		currentState = state;
	}

	public void stateAlert() {
		currentState.alert(this);
	}

	public String getStateName() {
		return currentStateName;
	}

	public void setStateName(String name) {
		currentStateName = name;
	}

	public void setPanel(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}

	public DrawingPanel getPanel() {
		return this.drawingPanel;
	}

	public void setaction(ActionControl action) {
		this.actionctrl = action;
	}

	public ActionControl getaction() {
		return this.actionctrl;
	}

}

//Controller pattern//
class LineView {
	public void display(int startX, int startY) {
		Line2D shape = new Line2D.Double(startX, startY, startX, startY + 70);
		DrawingShapes.storage.addItem(shape);
		DrawingShapes.counter += 1;

	}
}

class BoxView {
	public void display(int startX, int startY) {
		Rectangle shape = new Rectangle();
		shape.setBounds(startX, startY, 50, 50);
		DrawingShapes.storage.addItem(shape);
		DrawingShapes.counter += 1;
	}

}

class CircleView {
	public void display(int startX, int startY) {
		Ellipse2D shape = new Ellipse2D.Double(startX, startY, 50, 50);
		DrawingShapes.storage.addItem(shape);
		DrawingShapes.counter += 1;

	}
}
//Adapter

class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private int startX, startY;
	private LineView lineView;
	private BoxView boxView;
	private CircleView circleView;
	private ButtonStateContext ctx;

	public DrawingPanel(ButtonStateContext ctx) {
		addMouseListener(this); // add mouse listener
		addMouseMotionListener(this); // add mouse motion listener
		this.ctx = ctx;
		lineView = new LineView();
		boxView = new BoxView();
		circleView = new CircleView();
	}

	@Override
	public void paintComponent(final Graphics g) {

		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		System.out.println(DrawingShapes.counter);
		// iterator pattern
		Iterator<Shape> storage_iterator = new StorageClassIterator(DrawingShapes.storage.storage_iteratorist,
				DrawingShapes.counter);
		System.out.println("storage list " + DrawingShapes.storage.storage_iteratorist);
		while (storage_iterator.hasNext()) {

			g2d.draw(storage_iterator.next());
		}
	}

	public void removeshape() {

		int size = DrawingShapes.storage.storage_iteratorist.size();

		for (int i = size; i > DrawingShapes.counter; i--) {

			DrawingShapes.storage.removeItem();
		}
	}

	// Controller pattern Dispatcher
	public void drawshape() {
		if (ctx.getStateName() == "Line") {
			lineView.display(startX, startY);

		} else if (ctx.getStateName() == "Circle") {
			circleView.display(startX, startY);
		} else if (ctx.getStateName() == "Box") {
			boxView.display(startX, startY);

		}
		repaint();
	}

	public void mousePressed(MouseEvent evt) {
		startX = evt.getX();
		startY = evt.getY();
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		System.out.println(evt);
		if (ctx.getStateName() == "Line" || ctx.getStateName() == "Circle" || ctx.getStateName() == "Box") {
			removeshape();
		}
		drawshape();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}

class StorageClass {

	int position = 0;

	List<Shape> storage_iteratorist;

	public Iterator<Shape> createIterator() {
		return new StorageClassIterator(storage_iteratorist, 0);
	}

	public StorageClass() {
		storage_iteratorist = new ArrayList<>();
	}

	public void addItem(Shape e) {
		System.out.println("Adding shape");
		storage_iteratorist.add(e);
	}

	public void removeItem() {
		storage_iteratorist.remove(storage_iteratorist.size() - 1);
	}

}

// Iterator Pattern
class StorageClassIterator implements Iterator<Shape> {

	int position = 0;
	int counter = 0;

	List<Shape> storage_iteratorist;

	public StorageClassIterator(List<Shape> storage_iteratorist, int cnt) {
		this.storage_iteratorist = storage_iteratorist;
		this.counter = cnt;
	}

	@Override
	public boolean hasNext() {
		if (position >= storage_iteratorist.size() || position >= counter)
			return false;
		else
			return true;
	}

	@Override
	public Shape next() {
		Shape item = storage_iteratorist.get(position);
		position = position + 1;
		return item;
	}

	public void remove() {

	}
}

public class DrawingShapes extends JFrame {
	private static final long serialVersionUID = 1L;
	public static StorageClass storage;
	public static int counter = 0;
	public static Iterator<Shape> storage_iterator;
	private JButton undoButton, redoButton;
	private DrawingPanel drawingPanel;
	private ActionControl actionctrl;

	public static void main(final String[] args) {

		new DrawingShapes();
	}

	// Public Class

	public DrawingShapes() {

		super("Shape Drawings");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.white);

		storage = new StorageClass();

		ButtonStateContext stateContext = new ButtonStateContext();
		stateContext.stateAlert();

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.gray);
		buttonPanel.setPreferredSize(new Dimension(100, 300));

		drawingPanel = new DrawingPanel(stateContext);
		actionctrl = new ActionControl(stateContext);

		stateContext.setPanel(drawingPanel);
		stateContext.setaction(actionctrl);

		ShapeButton shapeButton[] = new ShapeButton[3];

		shapeButton[0] = new ShapeButton("Line", stateContext);
		shapeButton[1] = new ShapeButton("Box", stateContext);
		shapeButton[2] = new ShapeButton("Circle", stateContext);

		undoButton = new UndoButton("Undo", stateContext);
		redoButton = new RedoButton("Redo", stateContext);

		// Iterator Pattern
		for (ShapeButton ShapeButton : shapeButton)
			buttonPanel.add(ShapeButton, BorderLayout.WEST);

		buttonPanel.add(undoButton, BorderLayout.WEST);
		buttonPanel.add(redoButton, BorderLayout.WEST);

		drawingPanel.setBackground(Color.white);

		add(buttonPanel, BorderLayout.WEST);
		add(drawingPanel, BorderLayout.CENTER);

		setSize(500, 400);
		setVisible(true);

	}

}

class UndoButton extends JButton implements ActionListener {
	private static final long serialVersionUID = 1L;
	private ButtonStateContext tempstateContext;

	public UndoButton(String name, ButtonStateContext df) {
		setPreferredSize(new Dimension(80, 20));
		setText(name);
		addActionListener(this);
		this.tempstateContext = df;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		System.out.println(e.getSource());
		System.out.println("UndoButton");

		tempstateContext.getaction().setCommand(new UndoOnCommand(this));
		tempstateContext.getaction().buttonWasPressed();

	}

}

class RedoButton extends JButton implements ActionListener {

	private static final long serialVersionUID = 1L;
	private ButtonStateContext tempstateContext;

	public RedoButton(String name, ButtonStateContext df) {
		setPreferredSize(new Dimension(80, 20));
		setText(name);
		addActionListener(this);
		this.tempstateContext = df;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		System.out.println(e.getSource());
		System.out.println("RedoButton");

		tempstateContext.getaction().setCommand(new RedoOnCommand(this));
		tempstateContext.getaction().buttonWasPressed();

	}

}
//Command Pattern

interface Command {
	public void execute(ButtonStateContext ctx);
}

class UndoOnCommand implements Command {

	UndoButton undo;

	public UndoOnCommand(UndoButton undo) {
		this.undo = undo;
	}

	public void execute(ButtonStateContext ctx) {
		if (!DrawingShapes.storage.storage_iteratorist.isEmpty() & DrawingShapes.counter > 0) {
			System.out.println("UndoButton " + DrawingShapes.counter);
			DrawingShapes.counter -= 1;
			ctx.getPanel().repaint();

		}
	}
}

class RedoOnCommand implements Command {

	RedoButton redo;

	public RedoOnCommand(RedoButton redo) {
		this.redo = redo;
	}

	public void execute(ButtonStateContext ctx) {

		if (!DrawingShapes.storage.storage_iteratorist.isEmpty()
				& DrawingShapes.counter < DrawingShapes.storage.storage_iteratorist.size()) {
			DrawingShapes.counter += 1;
			ctx.getPanel().repaint();
		}

	}
}

class ActionControl {
	Command action_button;
	ButtonStateContext ctx;

	public ActionControl(ButtonStateContext ctx) {
		this.ctx = ctx;
	}

	public void setCommand(Command command) {

		action_button = command;
	}

	public void buttonWasPressed() {
		action_button.execute(ctx);
	}
}
