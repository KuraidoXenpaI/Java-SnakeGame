import javax.swing.JFrame;

class MainFrame extends JFrame {

    MainFrame() {

        // Add the GamePanel to the MainFrame
        this.add(new GamePanel());

        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(50, 50);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }
}