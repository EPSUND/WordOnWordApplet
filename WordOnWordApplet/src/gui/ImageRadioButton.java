package gui;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeListener;


public class ImageRadioButton extends JPanel {

	private JRadioButton radioButton;
	private JLabel image;
	
	public ImageRadioButton()
	{
		radioButton = new JRadioButton();
		image = new JLabel();
		
		add(radioButton);
		add(image);
	}
	
	public ImageRadioButton(Icon icon)
	{
		radioButton = new JRadioButton();
		image = new JLabel(icon);
		
		add(radioButton);
		add(image);
	}
	
	public void addToButtonGroup(ButtonGroup group)
	{
		group.add(radioButton);
	}
	
	public void addActionListener(ActionListener listener)
	{
		radioButton.addActionListener(listener);
	}
	
	public void addChangerListener(ChangeListener listener)
	{
		radioButton.addChangeListener(listener);
	}
	
	public Icon getImage()
	{
		return image.getIcon();
	}
	
	public void setImage(Icon icon)
	{
		image.setIcon(icon);
	}
	
	public void setSelected(boolean isSelected)
	{
		radioButton.setSelected(isSelected);
	}
	
	public boolean getSelected()
	{
		return radioButton.isSelected();
	}
}
