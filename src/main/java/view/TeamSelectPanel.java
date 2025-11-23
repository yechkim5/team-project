package view;

import javax.swing.*;
import java.awt.*;

public class TeamSelectPanel extends JPanel {
    public TeamSelectPanel(String[] speciesList) {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("Select Pokemon (Demo)", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));

        JList<String> list = new JList<>(speciesList);
        list.setFont(list.getFont().deriveFont(16f));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

    }
}
