package exercise01;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PendulumWithEnergyGraph extends JFrame {

	public PendulumWithEnergyGraph() {
		setTitle("감쇠 진자 + 에너지 그래프 시뮬레이터");
		setSize(1000, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new GridLayout(1, 2));
		EnergyGraphPanel energyPanel = new EnergyGraphPanel();
		add(new PendulumPanel(energyPanel));
		add(energyPanel);
		
		setVisible(true);
	}
	
	class PendulumPanel extends JPanel implements ActionListener {
		private Timer timer;
		private double angle = Math.PI / 4;
		private double angleVelocity = 0;
		private double angleAcceleration = 0;
		private final double gravity = 9.8;
		private final int length = 200;
		private final double mass = 1.0;
		private final double damping = 0.995;
		
		private final int originX = 200;
		private final int originY = 100;
		private EnergyGraphPanel energyPanel;  //그래프 참조
		
		public PendulumPanel(EnergyGraphPanel energyPanel) {
			this.energyPanel = energyPanel;
			timer = new Timer(16, this);  //약 60FPS로 갱신
			timer.start();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			angleAcceleration = -(gravity / length) * Math.sin(angle);
			angleVelocity += angleAcceleration;
			angleVelocity *= damping;  //감쇠 적용
			angle += angleVelocity;
			
			//에너지 계산
			double velocity = length * angleVelocity;
			double ke = 0.5 * mass * velocity * velocity;
			double pe = mass * gravity * length * (1 - Math.cos(angle));
			double te = ke + pe;
			energyPanel.addEnergy(ke, pe, te);
			
			repaint();
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int bobX = originX + (int)(length * Math.sin(angle));
			int bobY = originY + (int)(length * Math.cos(angle));
			g.setColor(Color.BLACK);
			g.drawLine(originX, originY, bobX, bobY);  //줄
			g.fillOval(bobX - 10, bobY - 10, 20, 20);  //추
		}
	}
	
	class EnergyGraphPanel extends JPanel {
		private java.util.List<Double> keList = new ArrayList<>();
		private java.util.List<Double> peList = new ArrayList<>();
		private java.util.List<Double> teList = new ArrayList<>();
		
		public void addEnergy(double ke, double pe, double te) {
			keList.add(ke);
			peList.add(pe);
			teList.add(te);
			if(keList.size() > getWidth()) {
				keList.remove(0);
				peList.remove(0);
				teList.remove(0);
			}
			repaint();
		}
		
		public EnergyGraphPanel() {
			setBackground(Color.WHITE);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int h = getHeight();
			
			for(int i=1; i<keList.size(); i++) {
				int x1 = i - 1, x2 = i;
				
				int y1_KE = h - (int)(keList.get(i - 1) * 10);
				int y2_KE = h - (int)(keList.get(i) * 10);
				g.setColor(Color.BLUE);
				g.drawLine(x1, y1_KE, x2, y2_KE);
				
				int y1_PE = h - (int)(peList.get(i - 1) * 10);
				int y2_PE = h - (int)(peList.get(i) * 10);
				g.setColor(Color.RED);
				g.drawLine(x1, y1_PE, x2, y2_PE);
				
				int y1_TE = h - (int)(teList.get(i - 1) * 10);
				int y2_TE = h - (int)(teList.get(i) * 10);
				g.setColor(Color.BLACK);
				g.drawLine(x1, y1_TE, x2, y2_TE);				
			}
		}
	}

	public static void main(String[] args) {
		new PendulumWithEnergyGraph();
	}
}
