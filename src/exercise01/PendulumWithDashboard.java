package exercise01;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;

public class PendulumWithDashboard extends JFrame {
	
	//그래프 및 정보 요약 레이블
	private JLabel energySummaryLabel = new JLabel("에너지 요약 준비 중");
	private JLabel frequencySummaryLabel = new JLabel("주기 요약 준비 중");
	private JLabel stateSummaryLabel = new JLabel("상태 메시지 준비 중");
	
	//테마 정의
	private Theme currentTheme;
	private final Theme darkTheme = new Theme(new Color(30, 30, 30), Color.WHITE, Color.GRAY,Color.BLUE, Color.RED, Color.GREEN);
	private final Theme classicTheme = new Theme(new Color(240, 240, 240), Color.BLACK, Color.DARK_GRAY,Color.BLUE, Color.RED, Color.BLACK);
	private final Theme medicalTheme = new Theme(new Color(0, 33, 66), new Color(230, 255, 255), new Color(0, 255, 204),new Color(255, 255, 255), new Color(255, 255, 255), new Color(0, 200, 200));
	private final Theme highContrastTheme = new Theme(Color.BLACK, Color.WHITE, Color.YELLOW,Color.CYAN, Color.WHITE, Color.BLACK);
	private final Theme originalTheme = new Theme(new Color(50, 70, 60), Color.WHITE, new Color(50, 70, 60),new Color(50, 70, 60), new Color(50, 70, 60), new Color(50, 70, 60));
	
	public PendulumWithDashboard() {
		//전체 초기 설정
		setGlobalFont(new Font("맑은 고딕", Font.PLAIN, 13));
		setTitle("진자 시뮬레이터 + 분석 패널");
		setSize(1000, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(1, 2));
		
		//주요 컴포넌트 생성
		JLabel angleLabel = new JLabel("설정 각도: ");
		DashboardPanel dashboard = new DashboardPanel();
		PendulumPanel pendulum = new PendulumPanel(dashboard, angleLabel);
		
		//색상 적용
		pendulum.setBackground(originalTheme.background);
		dashboard.setBackground(originalTheme.background);
		getContentPane().setBackground(originalTheme.background);
		
		angleLabel.setForeground(Color.WHITE);
		energySummaryLabel.setForeground(Color.WHITE);
		frequencySummaryLabel.setForeground(Color.WHITE);
		stateSummaryLabel.setForeground(Color.WHITE);
		
		//왼쪽 UI 구성
		JPanel leftPanel = new JPanel(new BorderLayout());
		
		//상단 정보 요약
		JPanel topBoxPanel = new JPanel(new GridLayout(1,3,5,5));
		topBoxPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		topBoxPanel.add(makeInfoBox("에너지 그래프 요약", energySummaryLabel,Color.YELLOW));
		topBoxPanel.add(makeInfoBox("진동수/주기 요약", frequencySummaryLabel,Color.CYAN));
		topBoxPanel.add(makeInfoBox("상태 메시지", stateSummaryLabel,Color.GREEN));
		
		//하단 조절 슬라이더 및 버튼 + 미니 그래프
		JPanel miniAndButtons = new JPanel(new GridLayout(2, 1));
		miniAndButtons.setBackground(new Color(50, 70, 60));
		miniAndButtons.add(wrapaWithBorder("총 에너지 미니 그래프", dashboard.getMiniGraph(), Color.GRAY));
		miniAndButtons.add(pendulum.getButtonPanel());
		
		JPanel controlSliders = new JPanel();
		controlSliders.setLayout(new BoxLayout(controlSliders, BoxLayout.Y_AXIS));
		
		JPanel lengthPanel = new JPanel();
		lengthPanel.add(new JLabel("줄 길이 조절"));
		lengthPanel.add(pendulum.lengthSlider);
		
		JPanel originPanel = new JPanel();
		originPanel.add(new JLabel("기준점 위치 조절"));
		originPanel.add(pendulum.originXSlider);
		
		JPanel anglePanel = new JPanel();
		anglePanel.add(new JLabel("속도 슬라이더"));
		anglePanel.add(pendulum.getSpeedSlider());
		
		controlSliders.add(lengthPanel);
		controlSliders.add(originPanel);
		controlSliders.add(anglePanel);
		
		//좌측 하단 패널 통합
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(miniAndButtons, BorderLayout.NORTH);
		bottomPanel.add(controlSliders, BorderLayout.SOUTH);
		
		leftPanel.add(topBoxPanel, BorderLayout.NORTH);
		leftPanel.add(pendulum, BorderLayout.CENTER);
		leftPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		//전체 프레임에 추가
		add(leftPanel);
		add(dashboard);
		
		setVisible(true);
		pack();
	}
	
