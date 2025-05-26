package exercise01;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PendulumSimulatorEx extends JPanel implements ActionListener {

	private Timer timer;
	private double angle = Math.PI / 4;  //초기 각도 45도
	private double angularVelocity = 0;  //각속도
	private double angularAcceleration = 0;  //각가속도
	private final int length = 200;  //진자의 길이 (픽셀)
	private final int originX = 400, originY = 100;  //고정점 위치
	private final double gravity = 0.5;  //중력 가속도
	
	public PendulumSimulatorEx() {
		timer = new Timer(16, this);  //약 60fps(Frames Per Second)
		timer.start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);  //화면을 먼저 깨끗하게 비운다 (clear)
		int bobX = originX + (int)(length * Math.sin(angle));
		int bobY = originY + (int)(length * Math.cos(angle));
		g.setColor(Color.black);
		g.drawLine(originX, originY, bobX, bobY);  //줄
		//g.setColor(Color.RED);
		g.fillOval(bobX - 10, bobY - 10, 20, 20);  //추
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//물리 시뮬레이션 공식
		angularAcceleration = (-gravity / length) * Math.sin(angle);  //진자의 기본 공식
		angularVelocity += angularAcceleration;
		angle += angularVelocity;
		
		//감쇠 효과 (공기저항처럼 조금씩 줄어들게)
		angularVelocity *= 0.99;
		
		repaint();
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Pendulum Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.add(new PendulumSimulatorEx());
		frame.setVisible(true);
	}
}
