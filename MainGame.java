package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MainGame extends JPanel implements Runnable,MouseMotionListener {//MML>>マウスの動き判定
      
	    public static final int NUM_BROW = 4;// ブロックの行数
	    public static final int NUM_BCOL = 6;// ブロックの列数
	    public static final int NBL = NUM_BROW * NUM_BCOL; // ブロック数
	    
	    private Racket racket; // ラケットバー
	    private Block0[] block0; //ブロック
	    private Block[] block; 
	    private Block2[] block2;
	    private Ball[] ball; //ボール
	    public static final int WIDTH = 500;// 横幅
	    public static final int HEIGHT = 650;// 縦幅
	    Random rand = new Random();
	    public static int nu;
	    //ボールが増えるとボールごとのブロックとの衝突判定などのループ範囲を拡大させる必要があるためその変数としてs
	    private int s = 0;

	    private Thread loop;
	    public MainGame(){
	        setPreferredSize(new Dimension(WIDTH,HEIGHT));//Width,Heightでサイズを設定
	        addMouseMotionListener(this);//マウスの動きを観察
	        racket = new Racket();
	        block0 = new Block0[NUM_BCOL];//1列だけでOKなのでNUM_BCOL
	        block = new Block[NBL];
	        block2 = new Block2[NBL];
	        ball = new Ball[4];
	        ball[0] = new Ball(rand.nextInt(WIDTH - Ball.SIZE),300,5,7); //まずは普通に一個ボール作る  
	        nu = 0;//ラケットにあたった回数を数えるためのnu
	       //ブロック0を   
	        for (int j = 0; j < NUM_BCOL; j++) {//一列表示
	                int x = j * Block.WIDTH + (WIDTH-Block.WIDTH*NUM_BCOL)/2;
	                int y = Block.HEIGHT*NUM_BROW * 2 + 30 + Block0.HEIGHT;
	                block0[j] = new Block0(x, y);//drawメソッド呼び出しで描画
	            }
	        // ブロック1を並べる
	        for (int i = 0; i < NUM_BROW; i++) {
	            for (int j = 0; j < NUM_BCOL; j++) {
	                int x = j * Block.WIDTH + (WIDTH-Block.WIDTH*NUM_BCOL)/2;
	                int y = i * Block.HEIGHT +Block.HEIGHT*NUM_BROW+30;
	                block[i * NUM_BCOL + j] = new Block(x, y);
	            }
	        }
	        //ブロック２を並べる
	        for (int i = 0; i < NUM_BROW; i++) {
	            for (int j = 0; j < NUM_BCOL; j++) {
	                int x = j * Block.WIDTH + (WIDTH-Block.WIDTH*NUM_BCOL)/2;
	                int y = i * Block.HEIGHT;
	                block2[i * NUM_BCOL + j] = new Block2(x, y);
	            }
	        }
	       loop = new Thread(this);
	       loop.start();//非同期で実行？
	    }
	    
	    int xp;
	    // マウスを動かしたとき
	    public void mouseMoved(MouseEvent e) {
	        xp = e.getX(); // マウスのX座標を取得
	        racket.move(xp); // ラケットをマウスから得た座標に合わせて移動
	        repaint();//これで実際に画面へ反映
	    }  
	    
	    
        private boolean gameover = false; //ボールが画面外に行ったときにループを止める。これがFalseならループは回る
	    public void run() {
	        while (!gameover) {
	        	for(int j=0;j<=s;j++) {//今あるボールについて捜査
	        	//画面外に言ってるか判断。行ってたらスコア表示とretryの案内
	        	if(ball[j].posX() == WIDTH + 10) {
	        	   JFrame frame = new JFrame();
	        	   frame.setBounds(0,100,WIDTH,300);
	        	   JLabel labels = new JLabel("       Score:"+String.valueOf(Block.getScore())+"   "); //スコア表示
	        	   JLabel labelA = new JLabel("                Great!!      ");
	        	   JLabel labelB = new JLabel("                 Nice!        ");
	        	   JLabel labelC = new JLabel("                Normal       ");
	        	  // JLabel labelD = new JLabel("　　　　　　　　　　　　　");
	        	 //  JLabel labelE = new JLabel("　　　　　　　　　　　　　");
	        	   JPanel panel = new JPanel(new BorderLayout());
	        	   
	        	   JButton btn = new JButton("Retry?");
	               labels.setFont(new Font("Arial",Font.PLAIN,60));
	               panel.add(labels,BorderLayout.PAGE_START);
	               panel.add(btn,BorderLayout.PAGE_END);
	               labelA.setFont(new Font("Arial",Font.PLAIN,40));
	               labelB.setFont(new Font("Arial",Font.PLAIN,40));
	               labelC.setFont(new Font("Arial",Font.PLAIN,40));
	              // panel.add(labelD,BorderLayout.LINE_END);
	               //panel.add(labelE,BorderLayout.LINE_START);
	               //スコアによってコメント表示
	               if(Block.getScore() >500) {
	               panel.add(labelA,BorderLayout.CENTER);
	               }
	               if(Block.getScore() <=500 && Block.getScore() > 250) {
		               panel.add(labelB,BorderLayout.CENTER);
		               }
	               if(Block.getScore() <=250 && Block.getScore() >=0) {
		               panel.add(labelC,BorderLayout.CENTER);
		               }
	               Container contentPane = frame.getContentPane();	  
	           	   contentPane.add(panel);
	           	   frame.setVisible(true);
	           	   //retryボタン押されたらもう一度新規ゲーム開始呼び出し
	           	   btn.addActionListener(new ActionListener() {
	    			public void actionPerformed(ActionEvent e) {
	    		        Mainclass x = new Mainclass();
	    			}
	    		});
	           	   gameover = true;//元のゲームのこれでループを止めてやる
	        		}
	        	}
	        	
	        	//以下ゲーム内での通常動作
	        	for(int j=0;j<=s;j++) {
	            ball[j].move();
	            //ラケットにぶつかったら
	            if (racket.HitR(ball[j])) {
	                nu += 1;
	                System.out.println("nu:"+nu);
	                if( xp - racket.R_WID/3 <= ball[j].posX() && ball[j].posX() <= xp + racket.R_WID/3) {//中央付近に当たれば
	                ball[j].boundY();
	                }
	                else if(xp - racket.R_WID/2 +Ball.SIZE >= ball[j].posX() || ball[j].posX() <= xp + racket.R_WID/2) {//上以外でラケット内に当たれば
	                ball[j].boundYa();
	                }
	                
	                //難易度向上のためラケットにぶつかった回数nuが一定回数になるとボール追加
	              if(nu==2) {
	                ball[1] = new Ball(rand.nextInt(WIDTH - Ball.SIZE),300,-6,4);
	           	    s=s+1;
	               }
	              if(nu==4) {
	    	      	ball[2] = new Ball(rand.nextInt(WIDTH - Ball.SIZE),300,11,5);
	              	s=s+1;
	              }
	              if(nu==6){
	       	       ball[3] = new Ball(rand.nextInt(WIDTH - Ball.SIZE),300,9,10);
	               s=s+1;
	              }
	              System.out.println("s"+s);
	            }
	            }
	            
	        	//以下はすべてのブロック0に対し消えてるか、どう当たっているか判断
	        	  for (int i = 0; i < NUM_BCOL; i++) {//i all block
		            	for(int j=0;j<=s;j++) {//j: all ball
		                // すでに消えているブロックは無視
		                if (block0[i].getDestroyed())
		                    continue;
		                // ブロックの当たった位置を計算
		                int collidePos = block0[i].HitB(ball[j]);
		                // ブロックに当たっていたら
		                if (collidePos != Block0.NO_COLLISION) {
		                    block0[i].delete0();
		                    // ボールの当たった位置からボールの反射方向を計算
		                    switch (collidePos) {
		                        case Block0.DOWN :
		                        	ball[j].boundY();
		                        	break;
		                        case Block0.UP :
		                            ball[j].boundY();
		                            break;//ブロックの上か下に当たり判定でy方向の速度変化
		                        case Block0.LEFT :
		                        	ball[j].boundX();
		                        	break;
		                        case Block0.RIGHT :
		                            ball[j].boundX();//左右に当たり判定でx方向に速度変化
		                            break;
		                        case Block0.UP_LEFT :
		                        case Block0.UP_RIGHT :
		                        case Block0.DOWN_LEFT :
		                        case Block0.DOWN_RIGHT :
		                            ball[j].boundY();
		                            break;
		                    }
		                   // System.out.println("score:"+String.valueOf(Block.getScore()));
		                    break; // 1回に壊せるブロックは1つ
		                }
		            }
		            }	//以下はすべてのブロック0に対し消えてるか、どう当たっているか判断
	        	  for (int i = 0; i < NUM_BCOL; i++) {//i all block
		            	for(int j=0;j<=s;j++) {//j: all ball
		                // すでに消えているブロックは無視
		                if (block0[i].getDestroyed())
		                    continue;
		                // ブロックの当たった位置を計算
		                int collidePos = block0[i].HitB(ball[j]);
		                // ブロックに当たっていたら
		                if (collidePos != Block0.NO_COLLISION) {
		                    block0[i].delete0();
		                    // ボールの当たった位置からボールの反射方向を計算
		                    switch (collidePos) {
		                        case Block0.DOWN :
		                        	ball[j].boundY();
		                        	break;
		                        case Block0.UP :
		                            ball[j].boundY();
		                            break;//ブロックの上か下に当たり判定でy方向の速度変化
		                        case Block0.LEFT :
		                        	ball[j].boundX();
		                        	break;
		                        case Block0.RIGHT :
		                            ball[j].boundX();//左右に当たり判定でx方向に速度変化
		                            break;
		                        case Block0.UP_LEFT :
		                        case Block0.UP_RIGHT :
		                        case Block0.DOWN_LEFT :
		                        case Block0.DOWN_RIGHT :
		                            ball[j].boundY();
		                            break;
		                    }
		                   // System.out.println("score:"+String.valueOf(Block.getScore()));
		                    break; // 1回に壊せるブロックは1つ
		                }
		            }
		            }
		            repaint();//描きなおし
		            try {
		                Thread.sleep(20);//例外が発生？↓
		            } catch (InterruptedException e) {//捕まえる変数
		                e.printStackTrace();//例外発生時の処理
		            }
		            
		            
	         // 上と同様にブロック1に対して
	            for (int i = 0; i < NBL; i++) {
	            	for(int j=0;j<=s;j++) {
	                if (block[i].getDestroyed())
	                    continue;
	                int collidePos = block[i].HitB(ball[j]);
	                if (collidePos != Block.NO_COLLISION) {
	                    block[i].delete();
	                    switch (collidePos) {
	                        case Block.DOWN :
	                        	ball[j].boundY();
	                        	break;
	                        case Block.UP :
	                            ball[j].boundY();
	                            break;
	                        case Block.LEFT :
	                        	ball[j].boundX();
	                        	break;
	                        case Block.RIGHT :
	                            ball[j].boundX();
	                            break;
	                        case Block.UP_LEFT :
	                        case Block.UP_RIGHT :
	                        case Block.DOWN_LEFT :
	                        case Block.DOWN_RIGHT :
	                            ball[j].boundY();
	                            break;
	                    }
	                    //System.out.println("score:"+String.valueOf(Block.getScore()));
	                    break; 
	                }
	            }
	            }
	            repaint();
	            try {
	                Thread.sleep(20);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            
		         // ブロック2
	            for (int i = 0; i < NBL; i++) {
	            	for(int j=0;j<=s;j++) {
	                if (block2[i].getDestroyed())
	                    continue;
	                int collidePos = block2[i].HitB(ball[j]);
	                if (collidePos != Block2.NO_COLLISION) {
	                    block2[i].delete2();
	                    switch (collidePos) {
	                        case Block2.DOWN :
	                        	ball[j].boundY();
	                        	break;
	                        case Block2.UP :
	                            ball[j].boundY();
	                            break;
	                        case Block2.LEFT :
	                        	ball[j].boundX();
	                        	break;
	                        case Block2.RIGHT :
	                            ball[j].boundX();
	                            break;
	                        case Block2.UP_LEFT :
	                        case Block2.UP_RIGHT :
	                        case Block2.DOWN_LEFT :
	                        case Block2.DOWN_RIGHT :
	                            ball[j].boundY();
	                            break;
	                    }
	                   // System.out.println("score:"+Block.getScore());
	                    break; 
	                }
	            	}
	            }
	            repaint();
	            try {
	                Thread.sleep(20);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    
	   //全体的な部品の表示
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);	        // 背景
	        g.setColor(Color.white);
	        g.fillRect(0, 0, WIDTH, HEIGHT);
	        // ラケット
	        racket.draw(g); 
	        // ボール
	        for(int i=0;i<=s;i++) { 
	        ball[i].draw(g); 
	        }
	        // ブロック
	        for (int i = 0; i < NUM_BCOL; i++) {
	            if (!block0[i].getDestroyed()) {//消されていなければ
	                block0[i].draw(g);
	            }
	        }
	        //以下も同様だが、あと何回当てれば消えるか分かるようにまだ消えなくても当たれば色を変えていくため条件を付ける
	        for (int i = 0; i < NBL; i++) {
	            if (!block[i].getDestroyed()) {//消されていなければ
	            	if(block[i].getstate() == block[i].cnt-1) {
	                block[i].drawlast(g);
	            	}
	            	else {
	                block[i].draw(g);
	            	}
	            }
	        }
	        for (int i = 0; i < NBL; i++) {
	            if (!block2[i].getDestroyed()) {//消されていなければ
	            	if(block2[i].getstate() == block2[i].cnt-1) {
	            		block2[i].drawlast(g);
	            	}
	            	else if(block2[i].getstate() == block2[i].cnt-2) {
	            		block2[i].drawmid(g);
	            	}
	            	else {
	                block2[i].draw(g);
	            	}
	            }
	        }
	    }
	    
	    public void mouseDragged(MouseEvent e) {
	    }
}