	//UI 전체 폰트 일괄 적용
	private void setGlobalFont(Font font) {
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while(keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if(value instanceof FontUIResource) {
				UIManager.put(key, new FontUIResource(font));
			}
		}
	}
	
	//테마 적용 함수
	public void applyTheme(Theme theme) {
		currentTheme = theme;
		getContentPane().setBackground(theme.background);
		energySummaryLabel.setForeground(theme.text);
		frequencySummaryLabel.setForeground(theme.text);
		stateSummaryLabel.setForeground(theme.text);
	}
	
	//요약 정보 박스 스타일 정의
	private JPanel makeInfoBox(String title, JLabel contentLabel, Color borderColor) {
		JPanel box = new JPanel(new BorderLayout());
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(borderColor, 2), title);
		border.setTitleColor(Color.BLACK);
		box.setBorder(border);
		box.setBackground(Color.LIGHT_GRAY);
		contentLabel.setHorizontalAlignment(JLabel.CENTER);
		contentLabel.setForeground(Color.BLACK);
		box.add(contentLabel, BorderLayout.CENTER);
		return box;
	}
	
	//테두리 스타일 박스
	private JPanel wrapaWithBorder(String title, JComponent comp, Color borderColor) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(new Color(50, 70, 60));
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(borderColor, 2), title);
		border.setTitleColor(Color.WHITE);
		panel.setBorder(border);;
		comp.setBackground(new Color(50, 70, 60));
		comp.setForeground(Color.WHITE);
		if(comp instanceof JPanel) { ((JPanel) comp).setOpaque(true); }
		panel.add(comp, BorderLayout.CENTER);
		return panel;
	}
	
	public static void main(String[] args) {
		new PendulumWithDashboard();
	}
	
	//PendulumPanel 클래스
	class PendulumPanel extends JPanel implements ActionListener, MouseListener {
		private Timer timer;
		private double angle = Math.PI / 4;
		private double angleVelocity = 0;
		private double angleAcceleration = 0;
		private final double gravity = 9.8;
		private int length = 200;
		private final double mass = 1.0;
		private final double damping = 0.995;
		private int originX = 230;
		private final int originY = 100;
		
		private boolean dragging = false;
		private JLabel angleLabel;
		private DashboardPanel dashboard;
		private JSlider speedSlider;
		private List<Point> trail = new ArrayList<>();
		JSlider lengthSlider;
		JSlider originXSlider;
		
		public PendulumPanel(DashboardPanel dashboard, JLabel angleLabel) {
			this.dashboard = dashboard;
			this.angleLabel = angleLabel;
			
			setBackground(new Color(50, 70, 60));
			setLayout(new BorderLayout());
			setPreferredSize(new Dimension(500, 300));
			
			//타이머 설정 (약 60FPS)
			timer = new Timer(16, this);
			timer.start();
			
			//슬라이더 초기화
			speedSlider = new JSlider(1, 60, 16);  //1~60ms (기본값: 16ms)
			speedSlider.addChangeListener(e -> timer.setDelay(speedSlider.getValue()));
			
			lengthSlider = new JSlider(100, 300, 200);  
			originXSlider = new JSlider(100, 400, originX);
			
			lengthSlider.addChangeListener(e -> { length = lengthSlider.getValue(); });
			originXSlider.addChangeListener(e -> { originX = originXSlider.getValue(); });
			
			addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					dragging = true;
					updateAngleFromMouse(e.getX(), e.getY());
				}
				public void mouseReleased(MouseEvent e) {
					dragging = false;
				}
			});
			
			addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent e) {
					if(dragging) { updateAngleFromMouse(e.getX(), e.getY()); }
				}
			});
		}
		
		public JPanel getButtonPanel() {
			JPanel panel = new JPanel();
			panel.setBackground(new Color(50, 70, 60));
			panel.add(dashboard.velocityColorBtn);
			panel.add(dashboard.energyColorBtn);
			return panel;
		}
		
		public JSlider getSpeedSlider() { return speedSlider; }
		
		private void updateAngleFromMouse(int x, int y) {
			double dx = x - originX;
			double dy = y - originY;
			angle = Math.atan2(dx, dy);  //y 기준 아래가 + 방향
			angleLabel.setText(String.format("설정 각도: %.1fº, 속도 조절", Math.toDegrees(angle)));
			repaint();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			angleAcceleration = -(gravity / length) * Math.sin(angle);
			angleVelocity += angleAcceleration;
			angleVelocity *= damping;  //감쇠 적용
			angle += angleVelocity;
			
			double velocity = length * angleVelocity;
			double ke = 0.5 * mass * velocity * velocity;
			double pe = mass * gravity * length * (1 - Math.cos(angle));
			double te = ke + pe;
			double energyEfficiency = te / (mass * gravity * length * 2);  //에너지 보존율 추정
			double frequency = 1.0 / (2 * Math.PI) * Math.sqrt(gravity / length);
			
			dashboard.addData(angleVelocity, ke, pe, te, energyEfficiency, frequency);
			dashboard.getMiniGraph().addTotalEnergy(te);
			
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
			
			//각도 표시 테스트
			g.setColor(Color.WHITE);
			g.drawString(String.format("현재 각도: %.1fº", Math.toDegrees(angle)), 10, 20);
			
			//궤적 그리기
			g.setColor(new Color(150, 150, 150, 100));  //반투명 회색
			for(int i=1; i<trail.size(); i++) {
				Point p1 = trail.get(i - 1);
				Point p2 = trail.get(i);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
			trail.add(new Point(bobX, bobY));
			if(trail.size() > 300) trail.remove(0);
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			int dx = e.getX() - originX;
			int dy = e.getY() - originY;
			angle = Math.atan2(dx, dy);  //초기 각도 마우스로 설정
			angleVelocity = 0;
		}
		@Override public void mousePressed(MouseEvent e) {}
		@Override public void mouseReleased(MouseEvent e) {}
		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseExited(MouseEvent e) {}
	}
	
	//DashboardPanel 클래스
	class DashboardPanel extends JPanel {
		private AngularVelocityGraphPanel velocityGraph = new AngularVelocityGraphPanel();
		private EnergyGraphPanel energyGraph = new EnergyGraphPanel();
		private StatusColorBarPanel statusBar = new StatusColorBarPanel();
		private MiniEnergyGraphPanel miniGraph = new MiniEnergyGraphPanel();
		
		private JLabel efficiencyLabel = new JLabel("에너지 보존율: ");
		private JLabel frequencyLabel = new JLabel("진동수: ");
		private JButton saveButton = new JButton("CSV 저장");
		private JButton velocityColorBtn = new JButton("속도 색상 변경");
		private JButton energyColorBtn = new JButton("에너지 색상 변경");
		
		private List<String> csvLog = new ArrayList<>();
		
		public MiniEnergyGraphPanel getMiniGraph() { return miniGraph; }
		
		public DashboardPanel() {
			setLayout(new BorderLayout());
			
			//상단 요약 정보
			JPanel top = new JPanel(new GridLayout(1, 2));
			top.setBackground(new Color(50, 70, 60));
			top.add(efficiencyLabel);
			top.add(frequencyLabel);
			
			//가운데 그래프 패널
			JPanel center = new JPanel(new GridLayout(3, 1));
			center.add(wrapWithBorder("각속도 그래프", velocityGraph, Color.RED));
			center.add(wrapWithBorder("에너지 그래프", energyGraph, Color.GREEN));
			center.add(wrapWithBorder("상태 시각화 바", statusBar, Color.CYAN));
			
			//하단 버튼 바
			JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			buttonBar.setBackground(new Color(50, 70, 60));
			buttonBar.add(saveButton);
			buttonBar.add(velocityColorBtn);
			buttonBar.add(energyColorBtn);
			
			JButton themeToggleBtn = new JButton("테마 전환");
			buttonBar.add(Box.createHorizontalStrut(10));
			buttonBar.add(themeToggleBtn);
			
			//하단 통합 래퍼
			JPanel bottomWrap = new JPanel(new BorderLayout());
			bottomWrap.setBackground(new Color(50, 70, 60));
			bottomWrap.add(miniGraph, BorderLayout.CENTER);
			bottomWrap.add(buttonBar, BorderLayout.SOUTH);
			miniGraph.setPreferredSize(new Dimension(400, 50));
			
			//이벤트 리스너들
			themeToggleBtn.addActionListener(e -> toggleTheme());
			
			velocityColorBtn.addActionListener(e -> {
				Color newColor = JColorChooser.showDialog(this, "속도 그래프 색상 선택", Color.MAGENTA);
				if(newColor != null) velocityGraph.setGraphColor(newColor);
			});
			
			energyColorBtn.addActionListener(e -> {
				Color newColor = JColorChooser.showDialog(this, "총 에너지 색상 선택", Color.BLACK);
				if(newColor != null) energyGraph.setTEColor(newColor);
			});
			
			saveButton.addActionListener(e -> saveCSV());
			
			//구성 요소 배치
			add(top, BorderLayout.NORTH);
			add(center, BorderLayout.CENTER);
			add(bottomWrap, BorderLayout.SOUTH);
		}
		
		//그래프 + 제목 박스를 감싸는 보더 생성
		private JPanel wrapWithBorder(String title, JComponent comp, Color borderColor) {
			JPanel panel = new JPanel(new BorderLayout());
			panel.setBackground(new Color(50, 70, 60));
			
			TitledBorder border = BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(borderColor, 2), title);
			border.setTitleColor(Color.WHITE);
			panel.setBorder(border);;
			
			comp.setBackground(new Color(50, 70, 60));
			comp.setForeground(Color.WHITE);
			if(comp instanceof JPanel) { ((JPanel) comp).setOpaque(true); }
			
			panel.add(comp, BorderLayout.CENTER);
			return panel;
		}
		
		//시뮬레이션 결과를 CSV 파일로 저장
		public void saveCSV() {
			try (PrintWriter pw = new PrintWriter(new File("simulation_data.csv"))){
				pw.println("AngularVelocity,KE,PE,TE");
				for (String line : csvLog) pw.println(line);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//데이터를 수집하여 시각화 요소에 전달
		public void addData(double velocity, double ke, double pe, double te, double efficiency, double freq) {
			velocityGraph.addValue(velocity);
			energyGraph.addEnergy(ke, pe, te);
			statusBar.setStatusValue(velocity);
			
			efficiencyLabel.setText(String.format("에너지 보존율: %.2f%%", efficiency * 100));
			frequencyLabel.setText(String.format("진동수: %.2f Hz", freq));
			csvLog.add(String.format(Locale.US, "%.4f, %.4f, %.4f, %.4f", velocity, ke, pe, te));
			
			energySummaryLabel.setText(String.format("KE: %.2f | PE: %.2f | TE: %.2f", ke, pe, te));
			frequencySummaryLabel.setText(String.format("진동수: %.2fHz | 주기: %.2fs", freq, 1.0 / freq));
			stateSummaryLabel.setText(String.format("각속도: %.3f rad/s", velocity));
		}
		
		//테마 전환 순환
		private void toggleTheme() {
			if(currentTheme == classicTheme) currentTheme = darkTheme;
			else if(currentTheme == darkTheme) currentTheme = medicalTheme;
			else if(currentTheme == medicalTheme) currentTheme = highContrastTheme;
			else if(currentTheme == highContrastTheme) currentTheme = originalTheme;
			else currentTheme = classicTheme;
			applyTheme(currentTheme);
		}
		
		//테마 적용 메서드 (배경/텍스트/그래프 색상 변경)
		public void applyTheme(Theme theme) {
			setBackground(theme.background);
			efficiencyLabel.setForeground(theme.text);
			frequencyLabel.setForeground(theme.text);
			velocityGraph.setBackground(theme.background);
			energyGraph.setBackground(theme.background);
			miniGraph.setBackground(theme.background);
			statusBar.setBackground(theme.background);
			saveButton.setBackground(theme.background);
			saveButton.setForeground(theme.text);
			velocityColorBtn.setBackground(theme.background);
			velocityColorBtn.setForeground(theme.text);
			energyColorBtn.setBackground(theme.background);
			energyColorBtn.setForeground(theme.text);
			energyGraph.setTEColor(theme.teColor);
		}
	}
	
	//AngularVelocityGraphPanel 클래스-각속도 변화 그래프
	class AngularVelocityGraphPanel extends JPanel {
		private List<Double> values = new ArrayList<>();
		private Color graphColor = Color.MAGENTA;
		
		public void setGraphColor(Color c) { graphColor = c; }
		
		public void addValue(double value) {
			values.add(value);
			if(values.size() > getWidth()) values.remove(0);
			repaint();  //값 추가 후 다시 그리기
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int w = getWidth();
			int h = getHeight();
			if(values.isEmpty()) return;
			
			//정규화용 최대 절댓값
			double maxVal = values.stream()
					.mapToDouble(v -> Math.abs(v))
					.max()
					.orElse(1.0);
			
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(0, h / 2, w, h / 2);  //중앙 기준선
			
			g.setColor(graphColor);
			for(int i=1; i<values.size(); i++) {
				int x1 = i - 1, x2 = i;
				int y1 = h / 2 - (int)((values.get(i - 1) / maxVal) * (h / 2 - 5));
				int y2 = h / 2 - (int)((values.get(i) / maxVal) * (h / 2 - 5));
				g.drawLine(x1, y1, x2, y2);
			}
			g.setColor(Color.WHITE);
			g.drawString("각속도(rad/s)", 5, 15);  //그래프 제목
		}
	}
	
	//EnergyGraphPanel 클래스-KE/PE/TE 에너지 변화
	class EnergyGraphPanel extends JPanel {
		private List<Double> keList = new ArrayList<>();
		private List<Double> peList = new ArrayList<>();
		private List<Double> teList = new ArrayList<>();
		private Color teColor = Color.BLACK;
		
		public void setTEColor(Color c) { teColor = c; repaint();}
		
		public void addEnergy(double ke, double pe, double te) {
			keList.add(ke); peList.add(pe); teList.add(te);
			if(keList.size() > getWidth()) {
				keList.remove(0); peList.remove(0); teList.remove(0);
			}
			repaint();
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int h = getHeight();
			int w = getWidth();
			if(teList.isEmpty()) return;
			
			double maxTE = teList.stream().max(Double::compare).orElse(1.0);
			
			for(int i=1; i<keList.size(); i++) {
				int x1 = i - 1, x2 = i;
				
				//KE 파랑
				int yKE1 = h - (int)((keList.get(i - 1) / maxTE) * (h - 5) * 3.0);
				int yKE2 = h - (int)((keList.get(i) / maxTE) * (h - 5) * 3.0);
				g.setColor(Color.BLUE);
				g.drawLine(x1, yKE1, x2, yKE2);
				
				//PE 빨강
				int yPE1 = h - (int)((keList.get(i - 1) / maxTE) * (h - 5) * 2.0);
				int yPE2 = h - (int)((keList.get(i) / maxTE) * (h - 5) * 2.0);
				g.setColor(Color.RED);
				g.drawLine(x1, yPE1, x2, yPE2);
				
				//TE 사용자 색
				int yTE1 = h - (int)((keList.get(i - 1) / maxTE) * (h - 5));
				int yTE2 = h - (int)((keList.get(i) / maxTE) * (h - 5));
				g.setColor(teColor);
				g.drawLine(x1, yTE1, x2, yTE2);
			}
			
			g.setColor(Color.WHITE);
			g.drawString("에너지 변환(KE, PE, TE)", 5, 15);
		}
	}
	
	//StatusColorBarPanel 클래스-속도 상태 시각화
	class StatusColorBarPanel extends JPanel {
		private double statusValue = 0;
		
		public void setStatusValue(double value) {
			this.statusValue = value;
			repaint();
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int barWidth = (int)(Math.abs(statusValue) * 200);
			
			//색상: 양수면 초록, 음수면 빨강
			g.setColor(statusValue > 0 ? Color.GREEN : Color.RED);
			g.fillRect(0, getHeight()/2 - 10, barWidth, 20);
			
			g.setColor(Color.BLACK);
			g.drawRect(0, getHeight()/2 - 10, getWidth() - 1, 20);
			
			g.setColor(Color.WHITE);
			g.drawString("현재 속도 상태 바", 5, 15);
		}
	}
	
	//MiniEnergyGraphPanel 클래스-총 에너지 미니 그래프
	class MiniEnergyGraphPanel extends JPanel{
		private java.util.List<Double> teList = new ArrayList<>();
		
		public void addTotalEnergy(double te) {
			teList.add(te);
			if(teList.size() > getWidth()) teList.remove(0);
			repaint();
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(teList.isEmpty()) return;
			
			int h = getHeight();
			double maxVal = teList.stream().max(Double::compare).orElse(1.0);
			
			g.setColor(Color.GREEN);
			for(int i=1; i<teList.size(); i++) {
				int x1 = i - 1, x2 = i;
				int y1 = h - (int)((teList.get(i - 1) / maxVal) * (h - 5));
				int y2 = h - (int)((teList.get(i) / maxVal) * (h - 5));
				g.drawLine(x1, y1, x2, y2);
			}
		}
	}
	
	class Theme {
		public Color background;	//전체 배경 색
		public Color text;			//기본 텍스트 색
		public Color titleBorder;	//테두리 및 제목 색
		public Color keColor;		//운동 에너지 색
		public Color peColor;		//위치 에너지 색
		public Color teColor;		//총 에너지 색
		
		public Theme(Color bg, Color text, Color title, Color ke, Color pe, Color te) {
			this.background = bg;
			this.text = text;
			this.titleBorder = title;
			this.keColor = ke;
			this.peColor = pe;
			this.teColor = te;
		}
	}
}
