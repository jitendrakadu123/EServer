import java.awt.*;
import javax.swing.*;

class Help extends JFrame 
{
 
Help()
{
 super("Help");
 setLayout(null);
Label l1,l2,l3,l4,l5,l6,l7;
l1=new Label("How to run this Application?");
l2=new Label("1]  Run the server Application.(PC Application)");
l3=new Label("2]  Run Client Application.(Mobile Application)");
l4=new Label("3]  Select the Server device from searched list.");
l5=new Label("4]  Select any 1 mode of Execution.");
l6=new Label("5]  Later follow instructions on screen.");
l1.setBounds(30,30,200,30);
l2.setBounds(30,60,300,30);
l3.setBounds(30,80,300,30);
l4.setBounds(30,100,300,30);
l5.setBounds(30,120,300,30);
l6.setBounds(30,140,300,30);
add(l6);
add(l5);
add(l4);
add(l3);
add(l2);
add(l1);
setSize(350,250);
setLocation(970,450);
setResizable(true);
setVisible(true);
}
}