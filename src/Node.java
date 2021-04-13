import java.util.ArrayList;
import java.util.LinkedList;

class Node{
    public int id;//节点编号
    public int type;//节点类别
    public String describe="";
    public int type_dir=0;//因混合方向带来的类别，0普通，1子节点，2父节点
    ////////////////////
    //物理尺寸属性
    public double length;//长度
    public double weight;//宽度
    public double area;//面积
    //////////////////
    //能力属性
    public double capacity;//容量
    public double flow_limit;//流量限制
    public double v_limit;//速度限制///////////////////
    public double cap_safe;//安全容量约束
    //连接关系属性
    public Node[] relOut=null;//某区域的流出向的节点集合
    public double[] rateOut;//向各节点的分流比例
    public ArrayList<Node> relInTemp=new ArrayList <Node>();//向某区域的流入的区域id集合
    public Node[] relIn;//向某区域的流入的区域id集合
    public Node father=null;
    public Node[] children=null;
    //////////////////////
    //状态属性
    //public double num=0;//当前存量
    //public double den=0;//当前密度
    //public double v=0;//当前速度?是否分时
    //public double flow_in=0;//当前节拍的流入量
    //public double flow_out=0;//当前节拍的流出量
    //public double flow_out_max=0;//当前节拍的供给流出量
    public double cap_remain=0;//当前节拍的剩余容量
    public double num_current=0;//当前存量
    public int[] beOut;//流出的个数
    public int outCount=0;//流出的个数
//	public int beFlowout=0;//节点在当前节拍已流出

    //////////////////////////
    //统计属性
    public double[] number;//分时的存量
    public double[] supplys_in_acc;//分时的被供给量
    public double[] wait_nums_in;//分时的排队等待流入人数
    public double[] supplys_out_acc;//分时的供给量
    public double[] dens_average;//分时密度
    public double[] velocitys;//分时速度?
    public double[] flow_ins;//分时的流入量
    public double[] flow_outs;//分时的流出量
    public long[] time_passs;//分时的通过时间**微秒
    public double[] wait_nums_out;//分时的排队等待流出人数
    public double[] wait_time_out;//分时到达后的等待时间//以节拍为单位
    public double[] flow_in_rate;//分时的流率根据能力或密度流量关系计算得到
    ///////////////////////////////
    //辅助的统计信息
    public double[] supplys_out_new;//分时的新增供给人数
    public int time_out_index=0;//上一次流出的供给时刻t0的索引
    //public double passengers_out=0;//累计流出人数
    public double passenger_out_temp=0;//供给时刻t0流出的人数
    public int[] status;
    public double[] ontrain;
    public double[] boardtrain;
    public Station station=null;

    // 内部行走乘客列表
    private LinkedList<Pedestrian> walking = new LinkedList<Pedestrian>();

    // 进出口属性
    public int num_of_path;

    class Path{
        // 进出口属性

        public int path_id;
        public Node destination; // 指向
//        private int type_of_path = 0; // 类型: 0为入口 1为出口 2为列车入 3为列车出
        private LinkedList<Pedestrian> waiting_in_pedestrian = new LinkedList<Pedestrian>();

        // 队列最大值
        private int max_wait_in;

        public void addWaiting_in_pedestrian(Pedestrian a) {
            this.waiting_in_pedestrian.add(a);
        }

        public void deleteWaiting_in_pedestrian() {
            this.waiting_in_pedestrian.removeFirst();
        }

        public LinkedList<Pedestrian> getWaiting_in_pedestrian() {
            return waiting_in_pedestrian;
        }

    }


    public void init(){//初始化

        //对状态属性初始化

        //对统计属性初始化
        int len=station.time_number_index;
        number=new double[len];
        supplys_in_acc=new double[len];
        wait_nums_in=new double[len];
        supplys_out_acc=new double[len];
        dens_average=new double[len];
        velocitys=new double[len];
        flow_ins=new double[len];
        flow_outs=new double[len];
        time_passs=new long[len];
        wait_nums_out=new double[len];
        wait_time_out=new double[len];
        flow_in_rate=new double[len];
        supplys_out_new=new double[len];
        status=new int[len];
        ontrain=new double[len];
        boardtrain=new double[len];
        for(int i=0;i<len;i++){
            number[i]=0;
            supplys_in_acc[i]=0;
            wait_nums_in[i]=0;
            supplys_out_acc[i]=0;
            dens_average[i]=0;
            flow_ins[i]=0;
            velocitys[i]=0;
            flow_outs[i]=0;
            time_passs[i]=0;
            wait_nums_out[i]=0;
            wait_time_out[i]=0;
            flow_in_rate[i]=0;
            supplys_out_new[i]=0;
            status[i]=0;
            ontrain[i]=0;
            boardtrain[i]=0;
        }
    }


    public void reset(){//重置统计属性

        //对状态属性初始化
        num_current=0;
        cap_remain=capacity;

        time_out_index=0;
        passenger_out_temp=0;
        //对统计属性初始化
        int len=station.time_number_index;
        for(int i=0;i<len;i++){
            number[i]=0;
            supplys_in_acc[i]=0;
            wait_nums_in[i]=0;
            supplys_out_acc[i]=0;
            dens_average[i]=0;
            flow_ins[i]=0;
            velocitys[i]=v_limit;
            flow_outs[i]=0;
            time_passs[i]=0;
            wait_nums_out[i]=0;
            wait_time_out[i]=0;
            flow_in_rate[i]=flow_limit;
            if(type==6||type==5)//列车或进站节点的客流初始化为出现需求
                supplys_out_acc[i]=supplys_out_new[i];
            else
                supplys_out_new[i]=0;
        }
    }

}
