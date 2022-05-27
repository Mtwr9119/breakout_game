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


public class MainGame extends JPanel implements Runnable,MouseMotionListener {//MML>>�}�E�X�̓�������
      
	    public static final int NUM_BROW = 4;// �u���b�N�̍s��
	    public static final int NUM_BCOL = 6;// �u���b�N�̗�
	    public static final int NBL = NUM_BROW * NUM_BCOL; // �u���b�N��
	    
	    private Racket racket; // ���P�b�g�o�[
	    private Block0[] block0; //�u���b�N
	    private Block[] block; 
	    private Block2[] block2;
	    private Ball[] ball; //�{�[��
	    public static final int WIDTH = 500;// ����
	    public static final int HEIGHT = 650;// �c��
	    Random rand = new Random();
	    public static int nu;
	    //�{�[����������ƃ{�[�����Ƃ̃u���b�N�Ƃ̏Փ˔���Ȃǂ̃��[�v�͈͂��g�傳����K�v�����邽�߂��̕ϐ��Ƃ���s
	    private int s = 0;

	    private Thread loop;
	    public MainGame(){
	        setPreferredSize(new Dimension(WIDTH,HEIGHT));//Width,Height�ŃT�C�Y��ݒ�
	        addMouseMotionListener(this);//�}�E�X�̓������ώ@
	        racket = new Racket();
	        block0 = new Block0[NUM_BCOL];//1�񂾂���OK�Ȃ̂�NUM_BCOL
	        block = new Block[NBL];
	        block2 = new Block2[NBL];
	        ball = new Ball[4];
	        ball[0] = new Ball(rand.nextInt(WIDTH - Ball.SIZE),300,5,7); //�܂��͕��ʂɈ�{�[�����  
	        nu = 0;//���P�b�g�ɂ��������񐔂𐔂��邽�߂�nu
	       //�u���b�N0��   
	        for (int j = 0; j < NUM_BCOL; j++) {//���\��
	                int x = j * Block.WIDTH + (WIDTH-Block.WIDTH*NUM_BCOL)/2;
	                int y = Block.HEIGHT*NUM_BROW * 2 + 30 + Block0.HEIGHT;
	                block0[j] = new Block0(x, y);//draw���\�b�h�Ăяo���ŕ`��
	            }
	        // �u���b�N1����ׂ�
	        for (int i = 0; i < NUM_BROW; i++) {
	            for (int j = 0; j < NUM_BCOL; j++) {
	                int x = j * Block.WIDTH + (WIDTH-Block.WIDTH*NUM_BCOL)/2;
	                int y = i * Block.HEIGHT +Block.HEIGHT*NUM_BROW+30;
	                block[i * NUM_BCOL + j] = new Block(x, y);
	            }
	        }
	        //�u���b�N�Q����ׂ�
	        for (int i = 0; i < NUM_BROW; i++) {
	            for (int j = 0; j < NUM_BCOL; j++) {
	                int x = j * Block.WIDTH + (WIDTH-Block.WIDTH*NUM_BCOL)/2;
	                int y = i * Block.HEIGHT;
	                block2[i * NUM_BCOL + j] = new Block2(x, y);
	            }
	        }
	       loop = new Thread(this);
	       loop.start();//�񓯊��Ŏ��s�H
	    }
	    
