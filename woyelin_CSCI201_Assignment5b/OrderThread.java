package woyelin_CSCI201_Assignment5b;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JLabel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OrderThread extends Thread {

	Socket s;
	BufferedReader br;
	PrintWriter pw;
	Factory factory;
	
	ObjectInputStream ois;
	
	String jsonAddress = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
	
	public OrderThread(Factory factory, Socket s) {
		try {
			this.s = s;
			this.factory = factory;
			pw = new PrintWriter(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void sendDoneInfoToThisClient(String taskName) {
		pw.println("done");
		pw.flush();
		
		try {
			URL url = new URL(jsonAddress + taskName);
		    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String jsonString = "", line;
			while((line = br.readLine())!=null) 
				jsonString += line+"\n";
			
			// now have full json String Content, parse that
			JSONObject object = new JSONObject(jsonString);
			JSONObject responseData = (JSONObject) object.get("responseData");
			JSONArray results = (JSONArray) responseData.get("results");
			
			
			URL imageUrl = null;
			
			for(int i=0;i<results.length();i++) {
				JSONObject imageObject = (JSONObject) results.getJSONObject(i);
				String str = imageObject.get("url").toString();
				if( str.substring(str.length()-4).equalsIgnoreCase(".jpg") ||
						str.substring(str.length()-4).equalsIgnoreCase(".png") ||
						str.substring(str.length()-4).equalsIgnoreCase(".gif") ||
						str.substring(str.length()-4).equalsIgnoreCase(".bmp") ||
						str.substring(str.length()-5).equalsIgnoreCase(".jpeg")) {
					imageUrl = new URL(str);
					break;
				}
			}

			if (imageUrl != null) {
				pw.println("url");
				pw.flush();
				pw.println(imageUrl);
				pw.flush();
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// This is server receive order object from client and send information to the client
	public void run() {
		
		while(true) {
			try {

				int command = ois.readInt();
				if(command==1)
					return;
				
				else if (command == 0) {
					Recipe recipe = (Recipe) ois.readObject();
					
					Task task = new Task(recipe, this);
//					factory.taskVt.add(task);

					OrderPanel orderPanel = new OrderPanel(recipe);
					factory.centerOrderPanel.add(orderPanel);
					factory.centerOrderPanel.updateUI();
					factory.orderPanelVt.add(orderPanel);
					orderPanel.decline.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							pw.println("deny");
							pw.flush();
							factory.centerOrderPanel.remove(orderPanel);
							factory.centerOrderPanel.updateUI();
							factory.orderPanelVt.remove(orderPanel);
						}
					});
					orderPanel.accept.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							pw.println("accept");
							pw.flush();

							factory.taskVt.add(task);
							
							factory.centerOrderPanel.remove(orderPanel);
							factory.centerOrderPanel.updateUI();
							factory.orderPanelVt.remove(orderPanel);
							// we accept the order, now create a new recipe in
							// the factory and start working
//							factory.recipeVt.add(recipe);
//							Task newTask = new Task(recipe.name,OrderThread.this, );
//							factory.taskVt.add(newTask);
							JLabel lbl = new JLabel(recipe.name + "..."+ task.status);
							lbl.setFont(new Font("Monoco", Font.PLAIN, 16));
//							factory.money += recipe.cost;
//							factory.moneyInOrderLabel.setText("Money: $"+factory.money);
//							factory.moneyInOrderLabel.setFont(new Font("Monoco", Font.BOLD, 16));
							factory.taskBoardPanel.add(lbl);
							factory.taskBoardPanel.updateUI();
							// signal all workers who are currently waiting for
							// a task
							Worker.taskLock.lock();
							Worker.waitForTask.signal();
							Worker.taskLock.unlock();
						}
					});
				}  
				// end of else-if
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (EOFException e) {
				break;
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
}
