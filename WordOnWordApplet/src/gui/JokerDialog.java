package gui;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class JokerDialog extends JDialog implements ActionListener,
		PropertyChangeListener {

	private JTextField letterTextField;
	private JOptionPane optionPane;
	private String okBtnString = "OK";
	private String cancelBtnString = "Cancel";
	private Character joker;
	
	public JokerDialog(Window window)
	{	
		super(window, Dialog.ModalityType.APPLICATION_MODAL);
		
		joker = null;
		
		//Set the dialog title
		setTitle("Select joker");
		//Make the text field
		letterTextField = new JTextField(10);
		
		String text = "Type a letter:";
		
		Object[] dialogItems = {text, letterTextField};
		Object[] options = {okBtnString, cancelBtnString};
		
		optionPane = new JOptionPane(dialogItems,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                options,
                options[0]);
		
		//Make the dialog display the content
        setContentPane(optionPane);
        
        //Dispose the dialog on close
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        
        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent ce) {
                letterTextField.requestFocusInWindow();
            }
        });
        
        //Register an event handler that puts the text into the option pane.
        letterTextField.addActionListener(this);

        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
        
        pack();
	}
	
	//This method reacts to state changes in the option pane.
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		
		//Don't do anything if the dialog is not visible
		if (!isVisible())
			return;

		if(e.getSource() == optionPane &&
		   (JOptionPane.VALUE_PROPERTY.equals(prop) || 
		    JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))
		  )
		{
			Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }
            
            //Reset the JOptionPane's value.
	        optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
	        
	        if (value.equals(okBtnString))//If the user pressed OK 
	        {
	        	String jokerString = letterTextField.getText();
                jokerString = jokerString.toUpperCase();
                
                char[] jokerChars = jokerString.toCharArray();
                
                if(jokerString.length() == 0)//No joker has been inputed
                {
                	//Select text
                    letterTextField.selectAll();
                    //Show error message
                    JOptionPane.showMessageDialog(this, "No joker has been entered", "Invalid joker", JOptionPane.ERROR_MESSAGE);
                    
                    joker = null;
                    //Let the text field request focus
                    letterTextField.requestFocusInWindow();
                }
                else if(jokerString.length() > 1)//We may only input one character
                {
                	//Select text
                    letterTextField.selectAll();
                    //Show error message
                    JOptionPane.showMessageDialog(this, "You may only enter one letter", "Invalid joker", JOptionPane.ERROR_MESSAGE);
                    
                    joker = null;
                    //Let the text field request focus
                    letterTextField.requestFocusInWindow();
                }
                else if(!Character.isLetter(jokerChars[0]))//We may only input letters
                {
                	//Select text
                    letterTextField.selectAll();
                    //Show error message
                    JOptionPane.showMessageDialog(this, "You may only enter letters", "Invalid joker", JOptionPane.ERROR_MESSAGE);
                    
                    joker = null;
                    //Let the text field request focus
                    letterTextField.requestFocusInWindow();
                }
                else//All input checks are done, we have a valid joker!
                {
                	joker = jokerChars[0];
                	exit();
                }
	        }
	        else//The user pressed cancel
	        {
	        	joker = null;
	        	exit();		
	        }
		}
	}

	//This method handles events for the text field.
	public void actionPerformed(ActionEvent e) {
		optionPane.setValue(okBtnString);
	}
	
	/**
     * This method hides the dialog
     */
    public void exit() {
    	letterTextField.setText("");
        this.setVisible(false);
    }
    
    public Character getJoker()
    {
    	return joker;
    }

}
