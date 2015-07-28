import java.awt.AWTException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.event.InputEvent;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.swing.JFrame;
import javax.swing.*;




public class EServer extends JFrame implements ActionListener,WindowListener{

//UUID Universal Unique Identification represent 128 bit value, Server Socket
public final UUID uuid = new UUID( "00000102030405060740A1B1C1D1E100",false); //it can be generated randomly



//the name of the service
public final String name = "Bluetooth Server";
     
public final String url = "btspp://localhost:" + uuid + ";name=" + name+ ";authenticat+e=true;encrypt=true;";
LocalDevice local = null;
StreamConnectionNotifier server = null;
StreamConnection conn = null;
static JLabel l,l1,l2,l3,l4,l5,l6;
static String sname="";
DataOutputStream dout;
DataInputStream din;
Dimension dim;
Rectangle r;
Robot robot;
byte[] bytearray;
BufferedImage bi;
int width,height,swidth,sheight,x=0,y=0,mousex=0,mousey=0,remainx,remainy;
boolean s;
static int flag=0;

public EServer() {
      JMenu File= new JMenu("File");
      JMenuItem Help= new JMenuItem("Help");
      JMenuItem Exit= new JMenuItem("Exit") ;
      JMenuItem About= new JMenuItem("About") ;
      About.addActionListener(this);
      Help.addActionListener(this);
      Exit.addActionListener(this);
      File.add(Help);
      File.add(About);
      File.add(Exit);
      JMenuBar menubar = new JMenuBar();
      BufferedImage image = null;
      try {



	  
            image = ImageIO.read(getClass().getResource("dash.png")); //Read the image from resource
          } catch (IOException e) {
                               e.printStackTrace();
                              }
      setIconImage(image);
      menubar.add(File);
      setJMenuBar(menubar);
      setSize(350,250);
      setLocation(970,450);
      setResizable(false);
      setTitle("Bluetooth Server");
      setVisible(true);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}


public void BluetoothCode(JFrame f)
{
     try
     {
         s = LocalDevice.isPowerOn();  //Retrive the power status of the Local Bluetooth Device. Return(ON or OFF/0 or 1)
         if(!s)
         {
              l6=new JLabel("Please....  Turn On Bluetooth & Start Again.");
              l6.setBounds(40,10,250,30);
              f.repaint();
              f.add(l6);
              try {
                      Thread.sleep(6000);
                  } catch (InterruptedException ex) {
                                                 }
                  System.exit(0);
         }
         else
         {
		 //Retrive the LocalDevice Object for the local Bluetooth Device
            local = LocalDevice.getLocalDevice();

			//Retrive the Name of the Local Device. The bluetooth Specification call this name the ""
			//"Bluetooth device Name" or the "User Friendely Name"				
            sname=local.getFriendlyName();  
            l=new JLabel("Server started :");
            l1=new JLabel(sname);
            l.setBounds(20,10,250,30);
            l1.setBounds(115,10,60,30);
			
			/*............DiscoveryAgent.GIAC specifies "General Inquiry Access Code".
						No limit is set on how long the device remains in the discoverable mode.*/  
            local.setDiscoverable(DiscoveryAgent.GIAC) ;
			
            server = (StreamConnectionNotifier)Connector.open(url);
/*To create a new service record that represents the service, invoke "Connector.open" with a server connection URL argument,
and cast the result to a "StreamConnectionNotifier" that represents the service: 

         StreamConnectionNotifier service = (StreamConnectionNotifier) Connector.open("someURL"); 
*/			
			
            l2=new JLabel("Waiting for client connection.....");
            l2.setBounds(50,40,200,30);
            f.repaint();
            f.add(l);
            f.add(l1);
            f.add(l2);
            f.repaint();
            f.add(l);
            f.add(l1);
            f.add(l2);
			
			/*Obtain the service record created by the server device: 
			ServiceRecord sr = local.getRecord(service); 
			*/
			
			/* Indicate that the service is ready to accept a client connection:
			StreamConnection connection = (StreamConnection) service.acceptAndOpen(); */
            conn = server.acceptAndOpen();

			/* Note that acceptAndOpen blocks until a client connects. 
		      When the server is ready to exit, close the connection and remove the service record:
			  service.close(); */

          l3=new JLabel("Client Connected....");
          l3.setBounds(50,65,160,30);
          f.repaint();
          f.add(l3);

           din = new DataInputStream(conn.openInputStream());
           dout = new DataOutputStream(conn.openOutputStream());
           
		   /* Toolkit is 
		   Toolkit.getScreenSize(); gives the screen size of the native operating system
		   swidth = dim.width; give "width" of native OS
		   sheight = dim.height; give "height" of native OS
		   */
		   Toolkit toolkit =  Toolkit.getDefaultToolkit ();
           dim = toolkit.getScreenSize();
           swidth=dim.width;
           sheight=dim.height;


          try {
                  robot=new Robot();
              } catch (AWTException ex) {
                                     }

          while(true){

              String cmd = "";
              char c;
              while ((c = din.readChar()) > 0 && (c!='\n') )
                            cmd = cmd + c;
              String url=cmd.replace('\n',' ');
              String osName="";

              if(url.equals("mouse") || url.equals("left") || url.equals("right") || url.equals("up") || url.equals("down") || url.equals(
"fire") || url.equals("rclick"))
              {
			  
		/*
		x=MouseInfo.getPointerInfo().getLocation().x;
		It Return the point object correspoinding to current mouse position.
		*/
       y = MouseInfo.getPointerInfo().getLocation().y;
       x = MouseInfo.getPointerInfo().getLocation().x;

       if(url.equals("mouse"))
       {
		//It Return Next four Input bytes and Return one Integer.
           width=din.readInt();
          height=din.readInt();
       }

      if(url.equals("left") && (x>0 && (x-15)>=0))

		/* SET THE MOUSE X Y POSITION
		robot.mouseMove(300,550);
		
		*/
         robot.mouseMove(x-15,y);

      else if(url.equals("right") && (x<dim.getWidth() && (x+15)<=dim.getWidth())) //dim == size of screen

         robot.mouseMove(x+15,y);

      else if(url.equals("up") && (y>0 && (y-15)>=0))

         robot.mouseMove(x,y-15);


      else if(url.equals("down") && (y<dim.getHeight() && (y+15)<=dim.getHeight()))

         robot.mouseMove(x,y+15);


       else if(url.equals("fire"))
       {
          robot.mousePress(InputEvent.BUTTON1_MASK);
          robot.mouseRelease(InputEvent.BUTTON1_MASK);

        }

        else if(url.equals("rclick"))
       {
          robot.mousePress(InputEvent.BUTTON3_MASK);
          robot.mouseRelease(InputEvent.BUTTON3_MASK);

        }
       mousex=remainx=MouseInfo.getPointerInfo().getLocation().x;
       mousey=remainy=MouseInfo.getPointerInfo().getLocation().y;
     // Left side
                   if(x<width-20 && y<height-60)
                   {
                   r=new Rectangle(0,-25,width,height);
                   mousey=mousey+25;
                   }
                   else if(x<width-20 && (y>=height-60 && y<=sheight-height+20))
                   {
                   r=new Rectangle(0,y-(height/2),width,height);
                   if(url.equals("up"))
                   mousey=(height/2)-15;
                   else if(url.equals("down"))
                   mousey=(height/2)+15;
                   else
                       mousey=height/2;
                   }
                   else if(x<width-20 && y>sheight-height+15)
                   {
                   r=new Rectangle(0,sheight-height-15,width,height+15);
                   mousey=remainy-(sheight-height-15);
                   }

             //Right Side
                   else if(x>swidth-width+20 && y<height-40)
                   {
                   r=new Rectangle(swidth-width,-35,width,height+25);
                   mousex=remainx-(swidth-width);
                   mousey=mousey+35;
                   }
                   else if(x>swidth-width+20 && (y>=height-60 && y<=sheight-height+20))
                   {
                   r=new Rectangle(swidth-width,y-(height/2),width,height+25);
                   mousex=remainx-(swidth-width);
                   if(url.equals("up"))
                   mousey=(height/2)-15;
                   else if(url.equals("down"))
                   mousey=(height/2)+15;
                   else
                       mousey=height/2;
                   }
                   else if(x>swidth-width+20 && y>sheight-height-25)
                   {
                   r=new Rectangle(swidth-width,sheight-height-15,width,height+15);
                   mousex=remainx-(swidth-width);
                    mousey=remainy-(sheight-height-15);
                   }

            // upper side
                   else if((x>=width-20 && x<= swidth-width+20) && y<height-40)
                   {
                   r=new Rectangle(x-(width/2),-35,width,height+25);
                   mousex=width/2;
                   mousey=mousey+35;
                   if(url.equals("left"))
                   mousex=mousex-15;
                   else if(url.equals("right"))
                    mousex=mousex+15;
                   }
                   else if((x>=width-20 && x<=swidth-width+20) && (y>=height-60 && y<=sheight-height+20))
                   {
                   r=new Rectangle(x-(width/2),y-(height/2),width,height+25);
                   mousex=width/2;
                   mousey=height/2;
                   if(url.equals("up"))
                   mousey=(height/2)-15;
                   else if(url.equals("down"))
                   mousey=(height/2)+15;
                   else if(url.equals("left"))
                   mousex=mousex-15;
                   else if(url.equals("right"))
                    mousex=mousex+15;
                   }
                   else if((x>=width-20 && x<=swidth-width+20) && y>sheight-height-25)
                   {
                   r=new Rectangle(x-(width/2),sheight-height-15,width,height+15);
                   mousex=width/2;
                   mousey=remainy-(sheight-height-15);
                   if(url.equals("left"))
                   mousex=mousex-15;
                   else if(url.equals("right"))
                    mousex=mousex+15;
                   }

       ByteArrayOutputStream bo =new ByteArrayOutputStream();
       bi=robot.createScreenCapture(r);
		Graphics2D gr12D = bi.createGraphics();
                Image image=ImageIO.read( getClass().getResource("point3.gif"));
		gr12D.drawImage(image,mousex,mousey,this);
       gr12D.dispose();
       ImageIO.write(bi, "jpg", bo);
       bytearray=bo.toByteArray();
       dout.writeInt(bytearray.length);
       dout.flush();
       dout.write(bytearray,0,bytearray.length);

            }


       else if(url.startsWith("live"))
       {
               width=din.readInt();
               height=din.readInt();
               while((din.readInt())!=0)
               {
                  try {
                     r=new Rectangle(0,0,swidth,sheight);
                     ByteArrayOutputStream bo =new ByteArrayOutputStream();
                     bi=robot.createScreenCapture(r);
                     int type = bi.getType() == 0? BufferedImage.TYPE_INT_ARGB : bi.getType();
					 
					 //Resizing the image..........
                     BufferedImage resizeImagePng = resizeImage(bi, type);
					 
					 
                     ImageIO.write(resizeImagePng, "jpg", bo);
                     bytearray=bo.toByteArray();
                     
					 //New Resizing send to client side.....
					 dout.writeInt(bytearray.length);
                     dout.flush();
                     dout.write(bytearray,0,bytearray.length);

                    }

                  catch(Exception e)
                  {

                  }
             }
               System.out.println("outside the loop");
       }

           else if(url.startsWith("keyboard"))
           {
		          //Clear the Url fro Next Inputting
                  url = url.replaceAll("keyboard"," ");
				  
				  /*It returns a copy of this string with leading and trailing white space removed,
                  				  or this string if it has no leading or trailing white space.*/
                  url=url.trim();
                  try  {

                          if(url.equals("start"))
                          {
                              robot.keyPress(KeyEvent.VK_WINDOWS);
                              robot.keyRelease(KeyEvent.VK_WINDOWS);
                          }
                          else if(url.equals("close"))
                          {
                              robot.keyPress(KeyEvent.VK_ALT);
                              robot.keyPress(KeyEvent.VK_F4);
                              robot.keyRelease(KeyEvent.VK_F4);
                              robot.keyRelease(KeyEvent.VK_ALT);
                          }
                      }
                  catch(Exception e)
                 {

                 }
            }


            else if(url.startsWith("switch"))
            {
                  url = url.replaceAll("switch"," ");
                  url=url.trim();
                  try  {
                  if(url.equals("view"))
                  {
                         robot.keyPress(KeyEvent.VK_WINDOWS);
                         robot.keyPress(KeyEvent.VK_TAB);
                         robot.keyRelease(KeyEvent.VK_TAB);
                  }
                  else if(url.equals("select"))
                  {
                         robot.keyPress(KeyEvent.VK_TAB);
                         robot.keyRelease(KeyEvent.VK_TAB);
                  }
                  else if(url.equals("ok"))

                         robot.keyRelease(KeyEvent.VK_WINDOWS);
                  }
                  catch(Exception e)
                 {
                 }
           }


           else if(url.startsWith("mozila")||url.startsWith("internet"))
           {
                 url = url.replaceAll("mozila"," ");
                 url = url.replaceAll("internet"," ");
                 url=url.trim();
                 osName = System.getProperty("os.name");
                 boolean p=osName.startsWith("Windows");
                 try  {

                       if(url.equals("firefox.exe") || url.equals("iexplore.exe"))
                       {

                          try {
                               if (p)
                                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);

                              }
                          catch(Exception e)
                             {
                               }
                       }

            else if(url.equals("home"))
            {
                robot.keyPress(KeyEvent.VK_ALT);
                robot.keyPress(KeyEvent.VK_HOME);
                robot.keyRelease(KeyEvent.VK_HOME);
                robot.keyRelease(KeyEvent.VK_ALT);
            }
            else if(url.equals("newt"))
            {
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_T);
                robot.keyRelease(KeyEvent.VK_T);
                robot.keyRelease(KeyEvent.VK_CONTROL);
            }
            else if(url.equals("closet"))
            {
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_W);
                robot.keyRelease(KeyEvent.VK_W);
                robot.keyRelease(KeyEvent.VK_CONTROL);

            }
            else if(url.equals("nextt"))
            {
                 robot.keyPress(KeyEvent.VK_CONTROL);
                 robot.keyPress(KeyEvent.VK_TAB);
                 robot.keyRelease(KeyEvent.VK_TAB);
                 robot.keyRelease(KeyEvent.VK_CONTROL);
            }
            else if(url.equals("refresh"))
            {
                 robot.keyPress(KeyEvent.VK_CONTROL);
                 robot.keyPress(KeyEvent.VK_F5);
                 robot.keyRelease(KeyEvent.VK_F5);
                 robot.keyRelease(KeyEvent.VK_CONTROL);
           }
           else if(url.equals("back"))
           {
                 robot.keyPress(KeyEvent.VK_BACK_SPACE);
                 robot.keyRelease(KeyEvent.VK_BACK_SPACE);
           }
          else if(url.equals("addbook"))
          {
                 robot.keyPress(KeyEvent.VK_CONTROL);
                 robot.keyPress(KeyEvent.VK_D);
                 robot.keyRelease(KeyEvent.VK_D);
                 robot.keyRelease(KeyEvent.VK_CONTROL);
                 Thread.sleep(800);
                 robot.keyPress(KeyEvent.VK_ENTER);
                 robot.keyRelease(KeyEvent.VK_ENTER);
          }
          else if(url.equals("zoomout"))
          {
                 robot.keyPress(KeyEvent.VK_CONTROL);
                 robot.keyPress(KeyEvent.VK_MINUS);
                 robot.keyRelease(KeyEvent.VK_MINUS);
                 robot.keyRelease(KeyEvent.VK_CONTROL);
          }
          else if(url.equals("zoomin"))
         {
                 robot.keyPress(KeyEvent.VK_CONTROL);
                 robot.keyPress(KeyEvent.VK_SHIFT);
                 robot.keyPress(KeyEvent.VK_EQUALS);
                 robot.keyRelease(KeyEvent.VK_EQUALS);
                 robot.keyRelease(KeyEvent.VK_SHIFT);
                 robot.keyRelease(KeyEvent.VK_CONTROL);
         }
         else if(url.equals("scrolldown"))
        {
                 robot.keyPress(KeyEvent.VK_DOWN);
                 robot.keyRelease(KeyEvent.VK_DOWN);
        }
        else if(url.equals("scrollup"))
        {
                 robot.keyPress(KeyEvent.VK_UP);
                 robot.keyRelease(KeyEvent.VK_UP);
        }
      }
     catch(Exception e)
     {
     }
   }


else if(url.startsWith("media"))
{
  url = url.replaceAll("media"," ");
  url=url.trim();
  try{
 Robot robot=new Robot();

 if(url.equals("wmplayer.exe"))
  {
      osName = System.getProperty("os.name");
try {
     if (osName.startsWith("Windows")) {
	 
	 //Runtime for program executing............
       Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
               }
    }
       catch(Exception e)
      {
       }
  }

  else if(url.equals("ofile"))
  {
   robot.keyPress(KeyEvent.VK_CONTROL);
   robot.keyPress(KeyEvent.VK_O);
   robot.keyRelease(KeyEvent.VK_O);
   robot.keyRelease(KeyEvent.VK_CONTROL);
  }
 else if(url.equals("Play/Pause"))
  {
   robot.keyPress(KeyEvent.VK_CONTROL);
   robot.keyPress(KeyEvent.VK_P);
   robot.keyRelease(KeyEvent.VK_P);
   robot.keyRelease(KeyEvent.VK_CONTROL);
  }
  else if(url.equals("Previous"))
  {
   robot.keyPress(KeyEvent.VK_CONTROL);
   robot.keyPress(KeyEvent.VK_B);
   robot.keyRelease(KeyEvent.VK_B);
   robot.keyRelease(KeyEvent.VK_CONTROL);
  }
  else if(url.equals("Next"))
  {
   robot.keyPress(KeyEvent.VK_CONTROL);
   robot.keyPress(KeyEvent.VK_F);
   robot.keyRelease(KeyEvent.VK_F);
   robot.keyRelease(KeyEvent.VK_CONTROL);
  }
  else if(url.equals("Vup"))
  {
   robot.keyPress(KeyEvent.VK_F9);
   robot.keyRelease(KeyEvent.VK_F9);
  }
  else if(url.equals("Vdown"))
  {
   robot.keyPress(KeyEvent.VK_F8);
   robot.keyRelease(KeyEvent.VK_F8);
  }
  else if(url.equals("Forward"))
  {
   robot.keyPress(KeyEvent.VK_CONTROL);
   robot.keyPress(KeyEvent.VK_SHIFT);
   robot.keyPress(KeyEvent.VK_F);
    robot.keyRelease(KeyEvent.VK_F);
     robot.keyRelease(KeyEvent.VK_SHIFT);
   robot.keyRelease(KeyEvent.VK_CONTROL);
  }
  else if(url.equals("Mute"))
  {
   robot.keyPress(KeyEvent.VK_F7);
   robot.keyRelease(KeyEvent.VK_F7);
  }
  else if(url.equals("Close"))
    {
   robot.keyPress(KeyEvent.VK_ALT);
   robot.keyPress(KeyEvent.VK_F4);
   robot.keyRelease(KeyEvent.VK_F4);
   robot.keyRelease(KeyEvent.VK_ALT);
    }

  }
  catch(Exception e)
  {

  }
}

else if(url.startsWith("movekey"))
{
    url = url.replaceAll("movekey"," ");
    url=url.trim();
    try  {
        Robot robot=new Robot();
    if(url.equals("tab"))
    {
   robot.keyPress(KeyEvent.VK_TAB);
   robot.keyRelease(KeyEvent.VK_TAB);
    }
    else if(url.equals("back"))
    {
   robot.keyPress(KeyEvent.VK_BACK_SPACE);
   robot.keyRelease(KeyEvent.VK_BACK_SPACE);
    }
    else if(url.equals("down"))
    {
   robot.keyPress(KeyEvent.VK_DOWN);
   robot.keyRelease(KeyEvent.VK_DOWN);
    }
    else if(url.equals("up"))
    {
   robot.keyPress(KeyEvent.VK_UP);
   robot.keyRelease(KeyEvent.VK_UP);
    }
    else if(url.equals("right"))
    {
   robot.keyPress(KeyEvent.VK_RIGHT);
   robot.keyRelease(KeyEvent.VK_RIGHT);
    }
    else if(url.equals("left"))
    {
   robot.keyPress(KeyEvent.VK_LEFT);
   robot.keyRelease(KeyEvent.VK_LEFT);
    }
    else if(url.equals("ok"))
    {
   robot.keyPress(KeyEvent.VK_ENTER);
   robot.keyRelease(KeyEvent.VK_ENTER);
    }
    else if(url.equals("sall"))
    {
   robot.keyPress(KeyEvent.VK_CONTROL);
   robot.keyPress(KeyEvent.VK_A);
   robot.keyRelease(KeyEvent.VK_A);
    robot.keyRelease(KeyEvent.VK_CONTROL);
    }
    else if(url.equals("menubar"))
    {
   robot.keyPress(KeyEvent.VK_ALT);
   robot.keyRelease(KeyEvent.VK_ALT);
    }
    else if(url.equals("foption"))
    {
   robot.keyPress(KeyEvent.VK_CONTEXT_MENU);
   robot.keyRelease(KeyEvent.VK_CONTEXT_MENU);
    }
    else if(url.equals("escape"))
    {
   robot.keyPress(KeyEvent.VK_ESCAPE);
   robot.keyRelease(KeyEvent.VK_ESCAPE);
    }
    }
     catch(Exception e)
     {
         //System.out.println("Error occured while executing");
      }
}

else if(url.startsWith("vplayer"))
{
  url = url.replaceAll("vplayer"," ");
  url=url.trim();
  try{
 Robot robot=new Robot();
 if(url.equals("vlc"))
  {
      osName = System.getProperty("os.name");
try {
     if (osName.startsWith("Windows")) {
      Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe");
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe");
               }
    }
       catch(Exception e)
      {
       }
  }

  else if(url.equals("ofile"))
  {
   robot.keyPress(KeyEvent.VK_CONTROL);
   robot.keyPress(KeyEvent.VK_O);
   robot.keyRelease(KeyEvent.VK_O);
   robot.keyRelease(KeyEvent.VK_CONTROL);
  }
 else if(url.equals("Play/Pause"))
  {
   robot.keyPress(KeyEvent.VK_SPACE);
   robot.keyRelease(KeyEvent.VK_SPACE);
  }
  else if(url.equals("Previous"))
  {
   robot.keyPress(KeyEvent.VK_P);
   robot.keyRelease(KeyEvent.VK_P);
  }
  else if(url.equals("Next"))
  {
   robot.keyPress(KeyEvent.VK_N);
   robot.keyRelease(KeyEvent.VK_N);
  }
  else if(url.equals("Vup"))
  {
   robot.keyPress(KeyEvent.VK_CONTROL);
   robot.keyPress(KeyEvent.VK_UP);
   robot.keyRelease(KeyEvent.VK_UP);
   robot.keyRelease(KeyEvent.VK_CONTROL);
  }
  else if(url.equals("Vdown"))
  {
  robot.keyPress(KeyEvent.VK_CONTROL);
   robot.keyPress(KeyEvent.VK_DOWN);
   robot.keyRelease(KeyEvent.VK_DOWN);
   robot.keyRelease(KeyEvent.VK_CONTROL);
  }
  else if(url.equals("Forward"))
  {
   robot.keyPress(KeyEvent.VK_ALT);
   robot.keyPress(KeyEvent.VK_RIGHT);
    robot.keyRelease(KeyEvent.VK_RIGHT);
     robot.keyRelease(KeyEvent.VK_ALT);
  }
  else if(url.equals("Rewind"))
  {
   robot.keyPress(KeyEvent.VK_ALT);
   robot.keyPress(KeyEvent.VK_LEFT);
    robot.keyRelease(KeyEvent.VK_LEFT);
     robot.keyRelease(KeyEvent.VK_ALT);
  }
  else if(url.equals("Mute"))
  {
   robot.keyPress(KeyEvent.VK_M);
   robot.keyRelease(KeyEvent.VK_M);
  }
  else if(url.equals("Aratio"))
  {
   robot.keyPress(KeyEvent.VK_A);
   robot.keyRelease(KeyEvent.VK_A);
  }
  else if(url.equals("Cscreen"))
  {
   robot.keyPress(KeyEvent.VK_C);
   robot.keyRelease(KeyEvent.VK_C);
  }
  else if(url.equals("Fscreen"))
  {
   robot.keyPress(KeyEvent.VK_F);
   robot.keyRelease(KeyEvent.VK_F);
  }
  else if(url.equals("Exit"))
    {
   robot.keyPress(KeyEvent.VK_ALT);
   robot.keyPress(KeyEvent.VK_F4);
   robot.keyRelease(KeyEvent.VK_F4);
   robot.keyRelease(KeyEvent.VK_ALT);
    }

  }
  catch(Exception e)
  {

  }
}



else if(url.startsWith("power"))
{
    url = url.replaceAll("power"," ");
    url=url.trim();
    try  {

        if(url.startsWith("POWERPNT.exe"))
        {
                 osName = System.getProperty("os.name");
           try {
                  if (osName.startsWith("Windows")) {
                 Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                     }
                   }
       catch(Exception e)
            {
            }
        }

  else if(url.equals("ofile"))
  {
   robot.keyPress(KeyEvent.VK_CONTROL);
   robot.keyPress(KeyEvent.VK_O);
   robot.keyRelease(KeyEvent.VK_O);
   robot.keyRelease(KeyEvent.VK_CONTROL);
  }
    else if(url.equals("exit"))
    {
   robot.keyPress(KeyEvent.VK_ESCAPE);
   robot.keyRelease(KeyEvent.VK_ESCAPE);
    }
    else if(url.equals("next"))
    {
   robot.keyPress(KeyEvent.VK_N);
   robot.keyRelease(KeyEvent.VK_N);
    }
    else if(url.equals("previous"))
    {
   robot.keyPress(KeyEvent.VK_P);
   robot.keyRelease(KeyEvent.VK_P);
    }
    else if(url.equals("Close"))
    {
   robot.keyPress(KeyEvent.VK_ALT);
   robot.keyPress(KeyEvent.VK_F4);
   robot.keyRelease(KeyEvent.VK_F4);
   robot.keyRelease(KeyEvent.VK_ALT);
    }
    }
     catch(Exception e)
     {
         //System.out.println("Error occured while executing");
}
}

else if(url.startsWith("run"))
{

   url = url.replaceAll("run"," ");
    url=url.trim();
    try  {
    if(url.equals("shutdown"))
    Runtime.getRuntime().exec("shutdown -s -t 0");
    else if(url.equals("restart"))
     Runtime.getRuntime().exec("shutdown -r");
    else if(url.equals("logoff"))
   Runtime.getRuntime().exec("shutdown -l");
    else
       Runtime.getRuntime().exec(url);
    }
     catch(Exception e)
     {
         System.out.println("Error occured while executing");
      }
}
}
}
}
catch (IOException e)
{
    l5=new JLabel("Client Disconnected...");
    l5.setBounds(50,90,170,30);
    f.repaint();
    f.repaint();
    f.add(l5);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    System.exit(0);
}
    }

public void actionPerformed(ActionEvent e)
{
     String item = e.getActionCommand();
     if (item.equals("About"))
         new About();
     else if(item.equals("Exit"))
         System.exit(0);
     else if(item.equals("Help"))
         new Help();
}

private  BufferedImage resizeImage(BufferedImage originalImage, int type){
	BufferedImage resizedImage = new BufferedImage(height,width,type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage,0,0,height,width,null);
	g.dispose();

	return resizedImage;
    }


public void windowIconified(WindowEvent e)
{

}

public void windowDeactivated(WindowEvent e)
{}
public void windowActivated(WindowEvent e)
{}
public void windowDeiconified(WindowEvent e)
{}
public void windowClosed(WindowEvent e)
{}
public void windowClosing(WindowEvent e)
{}
public void windowOpened(WindowEvent e)
{}
public static void main (String args[]){
EServer echoserver = new EServer();
echoserver.BluetoothCode(echoserver);
}
}
