import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;
import java.util.List;

public class Main extends JFrame implements ActionListener {
    public Container container=null;
    JMenuItem start,exit;//菜单按钮
    JButton landlord[] =new JButton[2];//抢地主按钮
    JButton publishCard[]=new JButton[2];//出牌按钮
    int dizhuFlag;
    int turn;//出牌顺序
    JLabel dizhu;//地主头像
    List<Card>[] currentList =new Vector[3];//现在准备出牌的
    List<Card>[] playerList =new Vector[3];//现在已有出牌的
    List<Card> lordList;
    Card card[]=new Card[56];
    JTextField time[] = new JTextField[3];//计时器
    Time t;//定时器（线程）
    boolean nextPlayer=false;
    dealCard p0;


    public Main(){
        setIconImage(Toolkit.getDefaultToolkit().getImage("images/dizhu.gif"));
        Init();
        SetMenu();
        this.setVisible(true);

        CardInit();
        p0=new dealCard(this);
        Thread pp0=new Thread(p0);
        pp0.start();

        getLord();
        time[1].setVisible(true);
        t = new Time(this, 10);

        t.start();
    }

    public void Init() {
        this.setTitle("斗地主");
        this.setSize(830, 620);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(getOwner());

        container = this.getContentPane();
        container.setLayout(null);
        container.setBackground(Color.LIGHT_GRAY);
    }

    public void SetMenu(){
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.setBorderPainted(false);
        jMenuBar.setBackground(Color.WHITE);
        this.setJMenuBar(jMenuBar);

        start = new JMenuItem("新游戏");
        jMenuBar.add(start);
        exit = new JMenuItem("退出");
        jMenuBar.add(exit);
        exit.addActionListener(this);
        start.addActionListener(this);

        landlord[0] = new JButton("抢地主");
        landlord[1] = new JButton("不 抢");
        publishCard[0] = new JButton("出牌");
        publishCard[1] = new JButton("不要");

        for (int i = 0; i < 2; i++) {
            publishCard[i].setBounds(320 + i * 100, 400, 60, 20);
            landlord[i].setBounds(320 + i * 100, 400, 75, 20);
            container.add(landlord[i]);
            landlord[i].addActionListener(this);
            landlord[i].setVisible(false);
            container.add(publishCard[i]);
            publishCard[i].setVisible(false);
            publishCard[i].addActionListener(this);
        }
        for (int i = 0; i < 3; i++) {
            time[i] = new JTextField("倒计时:");
            time[i].setEditable(false);
            time[i].setVisible(false);
            container.add(time[i]);
        }
        time[0].setBounds(140, 230, 60, 20);
        time[1].setBounds(374, 360, 60, 20);
        time[2].setBounds(620, 230, 60, 20);

        for (int i = 0; i < 3; i++) {
            currentList[i] = new Vector<Card>();
        }
    }

//  对牌进行初始化
    public void CardInit(){
        int cnt=1;
        for (int i=1;i<=5;i++){
            for(int j=1;j<=13;j++){
                if((i==5) && (j>2))
                    break;
                else{
                    card[cnt]=new Card(this,i+"-"+j,false);
                    card[cnt].setLocation(350,50);
                    container.add(card[cnt]);
                    cnt++;
                }
            }
        }
//      洗牌
        Random random=new Random();
        for(int i=0;i<100;i++){
            int a=random.nextInt(54)+1;//从1-54
            int b=random.nextInt(54)+1;

            Card k=card[a];
            card[a]=card[b];
            card[b]=k;
        }

//        三位玩家
        for(int i=0;i<3;i++)
            playerList[i]=new Vector<Card>();

//        地主底牌
        lordList=new Vector<Card>();

        dizhu=new JLabel(new ImageIcon("images/dizhu.gif"));
        dizhu.setVisible(false);
        dizhu.setSize(40,40);
        container.add(dizhu);
    }

//    抢地主
    public void getLord(){
        for(int i=0;i<2;i++)
            landlord[i].setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==exit){
            this.dispose();
            System.exit(0);
        }
        if(e.getSource()==start){
            this.dispose();
            new Main();
        }
        if(e.getSource()==landlord[0]){
            time[1].setText("抢地主");
            t.isRun=false;
        }

        if (e.getSource() == landlord[1]) {
            time[1].setText("不抢");
            t.isRun = false;
        }

        if (e.getSource() == publishCard[1]) {
            this.nextPlayer = true;
            currentList[1].clear();
            time[1].setText("不要");
        }

        if(e.getSource()==publishCard[0]){
            List<Card> c=new Vector<Card>();
            for(int i=0;i<playerList[1].size();i++){
                Card card=playerList[1].get(i);
                if(card.clicked)
                    c.add(card);
            }
            int flag=0;
//            主动出牌
            if(time[0].getText().equals("不要")&&time[2].getText().equals("不要")){
                if(Common.jugdeType(c)!=CardType.c0)
                    flag=1;
            }
            else
                flag=Common.checkCards(c,currentList,this);

            //判断是否符合出牌
            if(flag==1){
                currentList[1]=c;
                playerList[1].removeAll(currentList[1]);
                Point point = new Point();
                point.x = (770 / 2) - (currentList[1].size() + 1) * 15 / 2;
                point.y = 300;
                for (int i = 0; i <currentList[1].size() ; i++) {
                    Card card = currentList[1].get(i);
                    Common.move(card, card.getLocation(), point);
                    point.x += 15;
                }
                Common.rePosition(this, playerList[1], 1);
                time[1].setVisible(false);
                this.nextPlayer = true;
            }
        }
    }
    public static void main(String args[]) {
        new Main();
    }
}


