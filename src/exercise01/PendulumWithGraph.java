package exercise01;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

//메인 클래스
public class PendulumWithGraph extends JFrame {
	public PendulumWithGraph() {
		setTitle("진자 + 그래프 시뮬레이터");
		setSize(900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//좌우로 진자 영역과 그래프 영역 나누기
		setLayout(new GridLayout(1, 2));
		
		GraphPanel graphPanel = new GraphPanel();
		add(new PendulumPanel(graphPanel));
		add(graphPanel);
		
		setVisible(true);
	}
	
	//진자 시뮬레이션 패널
	class PendulumPanel extends JPanel implements ActionListener {
		private Timer timer;
		private double angle = Math.PI / 4;
		private double angleVelocity = 0;
		private double angleAcceleration = 0;
		private final double gravity = 9.8;
		private final int length = 200;
		private final int originX = 200;
		private final int originY = 100;
		
		private GraphPanel graphPanel;  //그래프 참조
		
		public PendulumPanel(GraphPanel graphPanel) {
			this.graphPanel = graphPanel;
			timer = new Timer(16, this);  //약 60FPS로 갱신
			timer.start();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			angleAcceleration = -(gravity / length) * Math.sin(angle);
			angleVelocity += angleAcceleration;
			angle += angleVelocity;
			graphPanel.addAngle(angle);  //angle 전달
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
	
	//그래프 영역
	class GraphPanel extends JPanel{
		private java.util.List<Double> angleHistory = new java.util.ArrayList<>();
		
		public void addAngle(double angle) {
			angleHistory.add(angle);
			if(angleHistory.size() > getWidth()) {
				angleHistory.remove(0);  //오래된 값 제거
			}
			repaint();
		}
		
		public GraphPanel() {
			setBackground(Color.WHITE);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);  //기준선
			
			g.setColor(Color.BLUE);
			for(int i=1; i<angleHistory.size(); i++) {
				int x1 = i - 1;
				int y1 = getHeight()/2 - (int)(angleHistory.get(i - 1) * 100);
				int x2 = i;
				int y2 = getHeight()/2 - (int)(angleHistory.get(i) * 100);
				g.drawLine(x1, y1, x2, y2);
			}
		}
	}
	
	//메인 실행
	public static void main(String[] args) {
		new PendulumWithGraph();
	}

}
