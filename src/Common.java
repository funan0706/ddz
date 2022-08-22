import javax.xml.catalog.Catalog;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Common {
//    判断牌型
    public static CardType jugdeType(List<Card> list){
        int len=list.size();
        if(len<=4){
            if(len<=4){
//                牌都是一样的
                if(len>0 && Common.getValue(list.get(0))==Common.getValue(list.get(len-1))){
                    switch (len){
                        case 1:
                            return CardType.c1;
                        case 2:
                            return CardType.c2;
                        case 3:
                            return CardType.c3;
                        case 4:
                            return CardType.c4;
                    }
                }
//                王炸
                if(len==2&&Common.getColor(list.get(1))==5)
                    return CardType.c2;
                if(len == 4 && ((Common.getValue(list.get(0)) == Common.getValue(list.get(len - 2)))
                        || Common.getValue(list.get(1)) == Common.getValue(list.get(len - 1))))
                    return CardType.c31;
                else
                    return CardType.c0;
            }
        }
        if(len>=5){
            Card_index card_index=new Card_index();
            for (int i=0;i<4;i++)
                card_index.a[i]=new Vector<Integer>();
            Common.getMax(card_index,list);
            if(card_index.a[2].size()==1&&card_index.a[1].size()==1&&len==5)
                return CardType.c32;
            if (card_index.a[3].size() == 1&& len == 6)
                return CardType.c411;
            if (card_index.a[3].size() == 1 && card_index.a[1].size() == 2 && len == 8)
                return CardType.c422;
            if ((Common.getColor(list.get(0)) != 5) && (card_index.a[0].size() == len)
                    && (Common.getValue(list.get(0)) - Common.getValue(list.get(len - 1)) == len - 1))
                return CardType.c123;
//          对子的数目是所选的牌数的一半，说明是全是对子
            if (card_index.a[1].size() == len / 2 && len % 2 == 0 && len / 2 >= 3
                    && (Common.getValue(list.get(0)) - Common.getValue(list.get(len - 1)) == (len / 2 - 1)))
                return CardType.c112233;

            if (card_index.a[2].size() == len / 3 && (len % 3 == 0)&&len/3>=2
                    && (Common.getValue(list.get(0)) - Common.getValue(list.get(len - 1)) == (len / 3 - 1)))
                return CardType.c111222;
            if (card_index.a[2].size() == len / 4 && ((Integer) (card_index.a[2].get(len / 4 - 1))
                    - (Integer) (card_index.a[2].get(0)) == len / 4 - 1))
                return CardType.c11122234;
            if (card_index.a[2].size() == len / 5 && card_index.a[1].size() == len / 5
                    && ((Integer) (card_index.a[2].get(len / 5 - 1)) - (Integer) (card_index.a[2].get(0)) == len / 5
                    - 1))
                return CardType.c1112223344;
        }
        return CardType.c0;
    }

    public static void move(Card card, Point from, Point to) {
        if (to.x != from.x) {
            double k = (1.0) * (to.y - from.y) / (to.x - from.x);
            double b = to.y - to.x * k;
            int flag = 0;
            if (from.x < to.x)
                flag = 20;
            else {
                flag = -20;
            }
            for (int i = from.x; Math.abs(i - to.x) > 20; i += flag) {
                double y = k * i + b;

                card.setLocation(i, (int) y);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        card.setLocation(to);
    }

//    对牌进行排序
    public static void order(List<Card> list){
        Collections.sort(list,new Comparator<Card>(){

            @Override
            public int compare(Card o1, Card o2) {
//                牌值
                int a1=Integer.parseInt(o1.name.substring(0,1));
                int a2=Integer.parseInt(o2.name.substring(0,1));
//               花色
                int b1=Integer.parseInt(o1.name.substring(2,o1.name.length()));
                int b2=Integer.parseInt(o2.name.substring(2,o2.name.length()));

                int flag=0;
//              大王，小王，2，A
                if(a1==5)
                    b1+=100;
                if(a1==50&&b1==1)
                    b1+=50;
                if(a2==5)
                    b2+=100;
                if (a2 == 5 && b2 == 1)
                    b2 += 50;
                if (b1 == 1)
                    b1 += 20;
                if (b2 == 1)
                    b2 += 20;
                if (b1 == 2)
                    b1 += 30;
                if (b2 == 2)
                    b2 += 30;

                flag=b2-b1;
                if(flag==0)
                    return b2-b1;
                else
                    return flag;
            }
        });
    }

    // 重新定位 flag代表电脑1 ,2 或者是我
    public static void rePosition(Main m, List<Card> list, int flag) {
        Point p = new Point();
        if (flag == 0) {
            p.x = 50;
            p.y = (450 / 2) - (list.size() + 1) * 15 / 2;
        }
        if (flag == 1) {
            p.x = (800 / 2) - (list.size() + 1) * 21 / 2;
            p.y = 450;
        }
        if (flag == 2) {
            p.x = 700;
            p.y = (450 / 2) - (list.size() + 1) * 15 / 2;
        }
        int len = list.size();
        for (int i = 0; i < len; i++) {
            Card card = list.get(i);
            Common.move(card, card.getLocation(), p);
            m.container.setComponentZOrder(card, 0);
            if (flag == 1)
                p.x += 21;
            else
                p.y += 15;
        }
    }

//    是否抢地主
    public static int getScore(List<Card> list){
        int cnt=0;
        for (int i=0;i<list.size();i++){
            Card card=list.get(i);
            if(card.name.substring(0,1).equals("5"))
                cnt+=5;
            if (card.name.substring(2,card.name.length()).equals("2"))
                cnt+=2;
        }
        return cnt;
    }

//    返回花色
    public static int getColor(Card card){
        return Integer.parseInt(card.name.substring(0, 1));
    }

//    返回值
    public static int getValue(Card card){
        int i = Integer.parseInt(card.name.substring(2, card.name.length()));
        if (card.name.substring(2, card.name.length()).equals("2"))
            i += 13;//2
        if (card.name.substring(2, card.name.length()).equals("1"))
            i += 13;//A
        if (Common.getColor(card) == 5)
            i += 2;//king
        return i;
    }

//    记录一张牌重复多少次
    private static void getMax(Card_index card_index, List<Card> list) {
        int cnt[]=new int[14];
        for (int i=0;i<14;i++)
            cnt[i]=0;

        for (int i=0;i<list.size();i++){
            if(Common.getColor(list.get(i))==5)
                cnt[13]++;
            else
                cnt[Common.getValue(list.get(i))-1]++;
        }

        for(int i=0;i<14;i++){
            switch (cnt[i]){
                case 1:
                    card_index.a[0].add(i+1);
                    break;
                case 2:
                    card_index.a[1].add(i+1);
                    break;
                case 3:
                    card_index.a[2].add(i+1);
                    break;
                case 4:
                    card_index.a[3].add(i+1);
                    break;
            }
        }
    }

//    把一副牌拆开
    public static Model getModel(List<Card> list){
        List<Card> list2=new Vector<Card>(list);
        Model model=new Model();
        Common.getBoomb(list2, model); 
        // ------拆3带
        Common.getThree(list2, model);
        // 拆飞机
        Common.getPlane(list2, model);
        // ------拆对子
        Common.getTwo(list2, model);
        // 拆连队
        Common.getTwoTwo(list2, model);
        // 拆顺子
        Common.get123(list2, model);
        // 拆单
        Common.getSingle(list2, model);
        return model;
    }

    private static void getThree(List<Card> list, Model model) {
    List<Card> del=new Vector<>();
    int len=list.size();
    for(int i=0;i<len;i++){
        if(i+2<len && Common.getValue(list.get(i))==Common.getValue(list.get(i + 2))){
            String s=list.get(i).name+",";
            s+=list.get(i+1).name+",";
            s+=list.get(i+2).name+",";
            model.a3.add(s);
            for(int j=i;j<=i+2;j++)
                del.add(list.get(j));
            i+=2;
        }
    }
    list.removeAll(del);
    }

    private static void getBoomb(List<Card> list, Model model) {
        List<Card> del = new Vector<Card>();
        if (list.size() < 1)
            return;
//		王炸
        if (list.size() >= 2 && Common.getColor(list.get(0)) == 5 && Common.getColor(list.get(1)) == 5) {
            model.a4.add(list.get(0).name + "," + list.get(1).name);
            del.add(list.get(0));
            del.add(list.get(1));
        }
        if (Common.getColor(list.get(0)) == 5 && Common.getColor(list.get(1)) != 5) {
            del.add(list.get(0));
            model.a1.add(list.get(0).name);
        }
        list.removeAll(del);
        for (int i = 0, len = list.size(); i < len; i++) {
            if (i + 3 < len && Common.getValue(list.get(i)) == Common.getValue(list.get(i + 3))) {
                String s = list.get(i).name + ",";
                s += list.get(i + 1).name + ",";
                s += list.get(i + 2).name + ",";
                s += list.get(i + 3).name;
                model.a4.add(s);
                for (int j = i; j <= i + 3; j++)
                    del.add(list.get(j));
                i = i + 3;
            }
        }
        list.removeAll(del);
    }

    public static void getTwo(List<Card> list,Model model){
        List<Card> del=new Vector<>();
        int len=list.size();
        for (int i=0;i<len;i++){
            if(i+1<len && Common.getValue(list.get(i))==Common.getValue(list.get(i+1))){
                String s = list.get(i).name + ",";
                s += list.get(i + 1).name;
                model.a2.add(s);
                for (int j = i; j <= i + 1; j++)
                    del.add(list.get(j));
                i = i + 1;
            }
        }
        list.removeAll(del);
    }

    public static void getSingle(List<Card> list, Model model) {
        List<Card> del = new Vector<Card>();
        for (int i = 0, len = list.size(); i < len; i++) {
            model.a1.add(list.get(i).name);
            del.add(list.get(i));
        }
        list.removeAll(del);
    }

    public static void getPlane(List<Card> list, Model model){
        List<String> del = new Vector<String>();
        List<String> l = model.a3;// 从model里面的3带找
        int len=l.size();
        if (len< 2)
            return;
        Integer s[] = new Integer[l.size()];
        for (int i = 0; i < len; i++) {
            String[] name = l.get(i).split(",");
            s[i] = Integer.parseInt(name[0].substring(2, name[0].length()));
        }
        for (int i = 0; i < len; i++) {
            int k = i;
            for (int j = i; j < len; j++) {
                if (s[i] - s[j] == j - i)
                    k = j;
            }
            if (k != i) {
                String ss = "";
                for (int j = i; j < k; j++) {
                    ss += l.get(j) + ",";
                    del.add(l.get(j));
                }
                ss += l.get(k);
                model.a111222.add(ss);
                del.add(l.get(k));
                i = k;
            }
        }
        l.removeAll(del);
    }

    public static void getTwoTwo(List<Card> list, Model model) {
        List<String> del = new Vector<>();// 要删除的牌
        List<String> l = model.a2;
        if (l.size() < 3)
            return;
        Integer s[] = new Integer[l.size()];
        for (int i = 0, len = l.size(); i < len; i++) {
            String[] name = l.get(i).split(",");
            s[i] = Integer.parseInt(name[0].substring(2, name[0].length()));
        }
        // s0,1,2,3,4 13,9,8,7,6
        for (int i = 0, len = l.size(); i < len; i++) {
            int k = i;
            for (int j = i; j < len; j++) {
                if (s[i] - s[j] == j - i)
                    k = j;
            }
            if (k - i >= 2)// k=4 i=1
            {// 从i到k是连对
                String ss = "";
                for (int j = i; j < k; j++) {
                    ss += l.get(j) + ",";
                    del.add(l.get(j));
                }
                ss += l.get(k);
                model.a112233.add(ss);
                del.add(l.get(k));
                i = k;
            }
        }
        l.removeAll(del);
    }

    public static void get123(List<Card> list, Model model) {
        List<Card> del = new Vector<Card>();
        if (list.size() > 0 && (Common.getValue(list.get(0)) < 7 || Common.getValue(list.get(list.size() - 1)) > 10))
            return;
        if (list.size() <=5)//感觉<=5
            return;
        List<Card> list2 = new Vector<Card>();
        List<Card> temp = new Vector<Card>();
        List<Integer> integers = new Vector<Integer>();
        for (Card card : list2) {
            if (integers.indexOf(Common.getValue(card)) < 0) {
                integers.add(Common.getValue(card));
                temp.add(card);
            }

        }
        Common.order(temp);
        for (int i = 0, len = temp.size(); i < len; i++) {
            int k = i;
            for (int j = i; j < len; j++) {
                if (Common.getValue(temp.get(i)) - Common.getValue(temp.get(j)) == j - i) {
//
                    k = j;
                }
            }
            if (k - i >= 4) {
                String s = "";
                for (int j = i; j < k; j++) {
                    s += temp.get(j).name + ",";
                    del.add(temp.get(j));
                }
                s += temp.get(k).name;
                del.add(temp.get(k));
                model.a123.add(s);
                i = k;
            }
        }

        list.removeAll(del);
    }

    //    检查我选中的牌是否能出
    public static int checkCards(List<Card> c,List<Card>[] current,Main m){
        List<Card> currentlist;
        if(m.time[0].getText().equals("不要"))
            currentlist=current[2];
        else
            currentlist=current[0];
        CardType cType1=Common.jugdeType(c);
        CardType cType2=Common.jugdeType(currentlist);

        //如果牌数不相等并且不是炸弹的话，不能出牌
        if (cType1 != CardType.c4 && c.size() != currentlist.size())
            return 0;
        //如果牌数相等但类型不同并且不是炸弹的话，不能出牌
        if (cType1 != CardType.c4 && Common.jugdeType(c) != Common.jugdeType(currentlist)) {
            return 0;
        }
        if(cType1==CardType.c4 && cType2!=CardType.c4){
            return 1;
        }
        //出牌是王炸
        if(cType1 == CardType.c4&&c.size() == 2)
            return 1;

        //出牌是同类型，比牌大小
        if(cType1==CardType.c1 || cType1==CardType.c2 || cType1==CardType.c3 || cType1==CardType.c4){
            if (Common.getValue(c.get(0)) <= Common.getValue(currentlist.get(0))) {//判断第一张牌的大小
                return 0;
            } else {
                return 1;
            }
        }

        if (cType1==CardType.c123 || cType1==CardType.c112233 || cType1 == CardType.c111222){
            if (Common.getValue(c.get(0)) <= Common.getValue(currentlist.get(0)))
                return 0;
            else
                return 1;
        }

        if(cType1==CardType.c31 || cType1==CardType.c32 || cType1 == CardType.c411 || cType1 == CardType.c422
                || cType1 == CardType.c11122234 || cType1 == CardType.c1112223344){
            List<Card> a1 = Common.getOrder2(c);
            List<Card> a2 = Common.getOrder2(currentlist);
            if (Common.getValue(a1.get(0)) < Common.getValue(a2.get(0)))//对于重排后的进行判断第一的牌的大小
                return 0;
        }
        return 1;
    }
//先要进行一个排序，防止出现77444的情况
    public static List getOrder2(List<Card> list) {
        List<Card> list2 = new Vector<Card>(list);
        List<Card> list3 = new Vector<Card>();
        int len = list2.size();
        int a[] = new int[20];
        for (int i = 0; i < 20; i++)
            a[i] = 0;
        for (int i = 0; i < len; i++) {
            a[Common.getValue(list2.get(i))]++;
        }
        int max = 0;
        for (int i = 0; i < 20; i++) {
            max = 0;
            for (int j = 19; j >= 0; j--) {
                if (a[j] > a[max])
                    max = j;
            }

            for (int k = 0; k < len; k++) {
                if (Common.getValue(list2.get(k)) == max) {
                    list3.add(list2.get(k));
                }
            }
            list2.remove(list3);
            a[max] = 0;
        }
        return list3;
    }

    public static void hideCards(List<Card> list) {
        for (int i = 0, len = list.size(); i < len; i++) {
            list.get(i).setVisible(false);
        }
    }
}
class Card_index{
    List a[]=new Vector[4];
}
