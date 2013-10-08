package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dialog.ModalityType;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import main.WordOnWord;


public class NewGameDialog extends JDialog {

	private ButtonGroup blockOptionGroup;
	private ButtonGroup languageGroup;
	private JRadioButton weekBlocksRadioButton;
	private JRadioButton randomBlocksRadioButton;
	private JRadioButton totRandomBlocksRadioButton;
	private ImageRadioButton ukRadioButton;
	private ImageRadioButton sweRadioButton;
	private ImageRadioButton gerRadioButton;

	/**
	 * Create the dialog.
	 */
	public NewGameDialog() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("New game");
		setBounds(100, 100, 460, 275);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						hideDialog();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		{
			languageGroup = new ButtonGroup();
			JPanel languagePanel = new JPanel();
			languagePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Language", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			getContentPane().add(languagePanel, BorderLayout.NORTH);
			languagePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			{
				ukRadioButton = new ImageRadioButton();
				ukRadioButton.setImage(new ImageIcon(NewGameDialog.class.getResource("/images/ukFlag.png")));
				languagePanel.add(ukRadioButton);
				ukRadioButton.addToButtonGroup(languageGroup);
				
				sweRadioButton = new ImageRadioButton();
				sweRadioButton.setImage(new ImageIcon(NewGameDialog.class.getResource("/images/swedishFlag.png")));
				languagePanel.add(sweRadioButton);
				sweRadioButton.addToButtonGroup(languageGroup);
				
				gerRadioButton = new ImageRadioButton();
				gerRadioButton.setImage(new ImageIcon(NewGameDialog.class.getResource("/images/germanFlag.png")));
				languagePanel.add(gerRadioButton);
				gerRadioButton.addToButtonGroup(languageGroup);
				
				ukRadioButton.setSelected(true);
			}
		}
		{
			blockOptionGroup = new ButtonGroup();
			JPanel blockPanel = new JPanel();
			blockPanel.setBorder(new TitledBorder(null, "Block options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			getContentPane().add(blockPanel, BorderLayout.CENTER);
			blockPanel.setLayout(new BoxLayout(blockPanel, BoxLayout.X_AXIS));
			{
				weekBlocksRadioButton = new JRadioButton("The week's blocks");
				weekBlocksRadioButton.setAlignmentY(Component.TOP_ALIGNMENT);
				blockPanel.add(weekBlocksRadioButton);
				weekBlocksRadioButton.setSelected(true);
				blockOptionGroup.add(weekBlocksRadioButton);
			}
			{
				randomBlocksRadioButton = new JRadioButton("Random blocks");
				randomBlocksRadioButton.setAlignmentY(Component.TOP_ALIGNMENT);
				blockPanel.add(randomBlocksRadioButton);
				blockOptionGroup.add(randomBlocksRadioButton);
			}
			{
				totRandomBlocksRadioButton = new JRadioButton("Totally random blocks");
				totRandomBlocksRadioButton.setAlignmentY(Component.TOP_ALIGNMENT);
				blockPanel.add(totRandomBlocksRadioButton);
				blockOptionGroup.add(totRandomBlocksRadioButton);
			}
		}
	}
	
	private void hideDialog()
	{
		this.setVisible(false);
	}
	
	public int getVocabularyMode()
	{
		if(weekBlocksRadioButton.isSelected())
		{
			return WordOnWord.WEEK_VOCABULARY;
		}
		else if(randomBlocksRadioButton.isSelected())
		{
			return WordOnWord.NEW_VOCABULARY;
		}
		else
		{
			return WordOnWord.PURE_RANDOM;
		}
	}
	
	public int getLanguage()
	{
		if(ukRadioButton.getSelected())
		{
			return WordOnWord.ENGLISH;
		}
		else if(sweRadioButton.getSelected())
		{
			return WordOnWord.SWEDISH;
		}
		else
		{
			return WordOnWord.GERMAN;
		}
	}
}