	    int xp;
	    // �}�E�X�𓮂������Ƃ�
	    public void mouseMoved(MouseEvent e) {
	        xp = e.getX(); // �}�E�X��X���W���擾
	        racket.move(xp); // ���P�b�g���}�E�X���瓾�����W�ɍ��킹�Ĉړ�
	        repaint();//����Ŏ��ۂɉ�ʂ֔��f
	    }  
	    
	    
        private boolean gameover = false; //�{�[������ʊO�ɍs�����Ƃ��Ƀ��[�v���~�߂�B���ꂪFalse�Ȃ烋�[�v�͉��
	    public void run() {
	        while (!gameover) {
	        	for(int j=0;j<=s;j++) {//������{�[���ɂ��đ{��
	        	//��ʊO�Ɍ����Ă邩���f�B�s���Ă���X�R�A�\����retry�̈ē�
	        	if(ball[j].posX() == WIDTH + 10) {
	        	   JFrame frame = new JFrame();
	        	   frame.setBounds(0,100,WIDTH,300);
	        	   JLabel labels = new JLabel("       Score:"+String.valueOf(Block.getScore())+"   "); //�X�R�A�\��
	        	   JLabel labelA = new JLabel("                Great!!      ");
	        	   JLabel labelB = new JLabel("                 Nice!        ");
	        	   JLabel labelC = new JLabel("                Normal       ");
	        	  // JLabel labelD = new JLabel("�@�@�@�@�@�@�@�@�@�@�@�@�@");
	        	 //  JLabel labelE = new JLabel("�@�@�@�@�@�@�@�@�@�@�@�@�@");
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
	               //�X�R�A�ɂ���ăR�����g�\��
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
	           	   //retry�{�^�������ꂽ�������x�V�K�Q�[���J�n�Ăяo��
	           	   btn.addActionListener(new ActionListener() {
	    			public void actionPerformed(ActionEvent e) {
	    		        Mainclass x = new Mainclass();
	    			}
	    		});
	           	   gameover = true;//���̃Q�[���̂���Ń��[�v���~�߂Ă��
	        		}
	        	}
	        	
	        	//�ȉ��Q�[�����ł̒ʏ퓮��
	        	for(int j=0;j<=s;j++) {
	            ball[j].move();
	            //���P�b�g�ɂԂ�������
	            if (racket.HitR(ball[j])) {
	                nu += 1;
	                System.out.println("nu:"+nu);
	                if( xp - racket.R_WID/3 <= ball[j].posX() && ball[j].posX() <= xp + racket.R_WID/3) {//�����t�߂ɓ������
	                ball[j].boundY();
	                }
	                else if(xp - racket.R_WID/2 +Ball.SIZE >= ball[j].posX() || ball[j].posX() <= xp + racket.R_WID/2) {//��ȊO�Ń��P�b�g���ɓ������
	                ball[j].boundYa();
	                }
	                
	                //��Փx����̂��߃��P�b�g�ɂԂ�������nu�����񐔂ɂȂ�ƃ{�[���ǉ�
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
	            
	        	//�ȉ��͂��ׂẴu���b�N0�ɑ΂������Ă邩�A�ǂ��������Ă��邩���f
	        	  for (int i = 0; i < NUM_BCOL; i++) {//i all block
		            	for(int j=0;j<=s;j++) {//j: all ball
		                // ���łɏ����Ă���u���b�N�͖���
		                if (block0[i].getDestroyed())
		                    continue;
		                // �u���b�N�̓��������ʒu���v�Z
		                int collidePos = block0[i].HitB(ball[j]);
		                // �u���b�N�ɓ������Ă�����
		                if (collidePos != Block0.NO_COLLISION) {
		                    block0[i].delete0();
		                    // �{�[���̓��������ʒu����{�[���̔��˕������v�Z
		                    switch (collidePos) {
		                        case Block0.DOWN :
		                        	ball[j].boundY();
		                        	break;
		                        case Block0.UP :
		                            ball[j].boundY();
		                            break;//�u���b�N�̏ォ���ɓ����蔻���y�����̑��x�ω�
		                        case Block0.LEFT :
		                        	ball[j].boundX();
		                        	break;
		                        case Block0.RIGHT :
		                            ball[j].boundX();//���E�ɓ����蔻���x�����ɑ��x�ω�
		                            break;
		                        case Block0.UP_LEFT :
		                        case Block0.UP_RIGHT :
		                        case Block0.DOWN_LEFT :
		                        case Block0.DOWN_RIGHT :
		                            ball[j].boundY();
		                            break;
		                    }
		                   // System.out.println("score:"+String.valueOf(Block.getScore()));
		                    break; // 1��ɉ󂹂�u���b�N��1��
		                }
		            }
		            }	//�ȉ��͂��ׂẴu���b�N0�ɑ΂������Ă邩�A�ǂ��������Ă��邩���f
	        	  for (int i = 0; i < NUM_BCOL; i++) {//i all block
		            	for(int j=0;j<=s;j++) {//j: all ball
		                // ���łɏ����Ă���u���b�N�͖���
		                if (block0[i].getDestroyed())
		                    continue;
		                // �u���b�N�̓��������ʒu���v�Z
		                int collidePos = block0[i].HitB(ball[j]);
		                // �u���b�N�ɓ������Ă�����
		                if (collidePos != Block0.NO_COLLISION) {
		                    block0[i].delete0();
		                    // �{�[���̓��������ʒu����{�[���̔��˕������v�Z
		                    switch (collidePos) {
		                        case Block0.DOWN :
		                        	ball[j].boundY();
		                        	break;
		                        case Block0.UP :
		                            ball[j].boundY();
		                            break;//�u���b�N�̏ォ���ɓ����蔻���y�����̑��x�ω�
		                        case Block0.LEFT :
		                        	ball[j].boundX();
		                        	break;
		                        case Block0.RIGHT :
		                            ball[j].boundX();//���E�ɓ����蔻���x�����ɑ��x�ω�
		                            break;
		                        case Block0.UP_LEFT :
		                        case Block0.UP_RIGHT :
		                        case Block0.DOWN_LEFT :
		                        case Block0.DOWN_RIGHT :
		                            ball[j].boundY();
		                            break;
		                    }
		                   // System.out.println("score:"+String.valueOf(Block.getScore()));
		                    break; // 1��ɉ󂹂�u���b�N��1��
		                }
		            }
		            }
		            repaint();//�`���Ȃ���
		            try {
		                Thread.sleep(20);//��O�������H��
		            } catch (InterruptedException e) {//�߂܂���ϐ�
		                e.printStackTrace();//��O�������̏���
		            }
		            
		            
	         // ��Ɠ��l�Ƀu���b�N1�ɑ΂���
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
	            
		         // �u���b�N2
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
	    
	   //�S�̓I�ȕ��i�̕\��
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);	        // �w�i
	        g.setColor(Color.white);
	        g.fillRect(0, 0, WIDTH, HEIGHT);
	        // ���P�b�g
	        racket.draw(g); 
	        // �{�[��
	        for(int i=0;i<=s;i++) { 
	        ball[i].draw(g); 
	        }
	        // �u���b�N
	        for (int i = 0; i < NUM_BCOL; i++) {
	            if (!block0[i].getDestroyed()) {//������Ă��Ȃ����
	                block0[i].draw(g);
	            }
	        }
	        //�ȉ������l�����A���Ɖ��񓖂Ă�Ώ����邩������悤�ɂ܂������Ȃ��Ă�������ΐF��ς��Ă������ߏ�����t����
	        for (int i = 0; i < NBL; i++) {
	            if (!block[i].getDestroyed()) {//������Ă��Ȃ����
	            	if(block[i].getstate() == block[i].cnt-1) {
	                block[i].drawlast(g);
	            	}
	            	else {
	                block[i].draw(g);
	            	}
	            }
	        }
	        for (int i = 0; i < NBL; i++) {
	            if (!block2[i].getDestroyed()) {//������Ă��Ȃ����
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
