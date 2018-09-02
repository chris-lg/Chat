package com.sevele.ds.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.sevele.ds.activity.DecryptgameActivity;
import com.sevele.ds.utils.LogUtil;

/**
 * @author Maozhiqi
 * @time:2015年4月26日
 * @descrption:游戏视图组件
 * 
 */
public class GameView extends LinearLayout {
	private final int int_LINES = 3;// 设置宫格长度
	private Card[][] m_CardsMap = new Card[int_LINES][int_LINES];// 3X3宫格
	private List<Point> m_EmptyPoints = new ArrayList<Point>();

	private DecryptgameActivity.isComplete m_Signal;// 游戏完成信号

	private int int_Gamerank;// 解密游戏难度

	private DecryptgameActivity.Score m_Score;// 分数

	public GameView(Context context) {
		super(context);
		initGameView();// 初始化游戏界面
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGameView();
	}

	/**
	 * 初始化游戏界面
	 */
	private void initGameView() {
		setOrientation(LinearLayout.VERTICAL);
		setBackgroundColor(Color.WHITE);
		setOnTouchListener(new View.OnTouchListener() {
			private float startX, startY, offsetX, offsetY;// 定义手势操作偏移

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					offsetX = event.getX() - startX;
					offsetY = event.getY() - startY;
					if (Math.abs(offsetX) > Math.abs(offsetY)) {
						if (offsetX < -5) {
							swipeLeft();
						} else if (offsetX > 5) {
							swipeRight();
						}
					} else {
						if (offsetY < -5) {
							swipeUp();
						} else if (offsetY > 5) {
							swipeDown();
						}
					}
					break;
				}
				return true;
			}
		});
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldWidth,
			int oldHeight) {
		super.onSizeChanged(width, height, oldWidth, oldHeight);
		Card.int_Width = (Math.min(width, height) - 10) / int_LINES;
		addCards();
		startGame();
	}

	/**
	 * 添加cards
	 */
	private void addCards() {
		Card c;
		LinearLayout line;
		LinearLayout.LayoutParams lineLp;
		for (int y = 0; y < int_LINES; y++) {
			line = new LinearLayout(getContext());
			lineLp = new LinearLayout.LayoutParams(-1, Card.int_Width);
			addView(line, lineLp);
			for (int x = 0; x < int_LINES; x++) {
				c = new Card(getContext());
				line.addView(c, Card.int_Width, Card.int_Width);
				m_CardsMap[x][y] = c;
			}
		}
	}

	public void startGame() {
		for (int y = 0; y < int_LINES; y++) {
			for (int x = 0; x < int_LINES; x++) {
				m_CardsMap[x][y].setNum(0);
			}
		}
		addRandomNum(1);
	}

	/**
	 * 在card上添加数字
	 * 
	 * @param num
	 */
	private void addRandomNum(int num) {
		checkemptyPoints();
		if (m_EmptyPoints.size() > 0) {

			Point p = m_EmptyPoints.remove((int) (Math.random() * m_EmptyPoints
					.size()));
			m_CardsMap[p.x][p.y].setNum((int) (1 + Math.random()
					* (num - 1 + 1)));
			m_CardsMap[p.x][p.y].addScaleAnimation();
		}
	}

	/**
	 * 向左滑操作事件
	 */
	private void swipeLeft() {

		boolean merge = false;

		for (int y = 0; y < int_LINES; y++) {
			for (int x = 0; x < int_LINES; x++) {

				for (int x1 = x + 1; x1 < int_LINES; x1++) {
					if (m_CardsMap[x1][y].getNum() > 0) {

						if (m_CardsMap[x][y].getNum() <= 0) {
							m_CardsMap[x][y].setNum(m_CardsMap[x1][y].getNum());
							m_CardsMap[x1][y].setNum(0);

							x--;
							merge = true;

						} else if (m_CardsMap[x][y].equals(m_CardsMap[x1][y])) {

							m_CardsMap[x][y]
									.setNum(m_CardsMap[x][y].getNum() + 1);
							m_CardsMap[x1][y].setNum(0);
							m_Score.addScore(m_CardsMap[x][y].getNum());
							merge = true;
						}

						break;
					}
				}
			}
		}

		if (merge) {
			addRandomNum(1);
			checkComplete();
		}
	}

	/**
	 * 向右滑操作事件
	 */
	private void swipeRight() {

		boolean merge = false;

		for (int y = 0; y < int_LINES; y++) {
			for (int x = int_LINES - 1; x >= 0; x--) {
				for (int x1 = x - 1; x1 >= 0; x1--) {
					if (m_CardsMap[x1][y].getNum() > 0) {
						if (m_CardsMap[x][y].getNum() <= 0) {
							m_CardsMap[x][y].setNum(m_CardsMap[x1][y].getNum());
							m_CardsMap[x1][y].setNum(0);
							x++;
							merge = true;
						} else if (m_CardsMap[x][y].equals(m_CardsMap[x1][y])) {
							m_CardsMap[x][y]
									.setNum(m_CardsMap[x][y].getNum() + 1);
							m_CardsMap[x1][y].setNum(0);
							m_Score.addScore(m_CardsMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}
			}
		}

		if (merge) {
			addRandomNum(1);
			checkComplete();
		}
	}

	/**
	 * 向上滑操作事件
	 */
	private void swipeUp() {

		boolean merge = false;

		for (int x = 0; x < int_LINES; x++) {
			for (int y = 0; y < int_LINES; y++) {
				for (int y1 = y + 1; y1 < int_LINES; y1++) {
					if (m_CardsMap[x][y1].getNum() > 0) {
						if (m_CardsMap[x][y].getNum() <= 0) {
							m_CardsMap[x][y].setNum(m_CardsMap[x][y1].getNum());
							m_CardsMap[x][y1].setNum(0);
							y--;
							merge = true;
						} else if (m_CardsMap[x][y].equals(m_CardsMap[x][y1])) {
							m_CardsMap[x][y]
									.setNum(m_CardsMap[x][y].getNum() + 1);
							m_CardsMap[x][y1].setNum(0);
							m_Score.addScore(m_CardsMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}
			}
		}

		if (merge) {
			addRandomNum(1);
			checkComplete();
		}
	}

	/**
	 * 向下滑操作事件
	 */
	private void swipeDown() {
		boolean merge = false;
		for (int x = 0; x < int_LINES; x++) {
			for (int y = int_LINES - 1; y >= 0; y--) {
				for (int y1 = y - 1; y1 >= 0; y1--) {
					if (m_CardsMap[x][y1].getNum() > 0) {
						if (m_CardsMap[x][y].getNum() <= 0) {
							m_CardsMap[x][y].setNum(m_CardsMap[x][y1].getNum());
							m_CardsMap[x][y1].setNum(0);
							y++;
							merge = true;
						} else if (m_CardsMap[x][y].equals(m_CardsMap[x][y1])) {
							m_CardsMap[x][y]
									.setNum(m_CardsMap[x][y].getNum() + 1);
							m_CardsMap[x][y1].setNum(0);
							m_Score.addScore(m_CardsMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}
			}
		}

		if (merge) {
			addRandomNum(1);
			checkComplete();
		}
	}

	/**
	 * 检查游戏完成情况
	 */
	public void checkComplete() {
		int complete = 0;
		ALL: for (int y = 0; y < int_LINES; y++) {
			for (int x = 0; x < int_LINES; x++) {
				if (m_CardsMap[x][y].getNum() == int_Gamerank) {
					complete = 1;
					break ALL;
				} else if (m_CardsMap[x][y].getNum() == 0
						|| (x > 0 && m_CardsMap[x][y]
								.equals(m_CardsMap[x - 1][y]))
						|| (x < int_LINES - 1 && m_CardsMap[x][y]
								.equals(m_CardsMap[x + 1][y]))
						|| (y > 0 && m_CardsMap[x][y]
								.equals(m_CardsMap[x][y - 1]))
						|| (y < int_LINES - 1 && m_CardsMap[x][y]
								.equals(m_CardsMap[x][y + 1]))) {
					complete = 2;
				}
			}
		}
		if (complete == 1) {
			m_Signal.Complete(1);
		} else if (complete == 0) {
			m_Signal.Complete(2);
		}
	}

	/**
	 * 检查是否有空格子
	 */
	private void checkemptyPoints() {
		m_EmptyPoints.clear();
		for (int y = 0; y < int_LINES; y++) {
			for (int x = 0; x < int_LINES; x++) {
				if (m_CardsMap[x][y].getNum() <= 0) {
					m_EmptyPoints.add(new Point(x, y));
				}
			}
		}
	}

	public void setComplete(DecryptgameActivity.isComplete signal) {
		this.m_Signal = signal;
	}

	public void setGameRank(int gamerank) {
		this.int_Gamerank = gamerank;
		LogUtil.LogTest("2游戏难度" + gamerank);
	}

	public void setScore(DecryptgameActivity.Score score) {
		this.m_Score = score;
	}
}
