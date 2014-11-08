

import java.awt.EventQueue;

import com.uag.sd.weathermonitor.gui.SensorNetworkSimGUI;


public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SensorNetworkSimGUI window = new SensorNetworkSimGUI();
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
