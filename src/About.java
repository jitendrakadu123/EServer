
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;



    class About {
        public About()
        {
            //ImageIcon icon=new ImageIcon("java.jpg");
 JOptionPane.showMessageDialog(null,
         "<html>Remote PC Administration Using Mobile"+"<ul><li><p>Ver# 1.0</li>"
		        + "<li><p>Coded by: P.R PATIL COLLAGE OF ENGINEERING</li><ul><li><p>JITENDRA KADU</li><li><p>SWAPNIL BHONGADE</li><li><p>VIVEK BARASKAR</li><li>Priyam Tayade</li></ul><li>"
		        + "<p>Copyright<font size=\"2\">&copy;</font> 2012 - 2013</li></ul></html>","Message",JOptionPane.INFORMATION_MESSAGE,new ImageIcon(About.class.getResource("1.JPG")));
        }
    }